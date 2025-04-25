package dev.wolfieboy09.qtech.api.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.capabilities.gas.GasTank;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record GasCanisterComponent(GasTank gasTankHandler) {
    public static final Codec<GasCanisterComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    GasTank.CODEC.fieldOf("gas_tank").forGetter(GasCanisterComponent::gasTankHandler)
            ).apply(instance, GasCanisterComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, GasCanisterComponent> STREAM_CODEC = StreamCodec.composite(
            GasTank.STREAM_CODEC, GasCanisterComponent::gasTankHandler,
            GasCanisterComponent::new
    );

    // Data component data stuff

    public GasTank getGasTank() {
        return this.gasTankHandler;
    }

    public int getTankCapacity() {
        return this.gasTankHandler.getTankCapacity(0);
    }

    public int getAmount() {
        return this.gasTankHandler.getGas().getAmount();
    }

    public GasStack getGas() {
        return this.gasTankHandler.getGas();
    }
}

