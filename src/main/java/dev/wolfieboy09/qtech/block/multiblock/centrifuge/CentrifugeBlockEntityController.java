package dev.wolfieboy09.qtech.block.multiblock.centrifuge;

import dev.wolfieboy09.qtech.api.multiblock.blocks.BaseMultiblockEntityController;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import dev.wolfieboy09.qtech.registries.QTMultiblockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CentrifugeBlockEntityController extends BaseMultiblockEntityController {
    public CentrifugeBlockEntityController(BlockPos pos, BlockState blockState) {
        super(QTBlockEntities.CENTRIFUGE_CONTROLLER.get(), QTMultiblockTypes.CENTRIFUGE.get(), pos, blockState);
    }
}
