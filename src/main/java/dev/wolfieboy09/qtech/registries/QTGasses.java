package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasBuilder;
import dev.wolfieboy09.qtech.api.util.ColorUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class QTGasses {
    public static final DeferredRegister<Gas> GASSES = DeferredRegister.create(QTRegistries.GAS, QuantiumizedTech.MOD_ID);

    public static final Supplier<Gas> EMPTY  = GASSES.register("empty", () -> new GasBuilder().build());

    public static final Supplier<Gas> HYDROGEN = GASSES.register("hydrogen", () ->
            new GasBuilder().tint(ColorUtil.fromArgb(128, 256, 256, 256)).flammable(true).effects(
                    new MobEffectInstance(QTEffects.OXYGEN_DEPRIVATION, 120)
            ).build()
    );

    public static final Supplier<Gas> NITROGEN = GASSES.register("nitrogen", () ->
            new GasBuilder().effects(
                    new MobEffectInstance(QTEffects.OXYGEN_DEPRIVATION, 120)
            ).build());

    public static final Supplier<Gas> NITROGEN_SULFIDE = GASSES.register("nitrogen_sulfide", () ->
            new GasBuilder()
                    .effects(new MobEffectInstance(MobEffects.CONFUSION, 120))
                    .build());

    public static void register(IEventBus bus) {
        GASSES.register(bus);
    }
}
