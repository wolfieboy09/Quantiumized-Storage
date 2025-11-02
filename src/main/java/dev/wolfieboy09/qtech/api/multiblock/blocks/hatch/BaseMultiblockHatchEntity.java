package dev.wolfieboy09.qtech.api.multiblock.blocks.hatch;

import dev.wolfieboy09.qtech.api.multiblock.MultiblockHatchRule;
import dev.wolfieboy09.qtech.block.GlobalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public abstract class BaseMultiblockHatchEntity<C> extends GlobalBlockEntity {
    private final BlockCapability<C, @Nullable Direction> blockCapability;

    public BaseMultiblockHatchEntity(BlockEntityType<?> type, BlockCapability<C, @Nullable Direction> blockCapability, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.blockCapability = blockCapability;
    }

    public BlockCapability<C, @Nullable Direction> getBlockCapability() {
        return this.blockCapability;
    }

    public abstract MultiblockHatchRule getHatchRules();

}
