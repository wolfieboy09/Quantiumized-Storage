package dev.wolfieboy09.qstorage.block.pipe;

import dev.wolfieboy09.qstorage.block.GlobalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BasePipeBlockEntity extends GlobalBlockEntity {
    public BasePipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
}