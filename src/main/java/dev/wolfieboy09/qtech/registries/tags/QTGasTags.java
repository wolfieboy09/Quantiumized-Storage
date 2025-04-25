package dev.wolfieboy09.qtech.registries.tags;

import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.tags.GasTags;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

public class QTGasTags {
    public static final TagKey<Gas> FLAMMABLE = gasTag("flammable");
    public static final TagKey<Gas> OXYGEN_DEPRIVATION = gasTag("oxygen_deprivation");

    private static @NotNull TagKey<Gas> gasTag(String path) {
        return GasTags.create(ResourceHelper.asResource(path));
    }
}
