package dev.wolfieboy09.qstorage.api.storage;

import dev.wolfieboy09.qstorage.item.ItemStorageDisk;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;

/**
 * Ability to make custom storage disks for use with all disks.
 */
public class CustomDiskType {
    private final int capacity;
    private final ChatFormatting color;

    /**
     * Ability to make a custom storage disks
     * @param capacity The capacity of the disk
     * @param color For the item color when you hover over it, like the rarity color.
     */
    public CustomDiskType(int capacity, ChatFormatting color) {
        this.capacity = capacity;
        this.color = color;
    }

    public CustomDiskType(@NotNull ItemStorageType storageType) {
        this(storageType.getCapacity(), storageType.getColor());
    }

    public int getCapacity() {
        return this.capacity;
    }

    public ChatFormatting getColor() {
        return this.color;
    }
}