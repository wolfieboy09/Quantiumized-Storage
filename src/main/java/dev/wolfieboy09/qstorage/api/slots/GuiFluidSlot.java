package dev.wolfieboy09.qstorage.api.slots;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class GuiFluidSlot extends FluidSlot {
    public GuiFluidSlot(IFluidHandler fluidHandler, int slot, int x, int y) {
        super(fluidHandler, slot, x, y);
    }

    @Override
    public boolean mayDrain(@NotNull Player playerIn) {
        return false;
    }
}
