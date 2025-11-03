package dev.wolfieboy09.qtech.block.multiblock.hatches.energy;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.multiblock.blocks.hatch.BaseMultiblockHatch;
import dev.wolfieboy09.qtech.api.multiblock.blocks.hatch.BaseMultiblockHatchEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class EnergyInputHatchBlock extends BaseMultiblockHatch<IEnergyStorage> {
    private final MapCodec<EnergyInputHatchBlock> CODEC = simpleCodec(EnergyInputHatchBlock::new);

    public EnergyInputHatchBlock(Properties properties) {
        super(Capabilities.EnergyStorage.BLOCK, properties);
    }

    @Override
    public BaseMultiblockHatchEntity<IEnergyStorage> newMultiblockHatch(BlockPos blockPos, BlockState blockState) {
        return new EnergyInputHatchBlockEntity(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
