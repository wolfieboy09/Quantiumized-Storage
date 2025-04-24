package dev.wolfieboy09.qstorage.block.pipe;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ConnectionType implements StringRepresentable {
    /**
     * Has no connection at all
     */
    NONE,

    /**
     * Pipe to Pipe connection type
     */
    PIPE,

    /**
     * Pipe to connected block (chest for example)
     */
    BLOCK_NORMAL,

    /**
     * Connected block to pipe network, extracts resource from the container (chest for example)
     */
    BLOCK_EXTRACT;

    /**
     * Variable for {@link ConnectionType#values()}
     */
    public static final ConnectionType[] VALUES = values();

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }
}
