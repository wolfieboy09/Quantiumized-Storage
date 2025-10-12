package dev.wolfieboy09.qtech.api.widgets;

import dev.wolfieboy09.qtech.api.util.ColorUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

public class SidemenuWidget extends AbstractWidget {
    private final AbstractWidget child;
    private boolean expanded = false;

    private final int expandedWidth;
    private final int expandedHeight;

    private BooleanSupplier expandedCondition; // Optional condition for auto-expansion

    public SidemenuWidget(int x, int y, int width, int height, int expandedWidth, int expandedHeight, AbstractWidget child, BooleanSupplier keepOpen) {
        super(x, y, width, height, Component.empty());
        this.child = child;
        this.expandedWidth = expandedWidth;
        this.expandedHeight = expandedHeight;
        this.expandedCondition = keepOpen;
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Builder builder() {
        return new Builder();
    }

    public void setExpandedCondition(BooleanSupplier condition) {
        this.expandedCondition = condition;
    }

    private void updateExpanded() {
        this.expanded = this.isHovered() || (this.expandedCondition != null && this.expandedCondition.getAsBoolean());
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.active && this.isHovered()) {
            toggle();
            return true;
        }
        return this.expanded && this.child.mouseClicked(mouseX, mouseY, button);
    }

    private void toggle() {
        this.expanded = !this.expanded;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        updateExpanded();

        int color = this.isHoveredOrFocused() ? ColorUtil.newAlpha(128, ColorUtil.BLACK) : ColorUtil.newAlpha(200, ColorUtil.BLACK);

        int addedWidth = this.expanded ? this.expandedWidth : 0;
        int addedHeight = this.expanded ? this.expandedHeight : 0;

        guiGraphics.fill(
                getX(),
                getY(),
                getX() + this.width + addedWidth,
                getY() + this.height + addedHeight,
                color
        );

        if (this.expanded) {
            this.child.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
        this.child.updateNarration(narrationElementOutput);
    }

    public static class Builder {
        private int x;
        private int y;
        private int width;
        private int height;
        private int expandedWidth;
        private int expandedHeight;
        private AbstractWidget child;
        private BooleanSupplier keepOpen;

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

        public Builder expandedSize(int expandedWidth, int expandedHeight) {
            this.expandedWidth = expandedWidth;
            this.expandedHeight = expandedHeight;
            return this;
        }

        public Builder child(AbstractWidget child) {
            this.child = child;
            return this;
        }

        public Builder conditional(BooleanSupplier keepOpen) {
            this.keepOpen = keepOpen;
            return this;
        }

        public SidemenuWidget build() {
            if (this.child == null) {
                throw new IllegalStateException("Child widget must be provided for SidemenuWidget");
            }
            return new SidemenuWidget(x, y, width, height, expandedWidth, expandedHeight, child, keepOpen);
        }
    }
}
