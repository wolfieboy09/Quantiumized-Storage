package dev.wolfieboy09.qstorage.intergration.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.intergration.kubejs.schemas.DiskAssemblySchema;
import net.minecraft.resources.ResourceLocation;

public class QSKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.namespace(QuantiumizedStorage.MOD_ID);
        registry.register(locate("disk_assembly"), DiskAssemblySchema.SCHEMA);
    }

    private ResourceLocation locate(String id) {
        return ResourceLocation.fromNamespaceAndPath(QuantiumizedStorage.MOD_ID, id);
    }
}
