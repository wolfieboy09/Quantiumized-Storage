package dev.wolfieboy09.qtech.integration.kubejs.recipes;

import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeConstructor;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.TickDuration;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.block.disk_assembler.DiskAssemblerRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public final class QTRecipeSchema {
    private static final RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.instance().asList().key("ingredients", ComponentRole.INPUT);
    private static final RecipeKey<List<Ingredient>> EXTRA_INGREDIENTS = IngredientComponent.INGREDIENT.instance().asList().key("extras", ComponentRole.INPUT);
    private static final RecipeKey<Integer> ENERGY = NumberComponent.INT.key("energy", ComponentRole.OTHER);
    private static final RecipeKey<TickDuration> TICKS = TimeComponent.TICKS.key("ticks", ComponentRole.OTHER);
    private static final RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.key("result", ComponentRole.OUTPUT);

    private static final RecipeKey<List<Either<ItemStack, FluidStack>>> WASTE = ItemStackComponent.ITEM_STACK.instance().or(FluidStackComponent.FLUID_STACK.instance()).asList().key("waste", ComponentRole.OUTPUT);
    private static final RecipeKey<Integer> TEMPERATURE = NumberComponent.INT.key("temperature", ComponentRole.OTHER);

    private static RecipeSchema create(String id, QTRecipeFactory factory, RecipeKey<?>... keys) {
        return new RecipeSchema(keys).factory(new KubeRecipeFactory(ResourceHelper.asResource(id), TypeInfo.of(DiskAssemblerRecipe.class), () -> factory));
    }

    public static final RecipeSchema DISK_ASSEMBLY = create("disk_assembly",
            new QTRecipeFactory()
                    .ingredients(INGREDIENTS, 3)
                    .extraIngredients(EXTRA_INGREDIENTS, 4)
                    .resultItemOutputs(RESULT, 1)
                    .energy(ENERGY)
                    .duration(TICKS), RESULT, INGREDIENTS, EXTRA_INGREDIENTS, ENERGY, TICKS);
}
