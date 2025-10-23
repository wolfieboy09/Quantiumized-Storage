package dev.wolfieboy09.qtech.block.multiblock;

import dev.wolfieboy09.qtech.api.multiblock.blocks.BaseMultiblockController;
import dev.wolfieboy09.qtech.registries.QTMultiblockTypes;

public class CentrifugeController extends BaseMultiblockController {
    public CentrifugeController(Properties properties) {
        super(properties, QTMultiblockTypes.CENTRIFUGE);
    }
}
