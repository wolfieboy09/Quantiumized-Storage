package dev.wolfieboy09.qstorage.item;


import dev.wolfieboy09.qstorage.api.storage.IItemStorageDisk;
import dev.wolfieboy09.qstorage.api.storage.ItemStorageType;
import dev.wolfieboy09.qstorage.api.storage.StorageType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class ItemStorageDisk extends Item implements IItemStorageDisk {
    private final ItemStorageType storageType;
    private UUID id;

    public ItemStorageDisk(ItemStorageType storageType) {
        super(new Properties().stacksTo(1));
        this.storageType = storageType;
    }

    @Override
    public void setId(UUID id) {
        this.id = Objects.requireNonNullElseGet(id, () -> UUID.nameUUIDFromBytes(getContents().toString().getBytes()));
    }

    @Override
    public NonNullList<ItemStack> getContents() {
        return null;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public int getCapacity() {
        return this.storageType.getCapacity();
    }

    @Override
    public boolean isValid() {
        return this.id != null && !this.id.equals(UUID.nameUUIDFromBytes(getContents().toString().getBytes()));
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
    public @NotNull Component getName(ItemStack stack) {
        return Component.translatable(stack.getDescriptionId()).withStyle(this.storageType.getColor());
    }
}
