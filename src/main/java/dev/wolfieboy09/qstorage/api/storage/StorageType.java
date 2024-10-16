package dev.wolfieboy09.qstorage.api.storage;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum StorageType implements StringRepresentable {
    ITEM,
    FLUID,
    ENERGY;

    @Override
    public @NotNull String getSerializedName() {
        return "storage_type";
    }
}
