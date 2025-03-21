package dev.wolfieboy09.qstorage.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class CombinedRecipeInput implements RecipeInput {
    protected final IFluidHandler fluidHandler;
    protected final IItemHandler itemHandler;

    public CombinedRecipeInput(IFluidHandler fluidHandler, IItemHandler itemHandler) {
        this.fluidHandler = fluidHandler;
        this.itemHandler = itemHandler;
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return this.itemHandler.getStackInSlot(i);
    }

    public @NotNull FluidStack getFluid(int i) {
        return this.fluidHandler.getFluidInTank(i);
    }

    public boolean matchFluid(Fluid fluid, int i) {
        return this.fluidHandler.getFluidInTank(i).getFluid() == fluid;
    }

    @Override
    public int size() {
        return this.fluidHandler.getTanks() + this.itemHandler.getSlots();
    }
}
