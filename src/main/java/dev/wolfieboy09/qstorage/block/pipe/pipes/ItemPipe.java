package dev.wolfieboy09.qstorage.block.pipe.pipes;

import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlock;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemPipe extends BasePipeBlock<IItemHandler> {
    public ItemPipe() {
        super(Capabilities.ItemHandler.BLOCK, MapColor.COLOR_BLUE);
    }
}
