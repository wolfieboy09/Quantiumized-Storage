package dev.wolfieboy09.qstorage.api.storage;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ItemStorageType implements StringRepresentable {
    BASIC(500),
    ADVANCED(1000);


    private final int capacity;

    ItemStorageType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public @NotNull String getSerializedName() {
        return "item_storage_type";
    }
}
