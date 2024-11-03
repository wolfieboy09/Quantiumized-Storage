package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.registries.QSBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class QSBlockTagsProvider extends BlockTagsProvider {
    public QSBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    public void addTags(@NotNull HolderLookup.Provider provider) {
        tag(Tags.Blocks.NEEDS_WOOD_TOOL)
                .add(QSBlocks.DISK_ASSEMBLER.get())
                .add(QSBlocks.STORAGE_MATRIX.get());
    }
}
