package dev.wolfieboy09.qstorage.api.capabilities.gas;

import dev.wolfieboy09.qstorage.api.registry.gas.GasStack;

public interface IGasHandlerModifiable extends IGasHandler {
    void setStackInTank(int index, GasStack fluidStack);
}
