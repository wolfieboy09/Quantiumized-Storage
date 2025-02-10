package dev.wolfieboy09.qstorage.api.registry.gas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class GasStack {
    public static final GasStack EMPTY = new GasStack((Void) null);
    @Nullable
    private final Gas gas;
    private int amount;

    public static final Codec<Holder<Gas>> GAS_NON_EMPTY_CODEC = QSRegistries.GAS_REGISTRY.holderByNameCodec();
    public static final Codec<GasStack> SINGLE_GAS_CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create((instance) -> instance.group(GAS_NON_EMPTY_CODEC.fieldOf("id").forGetter(GasStack::getGasHolder)).apply(instance, GasStack::new)));
    public static final StreamCodec<RegistryFriendlyByteBuf, GasStack> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(QSRegistries.GAS_REGISTRY_KEY), GasStack::getGasHolder,
            ByteBufCodecs.INT, GasStack::getAmount,
            GasStack::new
    );

    private GasStack(@Nullable Void unused) {
        this.gas = null;
    }

    public static final Codec<GasStack> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                Gas.CODEC.fieldOf("gas").forGetter(GasStack::getGas),
                Codec.INT.fieldOf("amount").forGetter(GasStack::getAmount)
        ).apply(instance, GasStack::new)
    );

    public GasStack(GasLike gas) {
        this(gas, 1);
    }

    public GasStack(GasLike gasLike, int amount) {
        this.gas = gasLike.asGas();
        this.amount = amount;
    }

    public GasStack(Holder<Gas> gas, int amount) {
        this(gas.value(), amount);
    }

    public GasStack(Holder<Gas> gas) {
        this(gas.value(), 1);
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public GasStack copy() {
        return new GasStack(this.gas, this.amount);
    }

    @Nullable
    private Gas getGas() {
        return this.gas;
    }

    public boolean isEmpty() {
        return this.amount <= 0 || this == EMPTY;
    }

    public Holder<Gas> getGasHolder() {
        return this.getGas().builtInRegistryHolder();
    }
}
