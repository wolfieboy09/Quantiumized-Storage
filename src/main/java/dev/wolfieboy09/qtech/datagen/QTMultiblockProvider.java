package dev.wolfieboy09.qtech.datagen;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.datagen.MultiblockPatternProvider;
import net.minecraft.data.PackOutput;

public class QTMultiblockProvider extends MultiblockPatternProvider {
    protected QTMultiblockProvider(PackOutput output) {
        super(output, QuantiumizedTech.MOD_ID);
    }

    @Override
    protected void registerPatterns() {
        //TODO: Make multiblock's and make the pattern in here
    }
}
