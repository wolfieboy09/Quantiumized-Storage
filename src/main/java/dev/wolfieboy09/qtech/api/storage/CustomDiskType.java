package dev.wolfieboy09.qtech.api.storage;

import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;

/**
 * Ability to make custom storage disks for use with all disks.
 */
public record CustomDiskType(int capacity, ChatFormatting color) {
    /**
     * Ability to make a custom storage disks
     *
     * @param capacity The capacity of the disk
     * @param color    For the item color when you hover over it, like the rarity color.
     */
    public CustomDiskType {
    }

    public CustomDiskType(@NotNull ItemStorageType storageType) {
        this(storageType.getCapacity(), storageType.getColor());
    }
}