package dev.wolfieboy09.qtech.datagen;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.multiblock.blocks.controller.BaseMultiblockController;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class QTBlockStateProvider extends BlockStateProvider {
    public QTBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, QuantiumizedTech.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        fourRotationBlock(QTBlocks.DISK_ASSEMBLER, DiskAssemblerBlock.FACING, "disk_assembler");
        fourRotationBlock(QTBlocks.STORAGE_MATRIX, StorageMatrixBlock.FACING, "storage_matrix");
        fourRotationBlock(QTBlocks.SMELTERY, SmelteryBlock.FACING, "smeltery");

        createMultiblockController(QTBlocks.CENTRIFUGE_CONTROLLER,"centrifuge_controller");

        getVariantBuilder(QTBlocks.GAS_CANISTER.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(existingModelFile("block/gas_canister")).build());

        createPipeModel(QTBlocks.ITEM_PIPE);
        createPipeModel(QTBlocks.FLUID_PIPE);
        createPipeModel(QTBlocks.ENERGY_PIPE);
    }

    @CanIgnoreReturnValue
    private @NotNull VariantBlockStateBuilder createMultiblockController(DeferredBlock<? extends BaseMultiblockController> block, String name) {
        return fourRotationBlock(block, BaseMultiblockController.FACING, name);
    }

    @CanIgnoreReturnValue
    private @NotNull VariantBlockStateBuilder fourRotationBlock(@NotNull DeferredBlock<?> block, DirectionProperty property, String name) {
        return getVariantBuilder(block.get())
                .forAllStates(state -> {
                    Direction facing = state.getValue(property);
                    int rotation = switch (facing) {
                        case EAST -> 90;
                        case SOUTH -> 180;
                        case WEST -> 270;
                        default -> 0;
                    };
                    return ConfiguredModel.builder()
                            .modelFile(existingModelFile("block/" + name))
                            .rotationY(rotation)
                            .build();
                });
    }

    @CanIgnoreReturnValue
    private @NotNull MultiPartBlockStateBuilder createPipeModel(@NotNull DeferredBlock<? extends BasePipeBlock<?>> block) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get())
                .part()
                .modelFile(existingModelFile("block/pipe_dot"))
                .addModel()
                .end();

        // This is done so it stops generating different JSONS even when nothing has changed at all
        Map<EnumProperty<ConnectionType>, Tuple<Integer, Integer>> propertyMap = new LinkedHashMap<>();
        propertyMap.put(BasePipeBlock.NORTH, new Tuple<>(0, 0));
        propertyMap.put(BasePipeBlock.EAST, new Tuple<>(0, 90));
        propertyMap.put(BasePipeBlock.SOUTH, new Tuple<>(0, 180));
        propertyMap.put(BasePipeBlock.WEST, new Tuple<>(0, 270));
        propertyMap.put(BasePipeBlock.UP, new Tuple<>(270, 0));
        propertyMap.put(BasePipeBlock.DOWN, new Tuple<>(90, 0));

        propertyMap.forEach((property, rotations) -> {
            builder
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
        return builder;
    }


    private ModelFile.@NotNull ExistingModelFile existingModelFile(String path) {
        return models().getExistingFile(ResourceHelper.asResource(path));
    }
}
