package dev.wolfieboy09.qstorage;

import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import dev.wolfieboy09.qstorage.particles.GasParticleProvider;
import dev.wolfieboy09.qstorage.registries.QSParticleTypes;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.NotNull;

public class QSEvents {
    public static void registerRegistries(@NotNull NewRegistryEvent event) {
        event.register(QSRegistries.GAS_REGISTRY);
    }

    public static void particle(@NotNull RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(QSParticleTypes.GAS_PARTICLE.get(), GasParticleProvider::new);
    }


}