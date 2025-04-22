package dev.wolfieboy09.qstorage.block.pipe.pipes;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlock;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

@NothingNullByDefault
public class ItemPipe extends BasePipeBlock<IItemHandler> {
    public ItemPipe() {
        super(Capabilities.ItemHandler.BLOCK);
    }

}
