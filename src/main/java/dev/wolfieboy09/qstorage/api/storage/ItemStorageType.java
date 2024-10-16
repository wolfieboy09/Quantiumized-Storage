package dev.wolfieboy09.qstorage.api.storage;

import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ItemStorageType implements StringRepresentable {
    BASIC(128, ChatFormatting.WHITE),
    ADVANCED(512, ChatFormatting.YELLOW),
    SUPERIOR(2048, ChatFormatting.AQUA),
    QUANTUM(8192, ChatFormatting.LIGHT_PURPLE),
    MULTI_DIMENSIONAL(16384, ChatFormatting.DARK_RED);


    private final int capacity;
    private final ChatFormatting color;

    ItemStorageType(int capacity, ChatFormatting color) {
        this.capacity = capacity;
        this.color = color;
    }

    public int getCapacity() {
        return capacity;
    }

    public ChatFormatting getColor() {
        return color;
    }

    @Override
    public @NotNull String getSerializedName() {
        return "item_storage_type";
    }
}
