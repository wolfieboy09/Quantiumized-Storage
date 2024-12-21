package dev.wolfieboy09.qstorage.block.creative_energy_block;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qstorage.block.AbstractBaseEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CreativeEnergyBlock extends AbstractBaseEntityBlock {
    private static final MapCodec<CreativeEnergyBlock> CODEC = simpleCodec(CreativeEnergyBlock::new);

    public CreativeEnergyBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CreativeEnergyBlockEntity(blockPos, blockState);
    }
}
