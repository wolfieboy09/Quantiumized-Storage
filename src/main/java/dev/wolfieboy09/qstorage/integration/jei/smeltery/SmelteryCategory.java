package dev.wolfieboy09.qstorage.integration.jei.smeltery;

import com.mojang.datafixers.util.Either;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.block.smeltery.SmelteryRecipe;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class SmelteryCategory implements IRecipeCategory<SmelteryRecipe> {
    public static final RecipeType<SmelteryRecipe> RECIPE_TYPE = RecipeType.create(QuantiumizedStorage.MOD_ID, "smeltery", SmelteryRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    private final int guiUOffset = 13;
    private final int guiVOffset = 4;

    public SmelteryCategory(@NotNull IGuiHelper guiHelper) {
        ResourceLocation location = ResourceHelper.asResource("textures/gui/smeltery.png");
        this.background = guiHelper.createDrawable(location, guiUOffset, guiVOffset, 160, 75);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(QSBlocks.SMELTERY.get()));
    }

    @Override
    public @NotNull RecipeType<SmelteryRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return QSBlocks.SMELTERY.get().getName();
    }

    @Override
    public void draw(SmelteryRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull SmelteryRecipe recipe, @NotNull IFocusGroup focuses) {
        int itemIndex = 0;
        int fluidIndex = 0;
        System.out.println("showing");

        for (Either<Ingredient, FluidStack> either : recipe.ingredients()) {
            System.out.println("Looping for: " + either.toString());
            if (either.left().isPresent() && itemIndex < 3) {
                Ingredient ingredient = either.left().get();
                switch (itemIndex) {
                    case 0 ->
                            builder.addSlot(RecipeIngredientRole.INPUT, 17 - guiUOffset, 27 - guiVOffset).addIngredients(ingredient);
                    case 1 ->
                            builder.addSlot(RecipeIngredientRole.INPUT, 17 - guiUOffset, 45 - guiVOffset).addIngredients(ingredient);
                    case 2 ->
                            builder.addSlot(RecipeIngredientRole.INPUT, 35 - guiUOffset, 36 - guiVOffset).addIngredients(ingredient);
                }
                itemIndex++;
            }

            if (either.right().isPresent() && fluidIndex < 3) {
                FluidStack fluid = either.right().get();
                int fluidX = 90 - guiUOffset;
                int fluidY = 20 + (fluidIndex * 18) - guiVOffset;

                builder.addSlot(RecipeIngredientRole.RENDER_ONLY, fluidX, fluidY)
                        .setFluidRenderer(1000, true, 16, 16)
                        .addFluidStack(fluid.getFluid(), fluid.getAmount());
                fluidIndex++;
            }

            if (itemIndex + fluidIndex >= 3) break;
        }
    }
}
