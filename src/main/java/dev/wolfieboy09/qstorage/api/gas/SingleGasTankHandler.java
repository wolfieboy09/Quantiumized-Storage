package dev.wolfieboy09.qstorage.api.gas;

import dev.wolfieboy09.qstorage.api.capabilities.gas.GasTank;
import dev.wolfieboy09.qstorage.api.capabilities.gas.IGasHandler;
import dev.wolfieboy09.qstorage.api.capabilities.gas.IGasHandlerModifiable;
import dev.wolfieboy09.qstorage.api.registry.gas.GasStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SingleGasTankHandler implements IGasHandler, IGasHandlerModifiable, INBTSerializable<CompoundTag> {
    private GasTank tank;

    @Override
    public void setStackInTank(int index, GasStack gasStack) {
        this.tank = this.tank.setGasInSlot(index, gasStack);
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public GasStack getGasInTank(int index) {
        return this.tank.getGas();
    }

    @Override
    public int getTankCapacity(int index) {
        return this.tank.getCapacity();
    }

    @Override
    public boolean isGasValid(int index, GasStack gasStack) {
        return this.tank.isGasValid(gasStack);
    }

    @Override
    public int fill(GasStack gasStack, boolean simulate) {
        return this.tank.fill(gasStack, simulate);
    }

    @Override
    public GasStack drain(GasStack gasStack, boolean simulate) {
        return this.tank.drain(gasStack, simulate);
    }

    @Override
    public GasStack drain(int index, boolean simulate) {
        return this.tank.drain(index, simulate);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        if (!this.tank.getGas().isEmpty()) {
            tag.put("Gas", this.tank.writeToNBT(provider, tag));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        this.tank = this.tank.readFromNBT(provider, compoundTag.getCompound("Gas"));
    }
}
