package dev.wolfieboy09.qtech.integration.cctweaked;

import dan200.computercraft.api.peripheral.GenericPeripheral;
import dev.wolfieboy09.qtech.QuantiumizedTech;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class SimplePeripheralId implements GenericPeripheral {
    public abstract String getUid();

    @Override
    public String id() {
        return QuantiumizedTech.MOD_ID + ":" + getUid();
    }
}
