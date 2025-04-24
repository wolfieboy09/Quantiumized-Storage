package dev.wolfieboy09.qstorage.block.pipe.network;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlock;
import dev.wolfieboy09.qstorage.block.pipe.ConnectionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Manages pipe networks across the game world.
 * This class provides the main API for interacting with pipe networks.
 */
public class PipeNetworkManager {
    private static final String SAVED_DATA_NAME = QuantiumizedStorage.MOD_ID + "_pipe_networks";

    /**
     * Gets or creates the SavedData for a level.
     * @param level The level to get the saved data for
     * @return The saved data, or null if on the client side
     */
    public static @Nullable PipeNetworkData getOrCreateSavedData(@NotNull Level level) {
        if (level.isClientSide || level.getServer() == null) {
            return null;
        }

        DimensionDataStorage dataStorage = level.getServer().overworld().getDataStorage();
        return dataStorage.computeIfAbsent(
                new net.minecraft.world.level.saveddata.SavedData.Factory<>(
                        PipeNetworkData::create,
                        PipeNetworkData::load
                ),
                SAVED_DATA_NAME
        );
    }

    /**
     * Called when a pipe is placed in the world.
     * Registers the pipe and creates or joins a network.
     * @param level The level containing the pipe
     * @param pipePos The position of the pipe
     */
    public static void onPipePlaced(Level level, BlockPos pipePos) {
        if (level.isClientSide) return;

        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData == null) return;

        // Phase 1: Register the pipe and establish connections
        savedData.registerPipe(pipePos);
        updatePipeConnections(level, pipePos);

        // Phase 2: Determine network membership
        determineNetworkMembership(level, pipePos, savedData);
    }

    /**
     * Called when a pipe is removed from the world.
     * Unregisters the pipe and updates networks.
     * @param level The level containing the pipe
     * @param pipePos The position of the pipe
     */
    public static void onPipeRemoved(Level level, BlockPos pipePos) {
        if (level.isClientSide) return;

        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData == null) return;

        // Get the network ID before removing
        UUID networkId = savedData.getNetworkForPipe(pipePos);

        // Store neighbor positions for later processing
        Map<Direction, BlockPos> neighbors = new HashMap<>();

        // Get connected pipes before removing
        PipeConnection connection = savedData.getPipeConnection(pipePos);
        if (connection != null) {
            // Disconnect from all connected pipes
            for (Direction dir : Direction.values()) {
                ConnectionState state = connection.getConnectionState(dir);
                if (state == ConnectionState.CONNECTED_TO_PIPE) {
                    BlockPos neighborPos = pipePos.relative(dir);
                    neighbors.put(dir, neighborPos);

                    // Update the neighbor's connection state
                    savedData.setConnectionState(neighborPos, dir.getOpposite(), ConnectionState.NONE);
                }
            }
        }

        // Remove from network
        savedData.unregisterPipe(pipePos);
        QuantiumizedStorage.LOGGER.info("Pipe removed at {}", pipePos);

        // Check if the network needs to be split
        if (networkId != null) {
            PipeNetwork network = savedData.getNetworkById(networkId);
            if (network != null && network.getMemberCount() > 1) {
                // The pipe has already been removed from the network, so we need to check
                // if the remaining pipes are still connected
                Set<BlockPos> members = network.getAllMembers();
                if (!members.isEmpty()) {
                    // Start from any pipe in the network
                    BlockPos startPos = members.iterator().next();
                    Set<BlockPos> connectedPipes = savedData.findConnectedPipes(startPos, members);
                    if (connectedPipes.size() < members.size()) {
                        // The network is split, so we need to create new networks
                        Set<PipeNetwork> resultingNetworks = savedData.splitNetwork(networkId);
                        QuantiumizedStorage.LOGGER.info("Split network {} into {} networks after removing pipe",
                                networkId, resultingNetworks.size());
                    }
                }
            }
        }

        // Check all neighbors to ensure they're in a network
        for (Map.Entry<Direction, BlockPos> entry : neighbors.entrySet()) {
            BlockPos neighborPos = entry.getValue();
            // Update the neighbor's connections
            updatePipeConnections(level, neighborPos);

            // Verify the neighbor is in a network
            UUID neighborNetworkId = savedData.getNetworkForPipe(neighborPos);
            if (neighborNetworkId == null) {
                // Neighbor is not in a network, create a new one
                PipeNetwork network = savedData.createNetwork(neighborPos, NetworkType.UNIVERSAL);
                QuantiumizedStorage.LOGGER.info("Created new network {} for orphaned pipe at {}",
                        network.getNetworkId(), neighborPos);
            }
        }
    }

    /**
     * Called when a pipe is updated.
     * Updates connections and networks.
     * @param level The level containing the pipe
     * @param pipePos The position of the pipe
     */
    private static void determineNetworkMembership(Level level, BlockPos pipePos, PipeNetworkData savedData) {
        // Check if already in a network
        UUID existingNetworkId = savedData.getNetworkForPipe(pipePos);
        if (existingNetworkId != null) {
            // Already in a network, check if we need to merge with neighbors
            mergeWithNeighborNetworks(level, pipePos, savedData, existingNetworkId);
            return;
        }

        // Find connected networks
        Set<UUID> connectedNetworks = findConnectedNetworks(level, pipePos, savedData);

        if (connectedNetworks.isEmpty()) {
            // No connected networks, create a new one
            PipeNetwork network = savedData.createNetwork(pipePos, NetworkType.UNIVERSAL);
            QuantiumizedStorage.LOGGER.info("Created new network {} for pipe at {}", network.getNetworkId(), pipePos);
        } else if (connectedNetworks.size() == 1) {
            // One connected network, join it
            UUID networkId = connectedNetworks.iterator().next();
            savedData.addPipeToNetwork(pipePos, networkId);
            QuantiumizedStorage.LOGGER.info("Added pipe at {} to network {}", pipePos, networkId);
        } else {
            // Multiple connected networks, merge them
            UUID targetNetworkId = connectedNetworks.iterator().next();
            UUID[] otherNetworks = connectedNetworks.stream()
                    .filter(id -> !id.equals(targetNetworkId))
                    .toArray(UUID[]::new);

            savedData.mergeNetworks(targetNetworkId, otherNetworks);
            savedData.addPipeToNetwork(pipePos, targetNetworkId);
            QuantiumizedStorage.LOGGER.info("Merged networks and added pipe at {} to network {}", pipePos, targetNetworkId);
        }
    }

    private static Set<UUID> findConnectedNetworks(Level level, BlockPos pipePos, PipeNetworkData savedData) {
        Set<UUID> connectedNetworks = new HashSet<>();

        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pipePos.relative(dir);

            // Check if the neighbor is a pipe
            BlockState neighborState = level.getBlockState(neighborPos);
            if (!(neighborState.getBlock() instanceof BasePipeBlock)) {
                continue;
            }

            // Check if the pipes are connected
            ConnectionState connectionState = savedData.getConnectionState(pipePos, dir);
            if (connectionState != ConnectionState.CONNECTED_TO_PIPE) {
                continue;
            }

            // Check if the neighbor is in a network
            UUID neighborNetworkId = savedData.getNetworkForPipe(neighborPos);
            if (neighborNetworkId != null) {
                connectedNetworks.add(neighborNetworkId);
            }
        }

        return connectedNetworks;
    }

    private static void mergeWithNeighborNetworks(Level level, BlockPos pipePos, PipeNetworkData savedData, UUID currentNetworkId) {
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pipePos.relative(dir);

            // Check if the neighbor is a pipe
            BlockState neighborState = level.getBlockState(neighborPos);
            if (!(neighborState.getBlock() instanceof BasePipeBlock)) {
                continue;
            }

            // Check if the pipes are connected
            ConnectionState connectionState = savedData.getConnectionState(pipePos, dir);
            if (connectionState != ConnectionState.CONNECTED_TO_PIPE) {
                continue;
            }

            // Check if the neighbor is in a different network
            UUID neighborNetworkId = savedData.getNetworkForPipe(neighborPos);
            if (neighborNetworkId != null && !neighborNetworkId.equals(currentNetworkId)) {
                savedData.mergeNetworks(currentNetworkId, neighborNetworkId);
                QuantiumizedStorage.LOGGER.info("Merged networks {} and {}", currentNetworkId, neighborNetworkId);
            }
        }
    }

    public static void onPipeUpdated(Level level, BlockPos pipePos) {
        if (level.isClientSide) return;

        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData == null) return;

        // Update connections
        updatePipeConnections(level, pipePos);

        // Determine network membership
        determineNetworkMembership(level, pipePos, savedData);
    }

    /**
     * Enables a connection between two pipes.
     * @param level The level containing the pipes
     * @param pipePos The position of the first pipe
     * @param direction The direction to the second pipe
     */
    public static void enableConnection(Level level, BlockPos pipePos, Direction direction) {
        if (level.isClientSide) return;

        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData == null) return;

        // Enable the connection
        savedData.enableConnection(pipePos, direction);
        QuantiumizedStorage.LOGGER.debug("Enabled connection at {} in direction {}", pipePos, direction);

        // Update the block state
        BlockState state = level.getBlockState(pipePos);
        ConnectionState connectionState = determineConnectionState(level, pipePos, direction);
        ConnectionType connectionType = PipeConnection.toConnectionType(connectionState);
        level.setBlockAndUpdate(pipePos, state.setValue(BasePipeBlock.getPropertyFromDirection(direction), connectionType));

        // Update the connected pipe
        BlockPos neighborPos = pipePos.relative(direction);
        if (level.getBlockState(neighborPos).getBlock() instanceof BasePipeBlock) {
            // Enable the connection on the other side
            savedData.enableConnection(neighborPos, direction.getOpposite());

            // Update the block state
            BlockState neighborState = level.getBlockState(neighborPos);
            ConnectionState neighborConnectionState = determineConnectionState(level, neighborPos, direction.getOpposite());
            ConnectionType neighborConnectionType = PipeConnection.toConnectionType(neighborConnectionState);
            level.setBlockAndUpdate(neighborPos, neighborState.setValue(
                    BasePipeBlock.getPropertyFromDirection(direction.getOpposite()),
                    neighborConnectionType));

            // Check if networks need to be merged
            UUID network1 = savedData.getNetworkForPipe(pipePos);
            UUID network2 = savedData.getNetworkForPipe(neighborPos);

            if (network1 != null && network2 != null && !network1.equals(network2)) {
                savedData.mergeNetworks(network1, network2);
                QuantiumizedStorage.LOGGER.info("Merged networks {} and {}", network1, network2);
            }
        }
    }

    /**
     * Disables a connection between two pipes.
     * @param level The level containing the pipes
     * @param pipePos The position of the first pipe
     * @param direction The direction to the second pipe
     */
    public static void disableConnection(Level level, BlockPos pipePos, Direction direction) {
        if (level.isClientSide) return;

        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData == null) return;

        // Disable the connection
        savedData.disableConnection(pipePos, direction);
        QuantiumizedStorage.LOGGER.debug("Disabled connection at {} in direction {}", pipePos, direction);

        // Update the block state
        BlockState state = level.getBlockState(pipePos);
        level.setBlockAndUpdate(pipePos, state.setValue(BasePipeBlock.getPropertyFromDirection(direction), ConnectionType.NONE));

        // Update the connected pipe
        BlockPos neighborPos = pipePos.relative(direction);
        if (level.getBlockState(neighborPos).getBlock() instanceof BasePipeBlock) {
            // Disable the connection on the other side
            savedData.disableConnection(neighborPos, direction.getOpposite());

            // Update the block state
            BlockState neighborState = level.getBlockState(neighborPos);
            level.setBlockAndUpdate(neighborPos, neighborState.setValue(
                    BasePipeBlock.getPropertyFromDirection(direction.getOpposite()),
                    ConnectionType.NONE));

            // Check if network needs to be split
            UUID networkId = savedData.getNetworkForPipe(pipePos);

            if (networkId != null) {
                PipeNetwork network = savedData.getNetworkById(networkId);
                if (network != null && network.getMemberCount() > 1) {
                    // Check if the network is split
                    // Start from the neighbor pipe that we just disconnected from
                    // This ensures we're checking connectivity from the perspective of the affected pipe
                    if (network.containsMember(neighborPos)) {
                        BlockPos startPos = neighborPos;
                        Set<BlockPos> connectedPipes = savedData.findConnectedPipes(startPos, network.getAllMembers());
                        if (connectedPipes.size() < network.getMemberCount()) {
                            Set<PipeNetwork> resultingNetworks = savedData.splitNetwork(networkId);
                            QuantiumizedStorage.LOGGER.info("Split network {} into {} networks", networkId, resultingNetworks.size());

                            // Check if the neighbor pipe is in a network
                            UUID neighborNetworkId = savedData.getNetworkForPipe(neighborPos);
                            if (neighborNetworkId == null) {
                                // Neighbor is not in a network, create a new one
                                PipeNetwork neighborNetwork = savedData.createNetwork(neighborPos, NetworkType.UNIVERSAL);
                                QuantiumizedStorage.LOGGER.info("Created new network {} for orphaned pipe at {}",
                                        neighborNetwork.getNetworkId(), neighborPos);
                            }
                        }
                    } else {
                        // If the neighbor is not in the network, we need to check from another pipe
                        BlockPos startPos = network.getAllMembers().stream()
                                .filter(pos -> !pos.equals(pipePos))
                                .findFirst()
                                .orElse(null);

                        if (startPos != null) {
                            Set<BlockPos> connectedPipes = savedData.findConnectedPipes(startPos, network.getAllMembers());
                            if (connectedPipes.size() < network.getMemberCount()) {
                                Set<PipeNetwork> resultingNetworks = savedData.splitNetwork(networkId);
                                QuantiumizedStorage.LOGGER.info("Split network {} into {} networks", networkId, resultingNetworks.size());

                                // Check if the neighbor pipe is in a network
                                UUID neighborNetworkId = savedData.getNetworkForPipe(neighborPos);
                                if (neighborNetworkId == null) {
                                    // Neighbor is not in a network, create a new one
                                    PipeNetwork neighborNetwork = savedData.createNetwork(neighborPos, NetworkType.UNIVERSAL);
                                    QuantiumizedStorage.LOGGER.info("Created new network {} for orphaned pipe at {}",
                                            neighborNetwork.getNetworkId(), neighborPos);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if a connection is enabled between two pipes.
     * @param level The level containing the pipes
     * @param pipePos The position of the first pipe
     * @param direction The direction to the second pipe
     * @return True if the connection is enabled, false otherwise
     */
    public static boolean isConnectionEnabled(Level level, BlockPos pipePos, Direction direction) {
        if (level.isClientSide) return true;

        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData == null) return true;

        return savedData.isConnectionEnabled(pipePos, direction);
    }

    /**
     * Determines the connection state between a pipe and another block.
     * @param level The level containing the pipe
     * @param pipePos The position of the pipe
     * @param direction The direction to check
     * @return The connection state
     */
    public static ConnectionState determineConnectionState(Level level, BlockPos pipePos, Direction direction) {
        // Check if the connection is disabled
        if (!isConnectionEnabled(level, pipePos, direction)) {
            return ConnectionState.MANUALLY_DISABLED;
        }

        BlockPos neighborPos = pipePos.relative(direction);
        BlockState neighborState = level.getBlockState(neighborPos);

        // Check if the neighbor is a pipe
        if (neighborState.getBlock() instanceof BasePipeBlock) {
            // Check if the neighbor's connection is enabled
            if (!isConnectionEnabled(level, neighborPos, direction.getOpposite())) {
                return ConnectionState.AUTO_DISABLED;
            }
            return ConnectionState.CONNECTED_TO_PIPE;
        }

        // Check if the neighbor has a compatible capability
        if (level.getBlockState(pipePos).getBlock() instanceof BasePipeBlock<?> pipeBlock) {
            if (canConnectToBlock(level, pipePos, direction, pipeBlock.getCapability())) {
                return ConnectionState.CONNECTED_TO_BLOCK;
            }
        }
        return ConnectionState.NONE;
    }

    /**
     * Checks if a pipe can connect to another pipe.
     * @param level The level containing the pipes
     * @param pipePos The position of the pipe
     * @param direction The direction to check
     * @return True if the pipe can connect, false otherwise
     */
    public static boolean canConnectToPipe(Level level, BlockPos pipePos, Direction direction) {
        if (!isConnectionEnabled(level, pipePos, direction)) {
            return false;
        }

        BlockPos neighborPos = pipePos.relative(direction);
        BlockState neighborState = level.getBlockState(neighborPos);

        if (neighborState.getBlock() instanceof BasePipeBlock) {
            return isConnectionEnabled(level, neighborPos, direction.getOpposite());
        }
        return false;
    }

    /**
     * Checks if a pipe can connect to a block with a specific capability.
     * @param level The level containing the pipe
     * @param pipePos The position of the pipe
     * @param direction The direction to check
     * @param capability The capability to check for
     * @return True if the pipe can connect, false otherwise
     */
    public static <C> boolean canConnectToBlock(Level level, BlockPos pipePos, Direction direction, BlockCapability<C, @Nullable Direction> capability) {
        if (!isConnectionEnabled(level, pipePos, direction)) {
            return false;
        }

        return level.getCapability(capability, pipePos.relative(direction), direction.getOpposite()) != null;
    }

    /**
     * Updates the block state of a pipe based on its connections.
     * @param state The current block state
     * @param level The level containing the pipe
     * @param pipePos The position of the pipe
     * @param direction The direction to update
     * @return The updated block state
     */
    public static BlockState updatePipeBlockState(BlockState state, Level level, BlockPos pipePos, Direction direction) {
        ConnectionState connectionState = determineConnectionState(level, pipePos, direction);
        ConnectionType connectionType = PipeConnection.toConnectionType(connectionState);
        return state.setValue(BasePipeBlock.getPropertyFromDirection(direction), connectionType);
    }

    /**
     * Updates all connections for a pipe.
     * @param level The level containing the pipe
     * @param pipePos The position of the pipe
     */
    private static void updatePipeConnections(Level level, BlockPos pipePos) {
        BlockState state = level.getBlockState(pipePos);
        if (!(state.getBlock() instanceof BasePipeBlock<?>)) return;

        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData == null) return;

        Map<Direction, ConnectionState> connectionStates = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            ConnectionState connectionState = determineConnectionState(level, pipePos, dir);
            connectionStates.put(dir, connectionState);
        }

        for (Direction dir : Direction.values()) {
            ConnectionState connectionState = connectionStates.get(dir);

            savedData.setConnectionState(pipePos, dir, connectionState);

            ConnectionType connectionType = PipeConnection.toConnectionType(connectionState);
            state = state.setValue(BasePipeBlock.getPropertyFromDirection(dir), connectionType);
        }

        level.setBlockAndUpdate(pipePos, state);
    }

    /**
     * Verifies and fixes network assignments for a pipe and its neighbors.
     * @param level The level containing the pipe
     * @param pipePos The position of the pipe
     */
    public static void verifyAndFixNetworks(Level level, BlockPos pipePos) {
        if (level.isClientSide) return;

        PipeNetworkData savedData = getOrCreateSavedData(level);
        if (savedData == null) return;

        UUID currentNetworkId = savedData.getNetworkForPipe(pipePos);

        // If the pipe isn't in a network, create one
        if (currentNetworkId == null) {
            Set<UUID> connectedNetworks = savedData.findConnectedNetworks(pipePos, level);
            if (!connectedNetworks.isEmpty()) {
                UUID targetNetworkId = connectedNetworks.iterator().next();
                savedData.addPipeToNetwork(pipePos, targetNetworkId);
                QuantiumizedStorage.LOGGER.info("Added pipe at {} to network {} during verification", pipePos, targetNetworkId);
                currentNetworkId = targetNetworkId;
            } else {
                PipeNetwork network = savedData.createNetwork(pipePos, NetworkType.UNIVERSAL);
                QuantiumizedStorage.LOGGER.info("Created new network {} for pipe at {} during verification", network.getNetworkId(), pipePos);
                currentNetworkId = network.getNetworkId();
            }
        }

        // Check all connected pipes to ensure they're in the same network
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pipePos.relative(dir);
            BlockState neighborState = level.getBlockState(neighborPos);

            if (!(neighborState.getBlock() instanceof BasePipeBlock)) continue;

            ConnectionState connectionState = savedData.getConnectionState(pipePos, dir);
            if (connectionState != ConnectionState.CONNECTED_TO_PIPE) {
                // Update connection state if pipes should be connected
                if (canConnectToPipe(level, pipePos, dir) && canConnectToPipe(level, neighborPos, dir.getOpposite())) {
                    savedData.setConnectionState(pipePos, dir, ConnectionState.CONNECTED_TO_PIPE);
                    savedData.setConnectionState(neighborPos, dir.getOpposite(), ConnectionState.CONNECTED_TO_PIPE);

                    BlockState pipeState = level.getBlockState(pipePos);
                    level.setBlockAndUpdate(pipePos, pipeState.setValue(
                            BasePipeBlock.getPropertyFromDirection(dir), ConnectionType.PIPE));

                    level.setBlockAndUpdate(neighborPos, neighborState.setValue(
                            BasePipeBlock.getPropertyFromDirection(dir.getOpposite()), ConnectionType.PIPE));

                    QuantiumizedStorage.LOGGER.info("Fixed connection between {} and {} during verification", pipePos, neighborPos);
                }
                continue;
            }

            // Check if neighbor is in a different network
            UUID neighborNetworkId = savedData.getNetworkForPipe(neighborPos);
            if (neighborNetworkId == null) {
                savedData.addPipeToNetwork(neighborPos, currentNetworkId);
                QuantiumizedStorage.LOGGER.info("Added neighbor at {} to network {} during verification", neighborPos, currentNetworkId);
            } else if (!neighborNetworkId.equals(currentNetworkId)) {
                savedData.mergeNetworks(currentNetworkId, neighborNetworkId);
                QuantiumizedStorage.LOGGER.info("Merged networks {} and {} during verification", currentNetworkId, neighborNetworkId);
            }
        }
    }
}
