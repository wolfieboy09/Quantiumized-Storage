package dev.wolfieboy09.qstorage.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.registry.gas.GasStack;
import dev.wolfieboy09.qstorage.registries.QSParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

@NothingNullByDefault
public class GasParticleOptions implements ParticleOptions {
    private final GasStack gasStack;
    //private final ParticleType<GasParticleOptions> type;
    private static final Codec<GasStack> GAS_CODEC = Codec.withAlternative(GasStack.SINGLE_GAS_CODEC, GasStack.GAS_NON_EMPTY_CODEC, GasStack::new);

    public static MapCodec<GasParticleOptions> codec(ParticleType<GasParticleOptions> particleType) {
        return GAS_CODEC.xmap((stack) -> new GasParticleOptions(particleType, stack), (option) -> option.gasStack).fieldOf("gas");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, GasParticleOptions> streamCodec(ParticleType<GasParticleOptions> particleType) {
        return GasStack.STREAM_CODEC.map((stack) -> new GasParticleOptions(particleType, stack), (option) -> option.gasStack);
    }

    public GasParticleOptions(ParticleType<GasParticleOptions> type, GasStack gasStack) {
        if (gasStack.isEmpty()) {
            throw new IllegalArgumentException("Empty stacks are not allowed");
        }
        //this.type = type;
        this.gasStack = gasStack.copy();
    }

    @Override
    public ParticleType<?> getType() {
        return QSParticleTypes.GAS_PARTICLE.get();
    }

    public GasStack getGas() {
        return this.gasStack;
    }
}
