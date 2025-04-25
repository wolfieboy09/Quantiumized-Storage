package dev.wolfieboy09.qtech.block.pipe.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

/**
 * Represents a network of connected pipes.
 */
public class PipeNetwork {
    private final UUID networkId;
    private final Set<BlockPos> memberPipes;
    private NetworkType networkType;
    
    /**
     * Creates a new pipe network with a random ID.
     * @param initialPipe The initial pipe in the network
     * @param type The type of the network
     */
    public PipeNetwork(BlockPos initialPipe, NetworkType type) {
        this(UUID.randomUUID(), initialPipe, type);
    }
    
    /**
     * Creates a new pipe network with the specified ID.
     * @param networkId The ID of the network
     * @param initialPipe The initial pipe in the network
     * @param type The type of the network
     */
    public PipeNetwork(UUID networkId, BlockPos initialPipe, NetworkType type) {
        this.networkId = networkId;
        this.memberPipes = new HashSet<>();
        this.networkType = type;
        
        if (initialPipe != null) {
            this.memberPipes.add(initialPipe);
        }
    }
    
    /**
     * Creates an empty pipe network with the specified ID.
     * @param networkId The ID of the network
     * @param type The type of the network
     */
    public PipeNetwork(UUID networkId, NetworkType type) {
        this(networkId, null, type);
    }
    
    /**
     * Adds a pipe to the network.
     * @param pipePos The position of the pipe to add
     */
    public void addMember(BlockPos pipePos) {
        this.memberPipes.add(pipePos);
    }
    
    /**
     * Removes a pipe from the network.
     * @param pipePos The position of the pipe to remove
     */
    public void removeMember(BlockPos pipePos) {
        this.memberPipes.remove(pipePos);
    }
    
    /**
     * Checks if the network contains a pipe.
     * @param pipePos The position of the pipe to check
     * @return True if the network contains the pipe, false otherwise
     */
    public boolean containsMember(BlockPos pipePos) {
        return this.memberPipes.contains(pipePos);
    }
    
    /**
     * Gets all pipes in the network.
     * @return An unmodifiable set of all pipe positions
     */
    public @Unmodifiable Set<BlockPos> getAllMembers() {
        return Collections.unmodifiableSet(this.memberPipes);
    }
    
    /**
     * Gets the number of pipes in the network.
     * @return The number of pipes
     */
    public int getMemberCount() {
        return this.memberPipes.size();
    }
    
    /**
     * Gets the type of the network.
     * @return The network type
     */
    public NetworkType getNetworkType() {
        return this.networkType;
    }
    
    /**
     * Sets the type of the network.
     * @param type The new network type
     */
    public void setNetworkType(NetworkType type) {
        this.networkType = type;
    }
    
    /**
     * Gets the ID of the network.
     * @return The network ID
     */
    public UUID getNetworkId() {
        return this.networkId;
    }
    
    /**
     * Finds all pipes in the network that connect to non-pipe blocks.
     * @param pipeConnections A map of pipe positions to connections
     * @return A set of interface pipe positions
     */
    public Set<BlockPos> findInterfacePipes(Map<BlockPos, PipeConnection> pipeConnections) {
        Set<BlockPos> interfacePipes = new HashSet<>();
        
        for (BlockPos pipePos : this.memberPipes) {
            PipeConnection connection = pipeConnections.get(pipePos);
            if (connection == null) continue;
            
            for (Map.Entry<Direction, ConnectionState> entry : connection.getAllConnectionStates().entrySet()) {
                if (entry.getValue() == ConnectionState.CONNECTED_TO_BLOCK || entry.getValue() == ConnectionState.CONNECTED_TO_BLOCK_TO_EXTRACT) {
                    interfacePipes.add(pipePos);
                    break;
                }
            }
        }
        
        return interfacePipes;
    }
    
    /**
     * Saves the network to NBT.
     * @param tag The tag to save to
     */
    public void saveToNbt(@NotNull CompoundTag tag) {
        tag.putUUID("NetworkId", this.networkId);
        tag.putInt("NetworkType", this.networkType.ordinal());
        
        ListTag membersList = new ListTag();
        for (BlockPos pos : this.memberPipes) {
            membersList.add(BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).getOrThrow());
        }
        tag.put("Members", membersList);
    }
    
    /**
     * Loads a network from NBT.
     * @param tag The tag to load from
     * @return The loaded network
     */
    public static @NotNull PipeNetwork loadFromNbt(@NotNull CompoundTag tag) {
        UUID networkId = tag.getUUID("NetworkId");
        int typeOrdinal = tag.getInt("NetworkType");
        NetworkType type = NetworkType.values()[typeOrdinal % NetworkType.values().length];
        
        PipeNetwork network = new PipeNetwork(networkId, type);
        
        ListTag membersList = tag.getList("Members", 11);
        for (net.minecraft.nbt.Tag value : membersList) {
          BlockPos pos = BlockPos.CODEC.decode(NbtOps.INSTANCE, value).getOrThrow().getFirst();
          network.addMember(pos);
        }
        
        return network;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PipeNetwork that = (PipeNetwork) o;
        return Objects.equals(this.networkId, that.networkId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.networkId);
    }
}
