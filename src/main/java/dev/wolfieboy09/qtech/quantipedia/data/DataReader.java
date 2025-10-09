package dev.wolfieboy09.qtech.quantipedia.data;

import dev.wolfieboy09.qtech.quantipedia.api.errors.MalformedDataException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DataReader {
    private final String header;
    private final Map<String, String> data;

    public DataReader(String parseFrom) {
        this.header = getHeaderFromRaw(parseFrom);
        this.data = getMapFromRaw(parseFrom);
    }

    private @NotNull String getHeaderFromRaw(@NotNull String raw) {
        int idx = raw.indexOf("[");
        if (idx == -1) {
            throw new MalformedDataException("Malformed data was given (Did you forget a '[' after the manifest?)");
        }
        return raw.substring(0, idx);
    }

    private @NotNull Map<String, String> getMapFromRaw(@NotNull String raw) {
        Map<String, String> toReturn = new HashMap<>();

        String content = raw.substring(raw.indexOf("["), raw.indexOf("]"));
        String[] split = content.split("\\|");
        for (String thing : split) {
            String[] sections = thing.split("=");
            toReturn.putIfAbsent(sections[0], sections[1]);
        }
        return toReturn;
    }

    public String getHeader() {
        return this.header;
    }

    public String get(String key) {
        return data.getOrDefault(key, "");
    }

    public String getOrDefault(String key, String defaultValue) {
        return this.data.getOrDefault(key, defaultValue);
    }
}
