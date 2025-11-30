package dev.wolfieboy09.qtech.integration.jei.category.recipes;

import dev.wolfieboy09.qtech.api.recipes.data.void_crafting.VoidCraftingRecipe;
import dev.wolfieboy09.qtech.api.recipes.result.ChanceResult;
import dev.wolfieboy09.qtech.integration.jei.category.QTRecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@ParametersAreNonnullByDefault
public class VoidCraftingCategory extends QTRecipeCategory<VoidCraftingRecipe> {
    public VoidCraftingCategory(Info<VoidCraftingRecipe> info) {
        super(info);
    }

    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, VoidCraftingRecipe recipe, IFocusGroup focuses) {
        int x = 0;
        int y = 25;

        for (int i = 0; i < recipe.getItemIngredients().size(); i++) {
           builder.addInputSlot(x, y)
                   .setBackground(getRenderedSlot(), -1, -1)
                   .addItemStacks(Arrays.stream(recipe.getItemIngredients().get(i).getItems()).toList());

            x += 20;

            // Make rows of 5
            if (i == 4) {
               y = 16 + 27;
               x = 0;
           }
        }


        x = 125;
        y = 0;

        for (int i = 0; i < recipe.getRollableResults().size(); i++) {
            ChanceResult<?> result = recipe.getRollableResults().get(i);
            builder.addOutputSlot(x, y)
                    .setBackground(getRenderedSlot(result), -1, -1)
                    .addItemStack(recipe.getRollableResults().get(i).getResult())
                    .addRichTooltipCallback(addChanceTooltip(result));

            y += 20;
        }
    }

    @Override
    protected void draw(VoidCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gui, double mouseX, double mouseY) {

    }
}
