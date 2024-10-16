package dev.wolfieboy09.qstorage.api.storage;

import dev.wolfieboy09.qstorage.api.Tiers;
import net.minecraft.core.NonNullList;

import java.util.UUID;

public interface IStorageDisk<T> {
    NonNullList<T> getContents();

    UUID getId();

    void setId(UUID id);

    int getCapacity();

    boolean isValid();

    StorageType getType();

    default Tiers getTier() { return Tiers.BASIC; };

}
