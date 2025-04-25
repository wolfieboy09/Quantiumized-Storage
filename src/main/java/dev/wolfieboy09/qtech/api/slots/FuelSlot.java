package dev.wolfieboy09.qtech.api.slots;

import dev.wolfieboy09.qtech.registries.QTDataMaps;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class FuelSlot extends SlotItemHandler {
    public FuelSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItemHolder().getData(QTDataMaps.SMELTERY_FUEL_ITEM) != null;
    }
}
