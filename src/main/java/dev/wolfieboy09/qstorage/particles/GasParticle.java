package dev.wolfieboy09.qstorage.particles;

import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import dev.wolfieboy09.qstorage.api.registry.gas.GasLike;
import dev.wolfieboy09.qstorage.registries.QSGasses;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class GasParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final Gas gas;

    public GasParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, GasLike gas) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.gravity = gas.asGas().getGasData().isHeavy() ? 1 : -1;
        this.spriteSet = spriteSet;
        int color = gas.asGas().getGasData().tint();
        this.setColor((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
        this.setAlpha((color >> 24) & 0xFF);
        this.setSpriteFromAge(spriteSet);
        this.gas = QSGasses.HYDROGEN.get();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.spriteSet);
        super.tick();
        // temp
        double range = 3.0;
        AABB area = new AABB(this.x - range, this.y - range, this.z - range, this.x + range, this.y + range, this.z + range);
        // TODO: Fix it not working and actually get the gas type for stuff to happen
        for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (!entity.level().isClientSide) {
                this.gas.getGasData().effects().forEach(entity::addEffect);
            }
        }
    }
}
