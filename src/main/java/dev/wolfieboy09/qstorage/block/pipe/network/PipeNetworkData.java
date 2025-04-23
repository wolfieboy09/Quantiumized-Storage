package dev.wolfieboy09.qstorage.block.pipe.network;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Saved data for pipe networks.
 * This class is responsible for persisting pipe network data across game sessions.
 */
public class PipeNetworkData extends SavedData {
    private final Map<BlockPos, PipeConnection> pipeConnections;
    private final NetworkService networkService;

    /**
     * Creates a new empty PipeNetworkData.
     */
    public PipeNetworkData() {
        this.pipeConnections = new HashMap<>();
        this.networkService = new NetworkService();
    }

    /**
     * Creates a PipeNetworkData from the given tag.
     * @param tag The tag to load from
     * @param lookupProvider The lookup provider
     */
    public PipeNetworkData(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.pipeConnections = new HashMap<>();
        this.networkService = new NetworkService();

        // Load pipe connections
        if (tag.contains("Pipes")) {
            ListTag pipesList = tag.getList("Pipes", 10); // 10 is the ID for CompoundTag
            for (int i = 0; i < pipesList.size(); i++) {
                CompoundTag pipeTag = pipesList.getCompound(i);
                BlockPos pos = BlockPos.CODEC.decode(NbtOps.INSTANCE, pipeTag.get("pos")).getOrThrow().getFirst();

                PipeConnection connection = new PipeConnection(pos);
                connection.loadFromNbt(pipeTag);
                this.pipeConnections.put(pos, connection);
            }
        }

        // Load networks
        if (tag.contains("Networks")) {
            ListTag networksList = tag.getList("Networks", 10); // 10 is the ID for CompoundTag
            for (int i = 0; i < networksList.size(); i++) {
                CompoundTag networkTag = networksList.getCompound(i);
                PipeNetwork network = PipeNetwork.loadFromNbt(networkTag);

                // Add the network to the manager
                UUID networkId = network.getNetworkId();
                for (BlockPos pos : network.getAllMembers()) {
                    this.networkService.addPipeToNetwork(pos, networkId);
                }
            }
        }
    }

    /**
     * Creates a new empty PipeNetworkData.
     * @return A new PipeNetworkData
     */
    public static @NotNull PipeNetworkData create() {
        return new PipeNetworkData();
    }

    /**
     * Loads a PipeNetworkData from the given tag.
     * @param tag The tag to load from
     * @param lookupProvider The lookup provider
     * @return The loaded PipeNetworkData
     */
    public static @NotNull PipeNetworkData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        return new PipeNetworkData(tag, lookupProvider);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        // Save pipe connections
        ListTag pipesList = new ListTag();
        for (Map.Entry<BlockPos, PipeConnection> entry : this.pipeConnections.entrySet()) {
            BlockPos pos = entry.getKey();
            PipeConnection connection = entry.getValue();

            CompoundTag pipeTag = new CompoundTag();
            pipeTag.put("pos", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).getOrThrow());

            connection.saveToNbt(pipeTag);
            pipesList.add(pipeTag);
        }
        tag.put("Pipes", pipesList);
        QuantiumizedStorage.LOGGER.debug("Saved {} pipe connections", pipesList.size());

        // Save networks
        Set<PipeNetwork> allNetworks = this.networkService.getAllNetworks();
        ListTag networksList = new ListTag();
        for (PipeNetwork network : allNetworks) {
            CompoundTag networkTag = new CompoundTag();
            network.saveToNbt(networkTag);
            networksList.add(networkTag);
            QuantiumizedStorage.LOGGER.debug("Saved network {} with {} members", network.getNetworkId(), network.getMemberCount());
        }
        tag.put("Networks", networksList);
        QuantiumizedStorage.LOGGER.debug("Saved {} networks", networksList.size());

        return tag;
    }

    /**
     * Registers a pipe in the network data.
     * @param pipePos The position of the pipe
     */
    public void registerPipe(BlockPos pipePos) {
        QuantiumizedStorage.LOGGER.debug("PipeNetworkData.registerPipe: {}", pipePos);
        PipeConnection connection = this.pipeConnections.computeIfAbsent(pipePos, PipeConnection::new);
        QuantiumizedStorage.LOGGER.debug("PipeNetworkData.registerPipe: Created connection at {}", pipePos);
        this.setDirty();
    }

    /**
     * Unregisters a pipe from the network data.
     * @param pipePos The position of the pipe
     */
    public void unregisterPipe(BlockPos pipePos) {
        this.pipeConnections.remove(pipePos);
        this.networkService.removePipeFromNetwork(pipePos);
        this.setDirty();
    }

    /**
     * Checks if a pipe is registered.
     * @param pipePos The position of the pipe
     * @return True if the pipe is registered, false otherwise
     */
    public boolean isPipeRegistered(BlockPos pipePos) {
        return this.pipeConnections.containsKey(pipePos);
    }

    /**
     * Gets the connection for a pipe.
     * @param pipePos The position of the pipe
     * @return The pipe connection, or null if not found
     */
    public PipeConnection getPipeConnection(BlockPos pipePos) {
        return this.pipeConnections.get(pipePos);
    }

    /**
     * Sets the connection state for a pipe in a specific direction.
     * @param pipePos The position of the pipe
     * @param direction The direction
     * @param state The connection state
     */
    public void setConnectionState(BlockPos pipePos, Direction direction, ConnectionState state) {
        PipeConnection connection = this.pipeConnections.get(pipePos);
        if (connection == null) return;

        connection.setConnectionState(direction, state);
        this.setDirty();
    }

    /**
     * Gets the connection state for a pipe in a specific direction.
     * @param pipePos The position of the pipe
     * @param direction The direction
     * @return The connection state
     */
    public ConnectionState getConnectionState(BlockPos pipePos, Direction direction) {
        PipeConnection connection = this.pipeConnections.get(pipePos);
        if (connection == null) return ConnectionState.NONE;

        return connection.getConnectionState(direction);
    }

    /**
     * Enables a connection for a pipe in a specific direction.
     * @param pipePos The position of the pipe
     * @param direction The direction
     */
    public void enableConnection(BlockPos pipePos, Direction direction) {
        QuantiumizedStorage.LOGGER.debug("PipeNetworkData.enableConnection: {} at {}", direction, pipePos);
        PipeConnection connection = this.pipeConnections.get(pipePos);
        if (connection == null) return;

        connection.enableConnection(direction);
        this.setDirty();
    }

    /**
     * Disables a connection for a pipe in a specific direction.
     * @param pipePos The position of the pipe
     * @param direction The direction
     */
    public void disableConnection(BlockPos pipePos, Direction direction) {
        QuantiumizedStorage.LOGGER.debug("PipeNetworkData.disableConnection: {} at {}", direction, pipePos);
        PipeConnection connection = this.pipeConnections.get(pipePos);
        if (connection == null) return;

        connection.disableConnection(direction);
        this.setDirty();
    }

    /**
     * Checks if a connection is enabled for a pipe in a specific direction.
     * @param pipePos The position of the pipe
     * @param direction The direction
     * @return True if the connection is enabled, false otherwise
     */
    public boolean isConnectionEnabled(BlockPos pipePos, Direction direction) {
        PipeConnection connection = this.pipeConnections.get(pipePos);
        if (connection == null) return false;

        return connection.isConnectionEnabled(direction);
    }

    /**
     * Assigns a pipe to a network.
     * @param pipePos The position of the pipe
     * @param networkId The ID of the network
     */
    public void assignPipeToNetwork(BlockPos pipePos, UUID networkId) {
        this.networkService.addPipeToNetwork(pipePos, networkId);
        this.setDirty();
    }

    /**
     * Removes a pipe from its network.
     * @param pipePos The position of the pipe
     */
    public void removePipeFromNetwork(BlockPos pipePos) {
        this.networkService.removePipeFromNetwork(pipePos);
        this.setDirty();
    }

    /**
     * Gets the network ID for a pipe.
     * @param pipePos The position of the pipe
     * @return The network ID, or null if not found
     */
    public UUID getNetworkForPipe(BlockPos pipePos) {
        PipeNetwork network = this.networkService.getNetworkForPipe(pipePos);
        if (network == null) return null;
        return network.getNetworkId();
    }

    /**
     * Gets the network manager.
     * @return The network manager
     */
    public NetworkService getNetworkManager() {
        return this.networkService;
    }

    /**
     * Gets all pipe connections.
     * @return A map of all pipe connections
     */
    public Map<BlockPos, PipeConnection> getAllPipeConnections() {
        return this.pipeConnections;
    }

    /**
     * Legacy support: Gets or creates a pipe connection.
     * @param pos The position of the pipe
     * @return The pipe connection
     */
    public PipeConnection getOrCreatePipe(BlockPos pos) {
        QuantiumizedStorage.LOGGER.debug("PipeNetworkData.getOrCreatePipe: {}", pos);
        PipeConnection connection = this.pipeConnections.computeIfAbsent(pos, PipeConnection::new);
        QuantiumizedStorage.LOGGER.debug("PipeNetworkData.getOrCreatePipe: Created connection, disconnectedSides = {}", connection.getDisconnectedSides());
        this.setDirty();
        return connection;
    }

    /**
     * Legacy support: Removes a pipe.
     * @param pos The position of the pipe
     */
    public void removePipe(BlockPos pos) {
        unregisterPipe(pos);
    }

    /**
     * Legacy support: Gets a pipe connection.
     * @param pos The position of the pipe
     * @return An optional containing the pipe connection
     */
    public Optional<PipeConnection> getPipe(BlockPos pos) {
        return Optional.ofNullable(getPipeConnection(pos));
    }

    /**
     * Legacy support: Disconnects a side of a pipe.
     * @param pos The position of the pipe
     * @param side The side to disconnect
     */
    public void disconnectSide(BlockPos pos, Direction side) {
        disableConnection(pos, side);
    }

    /**
     * Legacy support: Reconnects a side of a pipe.
     * @param pos The position of the pipe
     * @param side The side to reconnect
     */
    public void reconnectSide(BlockPos pos, Direction side) {
        enableConnection(pos, side);
    }

    /**
     * Legacy support: Checks if a side of a pipe is disconnected.
     * @param pos The position of the pipe
     * @param side The side to check
     * @return True if the side is disconnected, false otherwise
     */
    public boolean isSideDisconnected(BlockPos pos, Direction side) {
        return !isConnectionEnabled(pos, side);
    }
}
