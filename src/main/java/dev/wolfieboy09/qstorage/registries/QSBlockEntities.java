package dev.wolfieboy09.qstorage.registries;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerBlockEntity;

import static dev.wolfieboy09.qstorage.QuantiumizedStorage.LOGGER;
import static dev.wolfieboy09.qstorage.QuantiumizedStorage.REGISTRATE;

public class QSBlockEntities {
    public static final BlockEntityEntry<DiskAssemblerBlockEntity> DISK_ASSEMBLER = REGISTRATE
            .blockEntity("disk_assembler", DiskAssemblerBlockEntity::new)
            .validBlock(QSBlocks.DISK_ASSEMBLER)
            .register();

    public static void init() {

    }
}
