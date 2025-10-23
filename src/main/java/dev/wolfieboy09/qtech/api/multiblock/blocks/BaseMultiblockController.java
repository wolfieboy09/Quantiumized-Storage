package dev.wolfieboy09.qtech.api.multiblock.blocks;

import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public abstract class BaseMultiblockController extends Block {
    private final Supplier<MultiblockType> type;

    public BaseMultiblockController(Properties properties, Supplier<MultiblockType> type) {
        super(properties);
        this.type = type;
    }
}
