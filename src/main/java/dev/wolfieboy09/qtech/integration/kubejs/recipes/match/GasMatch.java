package dev.wolfieboy09.qtech.integration.kubejs.recipes.match;

import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatch;
import dev.wolfieboy09.qtech.api.gas.crafting.GasIngredient;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;

public interface GasMatch extends ReplacementMatch {
    boolean matches(RecipeMatchContext cx, GasStack stack, boolean exact);

    boolean matches(RecipeMatchContext cx, GasIngredient ingredient, boolean exact);
}
