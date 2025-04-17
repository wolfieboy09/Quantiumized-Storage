package dev.wolfieboy09.qstorage.block.pipe;

import dev.wolfieboy09.qstorage.block.GlobalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BasePipeBlockEntity extends GlobalBlockEntity {
    public BasePipeBlockEntity(BlockEntityType<?> blockEntityTypeFor, BlockPos pos, BlockState blockState) {
        super(blockEntityTypeFor, pos, blockState);
    }
}