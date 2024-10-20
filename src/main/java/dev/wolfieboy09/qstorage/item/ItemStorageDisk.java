package dev.wolfieboy09.qstorage.item;


import dev.wolfieboy09.qstorage.api.records.ItemStorageDiskRecord;
import dev.wolfieboy09.qstorage.api.storage.CustomDiskType;
import dev.wolfieboy09.qstorage.api.storage.IItemStorageDisk;
import dev.wolfieboy09.qstorage.api.storage.ItemStorageType;
import dev.wolfieboy09.qstorage.api.storage.StorageType;
import dev.wolfieboy09.qstorage.component.QSDataComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


/**
 * Represents a storage disk that can store items with a limited capacity.
 * This class implements the {@link IItemStorageDisk} interface, providing methods
 * to interact with the storage disk's capacity and type.
 * The storage disk can be created using predefined {@link ItemStorageType} or a custom disk type.
 */
public class ItemStorageDisk extends Item implements IItemStorageDisk {
    private final ItemStorageType storageType;
    private final int capacity;

    /**
     * Constructor for creating a storage disk with a predefined {@link ItemStorageType}.
     *
     * @param storageType The type of storage for the disk from the {@link ItemStorageType} enum.
     */
    public ItemStorageDisk(@NotNull ItemStorageType storageType) {
        super(new Properties().stacksTo(1));
        this.storageType = storageType;
        this.capacity = storageType.getCapacity();
    }

    /**
     * Constructor for creating a storage disk with a custom capacity.
     *
     * @param storageType The type of storage for the disk from the {@link ItemStorageType} enum.
     * @param capacity    The custom capacity of the disk.
     */
    public ItemStorageDisk(ItemStorageType storageType, int capacity) {
        super(new Properties().stacksTo(1));
        this.storageType = storageType;
        this.capacity = capacity;
    }

    /**
     * Constructor for creating a storage disk from a custom disk type.
     * The custom disk type allows for setting a unique color and capacity.
     *
     * @param diskType The custom disk type containing the color and capacity.
     */
    public ItemStorageDisk(@NotNull CustomDiskType diskType) {
        super(new Properties().stacksTo(1));
        ItemStorageType customType = ItemStorageType.CUSTOM;
        customType.setColor(diskType.getColor());
        this.storageType = customType;
        this.capacity = diskType.getCapacity();
    }

    /**
     * @return Storage disk capacity
     */
    @Override
    public int getCapacity() {
        return this.storageType.getCapacity();
    }

    /**
     * Returns the type of storage, which is always of type {@link StorageType#ITEM} for item disks.
     * @return {@link StorageType#ITEM}
     */
    @Override
    public StorageType getType() {
        return StorageType.ITEM;
    }

    /**
     * Returns the {@link ItemStorageType} of the storage disk.
     *
     * @return The storage type of the disk.
     */
    @Override
    public ItemStorageType getStorageType() {
        return this.storageType;
    }

    /**
     * Returns the localized name of the item with the color style of the disk's storage type.
     *
     * @param stack The {@link ItemStack} instance representing the storage disk.
     * @return A {@link Component} representing the localized name of the disk with the correct color style.
     */
    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(stack.getDescriptionId()).withStyle(this.storageType.getColor());
    }

    /**
     * Returns the {@link DataComponentType} for this item storage disk.
     * This is used to store and retrieve disk-related data.
     *
     * @return The data component type for this storage disk.
     */
    public DataComponentType<ItemStorageDiskRecord> getComponentType() {
        return QSDataComponents.ITEM_STORAGE_DISK_COMPONENT.get();
    }

    /**
     * Retrieves the data component stored within the given {@link ItemStack}.
     * This component holds the data specific to this storage disk.
     *
     * @param stack The item stack representing the storage disk.
     * @return The {@link ItemStorageDiskRecord} containing the disk's stored data.
     */
    public ItemStorageDiskRecord getComponent(@NotNull ItemStack stack) {
        return stack.get(getComponentType());
    }
}

