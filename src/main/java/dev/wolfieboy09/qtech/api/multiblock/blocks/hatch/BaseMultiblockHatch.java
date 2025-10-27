package dev.wolfieboy09.qtech.api.multiblock.blocks.hatch;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.block.AbstractBaseEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public abstract class BaseMultiblockHatch<C> extends AbstractBaseEntityBlock {
    private final BlockCapability<C, @Nullable Direction> capability;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BaseMultiblockHatch(BlockCapability<C, @Nullable Direction> blockCapability, Properties properties) {
        super(properties);
        this.capability = blockCapability;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public BlockCapability<C, @Nullable Direction> getCapability() {
        return this.capability;
    }

    public abstract BaseMultiblockHatchEntity<C> newMultiblockHatch(BlockPos blockPos, BlockState blockState);

    @Override
    public final BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return newMultiblockHatch(blockPos, blockState);
    }

    @Override
    public final @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : (lvl, pos, bState, bEntity) -> ((BaseMultiblockHatchEntity<?>) bEntity).tick();
    }
}
