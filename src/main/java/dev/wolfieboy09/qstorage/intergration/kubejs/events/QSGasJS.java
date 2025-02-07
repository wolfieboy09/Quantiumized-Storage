package dev.wolfieboy09.qstorage.intergration.kubejs.events;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import dev.wolfieboy09.qstorage.api.registry.gas.GasBuilder;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public class QSGasJS extends BuilderBase<Gas> {
    private final GasBuilder builder = new GasBuilder();

    public QSGasJS(ResourceLocation id) {
        super(id);
    }

    @Override
    public Gas createObject() {
        return this.builder.build();
    }

    @Info("If the gas is poisonous to entities")
    public QSGasJS poisonous(boolean isPoisonous) {
        this.builder.poisonous(isPoisonous);
        return this;
    }

    @Info("If the gas is heavy")
    public QSGasJS heavy(boolean isHeavy) {
        this.builder.heavy(isHeavy);
        return this;
    }

    @Info("Gas particle tint")
    public QSGasJS tint(int tint) {
        this.builder.tint(tint);
        return this;
    }

    @Info("If the gas will react to fire")
    public QSGasJS flammable(boolean flammable) {
        this.builder.flammable(flammable);
        return this;
    }
}
