package dev.wolfieboy09.qtech.datagen.recipes;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.datagen.builder.DiskAssemblyRecipeGen;
import dev.wolfieboy09.qtech.api.recipes.CleanRoomCondition;
import dev.wolfieboy09.qtech.api.recipes.data.disk_assembler.DiskAssemblerRecipeParams;
import dev.wolfieboy09.qtech.api.recipes.data.disk_assembler.DiskAssemblerStandardRecipe;
import dev.wolfieboy09.qtech.block.disk_assembler.NewDiskAssemblerRecipe;
import dev.wolfieboy09.qtech.registries.QTItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class QTDiskAssemblyProvider extends DiskAssemblyRecipeGen<DiskAssemblerRecipeParams, NewDiskAssemblerRecipe, DiskAssemblerStandardRecipe.Builder<NewDiskAssemblerRecipe>> {
    public QTDiskAssemblyProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, QuantiumizedTech.MOD_ID);
    }

    GeneratedRecipe
        BASIC_ITEM_DISK = create("basic_item_disk", b ->
            b.require(QTItems.ITEM_PORT.asItem())
                    .require(QTItems.STEEL_CASING.asItem())
                    .require(QTItems.STEEL_SCREW)
                    .requireExtra(QTItems.DATA_CRYSTAL.asItem())
                    .requireExtra(QTItems.BASIC_CIRCUIT.asItem())
                    .energyCost(2000)
                    .duration(120)
                    .output(QTItems.BASIC_ITEM_DISK)

    );
}
