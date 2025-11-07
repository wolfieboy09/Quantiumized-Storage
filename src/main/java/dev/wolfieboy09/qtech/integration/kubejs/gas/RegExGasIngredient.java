package dev.wolfieboy09.qtech.integration.kubejs.gas;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.latvian.mods.kubejs.util.RegExpKJS;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.gas.crafting.GasIngredient;
import dev.wolfieboy09.qtech.api.gas.crafting.GasIngredientType;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.regex.Pattern;
import java.util.stream.Stream;

@NothingNullByDefault
public class RegExGasIngredient extends GasIngredient {
    public static final MapCodec<RegExGasIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegExpKJS.CODEC.fieldOf("regex").forGetter(i -> i.pattern)
    ).apply(instance, RegExGasIngredient::new));

    public static final StreamCodec<ByteBuf, RegExGasIngredient> STREAM_CODEC = RegExpKJS.STREAM_CODEC.map(RegExGasIngredient::new, i -> i.pattern);

    public final Pattern pattern;
    public final String patternString;

    public RegExGasIngredient(Pattern pattern) {
        this.pattern = pattern;
        this.patternString = RegExpKJS.toRegExpString(pattern);
    }

    @Override
    public GasIngredientType<?> getType() {
        return KubeJSGasIngredients.REGEX.get();
    }

    @Override
    public boolean test(GasStack stack) {
        return this.pattern.matcher(stack.getGas().getResourceLocation().getNamespace()).find();
    }

    @Override
    protected Stream<GasStack> generateStacks() {
        return QTRegistries.GAS.stream().filter(gas -> this.pattern.matcher(gas.getResourceLocation().getNamespace()).find()).map(GasStack::new);
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public int hashCode() {
        return this.patternString.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof RegExGasIngredient r && this.patternString.equals(r.patternString);
    }
}
