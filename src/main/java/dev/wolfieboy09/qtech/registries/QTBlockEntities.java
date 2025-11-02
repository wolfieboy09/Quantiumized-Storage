package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.block.circut_engraver.CircuitEngraverBlockEntity;
import dev.wolfieboy09.qtech.block.creative_energy_block.CreativeEnergyBlockEntity;
import dev.wolfieboy09.qtech.block.disk_assembler.DiskAssemblerBlockEntity;
import dev.wolfieboy09.qtech.block.gas_canister.GasCanisterBlockEntity;
import dev.wolfieboy09.qtech.block.multiblock.centrifuge.CentrifugeBlockEntityController;
import dev.wolfieboy09.qtech.block.multiblock.hatches.item.ItemInputHatchBlockEntity;
import dev.wolfieboy09.qtech.block.pipe.pipes.energy.EnergyPipeBlockEntity;
import dev.wolfieboy09.qtech.block.pipe.pipes.fluid.FluidPipeBlockEntity;
import dev.wolfieboy09.qtech.block.pipe.pipes.item.ItemPipeBlockEntity;
import dev.wolfieboy09.qtech.block.smeltery.SmelteryBlockEntity;
import dev.wolfieboy09.qtech.block.storage_matrix.StorageMatrixBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class QTBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, QuantiumizedTech.MOD_ID);

    public static final Supplier<BlockEntityType<DiskAssemblerBlockEntity>> DISK_ASSEMBLER = createBlockEntity(
            "disk_assembler",
            DiskAssemblerBlockEntity::new,
            QTBlocks.DISK_ASSEMBLER
    );

    public static final Supplier<BlockEntityType<StorageMatrixBlockEntity>> STORAGE_MATRIX = createBlockEntity(
            "storage_matrix",
            StorageMatrixBlockEntity::new,
            QTBlocks.STORAGE_MATRIX
    );

    public static final Supplier<BlockEntityType<CircuitEngraverBlockEntity>> CIRCUIT_ENGRAVER = createBlockEntity(
            "circuit_engraver",
            CircuitEngraverBlockEntity::new,
            QTBlocks.CIRCUIT_ENGRAVER
    );

    public static final Supplier<BlockEntityType<CreativeEnergyBlockEntity>> CREATIVE_ENERGY_BLOCK = createBlockEntity(
            "creative_energy_block",
            CreativeEnergyBlockEntity::new,
            QTBlocks.CREATIVE_ENERGY_BLOCK
    );

    public static final Supplier<BlockEntityType<SmelteryBlockEntity>> SMELTERY = createBlockEntity(
            "smeltery",
            SmelteryBlockEntity::new,
            QTBlocks.SMELTERY
    );

    public static final Supplier<BlockEntityType<GasCanisterBlockEntity>> GAS_CANISTER = createBlockEntity(
            "gas_canister",
            GasCanisterBlockEntity::new,
            QTBlocks.GAS_CANISTER
    );

    public static final Supplier<BlockEntityType<ItemPipeBlockEntity>> ITEM_PIPE = createBlockEntity(
            "item_pipe",
            ItemPipeBlockEntity::new,
            QTBlocks.ITEM_PIPE
    );

    public static final Supplier<BlockEntityType<FluidPipeBlockEntity>> FLUID_PIPE = createBlockEntity(
            "fluid_pipe",
            FluidPipeBlockEntity::new,
            QTBlocks.FLUID_PIPE
    );

    public static final Supplier<BlockEntityType<EnergyPipeBlockEntity>> ENERGY_PIPE = createBlockEntity(
            "energy_pipe",
            EnergyPipeBlockEntity::new,
            QTBlocks.ENERGY_PIPE
    );

    public static final Supplier<BlockEntityType<CentrifugeBlockEntityController>> CENTRIFUGE_CONTROLLER = createBlockEntity(
            "centrifuge_controller",
            CentrifugeBlockEntityController::new,
            QTBlocks.CENTRIFUGE_CONTROLLER
    );

    public static final Supplier<BlockEntityType<ItemInputHatchBlockEntity>> ITEM_INPUT_HATCH = createBlockEntity(
            "item_input_hatch",
            ItemInputHatchBlockEntity::new,
            QTBlocks.ITEM_INPUT_HATCH
    );

    private static <T extends BlockEntity> @NotNull Supplier<BlockEntityType<T>> createBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier, Supplier<? extends Block> block) {
        return BLOCK_ENTITY_TYPES.register(
                name,
                () -> BlockEntityType.Builder.of(
                        blockEntitySupplier,
                        block.get()
                ).build(null)
        );
    }


    public static void register(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);
    }
}
