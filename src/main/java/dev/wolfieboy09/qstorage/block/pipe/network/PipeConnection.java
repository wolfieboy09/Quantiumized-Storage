package dev.wolfieboy09.qstorage.block.pipe.network;

import dev.wolfieboy09.qstorage.block.pipe.ConnectionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class PipeConnection {
    private final BlockPos pos;
    private final EnumMap<Direction, ConnectionType> connections;
    private final EnumMap<Direction, Boolean> disconnectedSides;

    public PipeConnection(BlockPos pos) {
        this.pos = pos;
        this.connections = new EnumMap<>(Direction.class);
        this.disconnectedSides = new EnumMap<>(Direction.class);
    }

    public void saveToNbt(CompoundTag tag) {
        CompoundTag disconnectedTag = new CompoundTag();
        for (Map.Entry<Direction, Boolean> entry : this.disconnectedSides.entrySet()) {
            disconnectedTag.putBoolean(entry.getKey().getSerializedName(), entry.getValue());
        }
        tag.put("DisconnectedSides", disconnectedTag);
    }

    public void loadFromNbt(@NotNull CompoundTag tag) {
        if (tag.contains("DisconnectedSides")) {
            CompoundTag disconnectedTag = tag.getCompound("DisconnectedSides");
            this.disconnectedSides.clear();

            for (Direction dir : Direction.values()) {
                String key = dir.getSerializedName();
                if (disconnectedTag.contains(key)) {
                    this.disconnectedSides.put(dir, disconnectedTag.getBoolean(key));
                }
            }
        }
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public void setConnection(Direction dir, ConnectionType type) {
        this.connections.put(dir, type);
    }

    public ConnectionType getConnection(Direction dir) {
        return this.connections.getOrDefault(dir, ConnectionType.NONE);
    }

    public Map<Direction, ConnectionType> getAllConnections() {
        return Collections.unmodifiableMap(this.connections);
    }

    public void disconnectSide(Direction direction) {
        this.disconnectedSides.put(direction, true);
    }

    public void reconnectSide(Direction direction) {
        this.disconnectedSides.remove(direction);
    }

    public boolean isSideDisconnected(Direction direction) {
        return Boolean.TRUE.equals(this.disconnectedSides.get(direction));
    }

    public EnumMap<Direction, Boolean> getDisconnectedSides() {
        return this.disconnectedSides;
    }
}
