package dev.wolfieboy09.qtech.api.codecs;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * A string writer for {@link ByteBuf}
 */
@NothingNullByDefault
public final class ByteBufStrings {
    /**
     * Writes one string to a {@link ByteBuf}
     * @param buf the {@link ByteBuf} to write to
     * @param string the string to write
     */
    public static void writeString(ByteBuf buf, String string) {
       writeStringArray(buf, new String[]{string});
    }

    /**
     * Reads a string from a {@link ByteBuf}
     * @param buf the {@link ByteBuf} to read from
     * @return A string
     */
    public static String readString(ByteBuf buf) {
        return readStringArray(buf)[0];
    }

    /**
     * Writes an array of strings to a {@link ByteBuf}
     * @param buf the {@link ByteBuf} to write to
     * @param strings the list of strings
     */
    public static void writeStringArray(ByteBuf buf, String[] strings) {
        buf.writeInt(strings.length);
        for (String string : strings) {
            byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }
    }

    /**
     * Reads an array of strings from a {@link ByteBuf}
     * @param buf the {@link ByteBuf} to read from
     * @return A list of strings
     */
    public static String[] readStringArray(ByteBuf buf) {
        int len = buf.readInt();
        String[] strings = new String[len];
        for (int i = 0; i < len; i++) {
            int stringLen = buf.readInt();
            byte[] bytes = new byte[stringLen];
            buf.readBytes(bytes);
            strings[i] = new String(bytes, StandardCharsets.UTF_8);
        }
        return strings;
    }
}
