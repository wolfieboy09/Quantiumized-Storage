package dev.wolfieboy09.qtech.block.multiblock;

import dev.wolfieboy09.qtech.block.GlobalBlockEntity;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CentrifugeBlockEntityController extends GlobalBlockEntity {
    public CentrifugeBlockEntityController(BlockPos pos, BlockState blockState) {
        super(QTBlockEntities.CENTRIFUGE_CONTROLLER.get(), pos, blockState);
    }
}
