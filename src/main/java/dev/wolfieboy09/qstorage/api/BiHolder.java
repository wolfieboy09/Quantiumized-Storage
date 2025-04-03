package dev.wolfieboy09.qstorage.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.mojang.datafixers.util.Either;
/**
 * Holds a left and right value. (Mojang's {@link Either} but can hold both values)
 * @param left The left value to hold
 * @param right The right value to hold
 * @param <L> The left type
 * @param <R> The right type
 */
public record BiHolder<L, R>(@Nullable L left,@Nullable R right) {
    public boolean isLeftPresent() {
        return this.left != null;
    }

    public boolean isRightPresent() {
        return this.right != null;
    }

    @Contract("_ -> new")
    public static <L, R> @NotNull BiHolder<L, R> left(@Nullable L value) {
        return new BiHolder<>(value, null);
    }

    @Contract("_ -> new")
    public static <L, R> @NotNull BiHolder<L, R> right(@Nullable R value) {
        return new BiHolder<>(null, value);
    }
}