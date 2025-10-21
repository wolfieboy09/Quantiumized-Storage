package dev.wolfieboy09.qtech.api.recipes.result;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

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

    public @Nullable T getIfRolled(RandomSource random) {
        return roll(random) ? copyResult() : null;
    }

    public @Nullable T getIfRolled() {
        return roll(RandomSource.create()) ? copyResult() : null;
    }

    protected abstract T copyResult();

    public abstract Codec<? extends ChanceResult<T>> codec();
    public abstract StreamCodec<RegistryFriendlyByteBuf, ? extends ChanceResult<T>> streamCodec();
}