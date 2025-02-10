package dev.wolfieboy09.qstorage.particles;

import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import dev.wolfieboy09.qstorage.api.registry.gas.GasLike;
import dev.wolfieboy09.qstorage.entity.GasCloudEntity;
import dev.wolfieboy09.qstorage.registries.QSEntities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class GasParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final Gas gas;

    public GasParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, @NotNull GasLike gas) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.gravity = 0;
        this.hasPhysics = false;
        this.spriteSet = spriteSet;
        this.gas = gas.asGas();
        this.lifetime *= 10;

        int color = gas.asGas().getGasData().tint();
        this.setColor((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
        this.setAlpha((color >> 24) & 0xFF);
        this.setSpriteFromAge(spriteSet);

        // Adjust motion for hovering
        this.xd *= 0.05; // Reduce horizontal drift
        this.yd = (this.random.nextDouble() * 0.02) - 0.01; // Slight up & down motion
        this.zd *= 0.05;
    }


    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.spriteSet);
        super.tick();

//        if (!this.level.isClientSide && this.age == 1) { // Only spawn once
//            ServerLevel serverLevel = (ServerLevel) this.level;
//            GasCloudEntity cloud = QSEntities.GAS_CLOUD.get().create(serverLevel);
//            if (cloud != null) {
//                cloud.moveTo(this.x, this.y, this.z);
//                serverLevel.addFreshEntity(cloud);
//            }
//        }
    }

}
