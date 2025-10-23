package dev.wolfieboy09.qtech.api.recipes.inputs;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * A recipe handler to handle recipes that use a {@link IItemHandler} and {@link IFluidHandler}
 * @param fluidHandler The {@link IFluidHandler}
 * @param itemHandler The {@link IItemHandler}
 */
public record CombinedRecipeInput(IFluidHandler fluidHandler, IItemHandler itemHandler) implements RecipeInput {

    @Override
    public @NotNull ItemStack getItem(int i) {
        return this.itemHandler.getStackInSlot(i);
    }

    public @NotNull FluidStack getFluid(int i) {
        return this.fluidHandler.getFluidInTank(i);
    }

    @Override
    public int size() {
        return this.itemHandler.getSlots();
    }

    public boolean matchItem(Ingredient ingredient) {
        boolean match = false;
        for (int i = 0; i < this.itemHandler.getSlots(); i++) {
            ItemStack stack = this.itemHandler.getStackInSlot(i);
            if (ingredient.test(stack)) {
                match = true;
            }
        }
        return match;
    }

    public Boolean matchFluid(SizedFluidIngredient fluidIngredient) {
        boolean match = false;
        for (int i = 0; i < this.fluidHandler.getTanks(); i++) {
            var stack = this.fluidHandler.getFluidInTank(i);
            if (fluidIngredient.test(stack)) {
                match = true;
            }
        }
        return match;
    }
}
