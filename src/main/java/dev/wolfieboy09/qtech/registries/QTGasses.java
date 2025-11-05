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
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class QTGasses {
    public static final DeferredRegister<Gas> GASSES =
            DeferredRegister.create(QTRegistries.GAS, QuantiumizedTech.MOD_ID);

    public static final Supplier<Gas> EMPTY = simpleGas("empty");

    public static final Supplier<Gas> HYDROGEN = GASSES.register("hydrogen", () ->
            new GasBuilder()
                    .tint(ColorUtil.newAlpha(128, 0xADD8E6))
                    .flammable(true)
                    .range(5.0f)
                    .particleSpeed(0.03f)
                    .effects(new MobEffectInstance(QTEffects.OXYGEN_DEPRIVATION, 60))
                    .build()
    );

    public static final Supplier<Gas> CARBON_MONOXIDE = GASSES.register("carbon_monoxide", () ->
            new GasBuilder()
                    .tint(ColorUtil.newAlpha(128, 0x808080))
                    .flammable(true)
                    .poisonous(true)
                    .heavy(false)
                    .effects(
                            new MobEffectInstance(QTEffects.OXYGEN_DEPRIVATION, 200),
                            new MobEffectInstance(MobEffects.POISON, 100)
                    )
                    .range(3.0f)
                    .build()
    );

    public static final Supplier<Gas> NITROGEN = inertGas("nitrogen", 0xE0FFFF);
    public static final Supplier<Gas> ARGON = inertGas("argon", 0x9966CC);
    public static final Supplier<Gas> NEON = inertGas("neon", 0xFF6666);
    public static final Supplier<Gas> HELIUM = inertGas("helium", 0xFFB6C1);

    public static final Supplier<Gas> OXYGEN = GASSES.register("oxygen", () ->
            new GasBuilder()
                    .tint(ColorUtil.newAlpha(128, 0x99CCFF))
                    .flammable(false)
                    .range(4.0f)
                    .build()
    );

    public static final Supplier<Gas> CARBON_DIOXIDE = GASSES.register("carbon_dioxide", () ->
            new GasBuilder()
                    .tint(ColorUtil.newAlpha(128, 0xCCCCCC))
                    .heavy(true)
                    .range(2.5f)
                    .particleSpeed(0.005f)
                    .effects(new MobEffectInstance(QTEffects.OXYGEN_DEPRIVATION, 200))
                    .build()
    );

    public static final Supplier<Gas> CARBON = GASSES.register("carbon", () ->
            new GasBuilder()
                    .tint(ColorUtil.newAlpha(128, 0x333333))
                    .heavy(true)
                    .range(2.0f)
                    .build()
    );

    public static final Supplier<Gas> AIR = GASSES.register("air", () ->
            new GasBuilder()
                    .tint(ColorUtil.newAlpha(64, 0xAEEEEE))
                    .range(4.0f)
                    .build()
    );


    private static @NotNull Supplier<Gas> simpleGas(String name) {
        return GASSES.register(name, () -> new GasBuilder().build());
    }

    private static @NotNull Supplier<Gas> inertGas(String name, int color) {
        return GASSES.register(name, () ->
                new GasBuilder()
                        .tint(ColorUtil.newAlpha(128, color))
                        .flammable(false)
                        .heavy(false)
                        .build()
        );
    }

    public static void register(IEventBus bus) {
        GASSES.register(bus);
    }
}
