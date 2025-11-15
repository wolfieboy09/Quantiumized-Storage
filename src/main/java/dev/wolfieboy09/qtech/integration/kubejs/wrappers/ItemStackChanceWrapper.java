package dev.wolfieboy09.qtech.integration.kubejs.wrappers;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.StringUtilsWrapper;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.result.ItemStackChanceResult;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

@NothingNullByDefault
public interface ItemStackChanceWrapper {
    static ItemStackChanceResult of(ItemStack stack) {
        return new ItemStackChanceResult(stack);
    }

    static ItemStackChanceResult of(ItemStack stack, double c) {
        float chance = (float) Mth.clamp(c, 0.0, 1.0);
        return new ItemStackChanceResult(stack, chance);
    }

    private static ItemStackChanceResult fromMapLike(Context cx, Object from, Function<String, Object> getter, boolean nested) {
        var chance = (float) Mth.clamp(StringUtilsWrapper.parseDouble(getter.apply("chance"), 1.0), 0.0, 1.0);
        if (nested) {
            var output = ItemWrapper.wrap(cx, getter.apply("output"));
            return new ItemStackChanceResult(output, chance);
        } else {
            return new ItemStackChanceResult(ItemWrapper.wrap(cx, from), chance);
        }
    }

    @HideFromJS
    static ItemStackChanceResult wrapItemStackChance(Context cx, @Nullable Object from) {
        return switch (from) {
            case null -> ItemStackChanceResult.EMPTY;
            case ItemStackChanceResult id -> id;
            case ItemStack s -> s.isEmpty() ? ItemStackChanceResult.EMPTY : new ItemStackChanceResult(s);
            case ItemLike i when i.asItem() == Items.AIR -> ItemStackChanceResult.EMPTY;
            case ItemLike i -> new ItemStackChanceResult(new ItemStack(i.asItem()));
            case JsonObject json when json.has("chance") -> fromMapLike(cx, json, json::get, json.has("output"));
            case Map<?, ?> map when map.containsKey("chance") -> fromMapLike(cx, map, map::get, map.containsKey("output"));
            default -> new ItemStackChanceResult(ItemWrapper.wrap(cx, from));
        };
    }
}
