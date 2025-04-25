package dev.wolfieboy09.qtech.api.registry;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class QTRegistries {
    public static final ResourceKey<Registry<Gas>> GAS_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(QuantiumizedTech.MOD_ID, "gas"));
    public static final Registry<Gas> GAS = new RegistryBuilder<>(GAS_KEY)
            .sync(true)
            .create();
}
