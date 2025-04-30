package dev.wolfieboy09.qtech.block.pipe.network;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.block.pipe.BasePipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Saved data for pipe networks.
 * This class is responsible for persisting pipe network data across game sessions.
 */
public class PipeNetworkData extends SavedData {
    private final Map<BlockPos, PipeConnection> pipeConnections;
    private final Map<UUID, PipeNetwork> networks;
    private final Map<BlockPos, UUID> pipeToNetworkMap;

    /**
     * Creates a new empty PipeNetworkData.
     */
    public PipeNetworkData() {
        this.pipeConnections = new HashMap<>();
        this.networks = new HashMap<>();
        this.pipeToNetworkMap = new HashMap<>();
    }

    /**
     * Creates a PipeNetworkData from the given tag.
     * @param tag The tag to load from
     * @param lookupProvider The lookup provider
     */
    public PipeNetworkData(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.pipeConnections = new HashMap<>();
        this.networks = new HashMap<>();
        this.pipeToNetworkMap = new HashMap<>();

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
                this.networks.put(networkId, network);
                for (BlockPos pos : network.getAllMembers()) {
                    this.pipeToNetworkMap.put(pos, networkId); // Directly map
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
        // We don't want to save invalid networks
        validateAllNetworks();
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
        QuantiumizedTech.LOGGER.debug("Saved {} pipe connections", pipesList.size());

        // Save networks
        Set<PipeNetwork> allNetworks = this.getAllNetworks();
        ListTag networksList = new ListTag();
        for (PipeNetwork network : allNetworks) {
            CompoundTag networkTag = new CompoundTag();
            network.saveToNbt(networkTag);
            networksList.add(networkTag);
            QuantiumizedTech.LOGGER.debug("Saved network {} with {} members", network.getNetworkId(), network.getMemberCount());
        }
        tag.put("Networks", networksList);
        QuantiumizedTech.LOGGER.debug("Saved {} networks", networksList.size());

        return tag;
    }

    /**
     * Registers a pipe in the network data.
     * @param pipePos The position of the pipe
     */
    public void registerPipe(BlockPos pipePos) {
        QuantiumizedTech.LOGGER.debug("PipeNetworkData.registerPipe: {}", pipePos);
        this.pipeConnections.computeIfAbsent(pipePos, PipeConnection::new);
        QuantiumizedTech.LOGGER.debug("PipeNetworkData.registerPipe: Created connection at {}", pipePos);
        this.setDirty();
    }

    /**
     * Unregisters a pipe from the network data.
     * @param pipePos The position of the pipe
     */
    public void unregisterPipe(BlockPos pipePos) {
        this.pipeConnections.remove(pipePos);
        this.removePipeFromNetwork(pipePos);
        this.setDirty();
    }

    /**
     * Gets the connection for a pipe.
     * @param pipePos The position of the pipe
     * @return The pipe connection, or null if not found
     */
    public @Nullable PipeConnection getPipeConnection(BlockPos pipePos) {
        return this.pipeConnections.getOrDefault(pipePos, null);
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
        QuantiumizedTech.LOGGER.debug("PipeNetworkData.enableConnection: {} at {}", direction, pipePos);
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
        QuantiumizedTech.LOGGER.debug("PipeNetworkData.disableConnection: {} at {}", direction, pipePos);
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
     * Removes a pipe from its network.
     * @param pipePos The position of the pipe
     */
    public void removePipeFromNetwork(BlockPos pipePos) {
        UUID networkId = this.pipeToNetworkMap.get(pipePos);
        if (networkId == null) return;

        PipeNetwork network = this.networks.get(networkId);
        if (network == null) return;

        network.removeMember(pipePos);
        this.pipeToNetworkMap.remove(pipePos);

        // If the network is now empty, remove it
        if (network.getMemberCount() == 0) {
            this.networks.remove(networkId);
        }
        this.setDirty();
    }

    /**
     * Gets the network ID for a pipe.
     * @param pipePos The position of the pipe
     * @return The network ID, or null if not found
     */
    public @Nullable UUID getNetworkForPipe(BlockPos pipePos) {
        return this.pipeToNetworkMap.getOrDefault(pipePos, null);
    }

    /**
     * Gets all pipe connections.
     * @return A map of all pipe connections
     */
    public Map<BlockPos, PipeConnection> getAllPipeConnections() {
        return this.pipeConnections;
    }

    /**
     * Creates a new network.
     * @param initialPipe The initial pipe in the network
     * @param type The type of the network
     * @return The created network
     */
    public PipeNetwork createNetwork(BlockPos initialPipe, NetworkType type) {
        // Check if the pipe is already in a network
        UUID existingNetworkId = this.pipeToNetworkMap.get(initialPipe);
        if (existingNetworkId != null) {
            // Pipe is already in a network, return that network
            PipeNetwork existingNetwork = this.networks.get(existingNetworkId);
            if (existingNetwork != null) {
                return existingNetwork;
            }
            // Network ID exists in map but network doesn't exist, remove the mapping
            this.pipeToNetworkMap.remove(initialPipe);
        }

        // Create a new network with a random UUID
        UUID networkId = UUID.randomUUID();
        PipeNetwork network = new PipeNetwork(networkId, initialPipe, type);

        // Add the network to the networks map
        this.networks.put(networkId, network);

        // Add the pipe to the network
        network.addMember(initialPipe);

        // Map the pipe to the network
        this.pipeToNetworkMap.put(initialPipe, networkId);
        this.setDirty();
        return network;
    }

    /**
     * Dissolves a network, removing it and all its pipes.
     * @param networkId The ID of the network to dissolve
     */
    public void dissolveNetwork(UUID networkId) {
        PipeNetwork network = this.networks.get(networkId);
        if (network == null) return;

        // Remove all pipes from the network
        for (BlockPos pos : network.getAllMembers()) {
            this.pipeToNetworkMap.remove(pos);
        }

        // Remove the network
        this.networks.remove(networkId);
        this.setDirty();
    }

    /**
     * Merges multiple networks into a target network.
     * @param targetNetworkId The ID of the target network
     * @param networkIdsToMerge The IDs of the networks to merge
     */
    public void mergeNetworks(UUID targetNetworkId, UUID... networkIdsToMerge) {
        PipeNetwork targetNetwork = this.networks.get(targetNetworkId);
        if (targetNetwork == null) {
            QuantiumizedTech.LOGGER.error("Cannot merge into non-existent network {}", targetNetworkId);
            return;
        }

        for (UUID networkId : networkIdsToMerge) {
            if (networkId.equals(targetNetworkId)) continue;

            PipeNetwork network = this.networks.get(networkId);
            if (network == null) {
                QuantiumizedTech.LOGGER.debug("Skipping merge with non-existent network {}", networkId);
                continue;
            }

            // Get all members before modifying the network
            Set<BlockPos> membersToMove = new HashSet<>(network.getAllMembers());

            // Add all pipes from the source network to the target network
            for (BlockPos pos : membersToMove) {
                targetNetwork.addMember(pos);
                this.pipeToNetworkMap.put(pos, targetNetworkId);
            }

            // Remove the source network
            this.networks.remove(networkId);
            QuantiumizedTech.LOGGER.debug("Merged network {} into {} with {} members", networkId, targetNetworkId, membersToMove.size());
        }
        this.setDirty();
    }

    /**
     * Splits a network into multiple networks.
     * @param networkId The ID of the network to split
     * @return The resulting networks
     */
    public Set<PipeNetwork> splitNetwork(UUID networkId) {
        PipeNetwork originalNetwork = this.networks.get(networkId);
        if (originalNetwork == null) return Collections.emptySet();

        // If the network has 0 or 1 pipes, it can't be split
        if (originalNetwork.getMemberCount() <= 1) {
            return Collections.singleton(originalNetwork);
        }

        // Find all connected components in the network
        Set<BlockPos> remainingPipes = new HashSet<>(originalNetwork.getAllMembers());
        Set<PipeNetwork> resultingNetworks = new HashSet<>();

        // First, verify that all pipes in the network are in the pipeToNetworkMap
        for (BlockPos pos : originalNetwork.getAllMembers()) {
            if (!this.pipeToNetworkMap.containsKey(pos)) {
                this.pipeToNetworkMap.put(pos, networkId);
            }
        }

        // Now split the network
        while (!remainingPipes.isEmpty()) {
            // Pick any remaining pipe
            BlockPos startPos = remainingPipes.iterator().next();

            // Find all pipes connected to it
            Set<BlockPos> connectedPipes = findConnectedPipes(startPos, remainingPipes);

            // Create a new network for this group
            PipeNetwork newNetwork = new PipeNetwork(UUID.randomUUID(), originalNetwork.getNetworkType());

            for (BlockPos pos : connectedPipes) {
                newNetwork.addMember(pos);
                this.pipeToNetworkMap.put(pos, newNetwork.getNetworkId());
                remainingPipes.remove(pos);
            }

            this.networks.put(newNetwork.getNetworkId(), newNetwork);
            resultingNetworks.add(newNetwork);
        }

        // Remove the original network
        this.networks.remove(networkId);
        this.setDirty();

        return resultingNetworks;
    }

    /**
     * Gets a network by its ID.
     * @param networkId The ID of the network
     * @return The network, or null if not found
     */
    public @Nullable PipeNetwork getNetworkById(UUID networkId) {
        return this.networks.getOrDefault(networkId, null);
    }

    /**
     * Gets a network by its {@link BlockPos} by wrapping {@link PipeNetworkData#getNetworkForPipe(BlockPos)} for fetching the network
     * @param pos The {@link BlockPos} of the pipe
     * @return The network, otherwise null if not found
     */
    public @Nullable PipeNetwork getNetworkByBlockPos(BlockPos pos) {
        return this.networks.getOrDefault(getNetworkForPipe(pos), null);
    }

    /**
     * Gets all networks.
     * @return An unmodifiable set of all networks
     */
    public Set<PipeNetwork> getAllNetworks() {
        return Set.copyOf(this.networks.values());
    }

    /**
     * Gets all networks of a specific type.
     * @param type The type of networks to get
     * @return A set of networks of the specified type
     */
    public Set<PipeNetwork> getNetworksByType(NetworkType type) {
        Set<PipeNetwork> result = new HashSet<>();
        for (PipeNetwork network : this.networks.values()) {
            if (network.getNetworkType() == type) {
                result.add(network);
            }
        }
        return result;
    }

    /**
     * Finds all pipes connected to a starting pipe, taking into account connection states.
     * @param startPos The position of the starting pipe
     * @param validPipes The set of valid pipes to consider
     * @return A set of connected pipe positions
     */
    public Set<BlockPos> findConnectedPipes(BlockPos startPos, Set<BlockPos> validPipes) {
        Set<BlockPos> connectedPipes = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>(); // Track visited positions to avoid cycles
        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            connectedPipes.add(current);

            // Add connected neighbors to queue
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.relative(dir);
                if (validPipes.contains(neighbor) && !visited.contains(neighbor)) {
                    // Check if the pipes are actually connected according to their connection states
                    ConnectionState currentToNeighbor = this.getConnectionState(current, dir);
                    ConnectionState neighborToCurrent = this.getConnectionState(neighbor, dir.getOpposite());

                    // If either connection state is CONNECTED_TO_PIPE, consider them connected
                    // This is more lenient and helps prevent false network splits
                    if (currentToNeighbor == ConnectionState.CONNECTED_TO_PIPE ||
                        neighborToCurrent == ConnectionState.CONNECTED_TO_PIPE) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
        }

        return connectedPipes;
    }

    /**
     * Checks if two pipes are connected.
     * @param pipePos1 The position of the first pipe
     * @param pipePos2 The position of the second pipe
     * @return True if the pipes are connected, false otherwise
     */
    public boolean arePipesConnected(BlockPos pipePos1, BlockPos pipePos2) {
        UUID network1 = this.pipeToNetworkMap.get(pipePos1);
        UUID network2 = this.pipeToNetworkMap.get(pipePos2);

        // If either pipe is not in a network, they're not connected
        if (network1 == null || network2 == null) return false;

        // If they're in the same network, they're connected
        return network1.equals(network2);
    }

    /**
     * Finds a path between two pipes.
     * @param startPos The position of the starting pipe
     * @param endPos The position of the ending pipe
     * @return A list of pipe positions forming a path, or an empty list if no path exists
     */
    public List<BlockPos> findPathBetween(BlockPos startPos, BlockPos endPos) {
        // If the pipes are not in the same network, there's no path
        if (!arePipesConnected(startPos, endPos)) {
            return Collections.emptyList();
        }

        // Use breadth-first search to find the shortest path
        Map<BlockPos, BlockPos> cameFrom = new HashMap<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(startPos);
        cameFrom.put(startPos, null);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();

            // If we've reached the end, reconstruct the path
            if (current.equals(endPos)) {
                List<BlockPos> path = new ArrayList<>();
                BlockPos pos = current;

                while (pos != null) {
                    path.add(pos);
                    pos = cameFrom.get(pos);
                }

                Collections.reverse(path);
                return path;
            }

            // Add connected neighbors to queue
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.relative(dir);

                // If the neighbor is in the same network, and we haven't visited it yet
                if (arePipesConnected(current, neighbor) && !cameFrom.containsKey(neighbor)) {
                    queue.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }

        // No path found
        return Collections.emptyList();
    }

    /**
     * Checks if a network is valid.
     * @param networkId The ID of the network to check
     * @return True if the network is valid, false otherwise
     */
    public boolean isNetworkValid(UUID networkId) {
        PipeNetwork network = this.networks.get(networkId);
        if (network == null) return false;

        // A network with 0 pipes is not valid
        if (network.getMemberCount() == 0) return false;

        // Check if all pipes in the network are connected
        Set<BlockPos> connectedPipes = findConnectedPipes(network.getAllMembers().iterator().next(), network.getAllMembers());
        return connectedPipes.size() == network.getMemberCount();
    }

    /**
     * Validates all networks, removing invalid ones.
     */
    public void validateAllNetworks() {
        Set<UUID> invalidNetworks = new HashSet<>();

        for (UUID networkId : this.networks.keySet()) {
            if (!isNetworkValid(networkId)) {
                invalidNetworks.add(networkId);
            }
        }

        for (UUID networkId : invalidNetworks) {
            dissolveNetwork(networkId);
        }
        this.setDirty();
    }

    /**
     * Adds a pipe to a network.
     *
     * @param pipePos   The position of the pipe
     * @param networkId The ID of the network
     */
    public void addPipeToNetwork(BlockPos pipePos, UUID networkId) {
        // Check if the pipe is already in a network
        UUID existingNetworkId = this.pipeToNetworkMap.get(pipePos);
        if (existingNetworkId != null) {
            // If it's already in the same network, nothing to do
            if (existingNetworkId.equals(networkId)) {
                return;
            }

            // If it's in a different network, remove it from that network first
            PipeNetwork existingNetwork = this.networks.get(existingNetworkId);
            if (existingNetwork != null) {
                existingNetwork.removeMember(pipePos);
                QuantiumizedTech.LOGGER.debug("Moved pipe at {} from network {} to {}", pipePos, existingNetworkId, networkId);
            }
        }

        // Get the target network
        PipeNetwork network = this.networks.get(networkId);
        if (network == null) {
            QuantiumizedTech.LOGGER.error("Cannot add pipe at {} to non-existent network {}", pipePos, networkId);
            return;
        }

        // Add the pipe to the network
        network.addMember(pipePos);

        // Map the pipe to the network
        this.pipeToNetworkMap.put(pipePos, networkId);
        this.setDirty();
    }

    /**
     * Finds all networks that a pipe would connect to if placed.
     * @param pipePos The position of the pipe
     * @param level The level
     * @return A set of network IDs
     */
    public Set<UUID> findConnectedNetworks(BlockPos pipePos, Level level) {
        Set<UUID> connectedNetworks = new HashSet<>();

        // First check if the pipe at pipePos exists in the block world
        BlockState pipeState = level.getBlockState(pipePos);
        if (!(pipeState.getBlock() instanceof BasePipeBlock<?>)) {
            return connectedNetworks; // Not a pipe, so no connections
        }

        // Check if this pipe is already in a network
        UUID existingNetwork = this.pipeToNetworkMap.get(pipePos);
        if (existingNetwork != null) {
            connectedNetworks.add(existingNetwork);
            QuantiumizedTech.LOGGER.debug("Pipe at {} is already in network {}", pipePos, existingNetwork);
        }

        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pipePos.relative(dir);

            // Check if the neighbor is a pipe
            BlockState neighborState = level.getBlockState(neighborPos);
            if (!(neighborState.getBlock() instanceof BasePipeBlock)) {
                continue; // Not a pipe, so no connection
            }

            // Check if the neighbor is in a network
            UUID neighborNetwork = this.pipeToNetworkMap.get(neighborPos);
            if (neighborNetwork == null) {
                continue; // Not in a network
            }

            // Always consider adjacent pipes as connectable during network detection
            // This ensures that pipes placed next to each other will be in the same network
            connectedNetworks.add(neighborNetwork);
            QuantiumizedTech.LOGGER.debug("Found connected network {} at {}", neighborNetwork, neighborPos);
        }

        QuantiumizedTech.LOGGER.debug("Found {} connected networks for pipe at {}", connectedNetworks.size(), pipePos);
        return connectedNetworks;
    }
}
