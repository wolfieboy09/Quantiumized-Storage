package dev.wolfieboy09.qstorage.api.capabilities.gas;

import net.minecraft.world.item.ItemStack;

public interface IGasHandlerItem extends IGasHandler {
    ItemStack getContainer();
}
