package dev.wolfieboy09.qtech.block.disk_assembler;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeConstrains;
import dev.wolfieboy09.qtech.api.recipes.data.disk_assembler.DiskAssemblerRecipeParams;
import dev.wolfieboy09.qtech.api.recipes.data.disk_assembler.DiskAssemblerStandardRecipe;

@NothingNullByDefault
public class DiskAssemblerRecipe extends DiskAssemblerStandardRecipe {
    public DiskAssemblerRecipe(DiskAssemblerRecipeParams params) {
        super(params);
    }

    @Override
    protected ProcessingRecipeConstrains getRecipeConstrains() {
        return ProcessingRecipeConstrains.builder()
                .maxItemIO(4, 1)
                .build();
    }
}
