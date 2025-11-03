package dev.wolfieboy09.qtech.block.multiblock.hatches.energy;

import dev.wolfieboy09.qtech.api.energy.ExtendedEnergyStorage;
import dev.wolfieboy09.qtech.api.multiblock.MultiblockHatchRule;
import dev.wolfieboy09.qtech.api.multiblock.blocks.hatch.BaseMultiblockHatchEntity;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnergyInputHatchBlockEntity extends BaseMultiblockHatchEntity<IEnergyStorage> {
    private final IEnergyStorage energyStorage = new ExtendedEnergyStorage(1_000_000, this);

    public EnergyInputHatchBlockEntity(BlockPos pos, BlockState blockState) {
        super(QTBlockEntities.ENERGY_INPUT_HATCH.get(), Capabilities.EnergyStorage.BLOCK, pos, blockState);
    }

    @Override
    protected void tick() {

    }

    @Override
    public @NotNull MultiblockHatchRule getHatchRules() {
        return MultiblockHatchRule.insertOnly();
    }

    @Override
    public List<IEnergyStorage> getCapabilities() {
        return List.of(energyStorage);
    }
}
