package dev.wolfieboy09.qtech.integration.kubejs.recipes.components.framework;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.SimpleRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.wolfieboy09.qtech.api.recipes.result.ChanceResult;

public abstract class ChanceComponent<T extends ChanceResult<?>> extends SimpleRecipeComponent<T> {
    public ChanceComponent(RecipeComponentType<?> type, Codec<T> codec, Class<T> chanceResultClass) {
        super(type, codec, TypeInfo.of(chanceResultClass));
    }

    @Override
    public abstract boolean hasPriority(RecipeMatchContext cx, Object from);

    @Override
    public abstract boolean matches(RecipeMatchContext cx, T value, ReplacementMatchInfo match);

    @Override
    public abstract T replace(RecipeScriptContext cx, T original, ReplacementMatchInfo match, Object with);

    @Override
    public abstract boolean isEmpty(T value);

    @Override
    public abstract void buildUniqueId(UniqueIdBuilder builder, T value);
}
