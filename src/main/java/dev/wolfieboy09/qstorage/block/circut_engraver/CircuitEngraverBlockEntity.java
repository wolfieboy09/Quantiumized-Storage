package dev.wolfieboy09.qstorage.block.circut_engraver;

import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CircuitEngraverBlockEntity extends AbstractEnergyBlockEntity {
    public CircuitEngraverBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.CIRCUIT_ENGRAVER.get(), pos, blockState, 20000, 1000, 0);
    }

    @Override
    public boolean canExtract() {
        return false;
    }


}
