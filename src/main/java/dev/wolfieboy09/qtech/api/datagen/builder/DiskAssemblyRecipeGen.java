package dev.wolfieboy09.qtech.api.datagen.builder;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.datagen.framework.ProcessingRecipeGen;
import dev.wolfieboy09.qtech.api.recipes.IRecipeTypeInfo;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipe;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeBuilder;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeParams;
import dev.wolfieboy09.qtech.api.recipes.data.disk_assembler.DiskAssemblerRecipeParams;
import dev.wolfieboy09.qtech.api.recipes.data.disk_assembler.DiskAssemblerStandardRecipe;
import dev.wolfieboy09.qtech.block.disk_assembler.NewDiskAssemblerRecipe;
import dev.wolfieboy09.qtech.registries.QTRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

@NothingNullByDefault
public abstract class DiskAssemblyRecipeGen<P extends ProcessingRecipeParams, R extends ProcessingRecipe<?, P>, B extends ProcessingRecipeBuilder<P, R, B>> extends ProcessingRecipeGen<DiskAssemblerRecipeParams, NewDiskAssemblerRecipe, DiskAssemblerStandardRecipe.Builder<NewDiskAssemblerRecipe>> {
    public DiskAssemblyRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String namespace) {
        super(output, registries, namespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return QTRecipeTypes.DISK_ASSEMBLY;
    }

    @Override
    protected DiskAssemblerStandardRecipe.Builder<NewDiskAssemblerRecipe> getBuilder(ResourceLocation id) {
        return new DiskAssemblerStandardRecipe.Builder<>(NewDiskAssemblerRecipe::new, id);
    }
}
