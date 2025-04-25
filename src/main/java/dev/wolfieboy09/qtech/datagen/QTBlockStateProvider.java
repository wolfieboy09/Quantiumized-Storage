package dev.wolfieboy09.qtech.datagen;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.block.disk_assembler.DiskAssemblerBlock;
import dev.wolfieboy09.qtech.block.pipe.BasePipeBlock;
import dev.wolfieboy09.qtech.block.pipe.ConnectionType;
import dev.wolfieboy09.qtech.block.smeltery.SmelteryBlock;
import dev.wolfieboy09.qtech.block.storage_matrix.StorageMatrixBlock;
import dev.wolfieboy09.qtech.registries.QTBlocks;
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

public class QTBlockStateProvider extends BlockStateProvider {
    public QTBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, QuantiumizedTech.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(QTBlocks.DISK_ASSEMBLER.get())
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

        getVariantBuilder(QTBlocks.STORAGE_MATRIX.get())
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

        getVariantBuilder(QTBlocks.SMELTERY.get())
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


        MultiPartBlockStateBuilder multiPartBuilder = getMultipartBuilder(QTBlocks.ITEM_PIPE.get())
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
                    // Normal pipe to pipe
                    .modelFile(existingModelFile("block/pipe"))
                    .rotationX(rotations.getA())
                    .rotationY(rotations.getB())
                    .addModel()
                    .condition(property, ConnectionType.PIPE)
                    .end()
                    // Pipe to block extract state
                    .part()
                    .modelFile(existingModelFile("block/pipe_connected"))
                    .rotationX(rotations.getA())
                    .rotationY(rotations.getB())
                    .addModel()
                    .condition(property, ConnectionType.BLOCK_EXTRACT)
                    .end()
                    // Pipe to block normal
                    .part()
                    .modelFile(existingModelFile("block/pipe"))
                    .rotationX(rotations.getA())
                    .rotationY(rotations.getB())
                    .addModel()
                    .condition(property, ConnectionType.BLOCK_NORMAL)
                    .end();
        });
    }

    private ModelFile.@NotNull ExistingModelFile existingModelFile(String path) {
        return models().getExistingFile(ResourceHelper.asResource(path));
    }
}
