package dev.wolfieboy09.qstorage.block;

import dev.wolfieboy09.qstorage.api.energy.ExtendedEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public abstract class AbstractEnergyBlockEntity extends GlobalBlockEntity implements IEnergyStorage {
    protected final ExtendedEnergyStorage energyStorage;

    public AbstractEnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, int maxTransfer) {
        super(type, pos, blockState);
        this.energyStorage = new ExtendedEnergyStorage(capacity, maxTransfer);
    }

    public ExtendedEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(maxReceive, this.energyStorage.getMaxReceive());
        if (!simulate) {
            getEnergyStorage().addEnergy(energyReceived);
        }
        return energyReceived;
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(maxExtract, this.energyStorage.getMaxExtract());
        if (!simulate) {
            getEnergyStorage().removeEnergy(energyExtracted);
        }
        return energyExtracted;
    }

    public int getEnergyStored() {
        return this.energyStorage.getEnergyStored();
    }

    public int getMaxEnergyStored() {
        return this.energyStorage.getMaxEnergyStored();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Energy", energyStorage.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(registries, Objects.requireNonNull(tag.get("Energy")));
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.putInt("Energy", this.energyStorage.getEnergyStored());
        return tag;
    }
}
