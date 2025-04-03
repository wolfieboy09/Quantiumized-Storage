package dev.wolfieboy09.qstorage.integration.kubejs;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.core.RegistryObjectKJS;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.item.ItemModificationKubeEvent;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.data.GeneratedData;
import dev.latvian.mods.kubejs.script.data.GeneratedDataStage;
import dev.latvian.mods.kubejs.script.data.VirtualDataPack;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.datamaps.SmelteryFuel;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import dev.wolfieboy09.qstorage.api.util.ColorUtil;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.integration.kubejs.bindings.MobEffectInstanceBinding;
import dev.wolfieboy09.qstorage.integration.kubejs.builders.KubeGasBuilder;
import dev.wolfieboy09.qstorage.integration.kubejs.events.QSDataMapEvent;
import dev.wolfieboy09.qstorage.integration.kubejs.events.QSKubeEvents;
import dev.wolfieboy09.qstorage.integration.kubejs.schemas.DiskAssemblySchema;
import dev.wolfieboy09.qstorage.integration.kubejs.schemas.SmelterySchema;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class QSKubeJSPlugin implements KubeJSPlugin {


    @Override
    public void registerRecipeSchemas(@NotNull RecipeSchemaRegistry registry) {
        registry.namespace(QuantiumizedStorage.MOD_ID);
        registry.register(locate("disk_assembly"), DiskAssemblySchema.SCHEMA);
        registry.register(locate("smeltery"), SmelterySchema.SCHEMA);
    }

    @Override
    public void registerBuilderTypes(@NotNull BuilderTypeRegistry registry) {
        registry.of(QSRegistries.GAS_KEY, reg -> {
            reg.addDefault(KubeGasBuilder.class, KubeGasBuilder::new);
            reg.add(locate("gas").toString(), KubeGasBuilder.class, KubeGasBuilder::new);
        });
    }

    @Override
    public void registerServerRegistries(@NotNull ServerRegistryRegistry registry) {
        registry.register(QSRegistries.GAS_KEY, Gas.CODEC, Gas.class);
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        JsonObject itemJson = new JsonObject();
        JsonObject fluidJson = new JsonObject();

        clearMappings();

        if (!QSKubeEvents.DATA_MAP_EVENT.hasListeners()) {
            return;
        }

        QSKubeEvents.DATA_MAP_EVENT.post(new QSDataMapEvent());

        for (Map.Entry<Item, SmelteryFuel> entry : QSDataMapEvent.SMELTERY_DATA_MAP.left().entrySet()) {
            JsonObject json = new JsonObject();
            SmelteryFuel value = entry.getValue();
            json.addProperty("burn_time",value.burnTime());
            json.addProperty("temperature", value.temperature());
            itemJson.add(entry.getKey().kjs$getId(), json);
        }

        for (Map.Entry<Fluid, SmelteryFuel> entry : QSDataMapEvent.SMELTERY_DATA_MAP.right().entrySet()) {
            JsonObject json = new JsonObject();
            SmelteryFuel value = entry.getValue();
            json.addProperty("burn_time",value.burnTime());
            json.addProperty("temperature", value.temperature());
            fluidJson.add(entry.getKey().kjs$getId(), json);
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

        System.out.println("--- ITEM JSON ---");
        System.out.println(itemJson);
        System.out.println("--- FLUID JSON ---");
        System.out.println(fluidJson);

        clearMappings();
    }

    private void clearMappings() {
        QSDataMapEvent.SMELTERY_DATA_MAP.left().clear();
        QSDataMapEvent.SMELTERY_DATA_MAP.right().clear();
    }

    @Override
    public void registerBindings(@NotNull BindingRegistry bindings) {
        bindings.add("ColorUtil", ColorUtil.class);
        bindings.add("MobEffectInstance", MobEffectInstanceBinding.class);
        bindings.add("MobEffects", MobEffects.class);
    }

    private @NotNull ResourceLocation locate(String id) {
        return ResourceHelper.asResource(id);
    }
}
