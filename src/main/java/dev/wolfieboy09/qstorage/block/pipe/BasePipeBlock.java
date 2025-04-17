package dev.wolfieboy09.qstorage.block.pipe;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public abstract class BasePipeBlock<C> extends Block implements SimpleWaterloggedBlock, EntityBlock {
    private final BlockCapability<C, @Nullable Direction> capability;

    public static final EnumProperty<ConnectionType> UP = EnumProperty.create("up", ConnectionType.class);
    public static final EnumProperty<ConnectionType> DOWN = EnumProperty.create("down", ConnectionType.class);
    public static final EnumProperty<ConnectionType> NORTH = EnumProperty.create("north", ConnectionType.class);
    public static final EnumProperty<ConnectionType> EAST = EnumProperty.create("east", ConnectionType.class);
    public static final EnumProperty<ConnectionType> SOUTH = EnumProperty.create("south", ConnectionType.class);
    public static final EnumProperty<ConnectionType> WEST = EnumProperty.create("west", ConnectionType.class);
    public static final BooleanProperty WATER_LOGGED = BlockStateProperties.WATERLOGGED;

    protected final VoxelShape[] pipeShapes = new VoxelShape[Direction.values().length];
    protected final VoxelShape[] blockConnectorShapes = new VoxelShape[Direction.values().length];

    protected final VoxelShape[] shapeCache = new VoxelShape[ConnectionType.VALUES.length * ConnectionType.VALUES.length * ConnectionType.VALUES.length * ConnectionType.VALUES.length * ConnectionType.VALUES.length * ConnectionType.VALUES.length];


    public BasePipeBlock(BlockCapability<C, @Nullable Direction> blockCap) {
        super(Properties.of().strength(0.5F).pushReaction(PushReaction.BLOCK).noOcclusion());
        this.capability = blockCap;

        for (Direction direction : Direction.values()) {
            this.pipeShapes[direction.ordinal()] = createCableShape(direction, 5);
            this.blockConnectorShapes[direction.ordinal()] = createBlockConnectorShape(direction);
        }

        createShapeCache();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, WATER_LOGGED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState()
                .setValue(UP, ConnectionType.NONE)
                .setValue(DOWN, ConnectionType.NONE)
                .setValue(NORTH, ConnectionType.NONE)
                .setValue(EAST, ConnectionType.NONE)
                .setValue(SOUTH, ConnectionType.NONE)
                .setValue(WEST, ConnectionType.NONE)
                .setValue(WATER_LOGGED, false);

        return calculateState(context.getLevel(), context.getClickedPos(), state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getCollisionShape(state, level, pos, context);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATER_LOGGED) ? Fluids.WATER.defaultFluidState() : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int index = calculateShapeIndex(
                state.getValue(NORTH),
                state.getValue(SOUTH),
                state.getValue(WEST),
                state.getValue(EAST),
                state.getValue(UP),
                state.getValue(DOWN)
        );
        return this.shapeCache[index];
    }

    private static VoxelShape createCableShape(Direction direction, int diameter) {
        int max = diameter + 6;

        return switch (direction) {
            case NORTH -> Block.box(diameter, diameter, 0, max, max, diameter);
            case SOUTH -> Block.box(diameter, diameter, max, max, max, 16);
            case WEST -> Block.box(0, diameter, diameter, diameter, max, max);
            case EAST -> Block.box(max, diameter, diameter, 16, max, max);
            case UP -> Block.box(diameter, max, diameter, max, 16, max);
            case DOWN -> Block.box(diameter, 0, diameter, max, diameter, max);
        };
    }

    private static VoxelShape createBlockConnectorShape(Direction direction) {
        int min = 4;
        int max = 12;

        return switch (direction) {
            case NORTH -> Block.box(min, min, 0, max, max, 1);
            case SOUTH -> Block.box(min, min, 16, max, max, 16);
            case WEST -> Block.box(0, min, min, 1, max, max);
            case EAST -> Block.box(15, min, min, 16, max, max);
            case UP -> Block.box(min, 15, min, max, 16, max);
            case DOWN -> Block.box(min, 0, min, max, 1, max);
        };
    }



    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide) {
            for (Direction direction : Direction.values()) {
                state = updateBlockState(state, level, pos, direction);
            }

            level.setBlockAndUpdate(pos, state);
        }
    }

    private BlockState updateBlockState(BlockState state, Level level, BlockPos pos, Direction facing) {
        if (canConnectToPipe(level, pos, facing)) {
            state = state.setValue(getPropertyFromDirection(facing), ConnectionType.PIPE);
        } else if (canConnectToBlock(level, pos, facing)) {
            state = state.setValue(getPropertyFromDirection(facing), ConnectionType.BLOCK);
        } else {
            state = state.setValue(getPropertyFromDirection(facing), ConnectionType.NONE);
        }
        return state;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATER_LOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return calculateState((Level) level, pos, state);
    }

    private @NotNull BlockState calculateState(Level world, BlockPos pos, BlockState state) {
        ConnectionType north = getConnectorType(world, pos, Direction.NORTH);
        ConnectionType south = getConnectorType(world, pos, Direction.SOUTH);
        ConnectionType west = getConnectorType(world, pos, Direction.WEST);
        ConnectionType east = getConnectorType(world, pos, Direction.EAST);
        ConnectionType up = getConnectorType(world, pos, Direction.UP);
        ConnectionType down = getConnectorType(world, pos, Direction.DOWN);
        state = state.setValue(NORTH, north)
                .setValue(SOUTH, south)
                .setValue(WEST, west)
                .setValue(EAST, east)
                .setValue(UP, up)
                .setValue(DOWN, down);
        return state;
    }

    protected ConnectionType getConnectorType(Level world, BlockPos connectorPos, Direction facing) {
        BlockPos pos = connectorPos.relative(facing);
        BlockState state = world.getBlockState(pos);
        if (world.getBlockEntity(connectorPos) instanceof BasePipeBlockEntity blockEntity) {
            if (blockEntity.getDisconnectedSides().contains(facing)) {
                return ConnectionType.NONE;
            }
        }
        if (state.is(this)) {
            return ConnectionType.PIPE;
        } else if (canConnectToBlock(world, connectorPos, facing)) {
            return ConnectionType.BLOCK;
        } else {
            return ConnectionType.NONE;
        }
    }

    public static EnumProperty<ConnectionType> getPropertyFromDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> BasePipeBlock.NORTH;
            case EAST -> BasePipeBlock.EAST;
            case SOUTH -> BasePipeBlock.SOUTH;
            case WEST -> BasePipeBlock.WEST;
            case UP -> BasePipeBlock.UP;
            case DOWN -> BasePipeBlock.DOWN;
        };
    }

    private boolean canConnectToBlock(Level world, BlockPos pos, Direction facing) {
        // Gets the block entity of itself, so I can get the stuff for this method
        if (world.getBlockEntity(pos) instanceof BasePipeBlockEntity pipe) {
            return world.getCapability(this.capability, pos.relative(facing), facing.getOpposite()) != null && pipe.allowConnectionFromDirection(facing);
        }
        return false;
    }

    public boolean isAbleToConnect(Level world, BlockPos pos, Direction facing) {
        return canConnectToPipe(world, pos, facing) || canConnectToBlock(world, pos, facing);
    }

    private boolean canConnectToPipe(Level world, BlockPos pos, Direction facing) {
        BlockState state = world.getBlockState(pos.relative(facing));
        BlockEntity blockEntity = world.getBlockEntity(pos.relative(facing));
        if (state.is(this) && blockEntity != null) {
            BasePipeBlockEntity pipeBlock = (BasePipeBlockEntity) blockEntity;
            return pipeBlock.allowConnectionFromDirection(facing);
        }
        return false;
    }

    @Override
    public abstract @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state);

    protected void createShapeCache() {
        for (ConnectionType up : ConnectionType.VALUES) {
            for (ConnectionType down : ConnectionType.VALUES) {
                for (ConnectionType north : ConnectionType.VALUES) {
                    for (ConnectionType south : ConnectionType.VALUES) {
                        for (ConnectionType east : ConnectionType.VALUES) {
                            for (ConnectionType west : ConnectionType.VALUES) {
                                int idx = calculateShapeIndex(north, south, west, east, up, down);
                                this.shapeCache[idx] = createShape(north, south, west, east, up, down);
                            }
                        }
                    }
                }
            }
        }
    }

    protected static int calculateShapeIndex(ConnectionType north, ConnectionType south, ConnectionType west, ConnectionType east, ConnectionType up, ConnectionType down) {
        int size = ConnectionType.VALUES.length;
        return ((((south.ordinal() * size + north.ordinal()) * size + west.ordinal()) * size + east.ordinal()) * size + up.ordinal()) * size + down.ordinal();
    }

    private VoxelShape createShape(ConnectionType north, ConnectionType south, ConnectionType west, ConnectionType east, ConnectionType up, ConnectionType down) {
        VoxelShape shape = Block.box(5, 5, 5, 11, 11, 11);
        shape = combineShape(shape, north, this.pipeShapes[Direction.NORTH.ordinal()], this.blockConnectorShapes[Direction.NORTH.ordinal()]);
        shape = combineShape(shape, south, this.pipeShapes[Direction.SOUTH.ordinal()], this.blockConnectorShapes[Direction.SOUTH.ordinal()]);
        shape = combineShape(shape, west, this.pipeShapes[Direction.WEST.ordinal()], this.blockConnectorShapes[Direction.WEST.ordinal()]);
        shape = combineShape(shape, east, this.pipeShapes[Direction.EAST.ordinal()], this.blockConnectorShapes[Direction.EAST.ordinal()]);
        shape = combineShape(shape, up, this.pipeShapes[Direction.UP.ordinal()], this.blockConnectorShapes[Direction.UP.ordinal()]);
        shape = combineShape(shape, down, this.pipeShapes[Direction.DOWN.ordinal()], this.blockConnectorShapes[Direction.DOWN.ordinal()]);
        return shape;
    }

    protected static VoxelShape combineShape(VoxelShape shape, ConnectionType connectorType, VoxelShape cableShape, VoxelShape blockShape) {
        if (connectorType == ConnectionType.PIPE) {
            return Shapes.join(shape, cableShape, BooleanOp.OR);
        } else if (connectorType == ConnectionType.BLOCK) {
            return Shapes.join(shape, Shapes.join(blockShape, cableShape, BooleanOp.OR), BooleanOp.OR);
        } else {
            return shape;
        }
    }
}