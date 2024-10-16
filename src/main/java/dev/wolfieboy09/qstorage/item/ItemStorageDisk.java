package dev.wolfieboy09.qstorage.item;


import dev.wolfieboy09.qstorage.api.storage.IItemStorageDisk;
import dev.wolfieboy09.qstorage.api.storage.ItemStorageType;
import dev.wolfieboy09.qstorage.api.storage.StorageType;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ItemStorageDisk extends Item implements IItemStorageDisk {
    private final ItemStorageType storageType;

    public ItemStorageDisk(Properties properties, ItemStorageType storageType) {
        super(properties);
        this.storageType = storageType;
    }



    @Override
    public NonNullList<ItemStack> getContents() {
        return null;
    }

    @Override
    public UUID getId() {
        return null;
    }

    @Override
    public int getCapacity() {
        return this.storageType.getCapacity();
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public StorageType getType() {
        return StorageType.ITEM;
    }

    @Override
    public ItemStorageType getStorageType() {
        return this.storageType;
    }
}
