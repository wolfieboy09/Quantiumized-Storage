package dev.wolfieboy09.qstorage.intergration.jei.disk_assembeler;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.util.FormattingUtil;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerRecipe;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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
        background = guiHelper.createDrawable(location, guiUOffset, guiVOffset, 160, 76);
        icon = guiHelper.createDrawableItemStack(new ItemStack(QSBlocks.DISK_ASSEMBLER.get()));
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
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DiskAssemblerRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 17 - guiUOffset, 27 - guiVOffset).addIngredients(recipe.diskPort());
        builder.addSlot(RecipeIngredientRole.INPUT, 17 - guiUOffset, 45 - guiVOffset).addIngredients(recipe.diskCasing());
        builder.addSlot(RecipeIngredientRole.INPUT, 35 - guiUOffset, 36 - guiVOffset).addIngredients(recipe.screws());

        builder.addSlot(RecipeIngredientRole.INPUT, 116 - guiUOffset, 27 - guiVOffset).addIngredients(recipe.extras().getFirst());
        builder.addSlot(RecipeIngredientRole.INPUT, 134 - guiUOffset, 27 - guiVOffset).addIngredients(recipe.extras().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 116 - guiUOffset, 45 - guiVOffset).addIngredients(recipe.extras().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 134 - guiUOffset, 45 - guiVOffset).addIngredients(recipe.extras().get(3));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80 - guiUOffset, 36 - guiVOffset).addIngredient(
            VanillaTypes.ITEM_STACK,
            recipe.result()
        );
    }

    @Override
    public void draw(DiskAssemblerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        // get correct numbers
        // graphics.fill(
        //        150,
        //        100 - (20000 / 10),
        //        154,
        //        65,
        //        0xFFCC2222
        //);

        graphics.drawCenteredString(Minecraft.getInstance().font, "Energy Cost: " + FormattingUtil.formatNumber(recipe.energyCost() * recipe.timeInTicks()) + " FE", 57, 65, 0xFFFFFFFF);
    }
}
