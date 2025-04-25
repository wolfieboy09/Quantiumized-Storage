package dev.wolfieboy09.qtech.datagen;

import dev.wolfieboy09.qtech.registries.QTBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class QTBlockTagsProvider extends BlockTagsProvider {
    public QTBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    public void addTags(@NotNull HolderLookup.Provider provider) {
        tag(Tags.Blocks.NEEDS_WOOD_TOOL)
                .add(QTBlocks.DISK_ASSEMBLER.get())
                .add(QTBlocks.STORAGE_MATRIX.get());
    }
}
