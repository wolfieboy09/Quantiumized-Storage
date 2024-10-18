package dev.wolfieboy09.qstorage.api.storage;

public interface IStorageDisk<T> {
    // NonNullList<T> getContents();

    int getCapacity();

    StorageType getType();
}
