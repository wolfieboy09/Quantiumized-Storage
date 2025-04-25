package dev.wolfieboy09.qtech.datagen;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.datagen.GasTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class QTGasTagsProvider extends GasTagProvider {
    public QTGasTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, QuantiumizedTech.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
//        tag(QTGasTags.FLAMMABLE)
//                .add(QTGasses.HYDROGEN.get());
//
//        tag(QTGasTags.OXYGEN_DEPRIVATION)
//                .add(QTGasses.HYDROGEN.get());
    }
}
