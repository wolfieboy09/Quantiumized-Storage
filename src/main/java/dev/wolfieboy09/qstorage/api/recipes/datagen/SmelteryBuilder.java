package dev.wolfieboy09.qstorage.api.recipes.datagen;

import com.mojang.datafixers.util.Either;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.block.smeltery.SmelteryRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NothingNullByDefault
public class SmelteryBuilder implements RecipeBuilder {
    protected final List<Either<Ingredient, FluidStack>> ingredients;
    protected final List<Either<ItemStack, FluidStack>> result;
    protected final List<Either<ItemStack, FluidStack>> waste;
    protected final int minFuelTemp;
    protected final int timeInTicks;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public SmelteryBuilder(List<Either<Ingredient, FluidStack>> ingredients, List<Either<ItemStack, FluidStack>> result, List<Either<ItemStack, FluidStack>> waste, int minFuelTemp, int timeInTicks) {
        this.ingredients = ingredients;
        this.result = result;
        this.waste = waste;
        this.minFuelTemp = minFuelTemp;
        this.timeInTicks = timeInTicks;
    }


    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public Item getResult() {
        if (this.result.getFirst().left().isEmpty()) return Items.AIR;
        return this.result.getFirst().left().get().getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation))
                .rewards(AdvancementRewards.Builder.recipe(resourceLocation))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);
        SmelteryRecipe recipe = new SmelteryRecipe(this.ingredients, this.result, this.waste, this.minFuelTemp, this.timeInTicks);
        recipeOutput.accept(resourceLocation, recipe, advancement.build(resourceLocation.withPath("recipes/")));
    }

    public static SmelteryBuilder create(List<Either<Ingredient, FluidStack>> ingredients, List<Either<ItemStack, FluidStack>> result, List<Either<ItemStack, FluidStack>> waste, int minFuelTemp, int timeInTicks) {
        return new SmelteryBuilder(ingredients, result, waste, minFuelTemp, timeInTicks);
    }
}
