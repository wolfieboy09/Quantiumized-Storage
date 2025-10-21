package dev.wolfieboy09.qtech.api.codecs;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

@NothingNullByDefault
public final class QTExtraStreamCodecs {
    public static final StreamCodec<ByteBuf, String[]> STRING_ARRAY = new StreamCodec<>() {
        @Override
        public String[] decode(ByteBuf byteBuf) {
            return ByteBufStrings.readStringArray(byteBuf);
        }

        @Override
        public void encode(ByteBuf o, String[] strings) {
           ByteBufStrings.writeStringArray(o, strings);
        }
    };

    public static final StreamCodec<ByteBuf, Character> CHAR = new StreamCodec<>() {
        @Override
        public Character decode(ByteBuf byteBuf) {
            return byteBuf.readChar();
        }

        @Override
        public void encode(ByteBuf o, Character character) {
            o.writeChar(character);
        }
    };
}
