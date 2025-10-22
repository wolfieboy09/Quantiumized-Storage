package dev.wolfieboy09.qtech.integration.kubejs.recipes.schemas;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.TickDuration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface DiskAssemblySchema {
    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.instance().asList().key("ingredients", ComponentRole.INPUT);
    RecipeKey<List<Ingredient>> EXTRAS = IngredientComponent.INGREDIENT.instance().asList().key("extras", ComponentRole.INPUT);
    RecipeKey<Integer> ENERGY = NumberComponent.INT.key("energy", ComponentRole.INPUT).optional(500);
    RecipeKey<TickDuration> TICKS = TimeComponent.TICKS.key("ticks", ComponentRole.INPUT).optional(TickDuration.of(100));
    RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENTS, EXTRAS, ENERGY, TICKS);
}
