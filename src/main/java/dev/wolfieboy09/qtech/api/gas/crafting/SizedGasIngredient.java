package dev.wolfieboy09.qtech.api.gas.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;

@NothingNullByDefault
public final class SizedGasIngredient {
    private final GasIngredient ingredient;
    private final int amount;

    @Nullable
    private GasStack[] cachedStacks;

    public static final Codec<SizedGasIngredient> FLAT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GasIngredient.MAP_CODEC_NONEMPTY.forGetter(SizedGasIngredient::ingredient),
            NeoForgeExtraCodecs.optionalFieldAlwaysWrite(ExtraCodecs.POSITIVE_INT, "amount", 1000).forGetter(SizedGasIngredient::amount)
    ).apply(instance, SizedGasIngredient::new));

    public static final Codec<SizedGasIngredient> NESTED_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GasIngredient.CODEC_NON_EMPTY.fieldOf("ingredient").forGetter(SizedGasIngredient::ingredient),
            NeoForgeExtraCodecs.optionalFieldAlwaysWrite(ExtraCodecs.POSITIVE_INT, "amount", 1000).forGetter(SizedGasIngredient::amount)
    ).apply(instance, SizedGasIngredient::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SizedGasIngredient> STREAM_CODEC = StreamCodec.composite(
            GasIngredient.STREAM_CODEC,
            SizedGasIngredient::ingredient,
            ByteBufCodecs.VAR_INT,
            SizedGasIngredient::amount,
            SizedGasIngredient::new
    );

    public static SizedGasIngredient of(Gas gas, int amount) {
        return new SizedGasIngredient(GasIngredient.of(gas), amount);
    }

    public static SizedGasIngredient of(GasStack stack) {
        return new SizedGasIngredient(GasIngredient.single(stack), stack.getAmount());
    }

    public static SizedGasIngredient of(TagKey<Gas> tag, int amount) {
        return new SizedGasIngredient(GasIngredient.tag(tag), amount);
    }

    public SizedGasIngredient(GasIngredient ingredient, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public GasIngredient ingredient() {
        return this.ingredient;
    }

    public int amount() {
        return this.amount;
    }

    public boolean test(GasStack stack) {
        return this.ingredient.test(stack) && stack.getAmount() >= this.amount;
    }

    public GasStack[] getGasses() {
        if (this.cachedStacks == null) {
            this.cachedStacks = Stream.of(this.ingredient.getStacks())
                    .map(s -> s.copyWithAmount(this.amount))
                    .toArray(GasStack[]::new);
        }
        return cachedStacks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SizedGasIngredient other)) return false;
        return this.amount == other.amount && this.ingredient.equals(other.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ingredient, this.amount);
    }

    @Override
    public String toString() {
        return this.amount + "x " + this.ingredient;
    }
}
