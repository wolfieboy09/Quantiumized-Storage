package dev.wolfieboy09.qstorage.intergration.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import dev.wolfieboy09.qstorage.api.util.ColorUtil;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.intergration.kubejs.bindings.MobEffectInstanceBinding;
import dev.wolfieboy09.qstorage.intergration.kubejs.builders.KubeGasBuilder;
import dev.wolfieboy09.qstorage.intergration.kubejs.schemas.DiskAssemblySchema;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.NotNull;

public class QSKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(@NotNull RecipeSchemaRegistry registry) {
        registry.namespace(QuantiumizedStorage.MOD_ID);
        registry.register(locate("disk_assembly"), DiskAssemblySchema.SCHEMA);
    }

    @Override
    public void registerBuilderTypes(@NotNull BuilderTypeRegistry registry) {
        registry.of(QSRegistries.GAS_REGISTRY_KEY, reg -> {
            reg.addDefault(KubeGasBuilder.class, KubeGasBuilder::new);
            reg.add(locate("gas"), KubeGasBuilder.class, KubeGasBuilder::new);
        });
    }

    @Override
    public void registerServerRegistries(@NotNull ServerRegistryRegistry registry) {
        registry.register(QSRegistries.GAS_REGISTRY_KEY, Gas.CODEC, Gas.class);
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("ColorUtil", ColorUtil.class);
        bindings.add("MobEffectInstance", MobEffectInstanceBinding.class);
        bindings.add("MobEffects", MobEffects.class);
    }

    private @NotNull ResourceLocation locate(String id) {
        return ResourceHelper.asResource(id);
    }
}
