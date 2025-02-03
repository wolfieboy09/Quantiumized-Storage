package dev.wolfieboy09.qstorage.api.registry.gas;

public interface GasData {
    default boolean isPoisonous() {
        return false;
    }

    default boolean isHeavy() {
        return false;
    }

    default boolean isFlammable() {
        return false;
    }

    default int tint() {
        return 0xFFFFFF;
    }
}
