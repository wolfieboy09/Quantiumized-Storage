package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class QSDamageTypes {
    public static final ResourceKey<DamageType> OXYGEN_DEPRIVATION_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceHelper.asResource("oxygen_deprivation"));
}
