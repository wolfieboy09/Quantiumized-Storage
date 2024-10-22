package dev.wolfieboy09.qstorage.block.disk_assembler;

import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class DiskAssemblerBlockEntity extends AbstractEnergyBlockEntity implements IEnergyStorage {
    public DiskAssemblerBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.DISK_ASSEMBLER.get(), pos, blockState, 20000, 1000);
    }

    @Override
    public int receiveEnergy(int i, boolean b) {
        return 0;
    }

    @Override
    public int extractEnergy(int i, boolean b) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return 0;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
