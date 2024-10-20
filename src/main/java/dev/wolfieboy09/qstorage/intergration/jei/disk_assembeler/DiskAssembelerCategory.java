package dev.wolfieboy09.qstorage.intergration.jei.disk_assembeler;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerRecipe;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiskAssembelerCategory implements IRecipeCategory<DiskAssemblerRecipe> {
    public static final RecipeType<DiskAssemblerRecipe> RECIPE_TYPE = RecipeType.create(QuantiumizedStorage.MOD_ID, "disk_assembler", DiskAssemblerRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public DiskAssembelerCategory(@NotNull IGuiHelper guiHelper) {
        ResourceLocation location = ResourceHelper.asResource("disk_assembler");
        background = guiHelper.createDrawable(location, 0, 0, 100, 100);
        icon = guiHelper.createDrawableItemStack(QSBlocks.DISK_ASSEMBLER.asStack());
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

    }
}
