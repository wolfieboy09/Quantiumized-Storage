package dev.wolfieboy09.qstorage.api.util;

public class FormattingUtil {
    public static String formatNumber(int number) {
        if (number >= 1_000_000_000) {
            return String.format("%.1fB", number / 1_000_000_000.0);
        } else if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000) {
            return String.format("%.1fk", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }
}
