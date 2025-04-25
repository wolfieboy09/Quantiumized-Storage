package dev.wolfieboy09.qtech.block.pipe.pipes;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.block.pipe.BasePipeBlock;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

@NothingNullByDefault
public class ItemPipe extends BasePipeBlock<IItemHandler> {
    public ItemPipe() {
        super(Capabilities.ItemHandler.BLOCK);
    }

}
