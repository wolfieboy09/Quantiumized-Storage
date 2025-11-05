package dev.wolfieboy09.qtech.api.gas.crafting;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.registries.QTGasIngredientTypes;
import dev.wolfieboy09.qtech.registries.QTGasses;
import net.minecraft.core.Holder;

import java.util.stream.Stream;

@NothingNullByDefault
public class SingleGasIngredient extends GasIngredient {
    public static final MapCodec<SingleGasIngredient> CODEC = GasStack.GAS_NON_EMPTY_CODEC
            .xmap(SingleGasIngredient::new, SingleGasIngredient::gas).fieldOf("gas");

    private final Holder<Gas> gas;

    public SingleGasIngredient(Holder<Gas> gas) {
        if (gas.is(QTGasses.EMPTY.get().builtInRegistryHolder())) {
            throw new IllegalStateException("SingleGasIngredient must not be constructed with qtech:empty, use GasIngredient.empty() instead!");
        }

        this.gas = gas;
    }

    @Override
    public boolean test(GasStack gasStack) {
        return gasStack.is(this.gas);
    }

    @Override
    protected Stream<GasStack> generateStacks() {
        return Stream.of(new GasStack(this.gas, 1000));
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public GasIngredientType<?> getType() {
        return QTGasIngredientTypes.SINGLE_GAS_INGREDIENT_TYPE.get();
    }

    @Override
    public int hashCode() {
        return this.gas().value().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof SingleGasIngredient other && other.gas.is(this.gas);
    }

    public Holder<Gas> gas() {
        return this.gas;
    }
}
