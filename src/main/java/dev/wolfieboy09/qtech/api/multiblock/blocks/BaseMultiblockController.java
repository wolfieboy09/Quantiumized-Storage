package dev.wolfieboy09.qtech.api.multiblock.blocks;

import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import dev.wolfieboy09.qtech.block.AbstractBaseEntityBlock;

import java.util.function.Supplier;

public abstract class BaseMultiblockController extends AbstractBaseEntityBlock {
    private final Supplier<MultiblockType> type;

    public BaseMultiblockController(Properties properties, Supplier<MultiblockType> type) {
        super(properties);
        this.type = type;
    }


    public MultiblockType getType() {
        return this.type.get();
    }
}
