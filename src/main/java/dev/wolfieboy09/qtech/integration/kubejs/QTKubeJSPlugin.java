package dev.wolfieboy09.qtech.integration.kubejs;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.data.GeneratedData;
import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.util.ColorUtil;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.integration.kubejs.bindings.MobEffectInstanceBinding;
import dev.wolfieboy09.qtech.integration.kubejs.builders.KubeGasBuilder;
import dev.wolfieboy09.qtech.integration.kubejs.events.QTDataMapEvent;
import dev.wolfieboy09.qtech.integration.kubejs.events.QTKubeEvents;
import dev.wolfieboy09.qtech.integration.kubejs.events.SmelteryFuelMapper;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.QTRecipeSchema;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.schemas.DiskAssemblySchema;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.schemas.SmelterySchema;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class QTKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(@NotNull RecipeSchemaRegistry registry) {
        registry.namespace(QuantiumizedTech.MOD_ID);
        registry.register(locate("disk_assembly"), QTRecipeSchema.DISK_ASSEMBLY);
        registry.register(locate("smeltery"), SmelterySchema.SCHEMA);
    }

    @Override
    public void registerBuilderTypes(@NotNull BuilderTypeRegistry registry) {
        registry.of(QTRegistries.GAS_KEY, reg -> {
            reg.addDefault(KubeGasBuilder.class, KubeGasBuilder::new);
            reg.add(locate("gas"), KubeGasBuilder.class, KubeGasBuilder::new);
        });
    }

    @Override
    public void registerServerRegistries(@NotNull ServerRegistryRegistry registry) {
        registry.register(QTRegistries.GAS_KEY, Gas.CODEC, Gas.class);
    }

    @Override
    public void registerEvents(@NotNull EventGroupRegistry registry) {
        registry.register(QTKubeEvents.GROUP);
    }

    @Override
    public void registerBindings(@NotNull BindingRegistry bindings) {
        bindings.add("ColorUtil", ColorUtil.class);
        bindings.add("MobEffectInstance", MobEffectInstanceBinding.class);
        bindings.add("MobEffects", MobEffects.class);
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        JsonObject itemJson = new JsonObject();
        JsonObject fluidJson = new JsonObject();

        clearMappings();

        if (!QTKubeEvents.DATA_MAP_EVENT.hasListeners()) {
            return;
        }

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

    private void clearMappings() {
        QTDataMapEvent.SMELTERY_DATA_MAP.left().clear();
        QTDataMapEvent.SMELTERY_DATA_MAP.right().clear();
    }

    private @NotNull ResourceLocation locate(String id) {
        return ResourceHelper.asResource(id);
    }
}
