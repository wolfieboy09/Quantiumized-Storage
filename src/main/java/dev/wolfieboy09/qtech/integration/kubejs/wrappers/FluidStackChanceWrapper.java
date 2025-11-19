package dev.wolfieboy09.qtech.integration.kubejs.wrappers;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.fluid.FluidLike;
import dev.latvian.mods.kubejs.fluid.FluidWrapper;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.StringUtilsWrapper;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.result.FluidStackChanceResult;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

@NothingNullByDefault
public interface FluidStackChanceWrapper {
    static FluidStackChanceResult of(FluidStack stack) {
        return new FluidStackChanceResult(stack);
    }

    static FluidStackChanceResult of(FluidStack stack, double c) {
        float chance = (float) Mth.clamp(c, 0.0, 1.0);
        return new FluidStackChanceResult(stack, chance);
    }

    private static FluidStackChanceResult fromMapLike(Context cx, Object from, Function<String, Object> getter, boolean nested) {
        var chance = (float) Mth.clamp(StringUtilsWrapper.parseDouble(getter.apply("chance"), 1.0), 0.0, 1.0);
        if (nested) {
            var output = FluidWrapper.wrap(cx, getter.apply("output"));
            return new FluidStackChanceResult(output, chance);
        } else {
            return new FluidStackChanceResult(FluidWrapper.wrap(cx, from), chance);
        }
    }

    @HideFromJS
    static FluidStackChanceResult wrapFluidStackChance(Context cx, @Nullable Object from) {
        return switch (from) {
            case null -> FluidStackChanceResult.EMPTY;
            case FluidStackChanceResult id -> id;
            case FluidStack s -> s.isEmpty() ? FluidStackChanceResult.EMPTY : new FluidStackChanceResult(s);
            case FluidLike i when i.kjs$getFluid() == Fluids.EMPTY -> FluidStackChanceResult.EMPTY;
            case FluidLike i -> new FluidStackChanceResult(new FluidStack(i.kjs$getFluid(), 1000));
            case JsonObject json when json.has("chance") -> fromMapLike(cx, json, json::get, json.has("output"));
            case Map<?, ?> map when map.containsKey("chance") -> fromMapLike(cx, map, map::get, map.containsKey("output"));
            default -> new FluidStackChanceResult(FluidWrapper.wrap(cx, from));
        };
    }
}
