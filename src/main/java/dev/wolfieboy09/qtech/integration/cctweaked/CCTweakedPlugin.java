package dev.wolfieboy09.qtech.integration.cctweaked;

import com.mojang.logging.LogUtils;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.core.computer.Computer;
import dev.wolfieboy09.qtech.integration.cctweaked.peripherals.DiskAssemblerPeripheral;
import dev.wolfieboy09.qtech.integration.cctweaked.peripherals.GasCanisterPeripheral;
import dev.wolfieboy09.qtech.integration.cctweaked.wrappers.gas.GasMethods;
import org.slf4j.Logger;

public final class CCTweakedPlugin {
    private static boolean REGISTERED = false;

    public static void register() {
        if (REGISTERED) {
            throw new IllegalStateException("CC: Tweaked peripherals can't be re-registered!");
        }
        REGISTERED = true;
        ComputerCraftAPI.registerGenericSource(new GasMethods());

        ComputerCraftAPI.registerGenericSource(new GasCanisterPeripheral());
        //ComputerCraftAPI.registerGenericSource(new DiskAssemblerPeripheral());
    }
}
