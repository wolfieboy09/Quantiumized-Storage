package dev.wolfieboy09.qtech.integration.cctweaked;

import dan200.computercraft.api.lua.LuaFunction;
import dev.wolfieboy09.qtech.block.AbstractEnergyBlockEntity;
import org.jetbrains.annotations.ApiStatus;

public interface EnergizedPeripheral<T extends AbstractEnergyBlockEntity> {
    @LuaFunction(mainThread = true)
    public default int getEnergyStored(T block) {
        return block.getEnergyStored();
    };

    @LuaFunction(mainThread = true)
    public default int getEnergyCapacity(T block) {
        return block.getMaxEnergyStored();
    };

    @LuaFunction(mainThread = true)
    public default boolean canReceive(T block) {
        return block.canReceive();
    };

    @LuaFunction(mainThread = true)
    public default boolean canExtract(T block) {
        return block.canExtract();
    };
}
