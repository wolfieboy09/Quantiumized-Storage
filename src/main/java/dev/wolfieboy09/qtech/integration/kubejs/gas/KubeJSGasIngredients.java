package dev.wolfieboy09.qtech.integration.kubejs.gas;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.gas.crafting.GasIngredient;
import dev.wolfieboy09.qtech.api.gas.crafting.GasIngredientType;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface KubeJSGasIngredients {
    DeferredRegister<GasIngredientType<?>> REGISTRY = DeferredRegister.create(QTRegistries.GAS_INGREDIENT_TYPES, QuantiumizedTech.MOD_ID);

    Supplier<GasIngredientType<?>> NAMESPACE = REGISTRY.register("namespace", () -> new GasIngredientType<>(NamespaceGasIngredient.CODEC, NamespaceGasIngredient.STREAM_CODEC));
    Supplier<GasIngredientType<?>> REGEX = REGISTRY.register("regex", () -> new GasIngredientType<>(RegExGasIngredient.CODEC, RegExGasIngredient.STREAM_CODEC));

    static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
