package dev.wolfieboy09.qstorage.block.pipe;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ConnectionType implements StringRepresentable {
    NONE,
    PIPE,
    BLOCK_NORMAL,
    BLOCK_EXTRACT;

    public static final ConnectionType[] VALUES = values();

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }
}
