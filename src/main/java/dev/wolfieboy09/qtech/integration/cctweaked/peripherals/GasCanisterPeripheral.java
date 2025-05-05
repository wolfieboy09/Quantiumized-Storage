package dev.wolfieboy09.qtech.integration.cctweaked.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dev.wolfieboy09.qtech.api.capabilities.gas.GasTank;
import dev.wolfieboy09.qtech.block.gas_canister.GasCanisterBlockEntity;
import dev.wolfieboy09.qtech.block.gas_canister.GasCanisterState;
import dev.wolfieboy09.qtech.integration.cctweaked.SimplePeripheralId;
import dev.wolfieboy09.qtech.integration.cctweaked.wrappers.gas.GasMethods;
import net.neoforged.neoforge.items.ItemStackHandler;

public final class GasCanisterPeripheral extends SimplePeripheralId {
    @LuaFunction(mainThread = true)
    public ItemStackHandler getInventory(GasCanisterBlockEntity gasCanister) {
        return gasCanister.getInventory();
    }

    @LuaFunction(mainThread = true)
    public GasCanisterState getCanisterState(GasCanisterBlockEntity gasCanister) {
        return gasCanister.getState();
    }

    @LuaFunction(mainThread = true)
    public GasTank getTank(GasCanisterBlockEntity gasCanister) {
        return gasCanister.getGasTank();
    }


    @Override
    public String getUid() {
        return "gas_canister";
    }
}
