package dev.wolfieboy09.qtech.api.recipes.result;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidStackChanceResult extends ChanceResult<FluidStack> {
    public static final FluidStackChanceResult EMPTY = new FluidStackChanceResult(FluidStack.EMPTY, 1);

    public static final Codec<FluidStackChanceResult> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("fluid").forGetter(r ->
                    BuiltInRegistries.FLUID.getKey(r.getResult().getFluid())
            ),
            Codec.INT.optionalFieldOf("amount", 1000).forGetter(r -> r.getResult().getAmount()),
            Codec.FLOAT.optionalFieldOf("chance", 1f).forGetter(FluidStackChanceResult::getChance)
    ).apply(instance, (fluidId, amount, chance) ->
            new FluidStackChanceResult(new FluidStack(
                    BuiltInRegistries.FLUID.getOptional(fluidId).orElseThrow(
                            () -> new IllegalArgumentException("Unknown fluid: " + fluidId)
                    ), amount),
                    chance
            )
    ));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidStackChanceResult> STREAM_CODEC =
            StreamCodec.composite(
                    FluidStack.STREAM_CODEC, FluidStackChanceResult::getResult,
                    ByteBufCodecs.FLOAT, FluidStackChanceResult::getChance,
                    FluidStackChanceResult::new
            );

    public FluidStackChanceResult(FluidStack result, float chance) {
        super(result, chance);
    }

    public FluidStackChanceResult(FluidStack result) {
        super(result, 1f);
    }

    @Override
    protected FluidStack copyResult() {
        return this.result.copy();
    }

    @Override
    public Codec<? extends ChanceResult<FluidStack>> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends ChanceResult<FluidStack>> streamCodec() {
        return STREAM_CODEC;
    }
}
