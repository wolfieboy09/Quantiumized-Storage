package dev.wolfieboy09.qtech.api.recipes.result;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Chance based recipe results
 * @param <T> The type to use (example: A recipe with a chance for an {@link ItemStack})
 */
public abstract class ChanceResult<T> {
    protected final T result;
    protected final float chance;

    protected ChanceResult(T result, float chance) {
        if (chance < 0.0f || chance > 1.0f)
            throw new IllegalArgumentException("Chance must be between 0 and 1");
        this.result = result;
        this.chance = chance;
    }

    protected ChanceResult(T result) {
        this(result, 1);
    }

    /**
     * Gets the result as-is, no rolling at all
     */
    public T getResult() {
        return this.result;
    }

    public float getChance() {
        return this.chance;
    }

    public boolean roll() {
        return RandomSource.create().nextFloat() < this.chance;
    }

    public boolean roll(@NotNull RandomSource random) {
        return random.nextFloat() < this.chance;
    }

    public Optional<T> getIfRolled(@NotNull Level level) {
        return roll(level.getRandom()) ? Optional.of(copyResult()) : Optional.empty();
    }

    public Optional<T> getIfRolled(RandomSource random) {
        return roll(random) ? Optional.of(copyResult()) : Optional.empty();
    }

    public Optional<T> getIfRolled() {
        return roll(RandomSource.create()) ? Optional.of(copyResult()) : Optional.empty();
    }

    protected abstract T copyResult();
    public abstract boolean isEmpty();

    public abstract Codec<? extends ChanceResult<T>> codec();
    public abstract StreamCodec<RegistryFriendlyByteBuf, ? extends ChanceResult<T>> streamCodec();
}