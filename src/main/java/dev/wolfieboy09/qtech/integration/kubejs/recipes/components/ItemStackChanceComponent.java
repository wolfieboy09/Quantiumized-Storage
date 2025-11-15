package dev.wolfieboy09.qtech.integration.kubejs.recipes.components;

import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.wolfieboy09.qtech.api.recipes.result.ItemStackChanceResult;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.components.framework.ChanceComponent;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.match.results.ItemStackChanceMatch;
import dev.wolfieboy09.qtech.integration.kubejs.wrappers.ItemStackChanceWrapper;
import net.minecraft.world.item.ItemStack;

public class ItemStackChanceComponent extends ChanceComponent<ItemStackChanceResult> {
    public ItemStackChanceComponent(RecipeComponentType<?> type) {
        super(type, ItemStackChanceResult.OPTIONAL_CODEC, ItemStackChanceResult.class);
    }

    @Override
    public boolean hasPriority(RecipeMatchContext cx, Object from) {
        return from instanceof ItemStackChanceResult;
    }

    @Override
    public boolean matches(RecipeMatchContext cx, ItemStackChanceResult value, ReplacementMatchInfo match) {
        return match.match() instanceof ItemStackChanceMatch m && m.matches(cx, value, match.exact());
    }

    @Override
    public ItemStackChanceResult replace(RecipeScriptContext cx, ItemStackChanceResult original, ReplacementMatchInfo match, Object with) {
        if (matches(cx, original, match)) {
            return switch (with) {
                case ItemStackChanceResult output -> output;
                case ItemStack stack -> new ItemStackChanceResult(stack);
                default -> {
                    var output = ItemStackChanceWrapper.wrapItemStackChance(cx.cx(), with);
                    if (output != ItemStackChanceResult.EMPTY &&
                        !(ItemStack.isSameItemSameComponents(output.getResult(), output.getResult()))) {
                            yield new ItemStackChanceResult(output.getResult(), original.getChance());
                    }
                    yield original;
                }
            };
        }

        return original;
    }

    @Override
    public boolean isEmpty(ItemStackChanceResult value) {
        return value == ItemStackChanceResult.EMPTY || value.isEmpty();
    }

    @Override
    public void buildUniqueId(UniqueIdBuilder builder, ItemStackChanceResult value) {
        if (!isEmpty(value)) {
            builder.append(value.getResult().kjs$getIdLocation());
        }
    }
}
