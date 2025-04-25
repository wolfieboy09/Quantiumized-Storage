package dev.wolfieboy09.qtech.block.gas_filler;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum GasFillerState implements StringRepresentable {
    FILL,
    DRAIN;

    @Override
    public @NotNull String getSerializedName() {
        return Integer.toString(ordinal());
    }
}
