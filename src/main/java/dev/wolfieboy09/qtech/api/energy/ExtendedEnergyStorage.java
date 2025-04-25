package dev.wolfieboy09.qtech.api.energy;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.energy.EnergyStorage;

/**
 * An extension of the {@link EnergyStorage} class, providing additional methods for managing energy storage.
 */
public class ExtendedEnergyStorage extends EnergyStorage {
    private final BlockEntity be;
    /**
     * Creates a new {@link ExtendedEnergyStorage} instance with the specified capacity.
     *
     * @param capacity the maximum amount of energy that can be stored
     * @param be the block entity associated with this storage
     */
    public ExtendedEnergyStorage(int capacity, BlockEntity be) {
        super(capacity);
        this.be = be;
    }

    /**
     * Creates a new {@link ExtendedEnergyStorage} instance with the specified capacity and maximum transfer amount.
     *
     * @param capacity the maximum amount of energy that can be stored
     * @param maxTransfer the maximum amount of energy that can be transferred in a single operation
     * @param be the block entity associated with this storage
     */
    public ExtendedEnergyStorage(int capacity, int maxTransfer, BlockEntity be) {
        super(capacity, maxTransfer);
        this.be = be;
    }

    /**
     * Creates a new {@link ExtendedEnergyStorage} instance with the specified capacity, maximum receive amount, and maximum extract amount.
     *
     * @param capacity The maximum amount of energy that can be stored
     * @param maxReceive The maximum amount of energy that can be received in a single operation
     * @param maxExtract The maximum amount of energy that can be extracted in a single operation
     * @param be The block entity associated with this storage
     */
    public ExtendedEnergyStorage(int capacity, int maxReceive, int maxExtract, BlockEntity be) {
        super(capacity, maxReceive, maxExtract);
        this.be = be;
    }

    /**
     * Creates a new {@link ExtendedEnergyStorage} instance with the specified capacity, maximum receive amount, maximum extract amount, and initial energy amount.
     *
     * @param capacity the maximum amount of energy that can be stored
     * @param maxReceive the maximum amount of energy that can be received in a single operation
     * @param maxExtract the maximum amount of energy that can be extracted in a single operation
     * @param energy the initial amount of energy stored
     * @param be the block entity associated with this storage
     */
    public ExtendedEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, BlockEntity be) {
        super(capacity, maxReceive, maxExtract, energy);
        this.be = be;
    }
    
    /**
     * Sets the current energy amount to the specified value, clamping it to the range [0, capacity].
     *
     * @param energy The new energy amount
     */
    public void setEnergy(int energy) {
        if(energy < 0)
            energy = 0;
        if(energy > this.capacity)
            energy = this.capacity;
        this.energy = energy;
        onEnergyChanged();
    }

    /**
     * Adds the specified amount of energy to the current energy amount, clamping the result to the range [0, capacity].
     *
     * @param energy the amount of energy to add
     */
    public void addEnergy(int energy) {
        setEnergy(this.energy + energy);
    }

    /**
     * Removes the specified amount of energy from the current energy amount, clamping the result to the range [0, capacity].
     *
     * @param energy the amount of energy to remove
     */
    public void removeEnergy(int energy) {
        setEnergy(this.energy - energy);
    }

    /**
     * Returns the maximum amount of energy that can be received in a single operation.
     *
     * @return the maximum receive amount
     */
    public int getMaxReceive() {
        return this.maxReceive;
    }

    /**
     * Returns the maximum amount of energy that can be extracted in a single operation.
     *
     * @return the maximum extract amount
     */
    public int getMaxExtract() {
        return this.maxExtract;
    }
    
    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        var toReturn = super.receiveEnergy(toReceive, simulate);
        if (!simulate) {
            onEnergyChanged();
        }
        return toReturn;
    }
    
    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        var toReturn = super.extractEnergy(toExtract, simulate);
        if (!simulate) {
            onEnergyChanged();
        }
        return toReturn;
    }

    public void onEnergyChanged() {
        this.be.setChanged();
    }
}
