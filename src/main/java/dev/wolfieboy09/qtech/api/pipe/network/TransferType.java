package dev.wolfieboy09.qtech.api.pipe.network;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@NothingNullByDefault
public class TransferType<S, N extends Number> {
    private static final List<TransferType<?, ?>> TRANSFERS = new ArrayList<>();

    private final String id;
    private final BlockCapability<S, @Nullable Direction> blockCapability;
    private final CapabilityHandler<S, N> insertFunction;
    private final CapabilityHandler<S, N> extractFunction;
    private final SimulateInsert<S, N> simulateInsert;
    private final Function<S, N> amountGetter;
    private final N maxAmount;
    private final Predicate<N> isEmpty;
    private final Predicate<S> canInsert;
    private final Predicate<S> canExtract;


    public TransferType(String id,
                        BlockCapability<S, @Nullable Direction> blockCapability,
                        CapabilityHandler<S, N> insertFunction,
                        CapabilityHandler<S, N> extractFunction,
                        SimulateInsert<S, N> simulateInsert,
                        Function<S, N> amountGetter,
                        N maxAmount,
                        Predicate<N> isEmpty,
                        Predicate<S> canInsert,
                        Predicate<S> canExtract) {
        this.id = Objects.requireNonNull(id);
        this.blockCapability = Objects.requireNonNull(blockCapability);
        this.insertFunction = Objects.requireNonNull(insertFunction);
        this.extractFunction = Objects.requireNonNull(extractFunction);
        this.simulateInsert = Objects.requireNonNull(simulateInsert);
        this.amountGetter = Objects.requireNonNull(amountGetter);
        this.maxAmount = Objects.requireNonNull(maxAmount);
        this.isEmpty = Objects.requireNonNull(isEmpty);
        this.canInsert = Objects.requireNonNull(canInsert);
        this.canExtract = Objects.requireNonNull(canExtract);

        TRANSFERS.add(this);
    }

    public String getId() {
        return this.id;
    }

    public BlockCapability<S, @Nullable Direction> getCapability() {
        return this.blockCapability;
    }

    @Contract(pure = true)
    public static @Unmodifiable List<TransferType<?, ?>> getAll() {
        return List.copyOf(TRANSFERS);
    }

    public void transfer(Level level, BlockPos fromPos, BlockPos toPos, Direction direction) {
        S fromStorage = getCapabilityInstance(level, fromPos, direction);
        S toStorage = getCapabilityInstance(level, toPos, direction);
        if (fromStorage == null || toStorage == null) return;
        if (!this.canExtract.test(fromStorage)) return;
        N amountInFrom = this.amountGetter.apply(fromStorage);
        if (this.isEmpty.test(amountInFrom)) return;
        if (!this.canInsert.test(toStorage)) return;
        N insertable = this.simulateInsert.simulate(toStorage, this.maxAmount);
        if (this.isEmpty.test(insertable)) return;
        N extracted = this.extractFunction.handle(fromStorage, insertable);
        if (this.isEmpty.test(extracted)) return;
        this.insertFunction.handle(toStorage, extracted);
    }

    @Nullable
    public S getCapabilityInstance(Level level, BlockPos pos, Direction direction) {
        return level.getCapability(this.blockCapability, pos, direction);
    }

    @FunctionalInterface
    public interface CapabilityHandler<S, N extends Number> {
        N handle(S storage, N transferLimit);
    }

    @FunctionalInterface
    public interface SimulateInsert<S, N extends Number> {
        N simulate(S storage, N maxAmount);
    }
}
