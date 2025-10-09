package dev.wolfieboy09.qtech.api.util;

/**
 * A simple color util class that takes in alpha, red, green, and blue (or just the rgb part).
 * <br><br>
 * <b>All params are from a 0-255 scale!</b>
 */
public final class ColorUtil {

    /**
     * Creates an ARGB value
     * @param a Alpha value
     * @param r Red value
     * @param g Green value
     * @param b Blue value
     * @return A color with the RGB plus the alpha
     */
    public static int fromArgb(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Creates an ARGB value - alpha defaults to 255
     * @param r Red value
     * @param g Green value
     * @param b Blue value
     * @return A color with RGB plus the alpha
     */
    public static int fromRgb(int r, int g, int b) {
        return fromArgb(255, r, g, b);
    }

    /**
     * Replaces the alpha value of the given color
     * @param a The new alpha value
     * @param color The color
     * @return The color with a new alpha value
     */
    public static int newAlpha(int a, int color) {
        return (a << 24) | (color & 0x00FFFFFF);
    }

    public static final int WHITE = fromRgb(255, 255, 255);
    public static final int BLACK = fromRgb(0, 0, 0);
}
