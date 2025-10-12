package dev.wolfieboy09.qtech.quantipedia.api.widgets;

import dev.wolfieboy09.qtech.api.widgets.UntexturedButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IconRenderableButton extends UntexturedButton {
    private final Item itemToRender;

    public IconRenderableButton(int x, int y, int width, int height, Component message, OnPress onPress, Item itemToRender) {
        super(x, y, width, height, message, onPress);
        this.itemToRender = itemToRender;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.renderFakeItem(new ItemStack(this.itemToRender), this.getX(), Math.round((float) (this.getY() + this.getWidth()) / 2));
    }
}
