package dev.wolfieboy09.qtech.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.gas.crafting.GasIngredient;
import dev.wolfieboy09.qtech.api.gas.crafting.SizedGasIngredient;
import dev.wolfieboy09.qtech.api.recipes.CleanroomCondition;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasStack;
import dev.wolfieboy09.qtech.api.util.ColorUtil;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.integration.kubejs.builders.KubeGasBuilder;
import dev.wolfieboy09.qtech.integration.kubejs.datagen.KJSSmelteryFuelDataGeneration;
import dev.wolfieboy09.qtech.integration.kubejs.events.QTKubeEvents;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.QTRecipeSchema;
import dev.wolfieboy09.qtech.integration.kubejs.recipes.schemas.SmelterySchema;
import dev.wolfieboy09.qtech.integration.kubejs.wrappers.GasWrapper;
import net.minecraft.resources.ResourceLocation;

@NothingNullByDefault
public class QTKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.namespace(QuantiumizedTech.MOD_ID);
        //registry.register(locate("disk_assembly"), QTRecipeSchema.DISK_ASSEMBLY);
        registry.register(locate("smeltery"), SmelterySchema.SCHEMA);
    }

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(QTRegistries.GAS_KEY, reg -> {
            reg.addDefault(KubeGasBuilder.class, KubeGasBuilder::new);
            reg.add(locate("gas"), KubeGasBuilder.class, KubeGasBuilder::new);
        });
    }

    @Override
    public void registerServerRegistries(ServerRegistryRegistry registry) {
        registry.register(QTRegistries.GAS_KEY, Gas.CODEC, Gas.class);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(QTKubeEvents.GROUP);
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("ColorUtil", ColorUtil.class);
        bindings.add("CleanroomCondition", CleanroomCondition.class);
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        KJSSmelteryFuelDataGeneration.generateData(generator);
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
        registry.register(GasStack.class, GasWrapper::wrap);
        registry.register(GasIngredient.class, GasWrapper::wrapIngredient);
        registry.register(SizedGasIngredient.class, GasWrapper::wrapSizedIngredient);
    }

    private ResourceLocation locate(String id) {
        return ResourceHelper.asResource(id);
    }
}
