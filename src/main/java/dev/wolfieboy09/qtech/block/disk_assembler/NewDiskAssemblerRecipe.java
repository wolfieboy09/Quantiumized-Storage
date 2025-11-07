package dev.wolfieboy09.qtech.block.disk_assembler;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeConstrains;
import dev.wolfieboy09.qtech.block.disk_assembler.recipe.DiskAssemblerRecipeParams;
import dev.wolfieboy09.qtech.block.disk_assembler.recipe.DiskAssemblerStandardRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

@NothingNullByDefault
public class NewDiskAssemblerRecipe extends DiskAssemblerStandardRecipe {
    public NewDiskAssemblerRecipe(DiskAssemblerRecipeParams params) {
        super(params);
    }

    @Override
    protected ProcessingRecipeConstrains getRecipeConstrains() {
        return ProcessingRecipeConstrains.builder()
                .maxItemIO(4, 1)
                .build();
    }

    @Override
    public boolean matches(RecipeWrapper input, Level level) {
        boolean extrasMatch = !getExtras().isEmpty();
        boolean ingredientsMatch = !getIngredients().isEmpty();
        for (Ingredient extra : getExtras()) {
            if (!extra.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.EXTRA_SLOT_1))
                    && !extra.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.EXTRA_SLOT_2))
                    && !extra.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.EXTRA_SLOT_3))
                    && !extra.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.EXTRA_SLOT_4))) {
                extrasMatch = false;
            }
        }

        for (Ingredient main : getIngredients()) {
            if (!main.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.MAIN_SLOT_1))
                    && !main.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.MAIN_SLOT_2))
                    && !main.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.MAIN_SLOT_3))) {
                ingredientsMatch = false;
            }
        }

        return extrasMatch && ingredientsMatch;
    }
}
