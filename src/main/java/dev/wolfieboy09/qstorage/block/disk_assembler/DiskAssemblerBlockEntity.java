package dev.wolfieboy09.qstorage.block.disk_assembler;

import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Objects;

public class DiskAssemblerBlockEntity extends AbstractEnergyBlockEntity {
    private int progress = 0;
    public DiskAssemblerBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.DISK_ASSEMBLER.get(), pos, blockState, 20000, 1000);
    }

    private final ItemStackHandler inventory = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            if (slot < 7) {
                resetProgress();
            }
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    protected void resetProgress() {
        if (this.progress != 0) {
            this.progress = 0;
            setChanged();
            Objects.requireNonNull(level).sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public boolean canExtract() {
        return false;
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
