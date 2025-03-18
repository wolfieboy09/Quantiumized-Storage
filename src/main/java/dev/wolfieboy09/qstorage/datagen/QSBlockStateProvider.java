package dev.wolfieboy09.qstorage.datagen;

import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerBlock;
import dev.wolfieboy09.qstorage.block.smeltery.SmelteryBlock;
import dev.wolfieboy09.qstorage.block.storage_matrix.StorageMatrixBlock;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class QSBlockStateProvider extends BlockStateProvider {
    public QSBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, QuantiumizedStorage.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(QSBlocks.DISK_ASSEMBLER.get())
                .forAllStates(state -> {
                    Direction facing = state.getValue(DiskAssemblerBlock.FACING);
                    int rotation = switch (facing) {
                        case EAST -> 90;
                        case SOUTH -> 180;
                        case WEST -> 270;
                        default -> 0;
                    };
                    return ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(modLoc("block/disk_assembler")))
                            .rotationY(rotation)
                            .build();
                });

        getVariantBuilder(QSBlocks.STORAGE_MATRIX.get())
                .forAllStates(state -> {
                    Direction facing = state.getValue(StorageMatrixBlock.FACING);
                    int rotation = switch (facing) {
                        case EAST -> 90;
                        case SOUTH -> 180;
                        case WEST -> 270;
                        default -> 0;
                    };
                    return ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(modLoc("block/storage_matrix")))
                            .rotationY(rotation)
                            .build();
                });

        getVariantBuilder(QSBlocks.SMELTERY.get())
                .forAllStates(state -> {
                    Direction facing = state.getValue(SmelteryBlock.FACING);
                    int rotation = switch (facing) {
                        case EAST -> 90;
                        case SOUTH -> 180;
                        case WEST -> 270;
                        default -> 0;
                    };
                    return ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(modLoc("block/smeltery")))
                            .rotationY(rotation)
                            .build();
                });
    }
}
