package dev.wolfieboy09.qtech.integration.kubejs.schemas;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.TickDuration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface DiskAssemblySchema {
    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.instance().asList().key("ingredients", ComponentRole.INPUT);
    RecipeKey<List<Ingredient>> EXTRAS = IngredientComponent.INGREDIENT.instance().asList().key("extras", ComponentRole.OTHER);
    RecipeKey<Integer> ENERGY = NumberComponent.INT.key("energy", ComponentRole.INPUT);
    RecipeKey<TickDuration> TICKS = TimeComponent.TICKS.key("ticks", ComponentRole.INPUT);
    RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENTS, EXTRAS, ENERGY, TICKS);
}
