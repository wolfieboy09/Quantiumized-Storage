package dev.wolfieboy09.qtech.integration.kubejs.gas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.gas.crafting.GasIngredient;
import dev.wolfieboy09.qtech.api.gas.crafting.GasIngredientType;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.stream.Stream;

@NothingNullByDefault
public class NamespaceGasIngredient extends GasIngredient {
    public static final MapCodec<NamespaceGasIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("namespace").forGetter(i -> i.namespace)
    ).apply(instance, NamespaceGasIngredient::new));

    public static final StreamCodec<ByteBuf, NamespaceGasIngredient> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
            NamespaceGasIngredient::new,
            i -> i.namespace
    );

    public final String namespace;

    public NamespaceGasIngredient(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public GasIngredientType<?> getType() {
        return KubeJSGasIngredients.NAMESPACE.get();
    }

    @Override
    public boolean test(GasStack stack) {
        return stack.getGas().getResourceLocation().getNamespace().equals(this.namespace);
    }

    @Override
    protected Stream<GasStack> generateStacks() {
        return QTRegistries.GAS.stream().filter(gas -> gas.getResourceLocation().getNamespace().equals(this.namespace)).map(gas -> new GasStack(gas, 1000));
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public int hashCode() {
        return this.namespace.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof NamespaceGasIngredient r && this.namespace.equals(r.namespace);
    }
}
