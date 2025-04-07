package dev.wolfieboy09.qstorage.api.capabilities.gas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qstorage.api.registry.gas.GasStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public class GasTank implements IGasHandler, IGasTank {
    protected Predicate<GasStack> validator;
    protected GasStack gas;
    protected int capacity;

    private Runnable consumer;

    public static final Codec<GasTank> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GasStack.CODEC.fieldOf("gas").forGetter(GasTank::getGas),
            Codec.INT.fieldOf("capacity").forGetter(GasTank::getCapacity)
    ).apply(instance, GasTank::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GasTank> STREAM_CODEC = StreamCodec.composite(
            GasStack.STREAM_CODEC, GasTank::getGas,
            ByteBufCodecs.INT, GasTank::getCapacity,
            GasTank::new
    );

    private GasTank(GasStack stack, int capacity) {
        this.capacity = capacity;
        this.gas = stack;
    }

    public GasTank(int capacity, Runnable callback) {
        this(capacity);
        this.consumer = callback;
    }

    public GasTank(int capacity) {
        this(capacity, (e) -> true);
    }

    public GasTank(int capacity, Predicate<GasStack> validator) {
        this.gas = GasStack.EMPTY;
        this.capacity = capacity;
        this.validator = validator;
    }

    public GasTank setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public GasTank setGasInSlot(int index, GasStack gasStack) {
        this.gas = gasStack;
        return this;
    }

    public GasTank setValidator(Predicate<GasStack> validator) {
        if (validator != null) {
            this.validator = validator;
        }
        return this;
    }

    public boolean isGasValid(GasStack stack) {
        return this.validator.test(stack);
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public GasStack getGas() {
        return this.gas;
    }

    @Override
    public int getGasAmount() {
        return this.gas.getAmount();
    }

    public void readFromNBT(HolderLookup.Provider lookupProvider, @NotNull CompoundTag nbt) {
        this.gas = GasStack.parseOptional(lookupProvider, nbt.getCompound("Gas"));
        this.setCapacity(nbt.getInt("Capacity"));
    }

    public CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        CompoundTag data = new CompoundTag();
        if (!this.gas.isEmpty()) {
            data.put("Gas", this.gas.save(lookupProvider));
            data.putInt("Capacity", this.capacity);
        }
        return data;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public GasStack getGasInTank(int index) {
        return this.getGas();
    }

    @Override
    public int getTankCapacity(int index) {
        return this.getCapacity();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(this.gas, ((GasTank) o).gas);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.gas);
    }

    @Override
    public boolean isGasValid(int index, GasStack gasStack) {
        return this.validator.test(gasStack);
    }

    /**
     * Called when the gas tank contents are updated.
     * @implNote Only runs when a {@link Runnable} is present on the constructor
     */
    protected void onContentsChanged() {
        if (this.consumer != null) {
            this.consumer.run();
        }
    }

    @Override
    public int fill(GasStack resource, boolean simulate) {
        if (!resource.isEmpty() && this.isGasValid(resource)) {
            if (simulate) {
                if (this.gas.isEmpty()) {
                    return Math.min(this.capacity, resource.getAmount());
                } else {
                    return !GasStack.isSameGasSameComponents(this.gas, resource) ? 0 : Math.min(this.capacity - this.gas.getAmount(), resource.getAmount());
                }
            } else if (this.gas.isEmpty()) {
                this.gas = resource.copyWithAmount(Math.min(this.capacity, resource.getAmount()));
                this.onContentsChanged();
                return this.gas.getAmount();
            } else if (!GasStack.isSameGasSameComponents(this.gas, resource)) {
                return 0;
            } else {
                int filled = this.capacity - this.gas.getAmount();
                if (resource.getAmount() < filled) {
                    this.gas.grow(resource.getAmount());
                    filled = resource.getAmount();
                } else {
                    this.gas.setAmount(this.capacity);
                }
                if (filled > 0) {
                    this.onContentsChanged();
                }
                return filled;
            }
        } else {
            return 0;
        }
    }

    @Override
    public GasStack drain(GasStack resource, boolean simulate) {
        return !resource.isEmpty() && GasStack.isSameGasSameComponents(resource, this.gas) ? this.drain(resource.getAmount(), simulate) : GasStack.EMPTY;
    }

    @Override
    public GasStack drain(int maxDrain, boolean simulate) {
        int drained = Math.min(this.gas.getAmount(), maxDrain);
        GasStack stack = this.gas.copyWithAmount(drained);
        if (!simulate && drained > 0) {
            this.gas.shrink(drained);
            this.onContentsChanged();;
        }
        return stack;
    }

    public GasTank copy() {
        return new GasTank(this.gas, this.capacity);
    }
}
