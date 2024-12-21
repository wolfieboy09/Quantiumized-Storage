package dev.wolfieboy09.qstorage.api.items;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class CapacityItemStackHandler extends ItemStackHandler {
    private final int capacity;

    public CapacityItemStackHandler(int capacity) {
        super((int) Math.ceil((double) capacity / 64));
        this.capacity = capacity;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (getTotalItemCount() + stack.getCount() > capacity) {
            return stack; // don't insert if it would exceed capacity
        }
        return super.insertItem(slot, stack, simulate);
    }

    private int getTotalItemCount() {
        int count = 0;
        for (int i = 0; i < getSlots(); i++) {
            count += getStackInSlot(i).getCount();
        }
        return count;
    }
}
