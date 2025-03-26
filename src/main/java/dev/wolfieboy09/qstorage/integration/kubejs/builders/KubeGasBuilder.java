package dev.wolfieboy09.qstorage.integration.kubejs.builders;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import dev.wolfieboy09.qstorage.api.registry.gas.GasBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

@SuppressWarnings("unused")
public class KubeGasBuilder extends BuilderBase<Gas> {
    private final GasBuilder builder = new GasBuilder();

    public KubeGasBuilder(ResourceLocation id) {
        super(id);
    }

    @Override
    public Gas createObject() {
        return this.builder.build();
    }

    @Info("If the gas is poisonous to entities")
    @ReturnsSelf
    public KubeGasBuilder poisonous(boolean isPoisonous) {
        this.builder.poisonous(isPoisonous);
        return this;
    }

    @Info("If the gas is heavy")
    @ReturnsSelf
    public KubeGasBuilder heavy(boolean isHeavy) {
        this.builder.heavy(isHeavy);
        return this;
    }

    @Info("Gas particle tint")
    @ReturnsSelf
    public KubeGasBuilder tint(int tint) {
        this.builder.tint(tint);
        return this;
    }

    @Info("If the gas will react to fire")
    @ReturnsSelf
    public KubeGasBuilder flammable(boolean flammable) {
        this.builder.flammable(flammable);
        return this;
    }

    @Info("The mob effects to give to any entity from the gas")
    @ReturnsSelf
    public KubeGasBuilder effects(MobEffectInstance... effects) {
        this.builder.effects(effects);
        return this;
    }
}
