package dev.wolfieboy09.qtech.block.multiblock.hatches.item;

import dev.wolfieboy09.qtech.api.multiblock.MultiblockHatchRule;
import dev.wolfieboy09.qtech.api.multiblock.blocks.hatch.BaseMultiblockHatchEntity;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class MultiblockItemInputHatchBlockEntity extends BaseMultiblockHatchEntity<IItemHandler> {
    public MultiblockItemInputHatchBlockEntity(BlockPos pos, BlockState blockState) {
        super(QTBlockEntities.ITEM_INPUT_HATCH.get(), Capabilities.ItemHandler.BLOCK, pos, blockState);
    }

    @Override
    protected void tick() {

    }

    @Override
    public @NotNull MultiblockHatchRule getHatchRules() {
        return MultiblockHatchRule.insertOnly();
    }
}
