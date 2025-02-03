package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import dev.wolfieboy09.qstorage.api.registry.gas.GasBuilder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class QSGasses {
    public static final DeferredRegister<Gas> GASSES = DeferredRegister.create(QSRegistries.GAS_REGISTRY, QuantiumizedStorage.MOD_ID);
    public static final Supplier<Gas> HYDROGEN = GASSES.register("hydrogen", () ->
            new GasBuilder().tint(0xFFFFFF).flammable(true).build());

    public static void register(IEventBus bus) {
        GASSES.register(bus);
    }
}
