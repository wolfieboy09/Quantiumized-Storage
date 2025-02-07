package dev.wolfieboy09.qstorage.particles;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

@NothingNullByDefault
public class GasParticleType extends ParticleType<GasParticleOptions>  {

    public GasParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<GasParticleOptions> codec() {
        return GasParticleOptions.codec(this);
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, GasParticleOptions> streamCodec() {
        return GasParticleOptions.streamCodec(this);
    }
}
