package dev.wolfieboy09.qtech.block.circut_engraver;

import dev.wolfieboy09.qtech.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CircuitEngraverBlockEntity extends AbstractEnergyBlockEntity {
    public CircuitEngraverBlockEntity(BlockPos pos, BlockState blockState) {
        super(QTBlockEntities.CIRCUIT_ENGRAVER.get(), pos, blockState, 20000, 1000, 0);
    }

    @Override
    public boolean canExtract() {
        return false;
    }


}
