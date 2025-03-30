package dev.wolfieboy09.qstorage.block.smeltery;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qstorage.api.BiHolder;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.recipes.CombinedRecipeInput;
import dev.wolfieboy09.qstorage.registries.QSRecipes;
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

import java.util.List;

@NothingNullByDefault
public record SmelteryRecipe(
        List<Either<Ingredient, SizedFluidIngredient>> ingredients,
        List<Either<ItemStack, FluidStack>> result,
        List<Either<ItemStack, FluidStack>> waste,
        int minFuelTemp,
        int timeInTicks
) implements Recipe<CombinedRecipeInput> {
    @Override
    public boolean matches(CombinedRecipeInput input, Level level) {
        // credts to 7410 in Turty's server for this return statement stuff
        return this.ingredients.stream()
                .allMatch(either -> either.map(
                        ingredient -> ingredient.test(input.getItem(0)) &&
                                ingredient.test(input.getItem(1)) &&
                                ingredient.test(input.getItem(2)),
                        fluid -> input.matchListOfFluid(fluid.getFluids(), 0) &&
                                input.matchListOfFluid(fluid.getFluids(), 1) &&
                                input.matchListOfFluid(fluid.getFluids(), 2)
                ));
    }

    @Override
    @Deprecated
    /**
     * Use {@link SmelteryRecipe#compile(CombinedRecipeInput, HolderLookup.Provider)} instead
     */
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

    public BiHolder<BiHolder<ItemStack, FluidStack>, BiHolder<ItemStack, FluidStack>> assembleRecipe(CombinedRecipeInput recipeWrapper, HolderLookup.Provider provider) {
        return new BiHolder<>(
                new BiHolder<>(getItemStackFromList(this.result), getFluidStackFromList(this.result)),
                new BiHolder<>(getItemStackFromList(this.waste), getFluidStackFromList(this.waste))
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
        return QSRecipes.SMELTERY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return QSRecipes.SMELTERY_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<SmelteryRecipe> {
        private static final MapCodec<SmelteryRecipe> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                        Codec.either(Ingredient.CODEC, SizedFluidIngredient.FLAT_CODEC).listOf(0, 3).fieldOf("ingredients").forGetter(SmelteryRecipe::ingredients),
                        Codec.either(ItemStack.CODEC, FluidStack.CODEC).listOf(1, 2).fieldOf("result").forGetter(SmelteryRecipe::result),
                        Codec.either(ItemStack.CODEC, FluidStack.CODEC).listOf(0, 2).fieldOf("waste").forGetter(SmelteryRecipe::waste),
                        Codec.INT.fieldOf("temperature").forGetter(SmelteryRecipe::minFuelTemp),
                        Codec.INT.fieldOf("ticks").forGetter(SmelteryRecipe::timeInTicks)
                ).apply(builder, SmelteryRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, SmelteryRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.either(Ingredient.CONTENTS_STREAM_CODEC, SizedFluidIngredient.STREAM_CODEC).apply(ByteBufCodecs.list(3)), SmelteryRecipe::ingredients,
                ByteBufCodecs.either(ItemStack.STREAM_CODEC, FluidStack.STREAM_CODEC).apply(ByteBufCodecs.list(2)), SmelteryRecipe::result,
                ByteBufCodecs.either(ItemStack.STREAM_CODEC, FluidStack.STREAM_CODEC).apply(ByteBufCodecs.list(2)), SmelteryRecipe::waste,
                ByteBufCodecs.INT, SmelteryRecipe::minFuelTemp,
                ByteBufCodecs.INT, SmelteryRecipe::timeInTicks,
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
