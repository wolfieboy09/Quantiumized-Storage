package dev.wolfieboy09.qstorage.block.pipe.pipes.entities;

import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ItemPipeBlockEntity extends BasePipeBlockEntity {
    public ItemPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.ITEM_PIPE_BLOCK.get(), pos, blockState);
    }
}
