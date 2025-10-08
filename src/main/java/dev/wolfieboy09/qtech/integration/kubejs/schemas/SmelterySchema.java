package dev.wolfieboy09.qtech.integration.kubejs.schemas;

import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public interface SmelterySchema {
    RecipeKey<List<Either<Ingredient, SizedFluidIngredient>>> INGREDIENTS = IngredientComponent.INGREDIENT.instance().or(SizedFluidIngredientComponent.FLAT.instance()).asList().key("ingredients", ComponentRole.INPUT);
    RecipeKey<List<Either<ItemStack, FluidStack>>> RESULTS = ItemStackComponent.ITEM_STACK.instance().or(FluidStackComponent.FLUID_STACK.instance()).asList().key("result", ComponentRole.OUTPUT);
    RecipeKey<List<Either<ItemStack, FluidStack>>> WASTE = ItemStackComponent.ITEM_STACK.instance().or(FluidStackComponent.FLUID_STACK.instance()).asList().key("waste", ComponentRole.OUTPUT);
    RecipeKey<Integer> TEMPERATURE = NumberComponent.INT.key("temperature", ComponentRole.OTHER);
    RecipeKey<Integer> TICKS = NumberComponent.INT.key("ticks", ComponentRole.OTHER);

    RecipeSchema SCHEMA = new RecipeSchema(INGREDIENTS, RESULTS, WASTE, TEMPERATURE, TICKS);
}
