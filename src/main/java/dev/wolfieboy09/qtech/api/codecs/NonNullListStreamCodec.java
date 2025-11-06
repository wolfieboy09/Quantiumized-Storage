package dev.wolfieboy09.qtech.api.codecs;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

// Source is from the Create mod and their catnip system
@NothingNullByDefault
public interface NonNullListStreamCodec {
    static <B extends ByteBuf, V> StreamCodec.CodecOperation<B, V, NonNullList<V>> nonNullList() {
        return streamCodec -> ByteBufCodecs.collection(NonNullList::createWithCapacity, streamCodec);
    }

    static <B extends ByteBuf, V> StreamCodec.CodecOperation<B, V, NonNullList<V>> nonNullList(int maxSize) {
        return streamCodec -> ByteBufCodecs.collection(NonNullList::createWithCapacity, streamCodec, maxSize);
    }

    static <B extends ByteBuf, V> StreamCodec<B, NonNullList<V>> nonNullList(StreamCodec<B, V> base) {
        return base.apply(nonNullList());
    }

    static <B extends ByteBuf, V> StreamCodec<B, NonNullList<V>> nonNullList(StreamCodec<B, V> base, int maxSize) {
        return base.apply(nonNullList(maxSize));
    }
}
