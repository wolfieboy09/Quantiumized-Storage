package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.datagen.GasTagProvider;
import dev.wolfieboy09.qstorage.registries.QSGasses;
import dev.wolfieboy09.qstorage.registries.tags.QSGasTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class QSGasTagsProvider extends GasTagProvider {
    public QSGasTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, QuantiumizedStorage.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
//        tag(QSGasTags.FLAMMABLE)
//                .add(QSGasses.HYDROGEN.get());
//
//        tag(QSGasTags.OXYGEN_DEPRIVATION)
//                .add(QSGasses.HYDROGEN.get());
    }
}
