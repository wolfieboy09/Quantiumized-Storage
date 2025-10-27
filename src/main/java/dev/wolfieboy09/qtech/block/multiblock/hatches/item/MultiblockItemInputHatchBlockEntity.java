package dev.wolfieboy09.qtech.block.multiblock.hatches.item;

import dev.wolfieboy09.qtech.api.multiblock.blocks.hatch.BaseMultiblockHatchEntity;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class MultiblockItemInputHatchBlockEntity extends BaseMultiblockHatchEntity<IItemHandler> {
    public MultiblockItemInputHatchBlockEntity(BlockPos pos, BlockState blockState) {
        super(QTBlockEntities.ITEM_INPUT_HATCH.get(), Capabilities.ItemHandler.BLOCK, pos, blockState);
    }
}
