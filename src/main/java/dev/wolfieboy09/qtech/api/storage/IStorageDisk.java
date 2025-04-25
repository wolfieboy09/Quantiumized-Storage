package dev.wolfieboy09.qtech.api.storage;

public interface IStorageDisk {
    // NonNullList<T> getContents();

    int getCapacity();

    StorageType getType();
}
