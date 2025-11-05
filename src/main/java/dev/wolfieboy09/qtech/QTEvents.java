package dev.wolfieboy09.qtech;

import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.particles.GasParticleProvider;
import dev.wolfieboy09.qtech.registries.QTParticleTypes;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.NotNull;

public class QTEvents {
    public static void registerRegistries(@NotNull NewRegistryEvent event) {
        event.register(QTRegistries.GAS);
        event.register(QTRegistries.MULTIBLOCK_TYPE);
        event.register(QTRegistries.GAS_INGREDIENT_TYPES);
    }

    public static void particle(@NotNull RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(QTParticleTypes.GAS_PARTICLE.get(), GasParticleProvider::new);
    }
}