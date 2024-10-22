package dev.wolfieboy09.qstorage.api.energy;

import net.neoforged.neoforge.energy.EnergyStorage;

/**
 * An extension of the {@link EnergyStorage} class, providing additional methods for managing energy storage.
 */
public class ExtendedEnergyStorage extends EnergyStorage {

    /**
     * Creates a new ExtendedEnergyStorage instance with the specified capacity.
     *
     * @param capacity the maximum amount of energy that can be stored
     */
    public ExtendedEnergyStorage(int capacity) {
        super(capacity);
    }

    /**
     * Creates a new ExtendedEnergyStorage instance with the specified capacity and maximum transfer amount.
     *
     * @param capacity the maximum amount of energy that can be stored
     * @param maxTransfer the maximum amount of energy that can be transferred in a single operation
     */
    public ExtendedEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    /**
     * Creates a new ExtendedEnergyStorage instance with the specified capacity, maximum receive amount, and maximum extract amount.
     *
     * @param capacity the maximum amount of energy that can be stored
     * @param maxReceive the maximum amount of energy that can be received in a single operation
     * @param maxExtract the maximum amount of energy that can be extracted in a single operation
     */
    public ExtendedEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    /**
     * Creates a new ExtendedEnergyStorage instance with the specified capacity, maximum receive amount, maximum extract amount, and initial energy amount.
     *
     * @param capacity the maximum amount of energy that can be stored
     * @param maxReceive the maximum amount of energy that can be received in a single operation
     * @param maxExtract the maximum amount of energy that can be extracted in a single operation
     * @param energy the initial amount of energy stored
     */
    public ExtendedEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    /**
     * Sets the current energy amount to the specified value, clamping it to the range [0, capacity].
     *
     * @param energy the new energy amount
     */
    public void setEnergy(int energy) {
        if(energy < 0)
            energy = 0;
        if(energy > this.capacity)
            energy = this.capacity;
        this.energy = energy;
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
}