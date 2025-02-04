package dev.wolfieboy09.qstorage.api.registry.gas;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GasParticleProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public GasParticleProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Override
    public @Nullable Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new GasParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
    }
}
