package dev.wolfieboy09.qstorage;

import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.NotNull;

public class QSEvents {
    public static void registerRegistries(@NotNull NewRegistryEvent event) {
        event.register(QSRegistries.GAS_REGISTRY);
    }
}
