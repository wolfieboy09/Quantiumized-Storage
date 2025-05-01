package dev.wolfieboy09.qtech.block.gas_canister;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.util.VoxelUtil;
import dev.wolfieboy09.qtech.block.AbstractBaseEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class GasCanisterBlock extends AbstractBaseEntityBlock {
    public static final MapCodec<GasCanisterBlock> CODEC = simpleCodec(GasCanisterBlock::new);

    private final VoxelShape root = Block.box(6, 0, 6, 10, 12, 10);
    private final VoxelShape stand1 = Block.box(6, 12, 6, 6.5, 13, 6.5);
    private final VoxelShape stand2 = Block.box(9.5, 12, 6, 10, 13, 6.5);
    private final VoxelShape stand3 = Block.box(6, 12, 9.5, 6.5, 13, 10);
    private final VoxelShape stand4 = Block.box(9.5, 12, 9.5, 10, 13, 10);
    private final VoxelShape standCollection = Shapes.join(
            Shapes.join(stand1, stand2, BooleanOp.OR),
            Shapes.join(stand3, stand4, BooleanOp.OR),
            BooleanOp.OR);
    private final VoxelShape rim1 = Block.box(6, 13, 6, 10, 13.5, 6.5);
    private final VoxelShape rim2 = Block.box(6, 13, 9.5, 10, 13.5, 10);
    private final VoxelShape rim3 = Block.box(6, 13, 6, 6.5, 13.5, 10);
    private final VoxelShape rim4 = Block.box(9.5, 13, 6, 10, 13.5, 10);
    private final VoxelShape rimCollection = Shapes.join(
            Shapes.join(rim1, rim2, BooleanOp.OR),
            Shapes.join(rim3, rim4, BooleanOp.OR),
            BooleanOp.OR);
    private final VoxelShape totalCollection = Shapes.join(
            Shapes.join(standCollection, rimCollection, BooleanOp.OR),
            root, BooleanOp.OR);

    public static EnumProperty<GasCanisterState> MODE = EnumProperty.create("mode", GasCanisterState.class);

    public GasCanisterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(MODE, GasCanisterState.FILL));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MODE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GasCanisterBlockEntity(blockPos, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.totalCollection;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getCollisionShape(state, level, pos, context);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : (lvl, pos, blockState, blockEntity) -> ((GasCanisterBlockEntity) blockEntity).tick();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (level.getBlockEntity(pos) instanceof GasCanisterBlockEntity be) {
                serverPlayer.openMenu(be, pos);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
