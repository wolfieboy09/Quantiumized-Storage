package dev.wolfieboy09.qstorage.block;

import dev.wolfieboy09.qstorage.api.energy.ExtendedEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class AbstractEnergyBlockEntity extends GlobalBlockEntity implements IEnergyStorage {
    protected final ExtendedEnergyStorage energyStorage;

    public AbstractEnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, int maxTransfer) {
        super(type, pos, blockState);
        this.energyStorage = new ExtendedEnergyStorage(capacity, maxTransfer) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int energyReceived = Math.min(maxReceive, this.getMaxReceive());
                if (!simulate) {
                    this.addEnergy(energyReceived);
                    setChanged();
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                }
                return energyReceived;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int energyExtracted = Math.min(maxExtract, this.getMaxExtract());
                if (!simulate) {
                    this.removeEnergy(energyExtracted);
                    setChanged();
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                }
                return energyExtracted;
            }
        };
    }

    public AbstractEnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, int maxRecieve, int maxExtract) {
        super(type, pos, blockState);
        this.energyStorage = new ExtendedEnergyStorage(capacity, maxRecieve, maxExtract) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int energyReceived = Math.min(maxReceive, this.getMaxReceive());
                if (!simulate) {
                    this.addEnergy(energyReceived);
                    setChanged();
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                }
                return energyReceived;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int energyExtracted = Math.min(maxExtract, this.getMaxExtract());
                if (!simulate) {
                    this.removeEnergy(energyExtracted);
                    setChanged();
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                }
                return energyExtracted;
            }
        };
    }


    public ExtendedEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    @Override
    public int getEnergyStored() {
        return this.energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return this.energyStorage.getMaxEnergyStored();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Energy")) {
            this.energyStorage.deserializeNBT(registries, tag.get("Energy"));
        }
    }

    @Override
    public int receiveEnergy(int i, boolean b) {
        return this.energyStorage.receiveEnergy(i, b);
    }

    @Override
    public int extractEnergy(int i, boolean b) {
        return this.energyStorage.extractEnergy(i, b);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Energy", this.energyStorage.serializeNBT(registries));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.put("Energy", this.energyStorage.serializeNBT(registries));
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider lookupProvider) {
        Tag energyTag = tag.get("Energy");
        if (energyTag == null) return;
        this.energyStorage.deserializeNBT(lookupProvider, energyTag);
    }

    public void syncToClient() {
        setChanged();
        if (level == null) return;
        level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }
}
