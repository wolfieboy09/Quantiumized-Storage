package dev.wolfieboy09.qtech.api.recipes.result;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public class GasStackChanceResult extends ChanceResult<GasStack> {
    public static final GasStackChanceResult EMPTY = new GasStackChanceResult(GasStack.EMPTY, 1000);

    public static final Codec<GasStackChanceResult> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("gas").forGetter(r ->
                    QTRegistries.GAS.getKey(r.getResult().getGas())
            ),
            Codec.INT.optionalFieldOf("amount", 1000).forGetter(r -> r.getResult().getAmount()),
            Codec.FLOAT.optionalFieldOf("chance", 1f).forGetter(GasStackChanceResult::getChance)
    ).apply(instance, (gasId, amount, chance) ->
            new GasStackChanceResult(
                    new GasStack(
                            QTRegistries.GAS.getOptional(gasId).orElseThrow(
                                    () -> new IllegalArgumentException("Unknown gas: " + gasId)
                            ), amount
                    ),
                    chance
            )));

    public static final StreamCodec<RegistryFriendlyByteBuf, GasStackChanceResult> STREAM_CODEC =
            StreamCodec.composite(
                    GasStack.STREAM_CODEC, GasStackChanceResult::getResult,
                    ByteBufCodecs.FLOAT, GasStackChanceResult::getChance,
                    GasStackChanceResult::new
            );

    public GasStackChanceResult(GasStack result, float chance) {
        super(result, chance);
    }

    public GasStackChanceResult(GasStack result) {
        super(result, 1);
    }

    @Override
    protected GasStack copyResult() {
        return this.result.copy();
    }

    @Override
    public Codec<? extends ChanceResult<GasStack>> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends ChanceResult<GasStack>> streamCodec() {
        return STREAM_CODEC;
    }
}
