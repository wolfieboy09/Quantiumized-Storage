package dev.wolfieboy09.qstorage.api.storage;

import net.minecraft.world.item.ItemStack;

public interface IItemStorageDisk extends IStorageDisk<ItemStack> {
   ItemStorageType getStorageType();
}
