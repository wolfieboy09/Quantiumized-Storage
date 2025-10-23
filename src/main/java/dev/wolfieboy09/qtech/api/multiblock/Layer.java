package dev.wolfieboy09.qtech.api.multiblock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a layer of a multiblock
 * @param rows Each row inside a multiblock
 */
public record Layer(List<String> rows) {
    public static final Codec<Layer> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Codec.STRING.listOf().fieldOf("rows").forGetter(Layer::rows)
    ).apply(inst, Layer::new));

    public static final StreamCodec<ByteBuf, Layer> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), Layer::rows,
            Layer::new
    );

    public Layer(List<String> rows) {
        this.rows = List.copyOf(rows); // We need it immutable
    }

    /**
     * Creates a layer from string rows
     * @param rows A string of rows
     * @return A {@link Layer} of the given rows
     */
    @Contract("_ -> new")
    public static @NotNull Layer of(String... rows) {
        return new Layer(List.of(rows));
    }

    /**
     * Creates a layer from a list of strings
     * @param rows A list of each row
     * @return A {@link Layer} of the given rows
     */
    @Contract("_ -> new")
    public static @NotNull Layer of(List<String> rows) {
        return new Layer(rows);
    }

    /**
     * Gets the width of the multiblock
     * @return The width of the multiblock
     * @apiNote This function goes through each row to find the largest row.
     * It does <b>not</b> assume all rows are the same length.
     * If you do not wish to use this function for any reason, use {@link Layer#getFirstWidth()} to get the width of the first row
     */
    public int getWidth() {
        int width = 0;
        for (String row : this.rows) {
            if (row.length() > width) {
                width = row.length();
            }
        }
        return width;
    }

    /**
     * Gets the width of the multiblock from the first row
     * @return The width of the multiblock
     * @apiNote If you want to find the "largest row," use {@link Layer#getWidth()} instead as that will search all rows to find the largest one.
     */
    public int getFirstWidth() {
        return this.rows.getFirst().length();
    }


    /**
     * @return The depth of the {@link Layer}
     * @implNote  This just calls <code>this.rows.size()</code>
     */
    public int getDepth() {
        return this.rows.size();
    }

    /**
     *
     * @param index Index to fetch from
     * @return A row of the {@link Layer}
     */
    public String getRow(int index) {
        return this.rows.get(index);
    }

    /**
     * Validates that all rows have the same length
     * @return Whether each row is the same length
     * @apiNote This function does not call {@link Layer#getWidth()} because it would be looping over the rows again
     * adding redundancy.
     */
    public boolean isValid() {
        if (this.rows.isEmpty()) return false;

        int width = getFirstWidth();
        for (String row : this.rows) {
            if (row.length() != width) return false;
        }
        return true;
    }
}
