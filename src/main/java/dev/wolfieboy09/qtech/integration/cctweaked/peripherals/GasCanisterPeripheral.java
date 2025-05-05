package dev.wolfieboy09.qtech.integration.cctweaked.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dev.wolfieboy09.qtech.block.gas_canister.GasCanisterBlockEntity;
import dev.wolfieboy09.qtech.block.gas_canister.GasCanisterState;
import dev.wolfieboy09.qtech.integration.cctweaked.SimplePeripheralId;

public final class GasCanisterPeripheral extends SimplePeripheralId {
    @LuaFunction(mainThread = true)
    public String canisterState(GasCanisterBlockEntity gasCanister) {
        if (gasCanister.getState() == GasCanisterState.FILL) {
            return "fill";
        } else {
            return "drain";
        }
    }

    @Override
    public String getUid() {
        return "gas_canister";
    }
}
