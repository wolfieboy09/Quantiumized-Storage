package dev.wolfieboy09.qstorage.integration.kubejs.schemas;

import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public interface SmelterySchema {
    //RecipeKey<List<Either<Ingredient, SizedFluidIngredient>>> INGREDIENTS = IngredientComponent.INGREDIENT.or(INSERT SIZED FLUID INGREDIENT HERE).asList().key("ingredients", ComponentRole.INPUT);
}
