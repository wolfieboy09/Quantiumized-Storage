package dev.wolfieboy09.qtech.api.gas.crafting;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.gas.GasStackLinkedSet;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NothingNullByDefault
public abstract class GasIngredient implements Predicate<GasStack> {
    private static final MapCodec<GasIngredient> SINGLE_OR_TAG_CODEC = MapCodec.recursive(
            "GasIngredient.SINGLE_OR_TAG_CODEC", self -> singleOrTagCodec()
    );

    public static final Codec<GasIngredient> CODEC = codec(true);
    public static final Codec<GasIngredient> CODEC_NON_EMPTY = codec(false);

    public static final MapCodec<GasIngredient> MAP_CODEC_NONEMPTY = makeMapCodec();

    private static final Codec<GasIngredient> MAP_CODEC_CODEC = MAP_CODEC_NONEMPTY.codec();

    public static final Codec<List<GasIngredient>> LIST_CODEC = MAP_CODEC_CODEC.listOf();

    public static final Codec<List<GasIngredient>> LIST_CODEC_NON_EMPTY = LIST_CODEC.validate(list -> list.isEmpty() ? DataResult.error(() -> "Gas ingredient cannot be empty, at least one item must be defined") : DataResult.success(list));

    public static final StreamCodec<RegistryFriendlyByteBuf, GasIngredient> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, GasIngredient>() {
        private static final StreamCodec<RegistryFriendlyByteBuf, GasIngredient> DISPATCH_CODEC = ByteBufCodecs.registry(QTRegistries.GAS_INGREDIENT_TYPES_KEY)
                .dispatch(GasIngredient::getType, GasIngredientType::streamCodec);

        private static final StreamCodec<RegistryFriendlyByteBuf, List<GasStack>> GAS_LIST_CODEC = GasStack.STREAM_CODEC.apply(
                ByteBufCodecs.collection(NonNullList::createWithCapacity)
        );

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, GasIngredient ingredient) {
            if (ingredient.isSimple()) {
                GAS_LIST_CODEC.encode(buffer, Arrays.asList(ingredient.getStacks()));
            } else {
                buffer.writeVarInt(-1);
                DISPATCH_CODEC.encode(buffer, ingredient);
            }
        }

        @Override
        public GasIngredient decode(RegistryFriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            if (size == -1) {
                return DISPATCH_CODEC.decode(buffer);
            }
            return CompoundGasIngredient.of(
                    Stream.generate(() -> GasStack.STREAM_CODEC.decode(buffer))
                            .limit(size)
                            .map(GasIngredient::single)
            );
        }
    };

    @Nullable
    private GasStack[] stacks;

    private static MapCodec<GasIngredient> singleOrTagCodec() {
        return NeoForgeExtraCodecs.xor(
                SingleGasIngredient.CODEC,
                TagGasIngredient.CODEC).xmap(either -> either.map(id -> id, id -> id), ingredient -> {
            if (ingredient instanceof SingleGasIngredient fluid) {
                return Either.left(fluid);
            } else if (ingredient instanceof TagGasIngredient tag) {
                return Either.right(tag);
            }
            throw new IllegalStateException("Basic gas ingredient should be either a gas or a tag!");
        });
    }

    protected abstract Stream<GasStack> generateStacks();

    public abstract boolean isSimple();

    public abstract GasIngredientType<?> getType();

    public final boolean isEmpty() {
        return this == empty();
    }

    public final boolean hasNoGasses() {
        return getStacks().length == 0;
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    public static GasIngredient empty() {
        return EmptyGasIngredient.INSTANCE;
    }

    public static GasIngredient of() {
        return empty();
    }

    public static GasIngredient of(Gas... gases) {
        return of(Arrays.stream(gases));
    }

    private static GasIngredient of(Stream<Gas> gasses) {
        return CompoundGasIngredient.of(gasses.map(GasIngredient::single));
    }

    public static GasIngredient single(GasStack stack) {
        return single(stack.getGas());
    }

    public static GasIngredient single(Gas gas) {
        return single(gas.builtInRegistryHolder());
    }

    public static GasIngredient single(Holder<Gas> holder) {
        return new SingleGasIngredient(holder);
    }

    public static GasIngredient tag(TagKey<Gas> tag) {
        return new TagGasIngredient(tag);
    }

    private static MapCodec<GasIngredient> makeMapCodec() {
        return NeoForgeExtraCodecs.<GasIngredientType<?>, GasIngredient, GasIngredient>dispatchMapOrElse(
                QTRegistries.GAS_INGREDIENT_TYPES.byNameCodec(),
                GasIngredient::getType,
                GasIngredientType::codec,
                GasIngredient.SINGLE_OR_TAG_CODEC).xmap(either -> either.map(id -> id, id -> id), ingredient -> {
                    if (ingredient instanceof SingleGasIngredient || ingredient instanceof TagGasIngredient) {
                        return Either.right(ingredient);
                    }
                    return Either.left(ingredient);
        }).validate(ingredient -> {
            if (ingredient.isEmpty()) {
                return DataResult.error(() -> "Cannot serialize empty gas ingredient using the map codec");
            }
            return DataResult.success(ingredient);
        });
    }

    private static Codec<GasIngredient> codec(boolean allowEmpty) {
        Codec<List<GasIngredient>> listCodec = Codec.lazyInitialized(() -> allowEmpty ? LIST_CODEC : LIST_CODEC_NON_EMPTY);
        return Codec.either(listCodec, MAP_CODEC_CODEC)
                .xmap(either -> either.map(CompoundGasIngredient::of, i -> i),
                        ingredient -> {
                            if (ingredient instanceof CompoundGasIngredient compound) {
                                return Either.left(compound.children());
                            } else if (ingredient.isEmpty()) {
                                return Either.left(List.of());
                            }
                            return Either.right(ingredient);
                        }
                );
    }

    public final GasStack[] getStacks() {
        if (this.stacks == null) {
            this.stacks = generateStacks()
                    .collect(Collectors.toCollection(GasStackLinkedSet::createTypeAndComponentsSet))
                    .toArray(GasStack[]::new);
        }
        return this.stacks;
    }
}
