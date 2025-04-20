package dev.wolfieboy09.qstorage.block.pipe.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PipeNetworkManager {
    private static final Map<Level, Map<BlockPos, PipeConnection>> pipeNetworks = new HashMap<>();

    public static PipeConnection getOrCreatePipe(Level level, BlockPos pos) {
        return pipeNetworks.computeIfAbsent(level, l -> new HashMap<>())
                    .computeIfAbsent(pos, PipeConnection::new);
    }

    public static void addPipe(@NotNull Level level, BlockPos pos) {
        if (level.isClientSide) return;
        pipeNetworks.computeIfAbsent(level, l -> new HashMap<>())
                    .computeIfAbsent(pos, PipeConnection::new);
    }

    public static @NotNull Optional<PipeConnection> getPipe(Level level, BlockPos pos) {
        return Optional.ofNullable(pipeNetworks.getOrDefault(level, Collections.emptyMap()).get(pos));
    }

    public static void removePipe(@NotNull Level level, BlockPos pos) {
        if (level.isClientSide) return;
        Map<BlockPos, PipeConnection> network = pipeNetworks.get(level);
        if (network != null) {
            network.remove(pos);
            if (network.isEmpty()) {
                pipeNetworks.remove(level);
            }
        }
    }
}
