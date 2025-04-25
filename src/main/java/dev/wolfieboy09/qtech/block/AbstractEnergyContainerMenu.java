package dev.wolfieboy09.qtech.block;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractEnergyContainerMenu extends AbstractContainerMenu {
    public AbstractEnergyContainerMenu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    public abstract int getEnergy();

    public abstract int getMaxEnergy();

    public int getEnergyStoredScaled() {
        return (int) ((getEnergy() * 100) / (float) getMaxEnergy());
    }
}
