package dev.wolfieboy09.qtech.block.pipe;

import dev.wolfieboy09.qtech.block.GlobalBlockEntity;
import dev.wolfieboy09.qtech.api.pipe.network.ConnectionState;
import dev.wolfieboy09.qtech.api.pipe.network.PipeNetwork;
import dev.wolfieboy09.qtech.api.pipe.network.PipeNetworkData;
import dev.wolfieboy09.qtech.api.pipe.network.PipeNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public abstract class BasePipeBlockEntity<T> extends GlobalBlockEntity {
    private final BlockCapability<T, @Nullable Direction> blockCapability;
    public static final BlockState NO_FACADE_STATE = Blocks.AIR.defaultBlockState();
    // Default to this
    private BlockState coverState = NO_FACADE_STATE;

    public BasePipeBlockEntity(BlockEntityType<?> type, BlockCapability<T, @Nullable Direction> blockCapability, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.blockCapability = blockCapability;
    }

    public void tick() {
        if (this.level == null) return;
        PipeNetworkData data = PipeNetworkManager.getOrCreateSavedData(this.level);
        if (data == null) return;
        PipeNetwork network = data.getNetworkByBlockPos(this.worldPosition);
        if (network == null) return;

        Set<Direction> extractable = getExtractableDirections();

        if (extractable.isEmpty()) return;

        List<BlockPos> sortedTargets = network.findNonExtractingPipes(network.getNetworkMap(this.level)).stream()
                .sorted(Comparator.comparingInt(pos -> pos.distManhattan(this.worldPosition)))
                .toList();

        for (Direction direction : extractable) {
            BlockPos sourcePos = this.worldPosition.relative(direction);
            T source = getSourceAt(sourcePos, direction.getOpposite());

            if (source != null && canExtract(source)) {
                outer:
                for (BlockPos targetPipe : sortedTargets) {
                    for (Direction insertDir : connectedTo(targetPipe)) {
                        if (!isInsertDirectionValid(targetPipe, insertDir)) continue;

                        BlockPos targetPos = targetPipe.relative(insertDir);
                        if (targetPipe.equals(this.worldPosition) && targetPos.equals(sourcePos)) continue;

                        T target = getTargetAt(targetPos, insertDir.getOpposite());
                        if (target != null && tryTransfer(source, target)) {
                            break outer;
                        }
                    }
                }
            }
        }
    }


    /**
     * Returns the directions from which this pipe can attempt to extract from.
     *
     * @return A set of directions where extraction is allowed.
     */
    protected Set<Direction> getExtractableDirections() {
        Set<Direction> directions = new HashSet<>();
        for (Direction dir : Direction.values()) {
            ConnectionType type = getBlockState().getValue(BasePipeBlock.getPropertyFromDirection(dir));
            if (type == ConnectionType.BLOCK_EXTRACT) {
                directions.add(dir);
            }
        }
        return directions;
    }

    /**
     * Determines whether a given direction on a pipe is valid for inserting into a target.
     * This is used to filter out non-insertable connections.
     *
     * @param targetPipe The position of the target pipe block.
     * @param direction  The direction being considered for insertion.
     * @return true if the pipe can insert in this direction, false otherwise.
     */
    protected boolean isInsertDirectionValid(BlockPos targetPipe, Direction direction) {
        if (this.level == null || this.level.isClientSide) return false;
        ConnectionType type = this.level.getBlockState(targetPipe).getValue(BasePipeBlock.getPropertyFromDirection(direction));
        return type == ConnectionType.BLOCK_NORMAL;
    }

    /**
     * Retrieves the source capability at the given position and direction.
     * Typically used to access the inventory/fluid/energy handler of an adjacent block.
     *
     * @param pos       The position of the block to extract from.
     * @param direction The direction from which the extraction is attempted.
     * @return A capability instance for the source, or null if unavailable.
     */
    protected T getSourceAt(BlockPos pos, Direction direction) {
        return this.level == null || this.level.isClientSide ? null : this.level.getCapability(this.blockCapability, pos, direction);
    }

    /**
     * Retrieves the target capability at the given position and direction.
     * Typically used to access the inventory/fluid/energy handler of a potential destination block.
     *
     * @param pos       The position of the block to insert into.
     * @param direction The direction from which the insertion is attempted.
     * @return A capability instance for the target, or null if unavailable.
     */
    protected T getTargetAt(BlockPos pos, Direction direction) {
        return this.level == null || this.level.isClientSide ? null : this.level.getCapability(this.blockCapability, pos, direction);
    }

    /**
     * Checks whether the given source capability can be extracted from.
     * This may include checking if there are items/fluids/energy available or if extraction is permitted.
     *
     * @param source The source capability.
     * @return true if there is something to extract, false otherwise.
     */
    protected abstract boolean canExtract(T source);

    /**
     * Attempts to transfer a single unit (or defined amount) from the source to the target.
     * This method should handle simulation and real transfer as appropriate.
     *
     * @param source The source capability.
     * @param target The target capability.
     * @return true if the transfer succeeded and no more processing is needed for this tick.
     */
    protected abstract boolean tryTransfer(T source, T target);



    protected @Unmodifiable Set<Direction> connectedTo(BlockPos pos) {
        if (this.level == null || this.level.isClientSide) return Collections.emptySet();
        Set<Direction> directions = new HashSet<>();
        for (Direction direction : Direction.values()) {
            ConnectionState type = PipeNetworkManager.getConnectionState(this.level, pos, direction);
            if (type != ConnectionState.NONE) {
                directions.add(direction);
            }
        }
        return Collections.unmodifiableSet(directions);
    }

    public BlockCapability<T, @Nullable Direction> getCapability() {
        return this.blockCapability;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (this.coverState != NO_FACADE_STATE) {
            tag.put("CoverState", NbtUtils.writeBlockState(this.coverState));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("CoverState")) {
            this.coverState = NbtUtils.readBlockState(getBlockGetter(), tag.getCompound("CoverState"));
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        if (this.coverState != NO_FACADE_STATE) {
            tag.put("CoverState", NbtUtils.writeBlockState(this.coverState));
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        if (tag.contains("CoverState")) {
            this.coverState = NbtUtils.readBlockState(getBlockGetter(), tag.getCompound("CoverState"));
        }
    }

    private HolderGetter<Block> getBlockGetter() {
        return this.level != null ? this.level.holderLookup(Registries.BLOCK) : BuiltInRegistries.BLOCK.asLookup();
    }

    public void updateFacadeBlock(BlockState setTo) {
        this.coverState = setTo;
    }

    public BlockState getFacadeState() {
        return this.coverState;
    }
}
