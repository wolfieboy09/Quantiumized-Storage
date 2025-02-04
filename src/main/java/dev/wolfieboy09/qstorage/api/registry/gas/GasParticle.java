package dev.wolfieboy09.qstorage.api.registry.gas;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class GasParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    public GasParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.gravity = 0;
        this.spriteSet = spriteSet;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(spriteSet);
        super.tick();
        double range = 3.0;
        AABB area = new AABB(this.x - range, this.y - range, this.z - range, this.x + range, this.y + range, this.z + range);
        // TODO: Fix it not working and actually get the gas type for stuff to happen
        for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (!entity.level().isClientSide) {
                entity.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 2));
            }
        }
    }
}
