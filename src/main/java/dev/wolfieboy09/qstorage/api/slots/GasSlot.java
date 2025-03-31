package dev.wolfieboy09.qstorage.api.slots;

import dev.wolfieboy09.qstorage.item.GasCanisterItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class GasSlot extends SlotItemHandler {
    public GasSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof GasCanisterItem;
    }
}
