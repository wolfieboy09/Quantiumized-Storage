package dev.wolfieboy09.qtech.quantipedia.api.widgets;

import dev.wolfieboy09.qtech.api.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public class UntexturedButton extends AbstractButton {
    private final OnPress onPress;
    private final boolean alignLeft;

    public UntexturedButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        this(x, y, width, height, message, onPress, false);
    }

    public UntexturedButton(int x, int y, int width, int height, Component message, OnPress onPress, boolean alignLeft) {
        super(x, y, width, height, message);
        this.onPress = onPress;
        this.alignLeft = alignLeft;
    }

    @Override
    public void onPress() {
        if (this.onPress != null) {
            this.onPress.onPress(this);
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, this.getMessage());
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();

        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, ColorUtil.newAlpha(128, ColorUtil.BLACK));

        int color = this.active ? 0xFFFFFF : 0xA0A0A0;
        if (this.isHoveredOrFocused()) {
            color = 0xFFFFA0;
        }

        int textY = this.getY() + (this.height - 8) / 2;

        if (alignLeft) {
            guiGraphics.drawString(mc.font, this.getMessage(), this.getX() + 2, textY, color, false);
        } else {
            guiGraphics.drawCenteredString(mc.font, this.getMessage(), this.getX() + this.width / 2, textY, color);
        }

    }

    @FunctionalInterface
    public interface OnPress {
        void onPress(UntexturedButton button);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Builder builder(Component message, OnPress onPress) {
        return new Builder(message, onPress);
    }

    public static class Builder {
        private int x;
        private int y;
        private int width = 100;  // default width
        private int height = 20;  // default height
        private Component message;
        private OnPress onPress;
        private boolean alignLeft = false;

        public Builder(Component message, OnPress onPress) {
            this.message = message;
            this.onPress = onPress;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder message(Component message) {
            this.message = message;
            return this;
        }

        public Builder onPress(OnPress callback) {
            this.onPress = callback;
            return this;
        }

        public Builder alignLeft(boolean alignLeft) {
            this.alignLeft = alignLeft;
            return this;
        }

        public Builder bounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }

        public UntexturedButton build() {
            return new UntexturedButton(x, y, width, height, message, onPress, alignLeft);
        }
    }

}
