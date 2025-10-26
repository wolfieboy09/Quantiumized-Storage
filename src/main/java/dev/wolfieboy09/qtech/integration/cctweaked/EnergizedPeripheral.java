package dev.wolfieboy09.qtech.integration.cctweaked;

import dan200.computercraft.api.lua.LuaFunction;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.block.AbstractEnergyBlockEntity;

@NothingNullByDefault
public interface EnergizedPeripheral<T extends AbstractEnergyBlockEntity> {
    @LuaFunction(mainThread = true)
    default int getEnergyStored(T block) {
        return block.getEnergyStored();
    };

    @LuaFunction(mainThread = true)
    default int getEnergyCapacity(T block) {
        return block.getMaxEnergyStored();
    };

    @LuaFunction(mainThread = true)
    default boolean canReceive(T block) {
        return block.canReceive();
    };

    @LuaFunction(mainThread = true)
    default boolean canExtract(T block) {
        return block.canExtract();
    };
}
