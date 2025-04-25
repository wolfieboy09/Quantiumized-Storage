package dev.wolfieboy09.qtech.integration.jei.modifiers;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

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

// Marked for removal.
    @Contract("_, _ -> new")
    @Override
    public @Unmodifiable List<Component> getTooltip(Gas ingredient, TooltipFlag tooltipFlag) {
        return List.of(Component.empty());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, Gas ingredient, TooltipFlag tooltipFlag) {
        tooltip.add(Component.literal(ingredient.getDescriptionId()));
    }
}

