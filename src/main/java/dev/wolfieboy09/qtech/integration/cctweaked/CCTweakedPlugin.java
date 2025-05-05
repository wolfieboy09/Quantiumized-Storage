package dev.wolfieboy09.qtech.integration.cctweaked;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.ForgeComputerCraftAPI;
import dev.wolfieboy09.qtech.api.capabilities.QTCapabilities;
import dev.wolfieboy09.qtech.integration.cctweaked.wrappers.gas.GasMethods;

public final class CCTweakedPlugin {
    private static boolean REGISTERED = false;

    public static void register() {
        if (REGISTERED) { throw new IllegalStateException("CC: Tweaked peripherals can't be re-registered!"); }
        REGISTERED = true;
        ComputerCraftAPI.registerGenericSource(new GasMethods());

        ForgeComputerCraftAPI.registerGenericCapability(QTCapabilities.GasStorage.BLOCK);
    }
}
