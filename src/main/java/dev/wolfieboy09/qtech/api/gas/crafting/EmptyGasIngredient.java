package dev.wolfieboy09.qtech.api.gas.crafting;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.registries.QTGasIngredientTypes;

import java.util.stream.Stream;

@NothingNullByDefault
public class EmptyGasIngredient extends GasIngredient {
    public static final EmptyGasIngredient INSTANCE = new EmptyGasIngredient();

    public static final MapCodec<EmptyGasIngredient> CODEC = MapCodec.unit(INSTANCE);

    private EmptyGasIngredient() {}

    @Override
    public boolean test(GasStack gasStack) {
        return gasStack.isEmpty();
    }

    @Override
    protected Stream<GasStack> generateStacks() {
        return Stream.empty();
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public GasIngredientType<?> getType() {
        return QTGasIngredientTypes.EMPTY_GAS_INGREDIENT_TYPE.get();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
