package dev.wolfieboy09.qtech.api.capabilities.gas;

import dev.wolfieboy09.qtech.api.registry.gas.GasStack;

public interface IGasHandler {
    int getTanks();
    GasStack getGasInTank(int index);
    int getTankCapacity(int index);
    boolean isGasValid(int index, GasStack gasStack);
    int fill(GasStack gasStack, boolean simulate);

    GasStack drain(GasStack gasStack, boolean simulate);
    GasStack drain(int maxDrain, boolean simulate);
}
