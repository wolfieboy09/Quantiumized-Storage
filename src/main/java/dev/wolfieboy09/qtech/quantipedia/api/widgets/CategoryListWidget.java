package dev.wolfieboy09.qtech.quantipedia.api.widgets;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

@NothingNullByDefault
public class CategoryListWidget extends AbstractWidget {
    private final List<AbstractWidget> children = new ArrayList<>();
    private boolean expanded = false;
    private final int headerHeight;
    private final UntexturedButton headerButton;

    public CategoryListWidget(int x, int y, int width, int height, Component title) {
        super(x, y, width, height, title);
        this.headerHeight = height;

        this.headerButton = UntexturedButton.builder(title, b -> toggle())
                .bounds(x, y, width, this.headerHeight)
                .build();
    }

    public void addChild(AbstractWidget widget) {
        this.children.add(widget);
    }

    private void toggle() {
        this.expanded = !this.expanded;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.headerButton.render(graphics, mouseX, mouseY, partialTick);

        if (this.expanded) {
            int offsetY = this.headerHeight;
            for (AbstractWidget child : this.children) {
                child.setX(getX());
                child.setY(getY() + offsetY);
                child.render(graphics, mouseX, mouseY, partialTick);
                offsetY += child.getHeight() + 2;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.headerButton.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (this.expanded) {
            for (AbstractWidget child : this.children) {
                if (child.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.headerButton.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        if (this.expanded) {
            for (AbstractWidget child : this.children) {
                if (child.mouseReleased(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.headerButton.getMessage());
        for (AbstractWidget widget : this.children) {
            widget.updateNarration(output);
        }
    }
}
