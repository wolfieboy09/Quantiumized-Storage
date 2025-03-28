package dev.wolfieboy09.qstorage.datagen;

import com.mojang.datafixers.util.Either;
import dev.wolfieboy09.qstorage.api.recipes.datagen.DiskAssemblerBuilder;
import dev.wolfieboy09.qstorage.api.recipes.datagen.SmelteryBuilder;
import dev.wolfieboy09.qstorage.registries.QSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QSRecipeProvider extends RecipeProvider {
    public QSRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private static @NotNull @Unmodifiable List<Ingredient> listedIngredients(ItemLike @NotNull ... ingredients) {
        List<Ingredient> list = new ArrayList<>(List.of());
        for (ItemLike item : ingredients) {
            list.add(Ingredient.of(item));
        }
        return list;
    }

    @Contract(value = "_ -> new", pure = true)
    private static @NotNull Either<Ingredient, FluidStack> leftIngredient(Ingredient ingredients) {
        return Either.left(ingredients);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        DiskAssemblerBuilder.create(
                listedIngredients(QSItems.ITEM_PORT, QSItems.STEEL_CASING, QSItems.STEEL_SCREW),
                listedIngredients(QSItems.DATA_CRYSTAL, QSItems.BASIC_CIRCUIT),
                2000,
                60,
                new ItemStack(QSItems.BASIC_ITEM_DISK.get())
        ).save(output);

        SmelteryBuilder.create(
            List.of(
                    Either.right(new FluidStack(Fluids.WATER, 1500)),
                    Either.left(Ingredient.of(new ItemStack(Items.NETHER_STAR)))),
            List.of(
                    Either.left(new ItemStack(QSItems.DATA_CRYSTAL.get()))),
            List.of(
                    Either.left(new ItemStack(Items.DIRT, 8))),
            500,
            20
        ).save(output, "soul_campfire_test");
    }
}
