package dev.wolfieboy09.qstorage.api.registry.gas;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import dev.wolfieboy09.qstorage.registries.QSGasses;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.common.util.DataComponentUtil;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;

@NothingNullByDefault
public class GasStack implements MutableDataComponentHolder {
    public static final GasStack EMPTY = new GasStack((Void) null);

    // Use @UnknownNullability so the IDE will shut up about it being null to represent being empty
    @UnknownNullability
    private final Gas gas;
    private int amount;
    private final PatchedDataComponentMap components;

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Codec<Holder<Gas>> GAS_NON_EMPTY_CODEC = QSRegistries.GAS.holderByNameCodec().validate(holder -> holder.is(QSGasses.EMPTY.get().builtInRegistryHolder()) ? DataResult.error(() -> "Gas must not be qstorage:empty") : DataResult.success(holder));
    public static final Codec<GasStack> SINGLE_GAS_CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create((instance) -> instance.group(GAS_NON_EMPTY_CODEC.fieldOf("id").forGetter(GasStack::getGasHolder)).apply(instance, GasStack::new)));
    public static final StreamCodec<RegistryFriendlyByteBuf, GasStack> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(QSRegistries.GAS_KEY), GasStack::getGasHolder,
            ByteBufCodecs.INT, GasStack::getAmount,
            GasStack::new
    );

    public static final Codec<GasStack> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Gas.CODEC.fieldOf("gas").forGetter(GasStack::getGas),
                    Codec.INT.fieldOf("amount").forGetter(GasStack::getAmount)
            ).apply(instance, GasStack::new)
    );

    private GasStack(@Nullable Void unused) {
        this.gas = null;
        this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
    }

    private GasStack(Gas gas, int amount, PatchedDataComponentMap components) {
        this.gas = gas;
        this.amount = amount;
        this.components = components;
    }

    public GasStack(GasLike gas) {
        this(gas, 1);
    }

    public GasStack(GasLike gasLike, int amount) {
        this(gasLike.asGas(), amount, new PatchedDataComponentMap(DataComponentMap.EMPTY));
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
        if (this.isEmpty() || this.gas == null) {
            return EMPTY;
        } else {
            return new GasStack(this.gas, this.amount, this.components);
        }
    }

    private Gas getGas() {
        return this.isEmpty() ? QSGasses.EMPTY.get() : this.gas;
    }

    public boolean isEmpty() {
        return this.amount <= 0 || this == EMPTY ;
    }

    public Holder<Gas> getGasHolder() {
        return this.getGas().builtInRegistryHolder();
    }

    public static GasStack parseOptional(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        return tag.isEmpty() ? EMPTY : parse(lookupProvider, tag).orElse(EMPTY);
    }

    public static Optional<GasStack> parse(HolderLookup.Provider lookupProvider, Tag tag) {
        return CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial((error) -> LOGGER.error("Tried to load invalid gas: '{}'", error));
    }

    public Tag save(HolderLookup.Provider lookupProvider) {
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty GasStack");
        } else {
            return DataComponentUtil.wrapEncodingExceptions(this, CODEC, lookupProvider);
        }
    }

    public Tag save(HolderLookup.Provider lookupProvider, Tag prefix) {
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty GasStack");
        } else {
            return DataComponentUtil.wrapEncodingExceptions(this, CODEC, lookupProvider, prefix);
        }
    }

    public Tag saveOptional(HolderLookup.Provider lookupProvider) {
        return this.isEmpty() ? new CompoundTag() : this.save(lookupProvider, new CompoundTag());
    }

    @Override
    public <T> @Nullable T set(DataComponentType<? super T> type, @Nullable T component) {
        return this.components.set(type, component);
    }

    @Override
    public <T> @Nullable T remove(DataComponentType<? extends T> type) {
        return this.components.remove(type);
    }

    @Override
    public void applyComponents(DataComponentPatch patch) {
        this.components.applyPatch(patch);
    }

    @Override
    public void applyComponents(DataComponentMap map) {
        this.components.setAll(map);
    }

    @Override
    public DataComponentMap getComponents() {
        return this.components;
    }

    public boolean is(Gas gas) {
        return this.getGas() == gas;
    }

    public static boolean isSameGasSameComponents(GasStack first, GasStack second) {
        if (!first.is(second.getGas())) {
            return false;
        } else {
            return first.isEmpty() && second.isEmpty() || Objects.equals(first.components, second.components);
        }
    }

    public void grow(int addedAmount) {
        this.setAmount(this.getAmount() + addedAmount);
    }

    public void shrink(int removedAmount) {
        this.grow(-removedAmount);
    }

    public GasStack copyWithAmount(int amount) {
        if (this.isEmpty()) {
            return EMPTY;
        } else {
            GasStack gasStack = this.copy();
            gasStack.setAmount(amount);
            return gasStack;
        }
    }
}
