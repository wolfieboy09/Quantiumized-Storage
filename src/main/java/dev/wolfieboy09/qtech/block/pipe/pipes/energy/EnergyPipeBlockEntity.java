package dev.wolfieboy09.qtech.block.pipe.pipes.energy;

import dev.wolfieboy09.qtech.block.pipe.BasePipeBlockEntity;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EnergyPipeBlockEntity extends BasePipeBlockEntity<IEnergyStorage> {
    public EnergyPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(QTBlockEntities.ENERGY_PIPE.get(), Capabilities.EnergyStorage.BLOCK, pos, blockState);
    }

    @Override
    protected boolean canExtract(IEnergyStorage source) {
        return source.canExtract();
    }

    @Override
    protected boolean tryTransfer(IEnergyStorage source, IEnergyStorage target) {
        int energyAvailable = source.extractEnergy(Integer.MAX_VALUE, true);
        if (energyAvailable <= 0) {
            // There is nothing to extract
            return false;
        }
        int energyAccepted = target.receiveEnergy(energyAvailable, true);

        if (energyAccepted <= 0) {
            // The target can't accept the energy
            return false;
        }

        int extracted = source.extractEnergy(energyAccepted, false);
        int received = target.receiveEnergy(extracted, false);

        return received > 0;
    }
}
