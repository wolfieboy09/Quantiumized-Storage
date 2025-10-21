package dev.wolfieboy09.qtech.integration.kubejs.datagen;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.script.data.GeneratedData;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.integration.kubejs.events.QTDataMapEvent;
import dev.wolfieboy09.qtech.integration.kubejs.events.QTKubeEvents;
import dev.wolfieboy09.qtech.integration.kubejs.events.SmelteryFuelMapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class KJSSmelteryFuelDataGeneration {
    public static void generateData(KubeDataGenerator generator) {
        if (!QTKubeEvents.DATA_MAP_EVENT.hasListeners()) {
            return;
        }

        JsonObject itemJson = new JsonObject();
        JsonObject fluidJson = new JsonObject();

        clearMappings();

        QTKubeEvents.DATA_MAP_EVENT.post(new QTDataMapEvent());

        for (Map.Entry<Item, SmelteryFuelMapper> entry : QTDataMapEvent.SMELTERY_DATA_MAP.left().entrySet()) {
            JsonObject json = new JsonObject();
            JsonObject data = new JsonObject();
            SmelteryFuelMapper wrapper = entry.getValue();

            data.addProperty("burn_time", wrapper.burnTime());
            data.addProperty("temperature", wrapper.temperature());

            json.add("value", data);
            if (wrapper.replace()) {
                json.addProperty("replace", true);
            }
            itemJson.add(entry.getKey().kjs$getId(), json);

        }

        for (Map.Entry<Fluid, SmelteryFuelMapper> entry : QTDataMapEvent.SMELTERY_DATA_MAP.right().entrySet()) {
            JsonObject json = new JsonObject();
            JsonObject data = new JsonObject();
            SmelteryFuelMapper wrapper = entry.getValue();

            data.addProperty("burn_time", wrapper.burnTime());
            data.addProperty("temperature", wrapper.temperature());

            json.add("value", data);
            if (wrapper.replace()) {
                json.addProperty("replace", true);
            }
            itemJson.add(entry.getKey().kjs$getId(), json);
        }

        if (!itemJson.isEmpty()) {
            JsonObject json = new JsonObject();
            json.add("values", itemJson);
            generator.add(GeneratedData.json(locate("data_maps/item/smeltery_fuel.json"), () -> json));
        }

        if (!fluidJson.isEmpty()) {
            JsonObject json = new JsonObject();
            json.add("values", fluidJson);
            generator.add(GeneratedData.json(locate("data_maps/fluid/smeltery_fuel.json"), () -> json));
        }

        clearMappings();
    }

    private static void clearMappings() {
        QTDataMapEvent.SMELTERY_DATA_MAP.left().clear();
        QTDataMapEvent.SMELTERY_DATA_MAP.right().clear();
    }

    private static @NotNull ResourceLocation locate(String id) {
        return ResourceHelper.asResource(id);
    }
}
