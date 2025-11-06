package dev.wolfieboy09.qtech.api.codecs.wrapper;

import com.mojang.serialization.Codec;
import dev.wolfieboy09.qtech.api.codecs.TriEitherCodec;
import dev.wolfieboy09.qtech.api.util.TriEither;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class TriEitherCodecs {

    @Contract("_, _, _ -> new")
    public static <A, B, C> @NotNull Codec<TriEither<A, B, C>> either(Codec<A> first, Codec<B> second, Codec<C> third) {
        return new TriEitherCodec<>(first, second, third);
    }
}
