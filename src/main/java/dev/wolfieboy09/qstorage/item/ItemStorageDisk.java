package dev.wolfieboy09.qstorage.item;


import dev.wolfieboy09.qstorage.api.records.ItemStorageDiskRecord;
import dev.wolfieboy09.qstorage.api.storage.IItemStorageDisk;
import dev.wolfieboy09.qstorage.api.storage.ItemStorageType;
import dev.wolfieboy09.qstorage.api.storage.StorageType;
import dev.wolfieboy09.qstorage.component.QSDataComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemStorageDisk extends Item implements IItemStorageDisk {
    private final ItemStorageType storageType;

    public ItemStorageDisk(ItemStorageType storageType) {
        super(new Properties().stacksTo(1));
        this.storageType = storageType;
    }

    @Override
    public int getCapacity() {
        return this.storageType.getCapacity();
    }

    @Override
    public StorageType getType() {
        return StorageType.ITEM;
    }

    @Override
    public ItemStorageType getStorageType() {
        return this.storageType;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(stack.getDescriptionId()).withStyle(this.storageType.getColor());
    }

    public DataComponentType<ItemStorageDiskRecord> getComponentType() {
        return QSDataComponents.ITEM_STORAGE_DISK_COMPONENT.get();
    }

    public ItemStorageDiskRecord getComponent(@NotNull ItemStack stack) {
        return stack.get(getComponentType());
    }
}
