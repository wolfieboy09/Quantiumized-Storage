package dev.wolfieboy09.qtech.block.pipe.pipes.item;

import dev.wolfieboy09.qtech.block.pipe.BasePipeBlockEntity;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ItemPipeBlockEntity extends BasePipeBlockEntity<IItemHandler> {
    public ItemPipeBlockEntity(BlockPos pos, BlockState state) {
        super(QTBlockEntities.ITEM_PIPE.get(), Capabilities.ItemHandler.BLOCK, pos, state);
    }

    @Override
    protected boolean canExtract(IItemHandler source) {
        for (int i = 0; i < source.getSlots(); i++) {
            if (!source.extractItem(i, 1, true).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean tryTransfer(IItemHandler source, IItemHandler target) {
        for (int i = 0; i < source.getSlots(); i++) {
            ItemStack extracted = source.extractItem(i, 1, true);
            if (extracted.isEmpty()) continue; // Go to next slot if unable to take out

        ItemStack remainder = ItemHandlerHelper.insertItem(target, extracted, false);
        if (remainder.isEmpty()) {
            source.extractItem(i, 1, false);
                return true;
            }
        }
        return false;
    }
}
