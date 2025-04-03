package dev.wolfieboy09.qstorage.api.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qstorage.api.capabilities.gas.GasTank;
import dev.wolfieboy09.qstorage.api.gas.SingleGasTankHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record GasCanisterComponent(SingleGasTankHandler gasTankHandler) {
    public static final Codec<GasCanisterComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SingleGasTankHandler.CODEC.fieldOf("gas_tank").forGetter(GasCanisterComponent::gasTankHandler)
            ).apply(instance, GasCanisterComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, GasCanisterComponent> STREAM_CODEC = StreamCodec.composite(
            SingleGasTankHandler.STREAM_CODEC, GasCanisterComponent::gasTankHandler,
            GasCanisterComponent::new
    );

    // Data component data stuff

    public GasTank getGasTank() {
        return this.gasTankHandler.getTank();
    }

    public int getTankCapacity() {
        return this.gasTankHandler.getTankCapacity(0);
    }
}
