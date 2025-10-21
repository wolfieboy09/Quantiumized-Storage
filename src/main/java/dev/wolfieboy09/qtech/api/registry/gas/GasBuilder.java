package dev.wolfieboy09.qtech.api.registry.gas;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.LinkedList;
import java.util.List;

@CanIgnoreReturnValue
public class GasBuilder implements GasInfo {
    private boolean isPoisonous = false;
    private int tint = 0xFFFFFF;
    private boolean flammable = false;
    private boolean heavyGas = false;
    private List<MobEffectInstance> effects = new LinkedList<>();
    private float range = 3;
    private float pSpeed = 0.01f;


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

    public GasBuilder effects(MobEffectInstance ... effects) {
        this.effects = List.of(effects);
        return this;
    }

    public GasBuilder range(float range) {
        this.range = range;
        return this;
    }

    public GasBuilder particleSpeed(float pSpeed) {
        this.pSpeed = pSpeed;
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

    @Override
    public List<MobEffectInstance> effects() {
        return this.effects;
    }

    @Override
    public float gasRange() {
        return this.range;
    }

    @Override
    public float particleSpeed() {
        return this.pSpeed;
    }
}