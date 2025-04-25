package dev.wolfieboy09.qtech.api.tags;

import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

public final class GasTags {
    public static @NotNull TagKey<Gas> create(ResourceLocation name) {
        return TagKey.create(QTRegistries.GAS_KEY, name);
    }
}
