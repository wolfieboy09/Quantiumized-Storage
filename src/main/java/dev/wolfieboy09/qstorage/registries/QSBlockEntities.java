package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.circut_engraver.CircuitEngraverBlockEntity;
import dev.wolfieboy09.qstorage.block.creative_energy_block.CreativeEnergyBlockEntity;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerBlockEntity;
import dev.wolfieboy09.qstorage.block.gas_filler.GasFillerBlockEntity;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlockEntity;
import dev.wolfieboy09.qstorage.block.pipe.pipes.entities.ItemPipeBlockEntity;
import dev.wolfieboy09.qstorage.block.smeltery.SmelteryBlockEntity;
import dev.wolfieboy09.qstorage.block.storage_matrix.StorageMatrixBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class QSBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, QuantiumizedStorage.MOD_ID);

    public static final Supplier<BlockEntityType<DiskAssemblerBlockEntity>> DISK_ASSEMBLER = createBlockEntity(
            "disk_assembler",
            DiskAssemblerBlockEntity::new,
            QSBlocks.DISK_ASSEMBLER
    );

    public static final Supplier<BlockEntityType<StorageMatrixBlockEntity>> STORAGE_MATRIX = createBlockEntity(
            "storage_matrix",
            StorageMatrixBlockEntity::new,
            QSBlocks.STORAGE_MATRIX
    );

    public static final Supplier<BlockEntityType<CircuitEngraverBlockEntity>> CIRCUIT_ENGRAVER = createBlockEntity(
            "circuit_engraver",
            CircuitEngraverBlockEntity::new,
            QSBlocks.CIRCUIT_ENGRAVER
    );

    public static final Supplier<BlockEntityType<CreativeEnergyBlockEntity>> CREATIVE_ENERGY_BLOCK = createBlockEntity(
            "creative_energy_block",
            CreativeEnergyBlockEntity::new,
            QSBlocks.CREATIVE_ENERGY_BLOCK
    );

    public static final Supplier<BlockEntityType<SmelteryBlockEntity>> SMELTERY = createBlockEntity(
            "smeltery",
            SmelteryBlockEntity::new,
            QSBlocks.SMELTERY
    );

    public static final Supplier<BlockEntityType<GasFillerBlockEntity>> GAS_FILLER = createBlockEntity(
            "gas_filler",
            GasFillerBlockEntity::new,
            QSBlocks.GAS_FILLER
    );

    //TODO Well, notes really
    // Either have BlockEntityType<ItemPipeBlockEntity> or have it as the BasePipeBlockEntity
    public static final Supplier<BlockEntityType<ItemPipeBlockEntity>> ITEM_PIPE_BLOCK = createBlockEntity(
            "item_pipe_block",
            ItemPipeBlockEntity::new,
            QSBlocks.ITEM_PIPE
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
