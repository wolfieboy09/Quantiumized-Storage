package dev.wolfieboy09.qtech.api.multiblock;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record MultiblockHatchRule(boolean allowInsert, boolean allowExtract) {
    public boolean isBothAllowed() {
        return this.allowInsert && this.allowExtract;
    }

    @Contract(" -> new")
    public static @NotNull MultiblockHatchRule insertOnly() {
        return new MultiblockHatchRule(true, false);
    }

    @Contract(" -> new")
    public static @NotNull MultiblockHatchRule extractOnly() {
        return new MultiblockHatchRule(false, true);
    }

    @Contract(" -> new")
    public static @NotNull MultiblockHatchRule allowBoth() {
        return new MultiblockHatchRule(true, true);
    }
}
