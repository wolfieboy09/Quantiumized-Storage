package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.registries.QSItems;
import dev.wolfieboy09.qstorage.registries.tags.QSItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class QSItemTagsProvider extends ItemTagsProvider {
    public QSItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    public void addTags(@NotNull HolderLookup.Provider provider) {
      tag(QSItemTags.SILICON)
              .add(QSItems.SILICON.get());

      tag(QSItemTags.DATA_CRYSTAL)
              .add(QSItems.DATA_CRYSTAL.get());

      tag(QSItemTags.STEEL_INGOT)
              .add(QSItems.STEEL_INGOT.get());

      tag(QSItemTags.ITEM_PORT)
              .add(QSItems.ITEM_PORT.get());

      tag(QSItemTags.BASIC_CIRCUIT)
              .add(QSItems.BASIC_CIRCUIT.get());

      tag(QSItemTags.ADVANCED_CIRCUIT)
              .add(QSItems.ADVANCED_CIRCUIT.get());

      tag(QSItemTags.ELITE_CIRCUIT)
              .add(QSItems.ELITE_CIRCUIT.get());

      tag(QSItemTags.ULTIMATE_CIRCUIT)
              .add(QSItems.ULTIMATE_CIRCUIT.get());

      tag(QSItemTags.QUANTUM_CIRCUIT)
              .add(QSItems.QUANTUM_CIRCUIT.get());

      tag(QSItemTags.UPGRADES)
              .add(QSItems.MAX_ENERGY_UPGRADE.get())
              .add(QSItems.SPEED_UPGRADE.get());
    }
}
