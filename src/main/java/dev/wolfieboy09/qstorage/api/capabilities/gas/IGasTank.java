package dev.wolfieboy09.qstorage.api.capabilities.gas;

import dev.wolfieboy09.qstorage.api.registry.gas.GasStack;

public interface IGasTank {
    GasStack getGas();
    int getGasAmount();
    int getCapacity();
    boolean isGasValid(GasStack gasStack);
    int fill(GasStack gasStack, boolean simulate);
    GasStack drain(int index, boolean simulate);
    GasStack drain(GasStack gasStack, boolean simulate);
}
