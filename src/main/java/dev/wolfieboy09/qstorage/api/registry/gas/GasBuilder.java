package dev.wolfieboy09.qstorage.api.registry.gas;

public class GasBuilder implements GasData {
    private boolean isPoisonous = false;
    private int tint = 0xFFFFFF;
    private boolean flammable = false;

    private boolean heavyGas = false;

    public GasBuilder() {}

    public GasBuilder poisonous(boolean isPoisonous) {
        this.isPoisonous = isPoisonous;
        return this;
    }

    public GasBuilder heavy(boolean isHeavy) {
        this.heavyGas = isHeavy;
        return this;
    }

    public GasBuilder tint(int tint) {
        this.tint = tint;
        return this;
    }

    public GasBuilder flammable(boolean flammable) {
        this.flammable = flammable;
        return this;
    }

    /**
     * Creates the {@link Gas} instance.
     * @return the {@link Gas} data from the builder.
     */
    public Gas build() {
        return new Gas(this);
    }

    @Override
    public boolean isPoisonous() {
        return this.isPoisonous;
    }

    @Override
    public boolean isHeavy() {
        return this.heavyGas;
    }

    @Override
    public boolean isFlammable() {
        return this.flammable;
    }

    @Override
    public int tint() {
        return this.tint;
    }
}

