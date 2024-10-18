package dev.wolfieboy09.qstorage.api.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NamingUtil {
    public static String toHumanReadable(String key) {
        String s = key.replaceAll("_", " ");
        s = Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(s)).map(StringUtils::capitalize).collect(Collectors.joining(" "));
        s = StringUtils.normalizeSpace(s);
        return s;
    }
}
