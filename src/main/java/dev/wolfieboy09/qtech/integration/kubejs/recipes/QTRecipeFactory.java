package dev.wolfieboy09.qtech.integration.kubejs.recipes;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentValue;
import dev.latvian.mods.kubejs.recipe.component.RecipeValidationContext;
import dev.latvian.mods.kubejs.util.ErrorStack;
import dev.latvian.mods.kubejs.util.TickDuration;
import dev.wolfieboy09.qtech.api.gas.crafting.SizedGasIngredient;
import dev.wolfieboy09.qtech.api.recipes.CleanroomCondition;
import dev.wolfieboy09.qtech.api.recipes.result.FluidStackChanceResult;
import dev.wolfieboy09.qtech.api.recipes.result.GasStackChanceResult;
import dev.wolfieboy09.qtech.api.recipes.result.ItemStackChanceResult;
import dev.wolfieboy09.qtech.api.util.TriEither;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class QTRecipeFactory extends KubeRecipe {
    RecipeKey<List<TriEither<Ingredient, SizedFluidIngredient, SizedGasIngredient>>> INGREDIENTS = null;
    RecipeKey<List<Ingredient>> EXTRAS = null;
    RecipeKey<Integer> ENERGY = null;
    RecipeKey<TickDuration> TICKS = null;
    RecipeKey<List<TriEither<ItemStackChanceResult, FluidStackChanceResult, GasStackChanceResult>>> RESULT = null;
    RecipeKey<CleanroomCondition> CLEANROOM_CONDITION = null;

    boolean hasProcessingTime = false;
    boolean usesEnergy = false;
    boolean usesExtraIngredients = false;

    private int maxIngredientInputs = 0;
    private int maxFluidInputs = 0;
    private int maxGasInputs = 0;

    private int maxExtraIngredientInputs = 0;

    private int maxItemOutputs = 0;
    private int maxFluidOutputs = 0;
    private int maxGasOutputs = 0;


    public QTRecipeFactory ingredients(RecipeKey<List<TriEither<Ingredient, SizedFluidIngredient, SizedGasIngredient>>> key, int maxIngredientInputs, int maxFluidInputs, int maxGasInputs) {
        this.INGREDIENTS = key;
        this.maxIngredientInputs = maxIngredientInputs;
        this.maxFluidInputs = maxFluidInputs;
        this.maxGasInputs = maxGasInputs;
        return this;
    }

    public QTRecipeFactory extraIngredients(RecipeKey<List<Ingredient>> key, int maxExtraIngredientInputs) {
        this.usesExtraIngredients = true;
        this.EXTRAS = key;
        this.maxExtraIngredientInputs = maxExtraIngredientInputs;
        return this;
    }

    public QTRecipeFactory energy(RecipeKey<Integer> key) {
        this.usesEnergy = true;
        this.ENERGY = key;
        return this;
    }

    public QTRecipeFactory duration(RecipeKey<TickDuration> key) {
        this.hasProcessingTime = true;
        this.TICKS = key;
        return this;
    }

    public QTRecipeFactory results(RecipeKey<List<TriEither<ItemStackChanceResult, FluidStackChanceResult, GasStackChanceResult>>> key, int maxItemOutputs, int maxFluidOutputs, int maxGasOutputs) {
        this.maxItemOutputs = maxItemOutputs;
        this.maxFluidOutputs = maxFluidOutputs;
        this.maxGasOutputs = maxGasOutputs;
        this.RESULT = key;
        return this;
    }

    public QTRecipeFactory cleanroom(RecipeKey<CleanroomCondition> cleanroomCondition) {
        this.CLEANROOM_CONDITION = cleanroomCondition;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(@NotNull RecipeValidationContext cx) {
        keyCheck(cx.errors());
        for (RecipeComponentValue<?> v : getRecipeComponentValues()) {
            RecipeKey<?> key = v.getKey();
            var value = v.getValue();
            if (key == INGREDIENTS && value instanceof List<?> g) {
                AtomicInteger itemIngredients = new AtomicInteger();
                AtomicInteger fluidIngredients =  new AtomicInteger();
                AtomicInteger gasIngredients =  new AtomicInteger();

                g.forEach(i -> ((TriEither<Ingredient, SizedFluidIngredient, SizedGasIngredient>) i).map(
                        ing -> itemIngredients.getAndIncrement(),
                        ing -> fluidIngredients.getAndIncrement(),
                        ing -> gasIngredients.getAndIncrement()
                ));

                if (itemIngredients.get() > maxIngredientInputs) {
                    cx.errors().push(new KubeRuntimeException("Recipe can only have a max of " + maxIngredientInputs + " ingredients").source(sourceLine));
                }

                if (fluidIngredients.get() > maxFluidInputs) {
                    cx.errors().push(new KubeRuntimeException("Recipe can only have a max of " + maxFluidInputs + " fluid ingredients").source(sourceLine));
                }

                if (gasIngredients.get() > maxGasInputs) {
                    cx.errors().push(new KubeRuntimeException("Recipe can only have a max of " + maxGasInputs + " gas ingredients").source(sourceLine));
                }

            }

            if (key == RESULT && value instanceof List<?> g && g.size() > maxItemOutputs) {
                AtomicInteger itemResults = new AtomicInteger();
                AtomicInteger fluidResults = new AtomicInteger();
                AtomicInteger gasResults = new AtomicInteger();

                g.forEach(i -> ((TriEither<ItemStackChanceResult, FluidStackChanceResult, GasStackChanceResult>) i).map(
                        ing -> itemResults.getAndIncrement(),
                        ing -> fluidResults.getAndIncrement(),
                        ing -> gasResults.getAndIncrement()
                ));

                if (itemResults.get() > maxItemOutputs) {
                    cx.errors().push(new KubeRuntimeException("Recipe can only have a max of " + maxItemOutputs + " results").source(sourceLine));
                }

                if (fluidResults.get() > maxFluidOutputs) {
                    cx.errors().push(new KubeRuntimeException("Recipe can only have a max of " + maxItemOutputs + " fluid results").source(sourceLine));
                }

                if (gasResults.get() > maxGasOutputs) {
                    cx.errors().push(new KubeRuntimeException("Recipe can only have a max of " + maxItemOutputs + " gas results").source(sourceLine));
                }
            }

            if (key == EXTRAS && usesExtraIngredients && value instanceof List<?> g && g.size() > maxExtraIngredientInputs) {
                cx.errors().push(new KubeRuntimeException("Recipe can only have a max of " + maxExtraIngredientInputs + " extra ingredients").source(sourceLine));
            }
        }
    }

    private void keyCheck(ErrorStack errorStack) {
        if (INGREDIENTS == null) {
            errorStack.push(new KubeRuntimeException("Recipe must have ingredients defined").source(sourceLine));
        }

        if (usesExtraIngredients && EXTRAS == null) {
            errorStack.push(new KubeRuntimeException("Recipe must have extra ingredients defined").source(sourceLine));
        }

        if (RESULT == null) {
            errorStack.push(new KubeRuntimeException("Recipe must have a result defined").source(sourceLine));
        }

        if (usesEnergy && ENERGY == null) {
            errorStack.push(new KubeRuntimeException("Recipe must have energy defined").source(sourceLine));
        }
    }
}
