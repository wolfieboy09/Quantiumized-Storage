package dev.wolfieboy09.qtech.api.fluids;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

@NothingNullByDefault
public class FluidStackHandler implements IFluidHandler, IFluidHandlerModifiable, INBTSerializable<CompoundTag> {
    protected NonNullList<FluidTank> stacks;

    public FluidStackHandler() {
        this(1000);
    }

    public FluidStackHandler(int size) {
        this.stacks = NonNullList.withSize(size, new FluidTank(size));
    }

    public FluidStackHandler(NonNullList<FluidTank> stacks) {
        this.stacks = stacks;
    }

    public void setSize(int size) {
        this.stacks = NonNullList.withSize(size, new FluidTank(size));
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.stacks.size()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.stacks.size() + ")");
        }
    }

    @Override
    public void setStackInTank(int index, FluidStack fluidStack) {
        this.validateSlotIndex(index);
        FluidTank tank = new FluidTank(this.stacks.get(index).getCapacity());
        tank.setFluid(fluidStack);
        this.stacks.set(index, tank);
        this.onContentsChanged(index);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < this.stacks.size(); ++i) {
            if (!this.stacks.get(i).isEmpty()) {
                CompoundTag fluidTag = new CompoundTag();
                fluidTag.putInt("Tank", i);
                nbtTagList.add(this.stacks.get(i).getFluidInTank(0).save(provider));
            }
        }

        CompoundTag nbt = new CompoundTag();
        nbt.put("Fluids", nbtTagList);
        nbt.putInt("Size", this.stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : this.stacks.size());
        ListTag tagList = nbt.getList("Fluids", Tag.TAG_COMPOUND);

        for (int i = 0; i < tagList.size(); ++i) {
            CompoundTag fluidTags = tagList.getCompound(i);
            int slot = fluidTags.getInt("Tank");
            if (slot >= 0 && slot < this.stacks.size()) {
                FluidStack.parse(provider, fluidTags).ifPresent((stack) -> {
                    FluidTank tank = new FluidTank(fluidTags.getInt("Capacity"));
                    tank.setFluid(stack);
                    this.stacks.set(slot, tank);
                });
            }
        }
    }

    @Override
    public int getTanks() {
        return this.stacks.size();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return this.stacks.get(i).getFluidInTank(i);
    }

    @Override
    public int getTankCapacity(int i) {
        return this.stacks.get(i).getCapacity();
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return this.stacks.get(i).isFluidValid(fluidStack);
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        for (FluidTank tank : this.stacks) {
            if (tank.getFluid() == fluidStack) {
                return tank.fill(fluidStack, fluidAction);
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        for (FluidTank tank : this.stacks) {
            if (tank.getFluid() == fluidStack) {
                return tank.drain(fluidStack, fluidAction);
            }
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        //TODO complete this part for draining
        return FluidStack.EMPTY;
    }

    protected void onContentsChanged(int index) {}
}
