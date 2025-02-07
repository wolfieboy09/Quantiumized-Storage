package dev.wolfieboy09.qstorage.api.util;

public class ColorUtil {
    public static int fromArgb(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
