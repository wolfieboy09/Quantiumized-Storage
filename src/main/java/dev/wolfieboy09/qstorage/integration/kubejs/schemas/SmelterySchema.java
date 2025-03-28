package dev.wolfieboy09.qstorage.integration.kubejs.schemas;

import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface SmelterySchema {
    //RecipeKey<List<Either<Ingredient, FluidStack>>> INGREDIENTS = IngredientComponent.INGREDIENT.or(FILL THIS OUT).asList().key("ingredients", ComponentRole.INPUT);
}
