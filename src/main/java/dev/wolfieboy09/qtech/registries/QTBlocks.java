package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.multiblock.blocks.controller.BaseMultiblockController;
import dev.wolfieboy09.qtech.block.circut_engraver.CircuitEngraverBlock;
import dev.wolfieboy09.qtech.block.creative_energy_block.CreativeEnergyBlock;
import dev.wolfieboy09.qtech.block.disk_assembler.DiskAssemblerBlock;
import dev.wolfieboy09.qtech.block.gas_canister.GasCanisterBlock;
import dev.wolfieboy09.qtech.block.multiblock.centrifuge.CentrifugeController;
import dev.wolfieboy09.qtech.block.pipe.pipes.energy.EnergyPipe;
import dev.wolfieboy09.qtech.block.pipe.pipes.fluid.FluidPipe;
import dev.wolfieboy09.qtech.block.pipe.pipes.item.ItemPipe;
import dev.wolfieboy09.qtech.block.smeltery.SmelteryBlock;
import dev.wolfieboy09.qtech.block.storage_matrix.StorageMatrixBlock;
import dev.wolfieboy09.qtech.item.GasCanisterItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
public class QTBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(QuantiumizedTech.MOD_ID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(QuantiumizedTech.MOD_ID);

    public static final DeferredBlock<DiskAssemblerBlock> DISK_ASSEMBLER = registerBlockWithProperties(
            "disk_assembler",
            () -> new DiskAssemblerBlock(BlockBehaviour.Properties.of().noOcclusion())
    );

    public static final DeferredBlock<StorageMatrixBlock> STORAGE_MATRIX = registerBlock(
            "storage_matrix",
            StorageMatrixBlock::new
    );

    public static final DeferredBlock<CircuitEngraverBlock> CIRCUIT_ENGRAVER = registerBlock(
            "circuit_engraver",
            CircuitEngraverBlock::new
    );

    public static final DeferredBlock<CreativeEnergyBlock> CREATIVE_ENERGY_BLOCK = registerBlock(
            "creative_energy_block",
            CreativeEnergyBlock::new
    );

    public static final DeferredBlock<SmelteryBlock> SMELTERY = registerBlockWithProperties(
            "smeltery",
            () -> new SmelteryBlock(BlockBehaviour.Properties.of().noOcclusion())
    );

    public static final DeferredBlock<GasCanisterBlock> GAS_CANISTER = registerGasCanister();

    public static final DeferredBlock<ItemPipe> ITEM_PIPE = registerNoPropertyBlock(
            "item_pipe",
            ItemPipe::new
    );

    public static final DeferredBlock<FluidPipe> FLUID_PIPE = registerNoPropertyBlock(
            "fluid_pipe",
            FluidPipe::new
    );

    public static final DeferredBlock<EnergyPipe> ENERGY_PIPE = registerNoPropertyBlock(
            "energy_pipe",
            EnergyPipe::new
    );

    public static final DeferredBlock<CentrifugeController> CENTRIFUGE_CONTROLLER = registerController("centrifuge_controller", () -> new CentrifugeController(BlockBehaviour.Properties.of()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, () -> block.apply(BlockBehaviour.Properties.of()));
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerNoPropertyBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithProperties(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static DeferredBlock<GasCanisterBlock> registerGasCanister() {
        DeferredBlock<GasCanisterBlock> toReturn = BLOCKS.register("gas_canister", () -> new GasCanisterBlock(BlockBehaviour.Properties.of()));
        BLOCK_ITEMS.register("gas_canister", () -> new GasCanisterItem(new Item.Properties()));
        return toReturn;
    }

    private static <T extends BaseMultiblockController> DeferredBlock<T> registerController(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }


    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ITEMS.register(bus);
    }
}
