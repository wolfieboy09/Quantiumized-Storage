package dev.wolfieboy09.qtech.api.gas.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.registries.QTGasIngredientTypes;

import java.util.Objects;
import java.util.stream.Stream;

public final class DifferenceGasIngredient extends GasIngredient {
    public static final MapCodec<DifferenceGasIngredient> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            GasIngredient.CODEC_NON_EMPTY.fieldOf("base").forGetter(DifferenceGasIngredient::base),
            GasIngredient.CODEC_NON_EMPTY.fieldOf("subtracted").forGetter(DifferenceGasIngredient::subtracted)
    ).apply(builder, DifferenceGasIngredient::new));

    private final GasIngredient base;
    private final GasIngredient subtracted;

    public DifferenceGasIngredient(GasIngredient base, GasIngredient subtracted) {
        this.base = base;
        this.subtracted = subtracted;
    }

    @Override
    protected Stream<GasStack> generateStacks() {
        return this.base.generateStacks().filter(subtracted.negate());
    }

    @Override
    public boolean test(GasStack stack) {
        return this.base.test(stack) && !this.subtracted.test(stack);
    }

    @Override
    public boolean isSimple() {
        return this.base.isSimple() && this.subtracted.isSimple();
    }

    @Override
    public GasIngredientType<?> getType() {
        return QTGasIngredientTypes.DIFFERENCE_GAS_INGREDIENT_TYPE.get();
    }

    public static GasIngredient of(GasIngredient base, GasIngredient subtracted) {
        return new DifferenceGasIngredient(base, subtracted);
    }

    public GasIngredient base() {
        return this.base;
    }

    public GasIngredient subtracted() {
        return this.subtracted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.base, this.subtracted);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof DifferenceGasIngredient other &&
                other.base.equals(this.base) && other.subtracted.equals(this.subtracted);
    }
}
