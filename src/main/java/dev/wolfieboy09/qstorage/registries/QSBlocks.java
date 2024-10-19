package dev.wolfieboy09.qstorage.registries;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerBlock;
import dev.wolfieboy09.qstorage.registries.util.BlockStateGen;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static dev.wolfieboy09.qstorage.QuantiumizedStorage.REGISTRATE;

public class QSBlocks {
    public static final BlockEntry<DiskAssemblerBlock> DISK_ASSEMBLER = REGISTRATE
            .block("disk_assembler", DiskAssemblerBlock::new)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen::simpleCustomModel)
            .item()
            .build()
            .register();

    public static void init() {

    }
}
