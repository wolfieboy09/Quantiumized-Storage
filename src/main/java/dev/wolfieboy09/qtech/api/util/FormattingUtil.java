package dev.wolfieboy09.qtech.api.util;

import org.jetbrains.annotations.NotNull;

public class FormattingUtil {
    public static @NotNull String formatNumber(int count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }
}
