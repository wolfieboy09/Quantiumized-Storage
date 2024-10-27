package dev.wolfieboy09.qstorage.intergration.jei.disk_assembeler;

import com.mojang.serialization.Codec;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerRecipe;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
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

    public DiskAssemblerCategory(@NotNull IGuiHelper guiHelper) {
        ResourceLocation location = ResourceHelper.asResource("textures/gui/disk_assembler.png");
        background = guiHelper.createDrawable(location, 0, 0, 176, 76);
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
        builder.addSlot(RecipeIngredientRole.INPUT, 17, 27).addIngredients(recipe.diskPort());
        builder.addSlot(RecipeIngredientRole.INPUT, 17, 45).addIngredients(recipe.diskCasing());
        builder.addSlot(RecipeIngredientRole.INPUT, 35, 36).addIngredients(recipe.screws());

        builder.addSlot(RecipeIngredientRole.INPUT, 116, 27).addIngredients(recipe.extras().getFirst());
        builder.addSlot(RecipeIngredientRole.INPUT, 134, 27).addIngredients(recipe.extras().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 116, 45).addIngredients(recipe.extras().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 134, 45).addIngredients(recipe.extras().get(3));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 36).addIngredient(
            VanillaTypes.ITEM_STACK,
            recipe.result()
        );
    }
}
