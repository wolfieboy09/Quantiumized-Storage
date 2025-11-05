package dev.wolfieboy09.qtech.api.gas.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.registries.QTGasIngredientTypes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@NothingNullByDefault
public final class IntersectionGasIngredient extends GasIngredient {
    public static final MapCodec<IntersectionGasIngredient> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            GasIngredient.LIST_CODEC_NON_EMPTY.fieldOf("children").forGetter(IntersectionGasIngredient::children)
    ).apply(builder, IntersectionGasIngredient::new));

    private final List<GasIngredient> children;

    public IntersectionGasIngredient(List<GasIngredient> children) {
        if (children.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an IntersectionGasIngredient with no children, use GasIngredient.of() to create an empty ingredient");
        }
        this.children = children;
    }

    public static GasIngredient of(GasIngredient... ingredients) {
        if (ingredients.length == 0) {
            throw new IllegalArgumentException("Cannot create an IntersectionGasIngredient with no children, use GasIngredient.of() to create an empty ingredient");
        }
        if (ingredients.length == 1) {
            return ingredients[0];
        }
        return new IntersectionGasIngredient(Arrays.asList(ingredients));
    }

    @Override
    public boolean test(GasStack stack) {
        for (GasIngredient child : this.children) {
            if (!child.test(stack)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected Stream<GasStack> generateStacks() {
        return this.children.stream()
                .flatMap(GasIngredient::generateStacks)
                .filter(this);
    }

    @Override
    public boolean isSimple() {
        for (GasIngredient child : this.children) {
            if (!child.isSimple()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public GasIngredientType<?> getType() {
        return QTGasIngredientTypes.INTERSECTION_GAS_INGREDIENT_TYPE.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.children);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof IntersectionGasIngredient other && this.children.equals(other.children);
    }

    public List<GasIngredient> children() {
        return this.children;
    }
}
