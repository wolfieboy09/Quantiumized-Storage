package dev.wolfieboy09.qtech.api.recipes.data.void_crafting;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipe;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeBuilder;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeConstrains;
import dev.wolfieboy09.qtech.registries.QTRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.List;

@NothingNullByDefault
public class VoidCraftingStandardRecipe extends ProcessingRecipe<RecipeWrapper, VoidCraftingRecipeParams> {
    public VoidCraftingStandardRecipe(VoidCraftingRecipeParams params) {
        super(QTRecipeTypes.VOID_CRAFTING, params);
    }

    public NonNullList<ResourceLocation> getDimensions() {
        return params.dimensions;
    }

    @Override
    protected ProcessingRecipeConstrains getRecipeConstrains() {
        return ProcessingRecipeConstrains.builder()
                .maxItemIO(10, 5)
                .build();
    }

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        if (this.ingredients.isEmpty()) return false;

        List<ItemStack> availableItems = new ArrayList<>();
        for (int i = 0; i < recipeWrapper.size(); i++) {
            ItemStack stack = recipeWrapper.getItem(i);
            if (!stack.isEmpty()) {
                availableItems.add(stack);
            }
        }

        if (availableItems.size() != this.ingredients.size()) return false;

        List<ItemStack> remainingItems = new ArrayList<>(availableItems);

        for (SizedIngredient ingredient : ingredients) {
            boolean found = false;

            for (int i = 0; i < remainingItems.size(); i++) {
                if (ingredient.test(remainingItems.get(i))) {
                    remainingItems.remove(i);
                    found = true;
                    break;
                }
            }

            if (!found) return false;
        }

        return true;
    }

    @Override
    public RecipeType<?> getType() {
        return QTRecipeTypes.VOID_CRAFTING.getType();
    }

    @FunctionalInterface
    public interface Factory<R extends VoidCraftingRecipe>
            extends ProcessingRecipe.Factory<VoidCraftingRecipeParams, R> {
        R create(VoidCraftingRecipeParams params);
    }

    public static class Builder<R extends VoidCraftingRecipe>
            extends ProcessingRecipeBuilder<VoidCraftingRecipeParams, R, Builder<R>> {

        public Builder(Factory<R> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        @Override
        protected VoidCraftingRecipeParams createParams() {
            return new VoidCraftingRecipeParams();
        }

        @Override
        public Builder<R> self() {
            return this;
        }

        public Builder<R> dimension(ResourceLocation dimensionType) {
            params.dimensions.add(dimensionType);
            return this;
        }

        public Builder<R> dimensions(NonNullList<ResourceLocation> dimensions) {
            params.dimensions = dimensions;
            return this;
        }
    }

    public static class Serializer<R extends VoidCraftingStandardRecipe>
            implements RecipeSerializer<R> {

        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(ProcessingRecipe.Factory<VoidCraftingRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, VoidCraftingRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, VoidCraftingRecipeParams.STREAM_CODEC);
        }

        @Override
        public MapCodec<R> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
            return streamCodec;
        }
    }
}
