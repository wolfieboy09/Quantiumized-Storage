package dev.wolfieboy09.qstorage.api.registry.gas;

import com.mojang.serialization.Codec;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class Gas {
    public static final Codec<Gas> CODEC = QSRegistries.GAS_REGISTRY.byNameCodec();

    public static final StreamCodec<RegistryFriendlyByteBuf, Gas> STREAM_CODEC = ByteBufCodecs.registry(QSRegistries.GAS_REGISTRY_KEY);

    private final GasBuilder gasData;

    public Gas(GasBuilder data) {
       this.gasData = data;
    }

    public GasData getGasData() {
        return this.gasData;
    }
}
