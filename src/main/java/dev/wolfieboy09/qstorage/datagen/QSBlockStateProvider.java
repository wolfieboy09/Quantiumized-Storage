package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerBlock;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlock;
import dev.wolfieboy09.qstorage.block.pipe.ConnectionType;
import dev.wolfieboy09.qstorage.block.smeltery.SmelteryBlock;
import dev.wolfieboy09.qstorage.block.storage_matrix.StorageMatrixBlock;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.util.Tuple;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

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


        MultiPartBlockStateBuilder multiPartBuilder = getMultipartBuilder(QSBlocks.ITEM_PIPE.get())
                .part()
                .modelFile(existingModelFile("block/pipe_dot"))
                .addModel()
                .end();
        Map.of(
                BasePipeBlock.NORTH, new Tuple<>(0, 0),
                BasePipeBlock.EAST, new Tuple<>(0, 90),
                BasePipeBlock.SOUTH, new Tuple<>(0, 180),
                BasePipeBlock.WEST, new Tuple<>(0, 270),
                BasePipeBlock.UP, new Tuple<>(270, 0),
                BasePipeBlock.DOWN, new Tuple<>(90, 0)
        ).forEach((property, rotations) -> {
            multiPartBuilder
                    .part()
                    .modelFile(existingModelFile("block/pipe"))
                    .rotationX(rotations.getA())
                    .rotationY(rotations.getB())
                    .addModel()
                    .condition(property, ConnectionType.PIPE)
                    .end()
                    .part()
                    .modelFile(existingModelFile("block/pipe_connected"))
                    .rotationX(rotations.getA())
                    .rotationY(rotations.getB())
                    .addModel()
                    .condition(property, ConnectionType.BLOCK)
                    .end();
        });
    }

    private ModelFile.@NotNull ExistingModelFile existingModelFile(String path) {
        return models().getExistingFile(ResourceHelper.asResource(path));
    }
}
