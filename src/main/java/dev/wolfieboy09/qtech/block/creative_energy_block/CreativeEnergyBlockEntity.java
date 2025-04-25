package dev.wolfieboy09.qtech.block.creative_energy_block;

import dev.wolfieboy09.qtech.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;

public class CreativeEnergyBlockEntity extends AbstractEnergyBlockEntity {
    public CreativeEnergyBlockEntity(BlockPos pos, BlockState blockState) {
        super(QTBlockEntities.CREATIVE_ENERGY_BLOCK.get(), pos, blockState, 1000000000, 1000000000);
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
