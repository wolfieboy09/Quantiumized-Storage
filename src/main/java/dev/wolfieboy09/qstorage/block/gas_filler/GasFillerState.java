package dev.wolfieboy09.qstorage.block.gas_filler;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum GasFillerState implements StringRepresentable {
    FILL(0),
    DRAIN(1);

    final int id;
    GasFillerState(int id) {
        this.id = id;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name();
    }
}
