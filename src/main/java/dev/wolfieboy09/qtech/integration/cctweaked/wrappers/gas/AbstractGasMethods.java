package dev.wolfieboy09.qtech.integration.cctweaked.wrappers.gas;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.PeripheralType;
import dev.wolfieboy09.qtech.QuantiumizedTech;

import java.util.Map;
import java.util.Optional;

public abstract class AbstractGasMethods<T> implements GenericPeripheral {
    @Override
    public PeripheralType getType() {
        return PeripheralType.ofType("gas_storage");
    }

    @Override
    public String id() {
        return QuantiumizedTech.MOD_ID + ":gas";
    }

    @LuaFunction(mainThread = true)
    public abstract Map<Integer, Map<String, ?>> tanks(T gasses);

    @LuaFunction(mainThread = true)
    public abstract int pushGas(T from, IComputerAccess computer, String toName, Optional<Integer> limit, Optional<String> gasName) throws LuaException;

    @LuaFunction(mainThread = true)
    public abstract int pullGas(T to, IComputerAccess computer, String fromName, Optional<Integer> limit, Optional<String> gasName) throws LuaException;
}
