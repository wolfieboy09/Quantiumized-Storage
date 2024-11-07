package dev.wolfieboy09.qstorage.block;

import dev.wolfieboy09.qstorage.api.energy.ExtendedEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class AbstractEnergyBlockEntity extends GlobalBlockEntity implements IEnergyStorage {
    protected final ExtendedEnergyStorage energyStorage;
    protected ContainerData energyContainer = new ContainerData() {
        private int energy = 0;
        @Override
        public int get(int i) {
            return energy;
        }
        
        @Override
        public void set(int i, int i1) {
            energy = i1;
        }
        
        @Override
        public int getCount() {
            return 1;
        }
    };
    
    public AbstractEnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, int maxTransfer) {
        super(type, pos, blockState);
        this.energyStorage = new ExtendedEnergyStorage(capacity, maxTransfer,this);
    }
    

    public AbstractEnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, int maxReceive, int maxExtract) {
        super(type, pos, blockState);
        this.energyStorage = new ExtendedEnergyStorage(capacity, maxReceive, maxExtract,this);
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
    public int receiveEnergy(int i, boolean b) {
        return this.energyStorage.receiveEnergy(i, b);
    }

    @Override
    public int extractEnergy(int i, boolean b) {
        return this.energyStorage.extractEnergy(i, b);
    }

    // optional functions for blocks

    /**
     * Gets called from {@link #saveAdditional}
     * @param tag
     * @param registries
     */
    public void saveExtra(CompoundTag tag, HolderLookup.Provider registries) {}

    /**
     * Gets called from {@link #loadAdditional}
     * @param tag
     * @param registries
     */
    protected void loadExtra(CompoundTag tag, HolderLookup.Provider registries) {}

    /**
     * Gets called from {@link #getUpdateTag}
     * @param tag
     * @param lookupProvider
     * @return
     */
    public CompoundTag updateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) { return tag; }

    /**
     * Gets called from {@link #handleUpdateTag}
     * @param tag
     * @param lookupProvider
     */
    public void handleUpdate(CompoundTag tag, HolderLookup.Provider lookupProvider) {}


    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Energy", this.energyStorage.serializeNBT(registries));
        saveExtra(tag, registries);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = updateTag(super.getUpdateTag(registries), registries);
        tag.put("Energy", this.energyStorage.serializeNBT(registries));
        return tag;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        Tag energyTag = tag.get("Energy");
        if (energyTag == null) return;
        this.energyStorage.deserializeNBT(registries, energyTag);
        loadExtra(tag, registries);
    }
    
    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider lookupProvider) {
        Tag energyTag = tag.get("Energy");
        if (energyTag == null) return;
        this.energyStorage.deserializeNBT(lookupProvider, energyTag);
        handleUpdate(tag, lookupProvider);
    }
    
    @Override
    public void setChanged() {
        super.setChanged();
        this.energyContainer.set(0, this.energyStorage.getEnergyStored());
    }
}
