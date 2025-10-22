package dev.wolfieboy09.qtech.datagen;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = QuantiumizedTech.MOD_ID)
public class DataGeneration {
    @SubscribeEvent
    public static void gatherData(@NotNull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        QTBlockTagsProvider blockTagsProvider = new QTBlockTagsProvider(output, lookupProvider, QuantiumizedTech.MOD_ID, existingFileHelper);

        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeClient(), new QTLangProvider(output));
        generator.addProvider(event.includeClient(), new QTBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new QTItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeServer(), new QTDataMapProvider(output, lookupProvider));
        //generator.addProvider(event.includeServer(), new QTGasTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new QTItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter()));
        generator.addProvider(event.includeServer(), new QTRecipeProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new QTMultiblockProvider(output));
    }
}
