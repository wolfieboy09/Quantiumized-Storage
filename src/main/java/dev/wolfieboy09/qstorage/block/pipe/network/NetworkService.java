package dev.wolfieboy09.qstorage.block.pipe.network;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

/**
 * Manages pipe networks.
 */
public class NetworkService {
    private final Map<UUID, PipeNetwork> networks;
    private final Map<BlockPos, UUID> pipeToNetworkMap;

    /**
     * Creates a new network manager.
     */
    public NetworkService() {
        this.networks = new HashMap<>();
        this.pipeToNetworkMap = new HashMap<>();
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
    }

    /**
     * Merges multiple networks into a target network.
     * @param targetNetworkId The ID of the target network
     * @param networkIdsToMerge The IDs of the networks to merge
     */
    public void mergeNetworks(UUID targetNetworkId, UUID... networkIdsToMerge) {
        PipeNetwork targetNetwork = this.networks.get(targetNetworkId);
        if (targetNetwork == null) {
            QuantiumizedStorage.LOGGER.error("Cannot merge into non-existent network {}", targetNetworkId);
            return;
        }

        for (UUID networkId : networkIdsToMerge) {
            if (networkId.equals(targetNetworkId)) continue;

            PipeNetwork network = this.networks.get(networkId);
            if (network == null) {
                QuantiumizedStorage.LOGGER.debug("Skipping merge with non-existent network {}", networkId);
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
            QuantiumizedStorage.LOGGER.debug("Merged network {} into {} with {} members", networkId, targetNetworkId, membersToMove.size());
        }
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

        return resultingNetworks;
    }

    /**
     * Gets a network by its ID.
     * @param networkId The ID of the network
     * @return The network, or null if not found
     */
    public PipeNetwork getNetworkById(UUID networkId) {
        return this.networks.get(networkId);
    }

    /**
     * Gets the network for a pipe.
     * @param pipePos The position of the pipe
     * @return The network, or null if not found
     */
    public PipeNetwork getNetworkForPipe(BlockPos pipePos) {
        UUID networkId = this.pipeToNetworkMap.get(pipePos);
        if (networkId == null) return null;
        return this.networks.get(networkId);
    }

    /**
     * Gets all networks.
     * @return An unmodifiable set of all networks
     */
    public Set<PipeNetwork> getAllNetworks() {
        return Collections.unmodifiableSet(new HashSet<>(this.networks.values()));
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
     * Finds all pipes connected to a starting pipe based on physical adjacency only.
     * This method does not check connection states and is used for network splitting
     * after we've already determined that a split is needed.
     * @param startPos The position of the starting pipe
     * @param validPipes The set of valid pipes to consider
     * @return A set of connected pipe positions
     */
    public Set<BlockPos> findConnectedPipes(BlockPos startPos, Set<BlockPos> validPipes) {
        Set<BlockPos> connectedPipes = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            if (connectedPipes.contains(current)) continue;

            connectedPipes.add(current);

            // Add connected neighbors to queue
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.relative(dir);
                if (validPipes.contains(neighbor) && !connectedPipes.contains(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }

        return connectedPipes;
    }

    /**
     * Finds all pipes connected to a starting pipe, taking into account connection states.
     * @param level The level containing the pipes
     * @param savedData The pipe network data
     * @param startPos The position of the starting pipe
     * @param validPipes The set of valid pipes to consider
     * @return A set of connected pipe positions
     */
    public Set<BlockPos> findConnectedPipes(Level level, PipeNetworkData savedData, BlockPos startPos, Set<BlockPos> validPipes) {
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
                    ConnectionState currentToNeighbor = savedData.getConnectionState(current, dir);
                    ConnectionState neighborToCurrent = savedData.getConnectionState(neighbor, dir.getOpposite());

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

                // If the neighbor is in the same network and we haven't visited it yet
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
    }

    /**
     * Adds a pipe to a network.
     * @param pipePos The position of the pipe
     * @param networkId The ID of the network
     * @return True if the pipe was added, false otherwise
     */
    public boolean addPipeToNetwork(BlockPos pipePos, UUID networkId) {
        // Check if the pipe is already in a network
        UUID existingNetworkId = this.pipeToNetworkMap.get(pipePos);
        if (existingNetworkId != null) {
            // If it's already in the same network, nothing to do
            if (existingNetworkId.equals(networkId)) {
                return true;
            }

            // If it's in a different network, remove it from that network first
            PipeNetwork existingNetwork = this.networks.get(existingNetworkId);
            if (existingNetwork != null) {
                existingNetwork.removeMember(pipePos);
                QuantiumizedStorage.LOGGER.debug("Moved pipe at {} from network {} to {}", pipePos, existingNetworkId, networkId);
            }
        }

        // Get the target network
        PipeNetwork network = this.networks.get(networkId);
        if (network == null) {
            QuantiumizedStorage.LOGGER.error("Cannot add pipe at {} to non-existent network {}", pipePos, networkId);
            return false;
        }

        // Add the pipe to the network
        network.addMember(pipePos);

        // Map the pipe to the network
        this.pipeToNetworkMap.put(pipePos, networkId);
        return true;
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
        // Note: We don't check for network splitting here anymore.
        // This is now handled in PipeNetworkManager.onPipeRemoved which has access to the Level and PipeNetworkData
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
            QuantiumizedStorage.LOGGER.debug("Pipe at {} is already in network {}", pipePos, existingNetwork);
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
            QuantiumizedStorage.LOGGER.debug("Found connected network {} at {}", neighborNetwork, neighborPos);
        }

        QuantiumizedStorage.LOGGER.debug("Found {} connected networks for pipe at {}", connectedNetworks.size(), pipePos);
        return connectedNetworks;
    }
}
