package dev.wolfieboy09.qtech.quantipedia.api;

import dev.wolfieboy09.qtech.quantipedia.data.DataReader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record QuanEntry(DataReader reader, String header, List<String> content) {
    @Contract("_, _, _ -> new")
    public static @NotNull QuanEntry create(String manifest, String header, @NotNull List<String> contents) {
        if (contents.getFirst().startsWith("manifest[")) {
            contents.removeFirst();
        }
        return new QuanEntry(new DataReader(manifest), header, contents);
    }

    @Contract("_ -> new")
    public static @NotNull QuanEntry create(@NotNull List<String> contents) {
        DataReader dataReader = new DataReader(contents.getFirst());
        String title = dataReader.getOrDefault("title", contents.get(2).replace("# ", ""));
        if (contents.getFirst().startsWith("manifest[")) {
            contents.removeFirst();
        }
        return new QuanEntry(dataReader, title, contents);
    }
}
