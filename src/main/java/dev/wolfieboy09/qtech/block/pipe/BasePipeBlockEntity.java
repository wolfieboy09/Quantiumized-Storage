package dev.wolfieboy09.qtech.block.pipe;

import dev.wolfieboy09.qtech.block.GlobalBlockEntity;
import dev.wolfieboy09.qtech.block.pipe.network.ConnectionState;
import dev.wolfieboy09.qtech.block.pipe.network.PipeNetwork;
import dev.wolfieboy09.qtech.block.pipe.network.PipeNetworkData;
import dev.wolfieboy09.qtech.block.pipe.network.PipeNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public abstract class BasePipeBlockEntity<T> extends GlobalBlockEntity {
    public BasePipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
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
    protected abstract Set<Direction> getExtractableDirections();

    /**
     * Determines whether a given direction on a pipe is valid for inserting into a target.
     * This is used to filter out non-insertable connections.
     *
     * @param targetPipe The position of the target pipe block.
     * @param direction  The direction being considered for insertion.
     * @return true if the pipe can insert in this direction, false otherwise.
     */
    protected abstract boolean isInsertDirectionValid(BlockPos targetPipe, Direction direction);

    /**
     * Retrieves the source capability at the given position and direction.
     * Typically used to access the inventory/fluid/energy handler of an adjacent block.
     *
     * @param pos       The position of the block to extract from.
     * @param direction The direction from which the extraction is attempted.
     * @return A capability instance for the source, or null if unavailable.
     */
    protected abstract T getSourceAt(BlockPos pos, Direction direction);

    /**
     * Retrieves the target capability at the given position and direction.
     * Typically used to access the inventory/fluid/energy handler of a potential destination block.
     *
     * @param pos       The position of the block to insert into.
     * @param direction The direction from which the insertion is attempted.
     * @return A capability instance for the target, or null if unavailable.
     */
    protected abstract T getTargetAt(BlockPos pos, Direction direction);

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
}
