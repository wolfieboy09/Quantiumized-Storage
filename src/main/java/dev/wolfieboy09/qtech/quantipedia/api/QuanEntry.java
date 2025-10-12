package dev.wolfieboy09.qtech.quantipedia.api;

import dev.wolfieboy09.qtech.quantipedia.data.DataReader;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record QuanEntry(DataReader reader, String header, List<String> content, Optional<ResourceLocation> entryIcon) {

    @Contract("_, _, _, _ -> new")
    public static @NotNull QuanEntry create(String manifest, String header, @NotNull List<String> contents, ResourceLocation entryIcon) {
        List<String> contentCopy = copyAndStripManifest(contents);
        return new QuanEntry(new DataReader(manifest), header, contentCopy, Optional.of(entryIcon));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull QuanEntry create(String manifest, String header, @NotNull List<String> contents) {
        List<String> contentCopy = copyAndStripManifest(contents);
        return new QuanEntry(new DataReader(manifest), header, contentCopy, Optional.empty());
    }

    @Contract("_ -> new")
    public static @NotNull QuanEntry create(@NotNull List<String> contents) {
        if (contents.isEmpty()) {
            throw new IllegalArgumentException("Contents cannot be empty");
        }

        List<String> contentCopy = copyAndStripManifest(contents);
        DataReader dataReader = new DataReader(contentCopy.get(0));

        String title = dataReader.getOrDefault(
                "title",
                contentCopy.size() > 2 ? contentCopy.get(2).replaceFirst("# ", "") : "Untitled"
        );

        return new QuanEntry(dataReader, title, contentCopy, getResourceLocation(dataReader));
    }

    private static @NotNull List<String> copyAndStripManifest(@NotNull List<String> original) {
        List<String> copy = new ArrayList<>(original);
        if (!copy.isEmpty() && copy.getFirst().startsWith("manifest[")) {
            copy.removeFirst();
        }
        return copy;
    }

    private static Optional<ResourceLocation> getResourceLocation(@NotNull DataReader reader) {
        return Optional.ofNullable(reader.get("icon"))
                .filter(s -> !s.isEmpty())
                .map(ResourceLocation::parse);
    }
}
