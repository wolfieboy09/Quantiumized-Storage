package dev.wolfieboy09.qtech.block.circut_engraver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.registries.QTRecipes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.List;

@NothingNullByDefault
public record CircuitEngraverRecipe(
        List<Ingredient> ingredients,
        ItemStack result,
        int energy,
        int timeInTicks
) implements Recipe<RecipeWrapper>, RecipeType<CircuitEngraverRecipe> {

    @Override
    public boolean matches(RecipeWrapper input, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeWrapper input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return QTRecipes.CIRCUIT_ENGRAVER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return QTRecipes.CIRCUIT_ENGRAVER_TYPE.get();
    }

    @MethodsReturnNonnullByDefault
    public static class Serializer implements RecipeSerializer<CircuitEngraverRecipe> {
        private static final MapCodec<CircuitEngraverRecipe> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                        Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(CircuitEngraverRecipe::ingredients),
                        ItemStack.CODEC.fieldOf("result").forGetter(CircuitEngraverRecipe::result),
                        Codec.INT.fieldOf("energy").forGetter(CircuitEngraverRecipe::energy),
                        Codec.INT.fieldOf("timeInTicks").forGetter(CircuitEngraverRecipe::timeInTicks)
                ).apply(builder, CircuitEngraverRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, CircuitEngraverRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list(4)), CircuitEngraverRecipe::ingredients,
                ItemStack.STREAM_CODEC, CircuitEngraverRecipe::result,
                ByteBufCodecs.INT, CircuitEngraverRecipe::energy,
                ByteBufCodecs.INT, CircuitEngraverRecipe::timeInTicks,
                CircuitEngraverRecipe::new
        );

        @Override
        public MapCodec<CircuitEngraverRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CircuitEngraverRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
