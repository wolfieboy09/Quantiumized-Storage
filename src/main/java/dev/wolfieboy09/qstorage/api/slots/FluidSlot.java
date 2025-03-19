package dev.wolfieboy09.qstorage.api.slots;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

@NothingNullByDefault
public class FluidSlot extends Slot {
//    This is necessary don't make it 0;
    private static final Container emptyContainer = new SimpleContainer(1);
    private final IFluidHandler fluidHandler;
    private final int index;

    public FluidSlot(IFluidHandler fluidHandler, int slot, int x, int y) {
        super(emptyContainer, slot, x, y);
        this.fluidHandler = fluidHandler;
        this.index = slot;
    }

    public FluidStack getFluid() {
        return this.getFluidHandler().getFluidInTank(this.index);
    }

    public void onQuickCraft(FluidStack oldStackIn, FluidStack newStackIn) {}

    public int getMaxStackSize() {
        return this.fluidHandler.getTankCapacity(this.index);
    }

    public boolean mayInsert(FluidStack stack) {
        return !stack.isEmpty() && this.fluidHandler.isFluidValid(this.index, stack);
    }

    public boolean mayDrain(Player playerIn) {
        return !this.getFluidHandler().drain(1, IFluidHandler.FluidAction.SIMULATE).isEmpty();
    }

    public FluidStack drain(int amount) {
        return this.getFluidHandler().drain(amount, IFluidHandler.FluidAction.EXECUTE);
    }

    public IFluidHandler getFluidHandler() {
        return this.fluidHandler;
    }

    @Override
    public final boolean mayPickup(Player player) {
        return super.mayPickup(player);
    }

    @Override
    public final boolean mayPlace(ItemStack stack) {
        return super.mayPlace(stack);
    }

    @Override
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }
}
