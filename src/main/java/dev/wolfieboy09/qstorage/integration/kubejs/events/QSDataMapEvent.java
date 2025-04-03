package dev.wolfieboy09.qstorage.integration.kubejs.events;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.wolfieboy09.qstorage.api.BiHolder;
import dev.wolfieboy09.qstorage.api.datamaps.SmelteryFuel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.IdentityHashMap;
import java.util.Map;

public class QSDataMapEvent implements KubeEvent {
    @HideFromJS
    public static final BiHolder<Map<Item, SmelteryFuel>, Map<Fluid, SmelteryFuel>> SMELTERY_DATA_MAP = new BiHolder<>(
            new IdentityHashMap<>(),
            new IdentityHashMap<>()
    );

    public void registerItem(ResourceLocation item, int burnTime, int temperature) {
        Item dingleBerry = BuiltInRegistries.ITEM.get(item);
        if (!SMELTERY_DATA_MAP.left().containsKey(dingleBerry)) {
            SMELTERY_DATA_MAP.left().put(dingleBerry, new SmelteryFuel(burnTime, temperature));
        }
    }

    public void registerFluid(ResourceLocation fluid, int burnTime, int temperature) {
        Fluid dingleBerry = BuiltInRegistries.FLUID.get(fluid);
        if (!SMELTERY_DATA_MAP.right().containsKey(dingleBerry)) {
            SMELTERY_DATA_MAP.right().put(dingleBerry, new SmelteryFuel(burnTime, temperature));
        }
    }
}
