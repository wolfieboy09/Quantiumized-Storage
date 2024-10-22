package dev.wolfieboy09.qstorage.block.disk_assembler;

import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class DiskAssemblerBlockEntity extends AbstractEnergyBlockEntity implements IEnergyStorage {
    public DiskAssemblerBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.DISK_ASSEMBLER.get(), pos, blockState, 20000, 1000);
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    public EnergyStorage getEnergyHandler(Direction side) {
        Direction blockFacing = this.getBlockState().getValue(DiskAssemblerBlock.FACING);
        return side == blockFacing.getOpposite() ? this.getEnergyStorage() : null;
    }
}
