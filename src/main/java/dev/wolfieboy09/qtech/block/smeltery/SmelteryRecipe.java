package dev.wolfieboy09.qtech.block.smeltery;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.Pair;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.CombinedRecipeInput;
import dev.wolfieboy09.qtech.registries.QTRecipes;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@NothingNullByDefault
public record SmelteryRecipe(
        List<Either<Ingredient, SizedFluidIngredient>> ingredients,
        List<Either<ItemStack, FluidStack>> result,
        List<Either<ItemStack, FluidStack>> waste,
        int minFuelTemp,
        int processingTime
) implements Recipe<CombinedRecipeInput> {
    @Override
    public boolean matches(CombinedRecipeInput input, Level level) {
        if (this.ingredients.isEmpty()) {
            return false;
        }
        // Check if ALL ingredient requirement is satisfied by the input
        return this.ingredients.stream().allMatch(eitherIngredient ->
                eitherIngredient.map(
                        // --- Case 1: Required ingredient is an ITEM ---
                        input::matchItem,
                        // --- Case 2: Required ingredient is a FLUID ---
                        input::matchFluid
                )
        );
    }

    /**
     * Use {@link SmelteryRecipe#assembleRecipe(CombinedRecipeInput, HolderLookup.Provider)} instead
     */
    @Override
    @ApiStatus.Obsolete
    public ItemStack assemble(CombinedRecipeInput recipeWrapper, HolderLookup.Provider provider) {
        return this.result.getFirst().left().orElse(ItemStack.EMPTY);
    }

    public ItemStack getItemStackFromList(List<Either<ItemStack, FluidStack>> selection) {
        for (Either<ItemStack, FluidStack> either : selection) {
            if (either.left().isPresent()) {
                return either.left().get();
            }
        }
        return ItemStack.EMPTY;
    }

    public FluidStack getFluidStackFromList(List<Either<ItemStack, FluidStack>> selection) {
        for (Either<ItemStack, FluidStack> either : selection) {
            if (either.right().isPresent()) {
                return either.right().get();
            }
        }
        return FluidStack.EMPTY;
    }

    public Pair<Pair<ItemStack, FluidStack>, Pair<ItemStack, FluidStack>> assembleRecipe(CombinedRecipeInput recipeWrapper, HolderLookup.Provider provider) {
        return new Pair<>(
                new Pair<>(getItemStackFromList(this.result), getFluidStackFromList(this.result)),
                new Pair<>(getItemStackFromList(this.waste), getFluidStackFromList(this.waste))
        );
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return getItemStackFromList(this.result);
    }

    // Do I need HolderLookup#Provider? no
    public FluidStack getResultFluid() {
        return getFluidStackFromList(this.result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return QTRecipes.SMELTERY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return QTRecipes.SMELTERY_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<SmelteryRecipe> {
        private static final MapCodec<SmelteryRecipe> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                        Codec.either(Ingredient.CODEC, SizedFluidIngredient.FLAT_CODEC).listOf(0, 3).fieldOf("ingredients").forGetter(SmelteryRecipe::ingredients),
                        Codec.either(ItemStack.CODEC, FluidStack.CODEC).listOf(1, 2).fieldOf("result").forGetter(SmelteryRecipe::result),
                        Codec.either(ItemStack.CODEC, FluidStack.CODEC).listOf(0, 2).fieldOf("waste").forGetter(SmelteryRecipe::waste),
                        Codec.INT.fieldOf("temperature").forGetter(SmelteryRecipe::minFuelTemp),
                        Codec.INT.fieldOf("processing_time").forGetter(SmelteryRecipe::processingTime)
                ).apply(builder, SmelteryRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, SmelteryRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.either(Ingredient.CONTENTS_STREAM_CODEC, SizedFluidIngredient.STREAM_CODEC).apply(ByteBufCodecs.list(3)), SmelteryRecipe::ingredients,
                ByteBufCodecs.either(ItemStack.STREAM_CODEC, FluidStack.STREAM_CODEC).apply(ByteBufCodecs.list(2)), SmelteryRecipe::result,
                ByteBufCodecs.either(ItemStack.STREAM_CODEC, FluidStack.STREAM_CODEC).apply(ByteBufCodecs.list(2)), SmelteryRecipe::waste,
                ByteBufCodecs.INT, SmelteryRecipe::minFuelTemp,
                ByteBufCodecs.INT, SmelteryRecipe::processingTime,
                SmelteryRecipe::new
        );

        @Override
        public MapCodec<SmelteryRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SmelteryRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
