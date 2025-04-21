package dev.wolfieboy09.qstorage.block.pipe.network;

import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlock;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlockEntity;
import dev.wolfieboy09.qstorage.block.pipe.ConnectionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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
        //PipeDebugRendering.addPos(pos);
        pipeNetworks.computeIfAbsent(level, l -> new HashMap<>())
                    .computeIfAbsent(pos, PipeConnection::new);
    }

    public static void sideDisconnected(@NotNull Level level, BlockPos mainPos, Direction disconnectionSide) {
        if (level.isClientSide) return;
        level.setBlockAndUpdate(mainPos,level.getBlockState(mainPos));
        var relativePos = mainPos.relative(disconnectionSide,1);
        if (level.getBlockState(relativePos).getBlock() instanceof BasePipeBlock<?>) {
            BasePipeBlockEntity be = (BasePipeBlockEntity) level.getBlockEntity(relativePos);
            if (be == null) return;
            be.disconnect(disconnectionSide.getOpposite());
            level.setBlockAndUpdate(relativePos,level.getBlockState(relativePos));
        }
    }

    public static @NotNull Optional<PipeConnection> getPipe(Level level, BlockPos pos) {
        return Optional.ofNullable(pipeNetworks.getOrDefault(level, Collections.emptyMap()).get(pos));
    }

    public static void removePipe(@NotNull Level level, BlockPos pos) {
        if (level.isClientSide) return;
        Map<BlockPos, PipeConnection> network = pipeNetworks.get(level);
        //PipeDebugRendering.removePos(pos);
        if (network != null) {
            network.remove(pos);
            if (network.isEmpty()) {
                pipeNetworks.remove(level);
            }
        }
    }
}
