package dev.wolfieboy09.qtech.api.gas.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.registries.QTGasIngredientTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.HolderSetCodec;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

@NothingNullByDefault
public class DataComponentGasIngredient extends GasIngredient {
    public static final MapCodec<DataComponentGasIngredient> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            HolderSetCodec.create(QTRegistries.GAS_KEY, QTRegistries.GAS.holderByNameCodec(), false).fieldOf("gasses").forGetter(DataComponentGasIngredient::gasses),
            DataComponentPredicate.CODEC.fieldOf("components").forGetter(DataComponentGasIngredient::components),
            Codec.BOOL.optionalFieldOf("strict", false).forGetter(DataComponentGasIngredient::isStrict)
    ).apply(builder, DataComponentGasIngredient::new));

    private final HolderSet<Gas> gasses;
    private final DataComponentPredicate components;
    private final boolean strict;
    private final GasStack[] stacks;

    public DataComponentGasIngredient(HolderSet<Gas> gasses, DataComponentPredicate components, boolean strict) {
        this.gasses = gasses;
        this.components = components;
        this.strict = strict;
        this.stacks = gasses.stream()
                .map(i -> new GasStack(i, 1000, components.asPatch()))
                .toArray(GasStack[]::new);
    }

    @Override
    public boolean test(GasStack stack) {
        if (this.strict) {
            for (GasStack stack2 : this.stacks) {
                if (GasStack.isSameGasSameComponents(stack, stack2)) return true;
            }
            return false;
        } else {
            return this.gasses.contains(stack.getGasHolder()) && this.components.test(stack);
        }
    }

    @Override
    protected Stream<GasStack> generateStacks() {
        return Stream.of(this.stacks);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public GasIngredientType<?> getType() {
        return QTGasIngredientTypes.DATA_COMPONENT_GAS_INGREDIENT_TYPE.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.gasses, this.components, this.strict);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DataComponentGasIngredient other)) return false;
        return other.gasses.equals(this.gasses)
                && other.components.equals(this.components)
                && other.strict == this.strict;
    }

    public HolderSet<Gas> gasses() {
        return this.gasses;
    }

    public DataComponentPredicate components() {
        return this.components;
    }

    public boolean isStrict() {
        return this.strict;
    }

    public static GasIngredient of(boolean strict, GasStack stack) {
        return of(strict, stack.getComponents(), stack.getGas());
    }

    public static <T> GasIngredient of(boolean strict, DataComponentType<? super T> type, T value, Gas... gasses) {
        return of(strict, DataComponentPredicate.builder().expect(type, value).build(), gasses);
    }

    public static <T> GasIngredient of(boolean strict, Supplier<? extends DataComponentType<? super T>> type, T value, Gas... gasses) {
        return of(strict, type.get(), value, gasses);
    }

    public static GasIngredient of(boolean strict, DataComponentMap map, Gas... gasses) {
        return of(strict, DataComponentPredicate.allOf(map), gasses);
    }

    public static GasIngredient of(boolean strict, DataComponentMap map, HolderSet<Gas> gasses) {
        return of(strict, DataComponentPredicate.allOf(map), gasses);
    }

    @SafeVarargs
    public static GasIngredient of(boolean strict, DataComponentPredicate predicate, Holder<Gas>... gasses) {
        return of(strict, predicate, HolderSet.direct(gasses));
    }

    public static GasIngredient of(boolean strict, DataComponentPredicate predicate, Gas... gasses) {
        return of(strict, predicate, HolderSet.direct(Arrays.stream(gasses).map(Gas::builtInRegistryHolder).toList()));
    }

    public static GasIngredient of(boolean strict, DataComponentPredicate predicate, HolderSet<Gas> gasses) {
        return new DataComponentGasIngredient(gasses, predicate, strict);
    }
}
