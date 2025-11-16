package dev.wolfieboy09.qtech.integration.kubejs.recipes.components;

import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.wolfieboy09.qtech.api.recipes.result.GasStackChanceResult;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.components.framework.ChanceComponent;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.match.results.GasStackChanceMatch;
import dev.wolfieboy09.qtech.integration.kubejs.wrappers.GasStackChanceWrapper;

public class GasStackChanceComponent extends ChanceComponent<GasStackChanceResult> {
    public static final RecipeComponentType<GasStackChanceResult> TYPE = RecipeComponentType.unit(ResourceHelper.asResource("gas_stack_chance"), GasStackChanceComponent::new);

    public GasStackChanceComponent(RecipeComponentType<?> type) {
        super(type, GasStackChanceResult.CODEC, GasStackChanceResult.class);
    }

    @Override
    public boolean hasPriority(RecipeMatchContext cx, Object from) {
        return from instanceof GasStackChanceResult
                || from instanceof GasStack
                || from instanceof Gas;
    }

    @Override
    public boolean matches(RecipeMatchContext cx, GasStackChanceResult value, ReplacementMatchInfo match) {
        return match.match() instanceof GasStackChanceMatch m && m.matches(cx, value, match.exact());
    }

    @Override
    public GasStackChanceResult replace(RecipeScriptContext cx, GasStackChanceResult original, ReplacementMatchInfo match, Object with) {
        if (matches(cx, original, match)) {
            return switch (with) {
                case GasStackChanceResult output -> output;
                case GasStack stack -> new GasStackChanceResult(stack);
                default -> {
                    var output = GasStackChanceWrapper.wrapGasStackChance(cx.cx(), with);
                    if (output != GasStackChanceResult.EMPTY &&
                            !(GasStack.isSameGasSameComponents(output.getResult(), output.getResult()))) {
                                yield new GasStackChanceResult(output.getResult(), original.getChance());
                    }
                    yield original;
                }
            };
        }
        return original;
    }

    @Override
    public boolean isEmpty(GasStackChanceResult value) {
        return value == GasStackChanceResult.EMPTY || value.isEmpty();
    }

    @Override
    public void buildUniqueId(UniqueIdBuilder builder, GasStackChanceResult value) {
        if (!isEmpty(value)) {
            builder.append(value.getResult().getGas().getResourceLocation());
        }
    }
}
