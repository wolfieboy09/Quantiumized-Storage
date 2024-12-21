package dev.wolfieboy09.qstorage.block.creative_energy_block;

import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.EnumSet;
import java.util.List;

public class CreativeEnergyBlockEntity extends AbstractEnergyBlockEntity {
    public CreativeEnergyBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.CREATIVE_ENERGY_BLOCK.get(), pos, blockState, 1000000000, 1000000000);
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    public EnergyStorage getEnergyHandler() {
        return this.getEnergyStorage();
    }

    @Override
    public int getEnergyStored() {
        return 1000000000;
    }

}
