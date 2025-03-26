package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import dev.wolfieboy09.qstorage.api.registry.gas.GasBuilder;
import dev.wolfieboy09.qstorage.api.util.ColorUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class QSGasses {
    public static final DeferredRegister<Gas> GASSES = DeferredRegister.create(QSRegistries.GAS_REGISTRY, QuantiumizedStorage.MOD_ID);
    public static final Supplier<Gas> HYDROGEN = GASSES.register("hydrogen", () ->
            new GasBuilder().tint(ColorUtil.fromArgb(128, 256, 256, 256)).flammable(true).effects(
                    new MobEffectInstance(QSEffects.OXYGEN_DEPRIVATION, 120)
            ).build()
    );

    public static final Supplier<Gas> NITROGEN = GASSES.register("nitrogen", () ->
            new GasBuilder().effects(
                    new MobEffectInstance(QSEffects.OXYGEN_DEPRIVATION, 120)
            ).build());

    public static final Supplier<Gas> NITROGEN_SULFIDE = GASSES.register("nitrogen_sulfide", () ->
            new GasBuilder()
                    .effects(new MobEffectInstance(MobEffects.CONFUSION, 120))
                    .build());

    public static void register(IEventBus bus) {
        GASSES.register(bus);
    }
}
