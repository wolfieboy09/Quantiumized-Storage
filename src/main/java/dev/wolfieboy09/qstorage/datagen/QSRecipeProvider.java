package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.api.recipes.DiskAssemblerBuilder;
import dev.wolfieboy09.qstorage.registries.QSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QSRecipeProvider extends RecipeProvider {
    public QSRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private static @NotNull @Unmodifiable List<Ingredient> extras(ItemLike @NotNull ... ingredients) {
        // Might be really useless, but oh well
        if (ingredients.length > 4) {
            throw new IllegalArgumentException("More than four ingredients provided.");
        }
        return List.of(Ingredient.of(ingredients));
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        DiskAssemblerBuilder.create(
                Ingredient.of(QSItems.ITEM_PORT),
                Ingredient.of(QSItems.STEEL_CASING),
                Ingredient.of(QSItems.STEEL_SCREW),
                extras(QSItems.DATA_CRYSTAL),
                1000,
                500,
                new ItemStack(QSItems.BASIC_ITEM_DISK.get())
        ).save(output);
    }
}
