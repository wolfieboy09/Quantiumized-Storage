package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.gas.crafting.*;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class QTGasIngredientTypes {
    private static final DeferredRegister<GasIngredientType<?>> GAS_INGREDIENT_TYPES =
            DeferredRegister.create(QTRegistries.GAS_INGREDIENT_TYPES, QuantiumizedTech.MOD_ID);

    public static final DeferredHolder<GasIngredientType<?>, GasIngredientType<SingleGasIngredient>> SINGLE_GAS_INGREDIENT_TYPE = GAS_INGREDIENT_TYPES.register("single", () -> new GasIngredientType<>(SingleGasIngredient.CODEC));
    public static final DeferredHolder<GasIngredientType<?>, GasIngredientType<TagGasIngredient>> TAG_GAS_INGREDIENT_TYPE = GAS_INGREDIENT_TYPES.register("tag", () -> new GasIngredientType<>(TagGasIngredient.CODEC));
    public static final DeferredHolder<GasIngredientType<?>, GasIngredientType<EmptyGasIngredient>> EMPTY_GAS_INGREDIENT_TYPE = GAS_INGREDIENT_TYPES.register("empty", () -> new GasIngredientType<>(EmptyGasIngredient.CODEC));
    public static final DeferredHolder<GasIngredientType<?>, GasIngredientType<CompoundGasIngredient>> COMPOUND_GAS_INGREDIENT_TYPE = GAS_INGREDIENT_TYPES.register("compound", () -> new GasIngredientType<>(CompoundGasIngredient.CODEC));
    public static final DeferredHolder<GasIngredientType<?>, GasIngredientType<DataComponentGasIngredient>> DATA_COMPONENT_GAS_INGREDIENT_TYPE = GAS_INGREDIENT_TYPES.register("components", () -> new GasIngredientType<>(DataComponentGasIngredient.CODEC));
    public static final DeferredHolder<GasIngredientType<?>, GasIngredientType<DifferenceGasIngredient>> DIFFERENCE_GAS_INGREDIENT_TYPE = GAS_INGREDIENT_TYPES.register("difference", () -> new GasIngredientType<>(DifferenceGasIngredient.CODEC));
    public static final DeferredHolder<GasIngredientType<?>, GasIngredientType<IntersectionGasIngredient>> INTERSECTION_GAS_INGREDIENT_TYPE = GAS_INGREDIENT_TYPES.register("intersection", () -> new GasIngredientType<>(IntersectionGasIngredient.CODEC));

    public static void register(IEventBus bus) {
        GAS_INGREDIENT_TYPES.register(bus);
    }
}
