package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.api.recipes.DiskAssemblerBuilder;
import dev.wolfieboy09.qstorage.registries.QSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QSRecipeProvider extends RecipeProvider {
    public QSRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private static @NotNull @Unmodifiable List<Ingredient> listedIngredients(ItemLike @NotNull ... ingredients) {
        return List.of(Ingredient.of(ingredients));
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        DiskAssemblerBuilder.create(
                listedIngredients(QSItems.ITEM_PORT, QSItems.STEEL_CASING, QSItems.STEEL_SCREW),
                listedIngredients(QSItems.DATA_CRYSTAL, QSItems.BASIC_CIRCUIT),
                2000,
                20,
                new ItemStack(QSItems.BASIC_ITEM_DISK.get())
        ).save(output);
    }
}
