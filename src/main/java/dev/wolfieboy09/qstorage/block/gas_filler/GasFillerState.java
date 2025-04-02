package dev.wolfieboy09.qstorage.block.gas_filler;

public enum GasFillerState {
    FILL(0),
    DRAIN(1);

    final int id;
    GasFillerState(int id) {
        this.id = id;
    }
}
