package dev.wolfieboy09.qtech.api.gas.crafting;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.registries.QTGasIngredientTypes;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@NothingNullByDefault
public class CompoundGasIngredient extends GasIngredient {
    public static final MapCodec<CompoundGasIngredient> CODEC = NeoForgeExtraCodecs.aliasedFieldOf(
            GasIngredient.LIST_CODEC_NON_EMPTY, "children", "ingredients"
    ).xmap(CompoundGasIngredient::new, CompoundGasIngredient::children);

    private final List<GasIngredient> children;

    public CompoundGasIngredient(List<? extends GasIngredient> children) {
        if (children.isEmpty()) {
            throw new IllegalArgumentException("Compound gas ingredient must have at least one child");
        }
        this.children = List.copyOf(children);
    }

    public static GasIngredient of(GasIngredient... children) {
        if (children.length == 0) {
            return GasIngredient.empty();
        }
        if (children.length == 1) {
            return children[0];
        }
        return new CompoundGasIngredient(List.of(children));
    }

    public static GasIngredient of(List<GasIngredient> children) {
        if (children.isEmpty()) {
            return GasIngredient.empty();
        }
        if (children.size() == 1) {
            return children.getFirst();
        }
        return new CompoundGasIngredient(children);
    }

    public static GasIngredient of(Stream<GasIngredient> stream) {
        return of(stream.toList());
    }

    @Override
    protected Stream<GasStack> generateStacks() {
        return this.children.stream().flatMap(GasIngredient::generateStacks);
    }

    @Override
    public boolean test(GasStack stack) {
        for (GasIngredient child : children) {
            if (child.test(stack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isSimple() {
        for (GasIngredient child : children) {
            if (!child.isSimple()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public GasIngredientType<?> getType() {
        return QTGasIngredientTypes.COMPOUND_GAS_INGREDIENT_TYPE.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.children);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof CompoundGasIngredient other && other.children.equals(this.children);
    }

    public List<GasIngredient> children() {
        return this.children;
    }
}
