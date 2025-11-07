package dev.wolfieboy09.qtech.block.disk_assembler.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.codecs.NonNullListStreamCodec;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeParams;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class DiskAssemblerRecipeParams extends ProcessingRecipeParams {
    public static final MapCodec<DiskAssemblerRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec(DiskAssemblerRecipeParams::new).forGetter(Function.identity()),
            Ingredient.CODEC.listOf(0, 3).fieldOf("extras").forGetter(DiskAssemblerRecipeParams::extras)
    ).apply(instance, (params, extras) -> {
        params.extras = NonNullList.copyOf(extras);
        return params;
    }));

    protected NonNullList<Ingredient> extras;

    protected final NonNullList<Ingredient> extras() {
        return this.extras;
    }

    @Override
    protected void encode(RegistryFriendlyByteBuf buffer) {
        super.encode(buffer);
        NonNullListStreamCodec.nonNullList(Ingredient.CONTENTS_STREAM_CODEC).encode(buffer, extras);
    }

    @Override
    protected void decode(RegistryFriendlyByteBuf buffer) {
        super.decode(buffer);
        extras = NonNullListStreamCodec.nonNullList(Ingredient.CONTENTS_STREAM_CODEC).decode(buffer);
    }
}
