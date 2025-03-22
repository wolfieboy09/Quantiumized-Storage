package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.datagen.GasTagProvider;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class QSGasTagsProvider extends GasTagProvider {
    protected QSGasTagsProvider(PackOutput output,  CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, QSRegistries.GAS_REGISTRY_KEY, lookupProvider, QuantiumizedStorage.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
//        tag(QSTags.FLAMMABLE)
//                .add(QSGasses.HYDROGEN.get());
    }
}
