package dev.wolfieboy09.qtech.api.gas.crafting;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.registries.QTGasIngredientTypes;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;

import java.util.stream.Stream;

@NothingNullByDefault
public class TagGasIngredient extends GasIngredient {
    public static final MapCodec<TagGasIngredient> CODEC = TagKey.codec(QTRegistries.GAS_KEY)
            .xmap(TagGasIngredient::new, TagGasIngredient::tag).fieldOf("tag");

    private final TagKey<Gas> tag;

    public TagGasIngredient(TagKey<Gas> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(GasStack gasStack) {
        return gasStack.is(this.tag);
    }

    @Override
    protected Stream<GasStack> generateStacks() {
        return QTRegistries.GAS.getTag(this.tag)
                .stream()
                .flatMap(HolderSet::stream)
                .map(gas -> new GasStack(gas, 1000));
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public GasIngredientType<?> getType() {
        return QTGasIngredientTypes.TAG_GAS_INGREDIENT_TYPE.get();
    }

    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof TagGasIngredient gasTag && gasTag.tag.equals(this.tag);
    }

    public TagKey<Gas> tag() {
        return this.tag;
    }
}
