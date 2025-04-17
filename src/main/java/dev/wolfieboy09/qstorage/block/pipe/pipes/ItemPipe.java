package dev.wolfieboy09.qstorage.block.pipe.pipes;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlock;
import dev.wolfieboy09.qstorage.block.pipe.pipes.entities.ItemPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class ItemPipe extends BasePipeBlock<IItemHandler> {
    public ItemPipe() {
        super(Capabilities.ItemHandler.BLOCK);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ItemPipeBlockEntity(pos, state);
    }
}
