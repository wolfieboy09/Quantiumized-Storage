package dev.wolfieboy09.qtech.integration.jei.modifiers;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.integration.jei.QTJEIPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class GasIngredientHelper implements IIngredientHelper<Gas> {
    @Override
    public IIngredientType<Gas> getIngredientType() {
        return QTJEIPlugin.GAS_TYPE;
    }

    @Override
    public String getDisplayName(Gas ingredient) {
        return ingredient.getDescriptionId();
    }

    @Override
    public String getUniqueId(Gas ingredient, UidContext context) {
        return ingredient.getName().getString();
    }

    @Override
    public ResourceLocation getResourceLocation(Gas ingredient) {
        return ingredient.getResourceLocation();
    }

    @Override
    public Gas copyIngredient(Gas ingredient) {
        return ingredient.copy();
    }

    @Override
    public String getErrorInfo(@Nullable Gas ingredient) {
        return "An error occurred with: " + ingredient;
    }
}
