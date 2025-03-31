package dev.wolfieboy09.qstorage.block.gas_filler;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.block.AbstractBaseEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class GasFillerBlock extends AbstractBaseEntityBlock {
    public static final MapCodec<GasFillerBlock> CODEC = simpleCodec(GasFillerBlock::new);

    public GasFillerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return super.getTicker(level, state, blockEntityType);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
