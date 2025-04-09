package dev.wolfieboy09.qstorage.block.pipe;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemPipe extends BasePipeBlock<BlockCapability<IItemHandler, Direction>> {
    public ItemPipe(Properties props) {
        super(Capabilities.ItemHandler.BLOCK, MapColor.COLOR_BLUE);
    }
}
