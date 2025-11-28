package dev.wolfieboy09.qtech.datagen.recipes;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.datagen.builder.VoidCraftingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class QTVoidCraftingProvider extends VoidCraftingRecipeGen {
    public QTVoidCraftingProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, QuantiumizedTech.MOD_ID);
    }
}
