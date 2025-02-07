package dev.wolfieboy09.qstorage.particles;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class GasParticleProvider implements ParticleProvider<GasParticleOptions> {
    private final SpriteSet sprites;

    public GasParticleProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Override
    public @Nullable Particle createParticle(GasParticleOptions gasOpt, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
        return new GasParticle(clientLevel, v, v1, v2, v3, v4, v5, this.sprites, gasOpt.getGas().getGasHolder().value());
    }
}
