package dev.wolfieboy09.qtech.api.storage;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum StorageType implements StringRepresentable {
    ITEM,
    FLUID,
    ENERGY,
    GAS;

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase();
    }
}
