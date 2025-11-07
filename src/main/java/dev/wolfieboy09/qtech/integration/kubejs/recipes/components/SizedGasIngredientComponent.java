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
public record SizedGasIngredientComponent(
        RecipeComponentType<?> type,
        Codec<SizedGasIngredient> codec,
        boolean allowEmpty
    ) implements RecipeComponent<SizedGasIngredient> {
    public static final RecipeComponentType<SizedGasIngredient> FLAT = RecipeComponentType.unit(ResourceHelper.asResource("flat_sized_gas_ingredient"), type -> new SizedGasIngredientComponent(type, SizedGasIngredient.FLAT_CODEC, false));
    public static final RecipeComponentType<SizedGasIngredient> NESTED = RecipeComponentType.unit(ResourceHelper.asResource("nested_sized_fluid_ingredient"), type -> new SizedGasIngredientComponent(type, SizedGasIngredient.NESTED_CODEC, false));
    public static final RecipeComponentType<SizedGasIngredient> OPTIONAL_FLAT = RecipeComponentType.unit(ResourceHelper.asResource("optional_flat_sized_gas_ingredient"), type -> new SizedGasIngredientComponent(type, SizedGasIngredient.FLAT_CODEC, true));
    public static final RecipeComponentType<SizedGasIngredient> OPTIONAL_NESTED = RecipeComponentType.unit(ResourceHelper.asResource("optional_nested_sized_fluid_ingredient"), type -> new SizedGasIngredientComponent(type, SizedGasIngredient.NESTED_CODEC, true));

    @Override
    public TypeInfo typeInfo() {
        return GasWrapper.SIZED_INGREDIENT_TYPE_INFO;
    }

    @Override
    public boolean hasPriority(RecipeMatchContext cx, Object from) {
        return from instanceof SizedGasIngredient
                || from instanceof GasIngredient
                || from instanceof GasStack
                || from instanceof Gas;
    }

    @Override
    public boolean matches(RecipeMatchContext cx, SizedGasIngredient value, ReplacementMatchInfo match) {
        return match.match() instanceof GasMatch m && m.matches(cx, value.ingredient(), match.exact());
    }

    @Override
    public boolean isEmpty(SizedGasIngredient value) {
        return value.amount() <= 0 || value.ingredient().isEmpty();
    }

    @Override
    public void buildUniqueId(UniqueIdBuilder builder, SizedGasIngredient value) {
        if (!value.ingredient().isEmpty()) {
            GasStack[] stacks = value.ingredient().getStacks();
            if (stacks.length > 0) {
                builder.append(stacks[0].getGas().getResourceLocation());
            }
        }
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
