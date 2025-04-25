package dev.wolfieboy09.qtech.datagen;

import com.mojang.datafixers.util.Either;
import dev.wolfieboy09.qtech.api.datagen.DiskAssemblerBuilder;
import dev.wolfieboy09.qtech.api.datagen.SmelteryBuilder;
import dev.wolfieboy09.qtech.registries.QTItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QTRecipeProvider extends RecipeProvider {
    public QTRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
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
                listedIngredients(QTItems.ITEM_PORT, QTItems.STEEL_CASING, QTItems.STEEL_SCREW),
                listedIngredients(QTItems.DATA_CRYSTAL, QTItems.BASIC_CIRCUIT),
                2000,
                60,
                new ItemStack(QTItems.BASIC_ITEM_DISK.get())
        ).save(output);

        SmelteryBuilder.create(
            List.of(
                    Either.left(Ingredient.of(new ItemStack(Items.NETHER_STAR)))),
            List.of(
                    Either.left(new ItemStack(QTItems.DATA_CRYSTAL.get()))),
            List.of(
                    Either.left(new ItemStack(Items.DIRT, 8))),
            500,
            20
        ).save(output, "data_crystal_from_smeltery");
    }
}
