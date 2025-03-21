package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.registries.QSItems;
import dev.wolfieboy09.qstorage.registries.tags.QSTags;
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
      tag(QSTags.SILICON)
              .add(QSItems.SILICON.get());

      tag(QSTags.DATA_CRYSTAL)
              .add(QSItems.DATA_CRYSTAL.get());

      tag(QSTags.STEEL_INGOT)
              .add(QSItems.STEEL_INGOT.get());

      tag(QSTags.ITEM_PORT)
              .add(QSItems.ITEM_PORT.get());

      tag(QSTags.BASIC_CIRCUIT)
              .add(QSItems.BASIC_CIRCUIT.get());

      tag(QSTags.ADVANCED_CIRCUIT)
              .add(QSItems.ADVANCED_CIRCUIT.get());

      tag(QSTags.ELITE_CIRCUIT)
              .add(QSItems.ELITE_CIRCUIT.get());

      tag(QSTags.ULTIMATE_CIRCUIT)
              .add(QSItems.ULTIMATE_CIRCUIT.get());

      tag(QSTags.QUANTUM_CIRCUIT)
              .add(QSItems.QUANTUM_CIRCUIT.get());

      tag(QSTags.UPGRADES)
              .add(QSItems.MAX_ENERGY_UPGRADE.get())
              .add(QSItems.SPEED_UPGRADE.get());
    }
}
