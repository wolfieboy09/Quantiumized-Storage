package dev.wolfieboy09.qtech.api.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.wolfieboy09.qtech.api.util.TriEither;

public record TriEitherCodec<F, S, L>(Codec<F> first, Codec<S> second, Codec<L> third) implements Codec<TriEither<F, S, L>> {
    @Override
    public <T> DataResult<Pair<TriEither<F, S, L>, T>> decode(DynamicOps<T> ops, T input) {
        DataResult<Pair<F, T>> firstResult = first.decode(ops, input);
        if (firstResult.result().isPresent()) {
            F value = firstResult.result().get().getFirst();
            T remainder = firstResult.result().get().getSecond();
            return DataResult.success(Pair.of(TriEither.left(value), remainder));
        }

        DataResult<Pair<S, T>> secondResult = second.decode(ops, input);
        if (secondResult.result().isPresent()) {
            S value = secondResult.result().get().getFirst();
            T remainder = secondResult.result().get().getSecond();
            return DataResult.success(Pair.of(TriEither.middle(value), remainder));
        }

        DataResult<Pair<L, T>> thirdResult = third.decode(ops, input);
        if (thirdResult.result().isPresent()) {
            L value = thirdResult.result().get().getFirst();
            T remainder = thirdResult.result().get().getSecond();
            return DataResult.success(Pair.of(TriEither.right(value), remainder));
        }

        return DataResult.error(() -> "Could not decode input as any variant of TriEither");
    }

    @Override
    public <T> DataResult<T> encode(TriEither<F, S, L> input, DynamicOps<T> ops, T prefix) {
        if (input.isLeft()) {
            TriEither.Left<F, S, L> left = (TriEither.Left<F, S, L>) input;
            return first.encode(left.get(), ops, prefix);
        } else if (input.isMiddle()) {
            TriEither.Middle<F, S, L> middle = (TriEither.Middle<F, S, L>) input;
            return second.encode(middle.get(), ops, prefix);
        } else if (input.isRight()) {
            TriEither.Right<F, S, L> right = (TriEither.Right<F, S, L>) input;
            return third.encode(right.get(), ops, prefix);
        } else {
            return DataResult.error(() -> "Unknown TriEither variant");
        }
    }

    public static <F, S, L> TriEitherCodec<F, S, L> triEither(Codec<F> first, Codec<S> second, Codec<L> third) {
        return new TriEitherCodec<>(first, second, third);
    }
}
