package dev.wolfieboy09.qstorage.block.pipe.network;

import dev.wolfieboy09.qstorage.block.pipe.ConnectionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class PipeConnection {
    private final BlockPos pos;
    private final EnumMap<Direction, ConnectionType> connections;

    public PipeConnection(BlockPos pos) {
        this.pos = pos;
        this.connections = new EnumMap<>(Direction.class);
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
}
