package dev.wolfieboy09.qtech.block.multiblock.hatches.item;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.multiblock.blocks.hatch.BaseMultiblockHatch;
import dev.wolfieboy09.qtech.api.multiblock.blocks.hatch.BaseMultiblockHatchEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

@NothingNullByDefault
public class MultiblockItemInputHatchBlock extends BaseMultiblockHatch<IItemHandler> {
    public MapCodec<MultiblockItemInputHatchBlock> CODEC = simpleCodec(MultiblockItemInputHatchBlock::new);

    public MultiblockItemInputHatchBlock(Properties properties) {
        super(Capabilities.ItemHandler.BLOCK, properties);
    }

    @Override
    public BaseMultiblockHatchEntity<IItemHandler> newMultiblockHatch(BlockPos blockPos, BlockState blockState) {
        return new MultiblockItemInputHatchBlockEntity(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
