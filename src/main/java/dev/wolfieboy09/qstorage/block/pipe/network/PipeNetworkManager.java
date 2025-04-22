package dev.wolfieboy09.qstorage.block.pipe.network;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlock;
import dev.wolfieboy09.qstorage.block.pipe.ConnectionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PipeNetworkManager {
    private static final String SAVED_DATA_NAME = "qstorage_pipe_networks";

    public static class PipeNetworkData extends SavedData {
        private final Map<BlockPos, PipeConnection> pipeConnections;

        public PipeNetworkData() {
            this.pipeConnections = new HashMap<>();
        }

        public PipeNetworkData(Level level, CompoundTag tag, HolderLookup.Provider lookupProvider) {
            this.pipeConnections = new HashMap<>();

            ListTag pipesList = tag.getList("Pipes", 10); // 10 is the ID for CompoundTag
            for (int i = 0; i < pipesList.size(); i++) {
                CompoundTag pipeTag = pipesList.getCompound(i);
                int x = pipeTag.getInt("X");
                int y = pipeTag.getInt("Y");
                int z = pipeTag.getInt("Z");
                BlockPos pos = new BlockPos(x, y, z);

                PipeConnection connection = new PipeConnection(pos);
                connection.loadFromNbt(pipeTag);
                this.pipeConnections.put(pos, connection);
            }
        }

        // Create new instance of saved data
        public static PipeNetworkData create() {
            return new PipeNetworkData();
        }

        // Load existing instance of saved data
        public static PipeNetworkData load(Level level, CompoundTag tag, HolderLookup.Provider lookupProvider) {
            return new PipeNetworkData(level, tag, lookupProvider);
        }

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
            ListTag pipesList = new ListTag();

            for (Map.Entry<BlockPos, PipeConnection> entry : this.pipeConnections.entrySet()) {
                BlockPos pos = entry.getKey();
                PipeConnection connection = entry.getValue();

                CompoundTag pipeTag = new CompoundTag();
                pipeTag.putInt("X", pos.getX());
                pipeTag.putInt("Y", pos.getY());
                pipeTag.putInt("Z", pos.getZ());

                connection.saveToNbt(pipeTag);
                pipesList.add(pipeTag);
            }

            tag.put("Pipes", pipesList);
            return tag;
        }

        public PipeConnection getOrCreatePipe(BlockPos pos) {
            QuantiumizedStorage.LOGGER.debug("PipeNetworkData.getOrCreatePipe: {}", pos);
            PipeConnection connection = this.pipeConnections.computeIfAbsent(pos, PipeConnection::new);
            QuantiumizedStorage.LOGGER.debug("PipeNetworkData.getOrCreatePipe: Created connection, disconnectedSides = {}", connection.getDisconnectedSides());
            this.setDirty();
            return connection;
        }

        public void removePipe(BlockPos pos) {
            this.pipeConnections.remove(pos);
            this.setDirty();
        }

        public Optional<PipeConnection> getPipe(BlockPos pos) {
            return Optional.ofNullable(this.pipeConnections.get(pos));
        }

        public void disconnectSide(BlockPos pos, Direction side) {
            QuantiumizedStorage.LOGGER.debug("PipeNetworkData.disconnectSide: {} at {}", side, pos);
            getPipe(pos).ifPresent(pipe -> {
                pipe.disconnectSide(side);
                this.setDirty();
            });
        }

        public void reconnectSide(BlockPos pos, Direction side) {
            QuantiumizedStorage.LOGGER.debug("PipeNetworkData.reconnectSide: {} at {}", side, pos);
            getPipe(pos).ifPresent(pipe -> {
                pipe.reconnectSide(side);
                this.setDirty();
            });
        }

        public boolean isSideDisconnected(BlockPos pos, Direction side) {
            return getPipe(pos)
                    .map(pipe -> pipe.isSideDisconnected(side))
                    .orElse(false);
        }
    }

    private static final Map<Level, Map<BlockPos, PipeConnection>> pipeNetworks = new HashMap<>();

    // Get or create the SavedData for a level
    private static PipeNetworkData getOrCreateSavedData(Level level) {
        if (level.isClientSide || level.getServer() == null) {
            return null;
        }

        var dataStorage = level.getServer().overworld().getDataStorage();
        return dataStorage.computeIfAbsent(
            new SavedData.Factory<>(
                    PipeNetworkData::create,
                (tag, lookupProvider) -> {
                    return PipeNetworkData.load(level, tag, lookupProvider);
                }
            ),
            SAVED_DATA_NAME
        );
    }

    // Sync the in-memory cache with the SavedData
    private static void syncWithSavedData(Level level) {
        if (level.isClientSide) return;

        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData == null) return;

        // Clear the in-memory cache for this level
        Map<BlockPos, PipeConnection> networkMap = new HashMap<>();
        pipeNetworks.put(level, networkMap);

        // Copy all pipe connections from the saved data to the in-memory cache
        for (Map.Entry<BlockPos, PipeConnection> entry : savedData.pipeConnections.entrySet()) {
            BlockPos pos = entry.getKey();
            PipeConnection connection = entry.getValue();

            // Create a new connection in the in-memory cache
            PipeConnection newConnection = networkMap.computeIfAbsent(pos, PipeConnection::new);

            // Copy all disconnected sides
            connection.getDisconnectedSides().forEach(
                    (side, value) -> newConnection.disconnectSide(side)
            );
        }
    }

    public static PipeConnection getOrCreatePipe(Level level, BlockPos pos) {
        QuantiumizedStorage.LOGGER.debug("PipeNetworkManager.getOrCreatePipe: {}", pos);

        // First, update the in-memory cache
        PipeConnection connection = pipeNetworks.computeIfAbsent(level, l -> new HashMap<>())
                    .computeIfAbsent(pos, PipeConnection::new);

        // Then, update the SavedData if on the server
        if (!level.isClientSide) {
            PipeNetworkData savedData = getOrCreateSavedData(level);
            if (savedData != null) {
                PipeConnection savedConnection = savedData.getOrCreatePipe(pos);
            }
        }

        return connection;
    }

    public static void addPipe(@NotNull Level level, BlockPos pos) {
        if (level.isClientSide) return;

        // Update the in-memory cache
        pipeNetworks.computeIfAbsent(level, l -> new HashMap<>())
                    .computeIfAbsent(pos, PipeConnection::new);

        // Update the SavedData
        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData != null) {
            savedData.getOrCreatePipe(pos);
            QuantiumizedStorage.LOGGER.info("Pipe added at {}", pos);
        }
    }

    public static void sideDisconnected(@NotNull Level level, BlockPos mainPos, Direction disconnectionSide) {
        if (level.isClientSide) return;


        // Update the main pipe's disconnected sides in the in-memory network
        PipeConnection mainPipe = getOrCreatePipe(level, mainPos);
        mainPipe.disconnectSide(disconnectionSide);
        QuantiumizedStorage.LOGGER.debug("PipeNetworkManager.sideDisconnected: After disconnection, is side disconnected? {}", mainPipe.isSideDisconnected(disconnectionSide));

        // Update the SavedData
        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData != null) {
            savedData.disconnectSide(mainPos, disconnectionSide);
            QuantiumizedStorage.LOGGER.debug("PipeNetworkManager.sideDisconnected: After disconnection, is side disconnected? {}", savedData.isSideDisconnected(mainPos, disconnectionSide));
        }

        // Update the block state
        BlockState state = level.getBlockState(mainPos);
        level.setBlockAndUpdate(mainPos, state.setValue(BasePipeBlock.getPropertyFromDirection(disconnectionSide), ConnectionType.NONE));

        // Update the connected pipe's disconnected sides
        var relativePos = mainPos.relative(disconnectionSide, 1);
        if (level.getBlockState(relativePos).getBlock() instanceof BasePipeBlock<?>) {
            // Update in-memory network
            PipeConnection relativePipe = getOrCreatePipe(level, relativePos);
            relativePipe.disconnectSide(disconnectionSide.getOpposite());

            // Update SavedData
            if (savedData != null) {
                savedData.disconnectSide(relativePos, disconnectionSide.getOpposite());
            }

            // Update the block state
            BlockState relativeState = level.getBlockState(relativePos);
            level.setBlockAndUpdate(relativePos, relativeState.setValue(BasePipeBlock.getPropertyFromDirection(disconnectionSide.getOpposite()), ConnectionType.NONE));
        }
    }

    public static void sideConnected(Level level, BlockPos mainPos, Direction connectedSide) {
        if (level.isClientSide) return;

        // Update the main pipe's disconnected sides in the in-memory network
        PipeConnection mainPipe = getOrCreatePipe(level, mainPos);
        mainPipe.reconnectSide(connectedSide);

        // Update the SavedData
        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData != null) {
            savedData.reconnectSide(mainPos, connectedSide);
        }

        // Update the block state
        BlockState state = level.getBlockState(mainPos);
        BasePipeBlock<?> pipe = (BasePipeBlock<?>) state.getBlock();
        ConnectionType connectorType = pipe.getConnectorType(level, mainPos, connectedSide);
        level.setBlockAndUpdate(mainPos, state.setValue(BasePipeBlock.getPropertyFromDirection(connectedSide), connectorType));

        // Update the connected pipe's disconnected sides
        BlockPos relativePos = mainPos.relative(connectedSide, 1);
        if (level.getBlockState(relativePos).getBlock() instanceof BasePipeBlock<?>) {
            // Update in-memory network
            PipeConnection relativePipe = getOrCreatePipe(level, relativePos);
            relativePipe.reconnectSide(connectedSide.getOpposite());

            // Update SavedData
            if (savedData != null) {
                savedData.reconnectSide(relativePos, connectedSide.getOpposite());
            }

            // Update the block state
            BlockState relativeState = level.getBlockState(relativePos);
            BasePipeBlock<?> relativePipe2 = (BasePipeBlock<?>) relativeState.getBlock();
            ConnectionType relativeConnectorType = relativePipe2.getConnectorType(level, relativePos, connectedSide.getOpposite());
            level.setBlockAndUpdate(relativePos, relativeState.setValue(BasePipeBlock.getPropertyFromDirection(connectedSide.getOpposite()), relativeConnectorType));
        }
    }

    public static @NotNull Optional<PipeConnection> getPipe(Level level, BlockPos pos) {
        // First check the in-memory cache
        Optional<PipeConnection> result = Optional.ofNullable(pipeNetworks.getOrDefault(level, Collections.emptyMap()).get(pos));

        // If not found and we're on the server, try to load from SavedData
        if (result.isEmpty() && !level.isClientSide) {
            PipeNetworkData savedData = getOrCreateSavedData(level);
            if (savedData != null) {
                Optional<PipeConnection> savedPipe = savedData.getPipe(pos);
                if (savedPipe.isPresent()) {
                    // Add to in-memory cache
                    PipeConnection connection = getOrCreatePipe(level, pos);
                    // Copy disconnected sides
                    savedPipe.get().getDisconnectedSides().forEach(
                            (side, value) -> connection.disconnectSide(side)
                    );
                    return Optional.of(connection);
                }
            }
        }

        return result;
    }

    public static boolean isSideDisconnected(Level level, BlockPos pos, Direction side) {
        // First check the in-memory cache
        Optional<PipeConnection> pipe = getPipe(level, pos);
        if (pipe.isPresent()) {
            return pipe.get().isSideDisconnected(side);
        }

        // If not found and we're on the server, check SavedData
        if (!level.isClientSide) {
            PipeNetworkData savedData = getOrCreateSavedData(level);
            if (savedData != null) {
                return savedData.isSideDisconnected(pos, side);
            }
        }

        return false;
    }

    public static void removePipe(@NotNull Level level, BlockPos pos) {
        if (level.isClientSide) return;

        // Remove from in-memory cache
        Map<BlockPos, PipeConnection> network = pipeNetworks.get(level);
        if (network != null) {
            network.remove(pos);
            if (network.isEmpty()) {
                pipeNetworks.remove(level);
            }
        }

        // Remove from SavedData
        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData != null) {
            savedData.removePipe(pos);
            QuantiumizedStorage.LOGGER.info("Pipe removed at {}", pos);
        }
    }
}
