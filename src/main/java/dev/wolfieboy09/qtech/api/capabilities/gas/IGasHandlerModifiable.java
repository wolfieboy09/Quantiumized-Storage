package dev.wolfieboy09.qtech.api.capabilities.gas;

import dev.wolfieboy09.qtech.api.registry.gas.GasStack;

public interface IGasHandlerModifiable extends IGasHandler {
    void setStackInTank(int index, GasStack fluidStack);
}
