package dev.wolfieboy09.qtech.api.recipes;

import com.google.common.base.Joiner;
import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.gas.crafting.GasIngredient;
import dev.wolfieboy09.qtech.api.gas.crafting.SizedGasIngredient;
import dev.wolfieboy09.qtech.api.recipes.result.FluidStackChanceResult;
import dev.wolfieboy09.qtech.api.recipes.result.GasStackChanceResult;
import dev.wolfieboy09.qtech.api.recipes.result.ItemStackChanceResult;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.List;

@NothingNullByDefault
public abstract class ProcessingRecipeBuilder<P extends ProcessingRecipeParams, R extends ProcessingRecipe<?, P>, S extends ProcessingRecipeBuilder<P, R, S>> {
    protected ResourceLocation recipeId;
    protected ProcessingRecipe.Factory<P, R> factory;
    protected P params;
    protected List<ICondition> recipeConditions;

    public ProcessingRecipeBuilder(ProcessingRecipe.Factory<P, R> factory, ResourceLocation recipeId) {
        this.recipeId = recipeId;
        this.factory = factory;
        this.params = createParams();
        this.recipeConditions = new ArrayList<>();
    }

    protected abstract P createParams();

    public abstract S self();

    public S withItemIngredients(SizedIngredient... ingredients) {
        return withItemIngredients(NonNullList.of(SizedIngredient.of(Items.AIR, 1), ingredients));
    }

    public S withItemIngredients(NonNullList<SizedIngredient> ingredients) {
        params.ingredients = ingredients;
        return self();
    }

    public S withSingleItemOutput(ItemStack output) {
        return withItemOutputs(new ItemStackChanceResult(output));
    }

    public S withItemOutputs(ItemStackChanceResult... outputs) {
        return withItemOutputs(NonNullList.of(ItemStackChanceResult.EMPTY, outputs));
    }

    public S withItemOutputs(NonNullList<ItemStackChanceResult> outputs) {
        params.itemResults = outputs;
        return self();
    }

    public S withFluidIngredients(SizedFluidIngredient... ingredients) {
        return withFluidIngredients(NonNullList.of(new SizedFluidIngredient(FluidIngredient.empty(), 1000), ingredients));
    }

    public S withFluidIngredients(NonNullList<SizedFluidIngredient> ingredients) {
        params.fluidIngredients = ingredients;
        return self();
    }

    public S withFluidOutputs(FluidStackChanceResult... outputs) {
        return withFluidOutputs(NonNullList.of(FluidStackChanceResult.EMPTY, outputs));
    }

    public S withFluidOutputs(NonNullList<FluidStackChanceResult> outputs) {
        params.fluidResults = outputs;
        return self();
    }

    public S withGasIngredients(SizedGasIngredient... ingredients) {
        params.gasIngredients = NonNullList.of(new SizedGasIngredient(GasIngredient.empty(), 1000), ingredients);
        return self();
    }

    public S withGasIngredients(NonNullList<SizedGasIngredient> ingredients) {
        params.gasIngredients = ingredients;
        return self();
    }

    public S withGasOutputs(GasStackChanceResult... outputs) {
        params.gasResults = NonNullList.of(GasStackChanceResult.EMPTY, outputs);
        return self();
    }

    public S duration(int ticks) {
        params.processingDuration = ticks;
        return self();
    }

    public S energyCost(int energyCost) {
        params.energyCost = energyCost;
        return self();
    }

    public S averageProcessingDuration() {
        return duration(100);
    }

    public S requiresCleanroom(CleanroomCondition condition) {
        params.requiredCleanRoom = condition;
        return self();
    }

    public R build() {
        return factory.create(params);
    }

    public void build(RecipeOutput consumer) {
        R recipe = build();
        IRecipeTypeInfo recipeType = recipe.getTypeInfo();
        ResourceLocation typeId = recipeType.getId();
        ResourceLocation id = recipeId.withPrefix(typeId.getPath() + "/");
        var errors = recipe.validate();
        if (!errors.isEmpty()) {
            errors.add(recipe.getClass().getSimpleName() + "with id " + id + " failed validation:");
            QuantiumizedTech.LOGGER.warn(Joiner.on('\n').join(errors));
        }
        consumer.accept(id, recipe, null, recipeConditions.toArray(new ICondition[0]));
    }

    public S require(TagKey<Item> tag) {
        return require(SizedIngredient.of(tag, 1));
    }

    public S require(ItemLike item) {
        return require(SizedIngredient.of(item, 1));
    }

    public S require(TagKey<Item> tag, int count, byte... dummy) {
        return require(SizedIngredient.of(tag, count));
    }

    public S require(ItemLike item, int count) {
        return require(SizedIngredient.of(item, count));
    }

    public S require(SizedIngredient ingredient) {
        params.ingredients.add(ingredient);
        return self();
    }

    public S require(FlowingFluid fluid, int amount) {
        return require(SizedFluidIngredient.of(fluid.getSource(), amount));
    }

    public S require(TagKey<Fluid> fluidTag, int amount) {
        return require(SizedFluidIngredient.of(fluidTag, amount));
    }

    public S require(SizedFluidIngredient ingredient) {
        params.fluidIngredients.add(ingredient);
        return self();
    }

    public S output(ItemLike item) {
        return output(item, 1);
    }

    public S output(float chance, ItemLike item) {
        return output(chance, item, 1);
    }

    public S output(ItemLike item, int amount) {
        return output(1, item, amount);
    }

    public S output(float chance, ItemLike item, int amount) {
        return output(chance, new ItemStack(item, amount));
    }

    public S output(ItemStack output) {
        return output(1, output);
    }

    public S output(float chance, ItemStack output) {
        return output(new ItemStackChanceResult(output, chance));
    }

    public S output(ItemStackChanceResult output) {
        params.itemResults.add(output);
        return self();
    }

    public S output(Fluid fluid, int amount) {
        fluid = convertToStill(fluid);
        return output(new FluidStackChanceResult(new FluidStack(fluid, 1000), amount));
    }

    public S output(FluidStackChanceResult fluidStack) {
        params.fluidResults.add(fluidStack);
        return self();
    }

    public S require(SizedGasIngredient gas) {
        params.gasIngredients.add(gas);
        return self();
    }

    public S output(Gas gas, int amount) {
        return output(new GasStackChanceResult(new GasStack(gas, 1000), amount));
    }

    public S output(GasStackChanceResult result) {
        params.gasResults.add(result);
        return self();
    }

    private static Fluid convertToStill(Fluid notSoStill) {
        if (notSoStill == Fluids.FLOWING_WATER)
            return Fluids.WATER;
        if (notSoStill == Fluids.FLOWING_LAVA)
            return Fluids.LAVA;
        if (notSoStill instanceof BaseFlowingFluid)
            return ((BaseFlowingFluid) notSoStill).getSource();
        return notSoStill;
    }
}
