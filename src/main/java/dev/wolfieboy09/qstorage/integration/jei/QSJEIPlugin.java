package dev.wolfieboy09.qstorage.integration.jei;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.integration.jei.disk_assembeler.DiskAssemblerCategory;
import dev.wolfieboy09.qstorage.integration.jei.modifiers.GasIngredientHelper;
import dev.wolfieboy09.qstorage.integration.jei.modifiers.GasIngredientRenderer;
import dev.wolfieboy09.qstorage.integration.jei.smeltery.SmelteryCategory;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import dev.wolfieboy09.qstorage.registries.QSRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
@NothingNullByDefault
public class QSJEIPlugin implements IModPlugin {
    public static final IIngredientType<Gas> GAS_TYPE = () -> Gas.class;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceHelper.asResource("jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new DiskAssemblerCategory(guiHelper), new SmelteryCategory(guiHelper));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        if (Minecraft.getInstance().level == null) return;
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(DiskAssemblerCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(QSRecipes.DISK_ASSEMBLER_TYPE.get()).stream()
                .map(RecipeHolder::value).toList());

        registration.addRecipes(SmelteryCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(QSRecipes.SMELTERY_RECIPE_TYPE.get()).stream()
                .map(RecipeHolder::value).toList());
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(QSBlocks.DISK_ASSEMBLER.get()), DiskAssemblerCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(QSBlocks.SMELTERY.get()), SmelteryCategory.RECIPE_TYPE);
    }

    @Override
    public void registerModInfo(IModInfoRegistration modAliasRegistration) {
        modAliasRegistration.addModAliases(QuantiumizedStorage.MOD_ID, "qstorage");
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(GAS_TYPE, QSRegistries.GAS.stream().toList(), new GasIngredientHelper(), new GasIngredientRenderer(16, 16), Gas.CODEC);
    }
}
