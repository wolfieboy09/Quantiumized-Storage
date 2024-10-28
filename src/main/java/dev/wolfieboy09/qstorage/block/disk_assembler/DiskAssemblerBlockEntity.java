package dev.wolfieboy09.qstorage.block.disk_assembler;

import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DiskAssemblerBlockEntity extends AbstractEnergyBlockEntity {
    private int progress = 0;
    private int crafting_duration = 0;
    private int energy_required = 0;

    public DiskAssemblerBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.DISK_ASSEMBLER.get(), pos, blockState, getEnergyCapacity(), 1000, 0);
    }

    public static class DiskAssemblerSlot {
        public static final int MAIN_SLOT_1 = 0;
        public static final int MAIN_SLOT_2 = 1;
        public static final int MAIN_SLOT_3 = 2;
        public static final int EXTRA_SLOT_1 = 3;
        public static final int EXTRA_SLOT_2 = 4;
        public static final int EXTRA_SLOT_3 = 5;
        public static final int EXTRA_SLOT_4 = 6;
        public static final int OUTPUT_SLOT = 7;
    }

    private final ItemStackHandler inventory = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            if (slot < 7) {
                resetProgress();
            }
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    };

    protected void resetProgress() {
        if (this.progress != 0) {
            this.progress = 0;
            setChanged();
            Objects.requireNonNull(level).sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
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

    public static int getEnergyCapacity() {
        return 20000;
    }

    public EnergyStorage getEnergyHandler(@Nullable Direction side) {
        if (side == null) return this.getEnergyStorage(); // for special cases
        Direction blockFacing = this.getBlockState().getValue(DiskAssemblerBlock.FACING);
        return side == blockFacing.getOpposite() ? this.getEnergyStorage() : null;
    }

    public ItemStackHandler getInventoryHandler() {
        return this.inventory;
    }
}
