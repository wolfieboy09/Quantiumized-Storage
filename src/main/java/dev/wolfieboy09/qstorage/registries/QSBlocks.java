package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;


public class QSBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(QuantiumizedStorage.MOD_ID);

    public static final DeferredBlock<DiskAssemblerBlock> DISK_ASSEMBLER = BLOCKS.register(
            "disk_assembler",
            () -> new DiskAssemblerBlock(BlockBehaviour.Properties.of().noOcclusion())
    );

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
