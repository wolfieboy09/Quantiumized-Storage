package dev.wolfieboy09.qstorage.api.storage;

import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ItemStorageType implements StringRepresentable {
    BASIC(128, ChatFormatting.WHITE),
    ADVANCED(512, ChatFormatting.YELLOW),
    SUPERIOR(2048, ChatFormatting.AQUA),
    QUANTUM(8192, ChatFormatting.LIGHT_PURPLE),
    MULTI_DIMENSIONAL(16384, ChatFormatting.DARK_RED),
    CUSTOM(1, ChatFormatting.GRAY);

    private int capacity;
    private ChatFormatting color;

    ItemStorageType(int capacity, ChatFormatting color) {
        this.capacity = capacity;
        this.color = color;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setColor(ChatFormatting color) {
        this.color = color;
    }

    public ChatFormatting getColor() {
        return color;
    }

    @Override
    public @NotNull String getSerializedName() {
        return "item_storage_type";
    }
}
