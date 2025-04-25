package dev.wolfieboy09.qtech.datagen;

import dev.wolfieboy09.qtech.registries.QTItems;
import dev.wolfieboy09.qtech.registries.tags.QTItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class QTItemTagsProvider extends ItemTagsProvider {
    public QTItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    public void addTags(@NotNull HolderLookup.Provider provider) {
      tag(QTItemTags.SILICON)
              .add(QTItems.SILICON.get());

      tag(QTItemTags.DATA_CRYSTAL)
              .add(QTItems.DATA_CRYSTAL.get());

      tag(QTItemTags.STEEL_INGOT)
              .add(QTItems.STEEL_INGOT.get());

      tag(QTItemTags.ITEM_PORT)
              .add(QTItems.ITEM_PORT.get());

      tag(QTItemTags.BASIC_CIRCUIT)
              .add(QTItems.BASIC_CIRCUIT.get());

      tag(QTItemTags.ADVANCED_CIRCUIT)
              .add(QTItems.ADVANCED_CIRCUIT.get());

      tag(QTItemTags.ELITE_CIRCUIT)
              .add(QTItems.ELITE_CIRCUIT.get());

      tag(QTItemTags.ULTIMATE_CIRCUIT)
              .add(QTItems.ULTIMATE_CIRCUIT.get());

      tag(QTItemTags.QUANTUM_CIRCUIT)
              .add(QTItems.QUANTUM_CIRCUIT.get());

      tag(QTItemTags.UPGRADES)
              .add(QTItems.MAX_ENERGY_UPGRADE.get())
              .add(QTItems.SPEED_UPGRADE.get());
    }
}
