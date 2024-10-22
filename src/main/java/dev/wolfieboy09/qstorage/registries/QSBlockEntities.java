package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class QSBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, QuantiumizedStorage.MOD_ID);

    public static final Supplier<BlockEntityType<DiskAssemblerBlockEntity>> DISK_ASSEMBLER = BLOCK_ENTITY_TYPES.register(
            "disk_assembler",
            () -> BlockEntityType.Builder.of(
                    DiskAssemblerBlockEntity::new,
                    QSBlocks.DISK_ASSEMBLER.get()
            ).build(null)
    );

    public static void init(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);
    }
}
