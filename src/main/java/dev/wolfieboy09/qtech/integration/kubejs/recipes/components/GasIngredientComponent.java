package dev.wolfieboy09.qtech.integration.kubejs.recipes.components;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.gas.crafting.GasIngredient;
import dev.wolfieboy09.qtech.api.gas.crafting.SizedGasIngredient;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.match.GasMatch;
import dev.wolfieboy09.qtech.integration.kubejs.wrappers.GasWrapper;

@NothingNullByDefault
public record GasIngredientComponent(
        RecipeComponentType<?> type,
        Codec<GasIngredient> codec,
        boolean allowEmpty
) implements RecipeComponent<GasIngredient> {
    public static final RecipeComponentType<GasIngredient> GAS_INGREDIENT = RecipeComponentType.unit(ResourceHelper.asResource("gas_ingredient"), type -> new GasIngredientComponent(type, GasIngredient.CODEC_NON_EMPTY, false));
    public static final RecipeComponentType<GasIngredient> OPTIONAL_GAS_INGREDIENT = RecipeComponentType.unit(ResourceHelper.asResource("gas_ingredient"), type -> new GasIngredientComponent(type, GasIngredient.CODEC_NON_EMPTY, true));


    @Override
    public TypeInfo typeInfo() {
        return GasWrapper.INGREDIENT_TYPE_INFO;
    }

    @Override
    public boolean hasPriority(RecipeMatchContext cx, Object from) {
        return from instanceof SizedGasIngredient
                || from instanceof GasIngredient
                || from instanceof GasStack
                || from instanceof Gas;
    }

    @Override
    public boolean matches(RecipeMatchContext cx, GasIngredient value, ReplacementMatchInfo match) {
        return match.match() instanceof GasMatch m && m.matches(cx, value, match.exact());
    }

    @Override
    public boolean isEmpty(GasIngredient value) {
        return value.isEmpty();
    }

    @Override
    public void buildUniqueId(UniqueIdBuilder builder, GasIngredient value) {
        if (!value.isEmpty()) {
            builder.append(value.getStacks()[0].getGas().getResourceLocation());
        }
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
