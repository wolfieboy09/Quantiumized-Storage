package dev.wolfieboy09.qstorage.block.pipe.network;

import dev.wolfieboy09.qstorage.block.pipe.ConnectionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

/**
 * Represents a pipe's connections in all directions.
 */
public class PipeConnection {
    private final BlockPos position;
    private final EnumMap<Direction, ConnectionState> connectionStates;

    /**
     * Creates a new PipeConnection at the specified position.
     * @param position The position of the pipe
     */
    public PipeConnection(BlockPos position) {
        this.position = position;
        this.connectionStates = new EnumMap<>(Direction.class);

        // Initialize all connections to NONE
        for (Direction dir : Direction.values()) {
            this.connectionStates.put(dir, ConnectionState.NONE);
        }
    }

    /**
     * Saves this pipe connection to NBT.
     * @param tag The tag to save to
     */
    public void saveToNbt(CompoundTag tag) {
        CompoundTag connectionsTag = new CompoundTag();
        for (Map.Entry<Direction, ConnectionState> entry : this.connectionStates.entrySet()) {
            connectionsTag.putInt(entry.getKey().getSerializedName(), entry.getValue().ordinal());
        }
        tag.put("ConnectionStates", connectionsTag);
    }

    /**
     * Loads this pipe connection from NBT.
     * @param tag The tag to load from
     */
    public void loadFromNbt(@NotNull CompoundTag tag) {
        if (tag.contains("ConnectionStates")) {
            CompoundTag connectionsTag = tag.getCompound("ConnectionStates");

            for (Direction dir : Direction.values()) {
                String key = dir.getSerializedName();
                if (connectionsTag.contains(key)) {
                    int stateOrdinal = connectionsTag.getInt(key);
                    if (stateOrdinal >= 0 && stateOrdinal < ConnectionState.values().length) {
                        this.connectionStates.put(dir, ConnectionState.values()[stateOrdinal]);
                    }
                }
            }
        } else if (tag.contains("DisconnectedSides")) {
            // Legacy support for old format
            CompoundTag disconnectedTag = tag.getCompound("DisconnectedSides");

            for (Direction dir : Direction.values()) {
                String key = dir.getSerializedName();
                if (disconnectedTag.contains(key) && disconnectedTag.getBoolean(key)) {
                    this.connectionStates.put(dir, ConnectionState.MANUALLY_DISABLED);
                }
            }
        }
    }

    /**
     * Gets the position of this pipe.
     * @return The position
     */
    public BlockPos getPosition() {
        return this.position;
    }

    /**
     * Sets the connection state in the specified direction.
     * @param direction The direction
     * @param state The new connection state
     */
    public void setConnectionState(Direction direction, ConnectionState state) {
        this.connectionStates.put(direction, state);
    }

    /**
     * Gets the connection state in the specified direction.
     * @param direction The direction
     * @return The connection state
     */
    public ConnectionState getConnectionState(Direction direction) {
        return this.connectionStates.getOrDefault(direction, ConnectionState.NONE);
    }

    /**
     * Enables the connection in the specified direction.
     * @param direction The direction
     */
    public void enableConnection(Direction direction) {
        ConnectionState currentState = getConnectionState(direction);
        if (currentState.isDisabled()) {
            this.connectionStates.put(direction, ConnectionState.NONE);
        }
    }

    /**
     * Disables the connection in the specified direction.
     * @param direction The direction
     */
    public void disableConnection(Direction direction) {
        this.connectionStates.put(direction, ConnectionState.MANUALLY_DISABLED);
    }

    /**
     * Checks if the connection in the specified direction is enabled.
     * @param direction The direction
     * @return True if enabled, false if disabled
     */
    public boolean isConnectionEnabled(Direction direction) {
        return !getConnectionState(direction).isDisabled();
    }

    /**
     * Gets all connection states.
     * @return An unmodifiable map of all connection states
     */
    public @Unmodifiable Map<Direction, ConnectionState> getAllConnectionStates() {
        return Collections.unmodifiableMap(this.connectionStates);
    }

    /**
     * Gets all directions that have active connections.
     * @return A set of directions with active connections
     */
    public Set<Direction> getConnectedDirections() {
        Set<Direction> result = new HashSet<>();
        for (Map.Entry<Direction, ConnectionState> entry : this.connectionStates.entrySet()) {
            if (entry.getValue().isConnected()) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    /**
     * Gets all directions that have disabled connections.
     * @return A set of directions with disabled connections
     */
    public Set<Direction> getDisabledDirections() {
        Set<Direction> result = new HashSet<>();
        for (Map.Entry<Direction, ConnectionState> entry : this.connectionStates.entrySet()) {
            if (entry.getValue().isDisabled()) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    /**
     * Converts a ConnectionState to a ConnectionType for block state rendering.
     * @param state The connection state
     * @return The corresponding connection type
     */
    public static ConnectionType toConnectionType(ConnectionState state) {
        return switch (state) {
            case CONNECTED_TO_PIPE -> ConnectionType.PIPE;
            case CONNECTED_TO_BLOCK -> ConnectionType.BLOCK_NORMAL;
            case CONNECTED_TO_BLOCK_TO_EXTRACT -> ConnectionType.BLOCK_EXTRACT;
            default -> ConnectionType.NONE;
        };
    }
}
