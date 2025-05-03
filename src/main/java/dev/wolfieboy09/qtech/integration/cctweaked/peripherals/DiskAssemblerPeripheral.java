package dev.wolfieboy09.qtech.integration.cctweaked.peripherals;

import dev.wolfieboy09.qtech.block.disk_assembler.DiskAssemblerBlockEntity;
import dev.wolfieboy09.qtech.integration.cctweaked.EnergizedPeripheral;
import dev.wolfieboy09.qtech.integration.cctweaked.SimplePeripheralId;

public class DiskAssemblerPeripheral extends SimplePeripheralId implements EnergizedPeripheral<DiskAssemblerBlockEntity> {
    @Override
    public String getUid() {
        return "disk_assembler";
    }
}
