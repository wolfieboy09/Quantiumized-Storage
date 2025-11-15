package dev.wolfieboy09.qtech.integration.kubejs.recipes.match.results.framework;

import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.wolfieboy09.qtech.api.recipes.result.ChanceResult;

public interface ChanceResultMatch<T extends ChanceResult<?>> {
    boolean matches(RecipeMatchContext cx, T result, boolean exact);
}
