package dev.wolfieboy09.qstorage.intergration.jei.modifiers;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

@NothingNullByDefault
public record GasIngredientRenderer(int width, int height) implements IIngredientRenderer<Gas> {

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void render(GuiGraphics graphics, Gas ingredient) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(ingredient.getResourceLocation().getNamespace(), "gasses/" +  ingredient.getResourceLocation().getPath() + ".png");
        try {
            graphics.blit(location, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
        } catch (Exception ignored) {}
    }

    @Override
    public List<Component> getTooltip(Gas ingredient, TooltipFlag tooltipFlag) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable(ingredient.getDescriptionId()));
        return tooltip;
    }

//    @Override
//    public void getTooltip(ITooltipBuilder tooltip, Gas ingredient, TooltipFlag tooltipFlag) {
//        tooltip.add(Component.literal(ingredient.getDescriptionId()));
//    }
}

