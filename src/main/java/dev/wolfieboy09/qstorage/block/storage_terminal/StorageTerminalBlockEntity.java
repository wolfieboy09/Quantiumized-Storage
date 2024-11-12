package dev.wolfieboy09.qstorage.block.storage_terminal;

import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class StorageTerminalBlockEntity extends AbstractEnergyBlockEntity {
    public StorageTerminalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState, 20000, 1000, 0);
    }

    @Override
    public boolean canExtract() {
        return false;
    }
}
