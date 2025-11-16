package dev.wolfieboy09.qtech.integration.kubejs.recipes.components;

import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.wolfieboy09.qtech.api.recipes.result.FluidStackChanceResult;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.components.framework.ChanceComponent;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.match.results.FluidStackChanceMatch;
import dev.wolfieboy09.qtech.integration.kubejs.wrappers.FluidStackChanceWrapper;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidStackChanceComponent extends ChanceComponent<FluidStackChanceResult> {
    public static final RecipeComponentType<FluidStackChanceResult> TYPE = RecipeComponentType.unit(ResourceHelper.asResource("fluid_stack_chance"), FluidStackChanceComponent::new);

    public FluidStackChanceComponent(RecipeComponentType<?> type) {
        super(type, FluidStackChanceResult.CODEC, FluidStackChanceResult.class);
    }

    @Override
    public boolean hasPriority(RecipeMatchContext cx, Object from) {
        return from instanceof FluidStackChanceResult
                || from instanceof FluidStack
                || from instanceof Fluid;
    }

    @Override
    public boolean matches(RecipeMatchContext cx, FluidStackChanceResult value, ReplacementMatchInfo match) {
        return match.match() instanceof FluidStackChanceMatch m && m.matches(cx, value, match.exact());
    }

    @Override
    public FluidStackChanceResult replace(RecipeScriptContext cx, FluidStackChanceResult original, ReplacementMatchInfo match, Object with) {
        if (matches(cx, original, match)) {
            return switch (with) {
                case FluidStackChanceResult output -> output;
                case FluidStack stack -> new FluidStackChanceResult(stack);
                default -> {
                    var output = FluidStackChanceWrapper.wrapFluidStackChance(cx.cx(), with);
                    if (output != FluidStackChanceResult.EMPTY &&
                            !(FluidStack.isSameFluidSameComponents(output.getResult(), output.getResult()))) {
                                yield new FluidStackChanceResult(output.getResult(), original.getChance());
                    }
                    yield original;
                }
            };
        }
        return original;
    }

    @Override
    public boolean isEmpty(FluidStackChanceResult value) {
        return value == FluidStackChanceResult.EMPTY || value.isEmpty();
    }

    @Override
    public void buildUniqueId(UniqueIdBuilder builder, FluidStackChanceResult value) {
        if (!isEmpty(value)) {
            builder.append(value.getResult().getFluid().kjs$getIdLocation());
        }
    }
}
