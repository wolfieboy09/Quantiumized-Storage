package dev.wolfieboy09.qstorage.block.pipe;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public abstract class BasePipeBlock<C> extends Block implements SimpleWaterloggedBlock, EntityBlock {
    private final BlockCapability<C, @Nullable Direction> capability;
    private final Class<?> pipeClass;

    public static final EnumProperty<ConnectionType> UP = EnumProperty.create("up", ConnectionType.class);
    public static final EnumProperty<ConnectionType> DOWN = EnumProperty.create("down", ConnectionType.class);
    public static final EnumProperty<ConnectionType> NORTH = EnumProperty.create("north", ConnectionType.class);
    public static final EnumProperty<ConnectionType> EAST = EnumProperty.create("east", ConnectionType.class);
    public static final EnumProperty<ConnectionType> SOUTH = EnumProperty.create("south", ConnectionType.class);
    public static final EnumProperty<ConnectionType> WEST = EnumProperty.create("west", ConnectionType.class);
    public static final BooleanProperty WATER_LOGGED = BlockStateProperties.WATERLOGGED;

    public BasePipeBlock(BlockCapability<C, @Nullable Direction> blockCap, MapColor mapColor) {
        super(Properties.of().mapColor(mapColor).strength(0.5F).pushReaction(PushReaction.BLOCK).noOcclusion());
        this.capability = blockCap;
        this.pipeClass = this.getClass(); // Grabs the higher class
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, WATER_LOGGED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(UP, ConnectionType.NONE)
                .setValue(DOWN, ConnectionType.NONE)
                .setValue(NORTH, ConnectionType.NONE)
                .setValue(EAST, ConnectionType.NONE)
                .setValue(SOUTH, ConnectionType.NONE)
                .setValue(WEST, ConnectionType.NONE)
                .setValue(WATER_LOGGED, false);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATER_LOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (isPipe((Level) level, pos, facing)) {
            state = state.setValue(getPropertyFromDirection(facing), ConnectionType.PIPE);
        } else if (canConnectTo((Level) level, pos, facing)) {
            state = state.setValue(getPropertyFromDirection(facing), ConnectionType.BLOCK);
        } else {
            state = state.setValue(getPropertyFromDirection(facing), ConnectionType.NONE);
        }


        return state;
    }

    // Is this useless? No idea.
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        Direction direction = Direction.getNearest(
                neighborPos.getX() - pos.getX(),
                neighborPos.getY() - pos.getY(),
                neighborPos.getZ() - pos.getZ()
        );

        if (level.getCapability(this.capability, neighborPos, direction) != null) {
            if (isPipe(level, neighborPos, direction)) {
                state.setValue(getPropertyFromDirection(direction), ConnectionType.PIPE);
                return;
            }
            state.setValue(getPropertyFromDirection(direction), ConnectionType.BLOCK);
        }
    }

    private EnumProperty<ConnectionType> getPropertyFromDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> BasePipeBlock.NORTH;
            case EAST -> BasePipeBlock.EAST;
            case SOUTH -> BasePipeBlock.SOUTH;
            case WEST -> BasePipeBlock.WEST;
            case UP -> BasePipeBlock.UP;
            case DOWN -> BasePipeBlock.DOWN;
        };
    }

    private boolean canConnectTo(Level world, BlockPos pos, Direction facing) {
        return world.getCapability(this.capability, pos.relative(facing), facing.getOpposite()) != null;
    }

    public boolean isAbleToConnect(Level world, BlockPos pos, Direction facing) {
        return isPipe(world, pos, facing) || canConnectTo(world, pos, facing);
    }

    private boolean isPipe(Level world, BlockPos pos, Direction facing) {
        // Checks if the wanting to connect class is the same class as the pipe class.
        return this.pipeClass.isInstance(world.getBlockState(pos.relative(facing)).getBlock());
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    public enum ConnectionType implements StringRepresentable {
        NONE,
        PIPE,
        BLOCK;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}