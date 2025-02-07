package dev.wolfieboy09.qstorage.api.registry.gas;

import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public interface GasInfo {
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

    default List<MobEffectInstance> effects() {
        return List.of();
    }
}
