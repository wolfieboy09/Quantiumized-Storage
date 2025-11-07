package dev.wolfieboy09.qtech.api.datagen.framework;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeParams;
import dev.wolfieboy09.qtech.api.recipes.StandardProcessingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

@NothingNullByDefault
public abstract class StandardProcessingRecipeGen<R extends StandardProcessingRecipe<?>> extends ProcessingRecipeGen<ProcessingRecipeParams, R, StandardProcessingRecipe.Builder<R>> {
    public StandardProcessingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
    }

    protected StandardProcessingRecipe.Serializer<R> getSerializer() {
        return getRecipeType().getSerializer();
    }

    @Override
    protected StandardProcessingRecipe.Builder<R> getBuilder(ResourceLocation id) {
        return new StandardProcessingRecipe.Builder<>(getSerializer().factory(), id);
    }
}