package dev.wolfieboy09.qstorage.effects;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.registries.QSDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

@NothingNullByDefault
public class OxygenDeprivationEffect extends MobEffect {
    public OxygenDeprivationEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.hurt(getDamageSource(livingEntity), 1);
        return true;
    }

    private static DamageSource getDamageSource(LivingEntity entity) {
        return new DamageSource(
                entity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE)
                        .getOrThrow(QSDamageTypes.OXYGEN_DEPRIVATION_DAMAGE)
        );
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
