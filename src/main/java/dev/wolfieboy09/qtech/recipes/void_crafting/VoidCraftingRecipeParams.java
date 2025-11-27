package dev.wolfieboy09.qtech.recipes.void_crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.codecs.NonNullListStreamCodec;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeParams;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class VoidCraftingRecipeParams extends ProcessingRecipeParams {
    public static final MapCodec<VoidCraftingRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec(VoidCraftingRecipeParams::new).forGetter(Function.identity()),
            ResourceLocation.CODEC.listOf().optionalFieldOf("dimensions", List.of()).forGetter(VoidCraftingRecipeParams::dimensions)
    ).apply(instance, (params, dimensions) -> {
        params.dimensions = NonNullList.copyOf(dimensions);
        return params;
    }));

    public static final StreamCodec<RegistryFriendlyByteBuf, VoidCraftingRecipeParams> STREAM_CODEC = streamCodec(VoidCraftingRecipeParams::new);

    protected NonNullList<ResourceLocation> dimensions;

    public VoidCraftingRecipeParams() {
        super();
        this.dimensions = NonNullList.create();
    }

    @Override
    protected void encode(RegistryFriendlyByteBuf buffer) {
        super.encode(buffer);
        NonNullListStreamCodec.nonNullList(ResourceLocation.STREAM_CODEC).encode(buffer, dimensions);
    }

    @Override
    protected void decode(RegistryFriendlyByteBuf buffer) {
        super.decode(buffer);
        dimensions = NonNullListStreamCodec.nonNullList(ResourceLocation.STREAM_CODEC).decode(buffer);
    }

    protected final NonNullList<ResourceLocation> dimensions() {
        return this.dimensions;
    }
}
