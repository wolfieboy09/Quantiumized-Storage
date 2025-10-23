package dev.wolfieboy09.qtech.api.multiblock.blocks;

import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import net.minecraft.world.level.block.Block;

public abstract class BaseMultiblockController extends Block {
    private final MultiblockType type;
    public BaseMultiblockController(Properties properties, MultiblockType type) {
        super(properties);
        this.type = type;
    }
}
