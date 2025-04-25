package dev.wolfieboy09.qtech.block.pipe.network;

/**
 * Represents the state of a pipe connection in a specific direction.
 */
public enum ConnectionState {
    /**
     * No connection possible
     */
    NONE,
    
    /**
     * Connected to another pipe
     */
    CONNECTED_TO_PIPE,
    
    /**
     * Connected to a compatible block
     */
    CONNECTED_TO_BLOCK,

    /**
     * Connected to a compatible block, but extracting from it
     */
    CONNECTED_TO_BLOCK_TO_EXTRACT,
    
    /**
     * Manually disabled by player
     */
    MANUALLY_DISABLED,
    
    /**
     * Automatically disabled (e.g., incompatible types)
     */
    AUTO_DISABLED;
    
    /**
     * Checks if this connection state represents an active connection.
     * @return True if connected, false otherwise
     */
    public boolean isConnected() {
        return this == CONNECTED_TO_PIPE || this == CONNECTED_TO_BLOCK;
    }
    
    /**
     * Checks if this connection state represents a disabled connection.
     * @return True if disabled, false otherwise
     */
    public boolean isDisabled() {
        return this == MANUALLY_DISABLED || this == AUTO_DISABLED;
    }
}
