package dev.wolfieboy09.qstorage.integration.jei.smeltery;

import com.mojang.datafixers.util.Either;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.util.FluidUtil;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.block.smeltery.SmelteryRecipe;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
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
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class SmelteryCategory implements IRecipeCategory<SmelteryRecipe> {
    public static final RecipeType<SmelteryRecipe> RECIPE_TYPE = RecipeType.create(QuantiumizedStorage.MOD_ID, "smeltery", SmelteryRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    private final int guiUOffset = 3;
    private final int guiVOffset = 4;

    public SmelteryCategory(@NotNull IGuiHelper guiHelper) {
        ResourceLocation location = ResourceHelper.asResource("textures/gui/smeltery.png");
        this.background = guiHelper.createDrawable(location, guiUOffset, guiVOffset, 240, 75);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(QSBlocks.SMELTERY.get()));
    }

    @Override
    public int getWidth() {
        return 240;
    }

    @Override
    public int getHeight() {
        return 75;
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
    public void draw(SmelteryRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
        int count = 0;

        for (Either<Ingredient, SizedFluidIngredient> either : recipe.ingredients()) {
            if (either.right().isPresent()) {
                switch (count) {
                    case 0 -> FluidUtil.renderFluid(guiGraphics, either.right().get().getFluids(), 8 - guiUOffset, 68 - guiVOffset, 18, 62);
                    case 1 -> FluidUtil.renderFluid(guiGraphics, either.right().get().getFluids(), 38 - guiUOffset, 68 - guiVOffset, 18, 62);
                    case 2 -> FluidUtil.renderFluid(guiGraphics, either.right().get().getFluids(), 67 - guiUOffset, 68 - guiVOffset, 18, 62);
                }
                count++;
            }
        }

        for (Either<ItemStack, FluidStack> either : recipe.result()) {
            if (either.right().isPresent()) {
                FluidUtil.renderFluid(guiGraphics, either.right().get(), 5, 6, 18, 62);
                break;
            }
        }

        for (Either<ItemStack, FluidStack> either : recipe.waste()) {
            if (either.right().isPresent()) {
                FluidUtil.renderFluid(guiGraphics, either.right().get(), 223 - guiUOffset, 6 - guiVOffset, 18, 62);
                break;
            }
        }
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull SmelteryRecipe recipe, @NotNull IFocusGroup focuses) {
        int itemIndex = 0;

        for (Either<Ingredient, SizedFluidIngredient> either : recipe.ingredients()) {
            if (either.left().isPresent()) {
                Ingredient ingredient = either.left().get();
                switch (itemIndex) {
                    case 0 ->
                            builder.addSlot(RecipeIngredientRole.INPUT, 93 - guiUOffset, 6 - guiVOffset).addIngredients(ingredient);
                    case 1 ->
                            builder.addSlot(RecipeIngredientRole.INPUT, 17 - guiUOffset, 45 - guiVOffset).addIngredients(ingredient);
                    case 2 ->
                            builder.addSlot(RecipeIngredientRole.INPUT, 35 - guiUOffset, 36 - guiVOffset).addIngredients(ingredient);
                }
                itemIndex++;
            }

            if (itemIndex >= 3) break;
        }

        for (Either<ItemStack, FluidStack> either : recipe.result()) {
            if (either.left().isPresent()) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, 171 - guiUOffset, 6 - guiVOffset).addItemStack(either.left().get());
                break;
            }
        }

        for (Either<ItemStack, FluidStack> either : recipe.waste()) {
            if (either.left().isPresent()) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, 171 - guiUOffset, 52 - guiVOffset).addItemStack(either.left().get());
                break;
            }
        }
    }
}
