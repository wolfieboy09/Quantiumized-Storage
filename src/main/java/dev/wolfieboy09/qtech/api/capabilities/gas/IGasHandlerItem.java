package dev.wolfieboy09.qtech.api.capabilities.gas;

import net.minecraft.world.item.ItemStack;

public interface IGasHandlerItem extends IGasHandler {
    ItemStack getContainer();
}
