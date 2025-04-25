package dev.wolfieboy09.qtech.item;

import dev.wolfieboy09.qtech.api.components.BaseStorageDisk;
import dev.wolfieboy09.qtech.api.components.ItemStorageDiskComponent;
import dev.wolfieboy09.qtech.api.items.CapacityItemStackHandler;
import dev.wolfieboy09.qtech.api.storage.CustomDiskType;
import dev.wolfieboy09.qtech.api.storage.IItemStorageDisk;
import dev.wolfieboy09.qtech.api.storage.ItemStorageType;
import dev.wolfieboy09.qtech.api.storage.StorageType;
import dev.wolfieboy09.qtech.component.QTDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;


/**
 * Represents a storage disk that can store items with a limited capacity.
 * This class implements the {@link IItemStorageDisk} interface, providing methods
 * to interact with the storage disk's capacity and type.
 * The storage disk can be created using predefined {@link ItemStorageType} or a custom disk type.
 */
@ParametersAreNonnullByDefault
public class ItemStorageDisk extends Item implements IItemStorageDisk {
    private final ItemStorageType storageType;
    private final int capacity;
    private final CapacityItemStackHandler inventory;

    /**
     * Constructor for creating a storage disk with a predefined {@link ItemStorageType}.
     *
     * @param storageType The type of storage for the disk from the {@link ItemStorageType} enum.
     */
    public ItemStorageDisk(@NotNull ItemStorageType storageType) {
        super(new Properties().stacksTo(1).component(QTDataComponents.ITEM_STORAGE_DISK_COMPONENT, new ItemStorageDiskComponent(new BaseStorageDisk(StorageType.ITEM), storageType, NonNullList.createWithCapacity(storageType.getCapacity()))));
        this.storageType = storageType;
        this.capacity = storageType.getCapacity();
        this.inventory = new CapacityItemStackHandler(storageType.getCapacity());
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
        this.inventory = new CapacityItemStackHandler(capacity);
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
        this.inventory = new CapacityItemStackHandler(diskType.getCapacity());
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
     * This is for Minecraft displaying the tooltip of the item. The translatable name and its color.
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
    public DataComponentType<ItemStorageDiskComponent> getComponentType() {
        return QTDataComponents.ITEM_STORAGE_DISK_COMPONENT.get();
    }

    /**
     * Retrieves the data component stored within the given {@link ItemStack}.
     * This component holds the data specific to this storage disk.
     *
     * @param stack The item stack representing the storage disk.
     * @return The {@link ItemStorageDiskComponent} containing the disk's stored data.
     */
    public ItemStorageDiskComponent getComponent(@NotNull ItemStack stack) {
        return stack.get(getComponentType());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        ItemStorageDiskComponent data = stack.get(QTDataComponents.ITEM_STORAGE_DISK_COMPONENT);
        if (data == null) return;
        int count = 0;
        for (ItemStack itemStack : data.contents()) count += itemStack.getCount();
        tooltip.add(Component.literal(count + " / " + this.capacity));
    }

    public IItemHandler getInventoryHandler(ItemStack stack) {
        ItemStorageDiskComponent data = stack.get(QTDataComponents.ITEM_STORAGE_DISK_COMPONENT);
        if (data == null) return null;
        NonNullList<ItemStack> contents = data.contents();
        for (int i = 0; i < contents.size(); i++) this.inventory.setStackInSlot(i, contents.get(i));
        stack.set(QTDataComponents.ITEM_STORAGE_DISK_COMPONENT, data);
        return this.inventory;
    }
}

