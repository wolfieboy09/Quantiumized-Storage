package dev.wolfieboy09.qstorage.block.pipe;

import net.minecraft.util.StringRepresentable;

public enum ConnectionType implements StringRepresentable {
    NONE,
    PIPE,
    BLOCK;

    public static final ConnectionType[] VALUES = values();

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
