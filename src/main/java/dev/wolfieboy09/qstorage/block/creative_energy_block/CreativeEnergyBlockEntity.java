package dev.wolfieboy09.qstorage.block.creative_energy_block;

import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;

public class CreativeEnergyBlockEntity extends AbstractEnergyBlockEntity {
    private final List<Direction> directions = List.of(Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

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

    public void tick() {
        if (level == null) return;
        for (Direction direction : this.directions) {
            BlockPos relativePos = getBlockPos().relative(direction);
            BlockEntity blockEntity = level.getBlockEntity(relativePos);

            if (blockEntity != null) {
                IEnergyStorage blockEnergyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, getBlockPos(), direction.getOpposite());
                if (blockEnergyStorage != null) {
                        int energyToTransfer = Math.min(100000, blockEnergyStorage.getMaxEnergyStored() - blockEnergyStorage.getEnergyStored());
                        if (energyToTransfer > 0) {
                            blockEnergyStorage.receiveEnergy(energyToTransfer, false);
                    }
                }
            }
        }
    }
}
