package dev.wolfieboy09.qstorage.block.pipe;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public abstract class BasePipeBlock<T extends BlockCapability<?, @Nullable Direction>> extends Block implements SimpleWaterloggedBlock, EntityBlock {
    private final BlockCapability<?, @Nullable Direction> capability;

    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty WATER_LOGGED = BlockStateProperties.WATERLOGGED;

    public BasePipeBlock(T blockCap, MapColor mapColor) {
        super(Properties.of().mapColor(mapColor).strength(0.5F).pushReaction(PushReaction.BLOCK));
        this.capability = blockCap;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, WATER_LOGGED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(WATER_LOGGED, false);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            Direction direction = Direction.getNearest(
                    pos.getX() - neighborPos.getX(),
                    pos.getY() - neighborPos.getY(),
                    pos.getZ() - neighborPos.getZ()
            );

            System.out.println("Can Connect: " + canConnectToSide(state, level, pos, neighborPos, direction));
        }
    }

    private boolean canConnectToSide(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor, @Nullable Direction context) {
        if (level.isClientSide()) return false;
        BlockEntity entity = level.getBlockEntity(neighbor);
        if (entity != null && entity.getLevel() instanceof ServerLevel serverLevel) {
            return entity.getLevel() != null && serverLevel.getCapability(this.capability, neighbor, context) != null;
        }
        return false;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATER_LOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}