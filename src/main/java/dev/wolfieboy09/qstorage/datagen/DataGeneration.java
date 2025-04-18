package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = QuantiumizedStorage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGeneration {
    @SubscribeEvent
    public static void gatherData(@NotNull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        QSBlockTagsProvider blockTagsProvider = new QSBlockTagsProvider(output, lookupProvider, QuantiumizedStorage.MOD_ID, existingFileHelper);

        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeClient(), new QSLangProvider(output));
        generator.addProvider(event.includeClient(), new QSBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new QSItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeServer(), new QSDataMapProvider(output, lookupProvider));
        //generator.addProvider(event.includeServer(), new QSGasTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new QSItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter()));
        generator.addProvider(event.includeServer(), new QSRecipeProvider(output, lookupProvider));

    }
}
