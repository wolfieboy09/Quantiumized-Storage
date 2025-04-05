package dev.wolfieboy09.qstorage.block;


import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

@NothingNullByDefault
public abstract class AbstractBaseEntityBlock extends BaseEntityBlock {
    public AbstractBaseEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
