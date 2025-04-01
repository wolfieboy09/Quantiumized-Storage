package dev.wolfieboy09.qstorage.integration.jei.disk_assembeler;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerRecipe;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class DiskAssemblerCategory implements IRecipeCategory<DiskAssemblerRecipe> {
    public static final RecipeType<DiskAssemblerRecipe> RECIPE_TYPE = RecipeType.create(QuantiumizedStorage.MOD_ID, "disk_assembler", DiskAssemblerRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final int guiUOffset = 13;
    private final int guiVOffset = 4;

    public DiskAssemblerCategory(@NotNull IGuiHelper guiHelper) {
        ResourceLocation location = ResourceHelper.asResource("textures/gui/disk_assembler.png");
        this.background = guiHelper.createDrawable(location, guiUOffset, guiVOffset, 160, 76);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(QSBlocks.DISK_ASSEMBLER.get()));
    }

    @Override
    public RecipeType<DiskAssemblerRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return QSBlocks.DISK_ASSEMBLER.get().getName();
    }

    @Override
    public int getWidth() {
        return 160;
    }

    @Override
    public int getHeight() {
        return 76;
    }

    @Override
    public void draw(DiskAssemblerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DiskAssemblerRecipe recipe, IFocusGroup focuses) {
        for (int i = 0; i < recipe.mainItems().size(); i++) {
            if (i == 0) {
                builder.addSlot(RecipeIngredientRole.INPUT, 17 - guiUOffset, 27 - guiVOffset).addIngredients(recipe.mainItems().get(i));
            } else if (i == 1) {
                builder.addSlot(RecipeIngredientRole.INPUT, 17 - guiUOffset, 45 - guiVOffset).addIngredients(recipe.mainItems().get(i));
            } else {
                builder.addSlot(RecipeIngredientRole.INPUT, 35 - guiUOffset, 36 - guiVOffset).addIngredients(recipe.mainItems().get(i));
            }
        }

        for (int i = 0; i < recipe.extras().size(); i++) {
            Ingredient extra = recipe.extras().get(i);
            int x = (116 + (i / 2) * 18) - guiUOffset;
            int y = (27 + (i % 2) * 18) - guiVOffset;
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, x, y);

            if (!extra.isEmpty()) {
                slot.addIngredients(extra);
            }
        }
        
        
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80 - guiUOffset, 36 - guiVOffset).addIngredient(
            VanillaTypes.ITEM_STACK,
            recipe.result()
        );
    }
}
