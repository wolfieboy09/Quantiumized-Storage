package dev.wolfieboy09.qstorage.effects;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.registries.QSDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@NothingNullByDefault
public class OxygenDeprivationEffect extends MobEffect {
    public OxygenDeprivationEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level().isClientSide && livingEntity instanceof Player) {
            applyShader();
        } else {
            livingEntity.hurt(getDamageSource(livingEntity), 0);
        }
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

    @OnlyIn(Dist.CLIENT)
    private void applyShader() {
//        if (shaderInstance == null) {
//            shaderInstance = Minecraft.getInstance().gameRenderer.getShader("oxygen_deprivation");
//        }
//        float severity = Math.min(1.0F, (600.0F - player.getEffect(QSEffects.OXYGEN_DEPRIVATION).getDuration()) / 600.0F);
//         shaderInstance.safeGetUniform("Severity").set(severity);
//        GameRenderer renderer = Minecraft.getInstance().gameRenderer;
//        renderer.loadEffect(ResourceHelper.asResource("shaders/post/oxygen_deprivation.json"));
    }
}
