package dev.wolfieboy09.qtech.api.multiblock.blocks.controller;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.multiblock.MultiblockPatternManager;
import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import dev.wolfieboy09.qtech.block.AbstractBaseEntityBlock;
import dev.wolfieboy09.qtech.packets.HideMultiblockPattern;
import dev.wolfieboy09.qtech.packets.ShowMultiblockPattern;
import dev.wolfieboy09.qtech.registries.QTItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@NothingNullByDefault
public abstract class BaseMultiblockController extends AbstractBaseEntityBlock {
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final Supplier<MultiblockType> type;

    public BaseMultiblockController(Properties properties, Supplier<MultiblockType> type) {
        super(properties);
        this.type = type;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FORMED, FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FORMED, false).setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public MultiblockType getType() {
        return this.type.get();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;

        if (level.getBlockEntity(pos) instanceof BaseMultiblockControllerEntity controller && !controller.isFormed()) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new ShowMultiblockPattern(
                    MultiblockPatternManager.getAllPatternsForType(this.getType()).getFirst(),
                    pos,
                    level.getBlockState(pos).getValue(FACING),
                    600
            ));
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof BaseMultiblockControllerEntity controller) {
            if (stack.is(QTItems.WRENCH) && !controller.isFormed()) {
                controller.attemptFormation();
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof BaseMultiblockControllerEntity controller) {
            PacketDistributor.sendToAllPlayers(new HideMultiblockPattern());
            controller.breakMultiblock();
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public final @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : ((level1, pos, state1, blockEntity) -> ((BaseMultiblockControllerEntity) blockEntity).tick());
    }

    public abstract BaseMultiblockControllerEntity newMultiblockController(BlockPos blockPos, BlockState blockState);

    @Override
    public final BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return newMultiblockController(blockPos, blockState);
    }
}
