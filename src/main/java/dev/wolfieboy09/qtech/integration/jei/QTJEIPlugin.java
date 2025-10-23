package dev.wolfieboy09.qtech.integration.jei;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.integration.jei.disk_assembeler.DiskAssemblerCategory;
import dev.wolfieboy09.qtech.integration.jei.modifiers.GasIngredientHelper;
import dev.wolfieboy09.qtech.integration.jei.modifiers.GasIngredientRenderer;
import dev.wolfieboy09.qtech.integration.jei.smeltery.SmelteryCategory;
import dev.wolfieboy09.qtech.registries.QTBlocks;
import dev.wolfieboy09.qtech.registries.QTRecipes;
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

@JeiPlugin
@NothingNullByDefault
public class QTJEIPlugin implements IModPlugin {
    public static final IIngredientType<Gas> GAS_TYPE = () -> Gas.class;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceHelper.asResource("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new DiskAssemblerCategory(guiHelper), new SmelteryCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level == null) return;
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(DiskAssemblerCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(QTRecipes.DISK_ASSEMBLER_TYPE.get()).stream()
                .map(RecipeHolder::value).toList());

        registration.addRecipes(SmelteryCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(QTRecipes.SMELTERY_RECIPE_TYPE.get()).stream()
                .map(RecipeHolder::value).toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(QTBlocks.DISK_ASSEMBLER.get()), DiskAssemblerCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(QTBlocks.SMELTERY.get()), SmelteryCategory.RECIPE_TYPE);
    }

    @Override
    public void registerModInfo(IModInfoRegistration modAliasRegistration) {
        modAliasRegistration.addModAliases(QuantiumizedTech.MOD_ID, "qtech");
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(GAS_TYPE, QTRegistries.GAS.stream().toList(), new GasIngredientHelper(), new GasIngredientRenderer(16, 16), Gas.CODEC);
    }
}
