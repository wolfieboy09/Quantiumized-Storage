package dev.wolfieboy09.qtech.integration.kubejs.recipes;

import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.TickDuration;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.wolfieboy09.qtech.api.gas.crafting.SizedGasIngredient;
import dev.wolfieboy09.qtech.api.recipes.result.FluidStackChanceResult;
import dev.wolfieboy09.qtech.api.recipes.result.GasStackChanceResult;
import dev.wolfieboy09.qtech.api.recipes.result.ItemStackChanceResult;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.api.util.TriEither;
import dev.wolfieboy09.qtech.block.disk_assembler.NewDiskAssemblerRecipe;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.components.SizedGasIngredientComponent;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.components.TriEitherComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public final class QTRecipeSchema {
    private static final RecipeKey<List<TriEither<Ingredient, SizedFluidIngredient, SizedGasIngredient>>> INGREDIENTS = TriEitherComponent.of(IngredientComponent.OPTIONAL_INGREDIENT.instance(), SizedFluidIngredientComponent.OPTIONAL_FLAT.instance(), SizedGasIngredientComponent.OPTIONAL_FLAT.instance()).asList().inputKey("ingredients");
    private static final RecipeKey<Integer> ENERGY = NumberComponent.INT.otherKey("energy");
    private static final RecipeKey<TickDuration> TICKS = TimeComponent.TICKS.otherKey("processing_time");
    //private static final RecipeKey<List<TriEither<ItemStackChanceResult, FluidStackChanceResult, GasStackChanceResult>>> RESULT = TriEitherComponent.of(ItemStackComponent.ITEM_STACK.instance(), FluidStackChanceResult.OPTIONAL_CODEC, GasStackChanceResult.OPTIONAL_CODEC).asList().outputKey("results");

    private static final RecipeKey<List<Ingredient>> EXTRA_INGREDIENTS = IngredientComponent.INGREDIENT.instance().asList().key("extras", ComponentRole.INPUT);
    private static final RecipeKey<List<Either<ItemStack, FluidStack>>> WASTE = ItemStackComponent.ITEM_STACK.instance().or(FluidStackComponent.FLUID_STACK.instance()).asList().key("waste", ComponentRole.OUTPUT);
    private static final RecipeKey<Integer> TEMPERATURE = NumberComponent.INT.key("temperature", ComponentRole.OTHER);

    private static RecipeSchema create(String id, Class<? extends Recipe<?>> recipeClass, QTRecipeFactory factory, RecipeKey<?>... keys) {
        return new RecipeSchema(keys).factory(new KubeRecipeFactory(ResourceHelper.asResource(id), TypeInfo.of(recipeClass), () -> factory));
    }

//    public static final RecipeSchema DISK_ASSEMBLY = create("disk_assembly", NewDiskAssemblerRecipe.class,
//            new QTRecipeFactory()
//                    .ingredients(INGREDIENTS, 3)
//                    .extraIngredients(EXTRA_INGREDIENTS, 4)
//                    .resultItemOutputs(RESULT, 1)
//                    .energy(ENERGY)
//                    .duration(TICKS), RESULT, INGREDIENTS, EXTRA_INGREDIENTS, ENERGY, TICKS);
}
