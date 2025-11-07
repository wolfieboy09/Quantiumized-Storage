package dev.wolfieboy09.qtech.block.disk_assembler.recipe;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeConstrains;
import dev.wolfieboy09.qtech.api.recipes.StandardProcessingRecipe;
import dev.wolfieboy09.qtech.registries.QTRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

@NothingNullByDefault
public class DiskAssemblerStandardRecipe extends StandardProcessingRecipe<RecipeWrapper> {
    protected NonNullList<Ingredient> extras;

    public DiskAssemblerStandardRecipe(DiskAssemblerRecipeParams params) {
        super(QTRecipeTypes.DISK_ASSEMBLY, params);
        this.extras = params.extras;
    }

    public NonNullList<Ingredient> getExtras() {
        return extras;
    }

    @Override
    protected ProcessingRecipeConstrains getRecipeConstrains() {
        return ProcessingRecipeConstrains.builder()
                .maxItemIO(3, 1)
                .build();
    }

    @Override
    public boolean matches(RecipeWrapper input, Level level) {
        return false;
    }

    @Override
    public RecipeType<?> getType() {
        return QTRecipeTypes.DISK_ASSEMBLY.getType();
    }
}
