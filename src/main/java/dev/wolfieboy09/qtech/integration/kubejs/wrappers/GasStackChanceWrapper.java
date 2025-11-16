package dev.wolfieboy09.qtech.integration.kubejs.wrappers;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.StringUtilsWrapper;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.wolfieboy09.qtech.api.recipes.result.GasStackChanceResult;
import dev.wolfieboy09.qtech.api.registry.gas.GasLike;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.registries.QTGasses;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public interface GasStackChanceWrapper {
    static GasStackChanceResult of(GasStack stack) {
        return new GasStackChanceResult(stack);
    }

    static GasStackChanceResult of(GasStack stack, double c) {
        float chance = (float) Mth.clamp(c, 0.0, 1.0);
        return new GasStackChanceResult(stack, chance);
    }

    private static GasStackChanceResult fromMapLike(Context cx, Object from, Function<String, Object> getter, boolean nested) {
        var chance = (float) Mth.clamp(StringUtilsWrapper.parseDouble(getter.apply("chance"), 1.0), 0.0, 1.0);
        if (nested) {
            var output = GasWrapper.wrap(RegistryAccessContainer.of(cx), getter.apply("output"));
            return new GasStackChanceResult(output, chance);
        } else {
            return new GasStackChanceResult(GasWrapper.wrap(RegistryAccessContainer.of(cx), from), chance);
        }
    }

    @HideFromJS
    static GasStackChanceResult wrapGasStackChance(Context cx, @Nullable Object from) {
        return switch (from) {
            case null -> GasStackChanceResult.EMPTY;
            case GasStackChanceResult id -> id;
            case GasStack s -> s.isEmpty() ? GasStackChanceResult.EMPTY : new GasStackChanceResult(s);
            case GasLike i when i.asGas() == QTGasses.EMPTY -> GasStackChanceResult.EMPTY;
            case GasLike i -> new GasStackChanceResult(new GasStack(i.asGas(), 1000));
            case JsonObject json when json.has("chance") -> fromMapLike(cx, json, json::get, json.has("output"));
            case Map<?, ?> map when map.containsKey("chance") -> fromMapLike(cx, map, map::get, map.containsKey("output"));
            default -> new GasStackChanceResult(GasWrapper.wrap(RegistryAccessContainer.of(cx), from));
        };
    }
}
