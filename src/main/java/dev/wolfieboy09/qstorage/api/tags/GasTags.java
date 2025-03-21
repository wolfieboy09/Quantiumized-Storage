package dev.wolfieboy09.qstorage.api.tags;

import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

public final class GasTags {
    public static @NotNull TagKey<Gas> create(ResourceLocation name) {
        return TagKey.create(QSRegistries.GAS_REGISTRY_KEY, name);
    }
}
