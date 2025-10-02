package dev.wolfieboy09.qtech.integration.kubejs.events;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.wolfieboy09.qtech.api.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.IdentityHashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class QTDataMapEvent implements KubeEvent {
    @HideFromJS
    public static final Pair<Map<Item, SmelteryFuelMapper>, Map<Fluid, SmelteryFuelMapper>> SMELTERY_DATA_MAP = new Pair<>(
            new IdentityHashMap<>(),
            new IdentityHashMap<>()
    );

    public void registerItem(ResourceLocation resourceLocation, int burnTime, int temperature) {
        Item item = BuiltInRegistries.ITEM.get(resourceLocation);
        if (!SMELTERY_DATA_MAP.left().containsKey(item)) {
            SMELTERY_DATA_MAP.left().put(item, new SmelteryFuelMapper(burnTime, temperature, false));
        }
    }

    public void registerFluid(ResourceLocation resourceLocation, int burnTime, int temperature) {
        Fluid fluid = BuiltInRegistries.FLUID.get(resourceLocation);
        if (!SMELTERY_DATA_MAP.right().containsKey(fluid)) {
            SMELTERY_DATA_MAP.right().put(fluid, new SmelteryFuelMapper(burnTime, temperature, false));
        }
    }

    public void registerItem(ResourceLocation resourceLocation, int burnTime, int temperature, boolean replace) {
        Item item = BuiltInRegistries.ITEM.get(resourceLocation);
        if (!SMELTERY_DATA_MAP.left().containsKey(item)) {
            SMELTERY_DATA_MAP.left().put(item, new SmelteryFuelMapper(burnTime, temperature, replace));
        }
    }

    public void registerFluid(ResourceLocation resourceLocation, int burnTime, int temperature, boolean replace) {
        Fluid fluid = BuiltInRegistries.FLUID.get(resourceLocation);
        if (!SMELTERY_DATA_MAP.right().containsKey(fluid)) {
            SMELTERY_DATA_MAP.right().put(fluid, new SmelteryFuelMapper(burnTime, temperature, replace));
        }
    }
}
