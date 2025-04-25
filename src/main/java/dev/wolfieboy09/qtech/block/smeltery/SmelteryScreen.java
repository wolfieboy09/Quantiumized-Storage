package dev.wolfieboy09.qtech.block.smeltery;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.api.widgets.FluidWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import static dev.wolfieboy09.qtech.block.smeltery.SmelteryBlockEntity.TANK_CAPACITY;

public class SmelteryScreen extends AbstractContainerScreen<SmelteryMenu> {
    private static final ResourceLocation BACKGROUND_LOCATION = ResourceHelper.asResource("textures/gui/smeltery.png");

    public SmelteryScreen(SmelteryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 232;
        this.imageWidth = 256;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new FluidWidget(() -> this.menu.getFluidInTank(0), this.leftPos + 8, this.topPos + 6, 18, 62, TANK_CAPACITY));
        addRenderableWidget(new FluidWidget(() -> this.menu.getFluidInTank(1), this.leftPos + 38, this.topPos + 6, 18, 62, TANK_CAPACITY));
        addRenderableWidget(new FluidWidget(() -> this.menu.getFluidInTank(2), this.leftPos + 67, this.topPos + 6, 18, 62, TANK_CAPACITY));

        addRenderableWidget(new FluidWidget(() -> this.menu.getFluidInTank(3), this.leftPos + 194, this.topPos + 6, 18, 62, TANK_CAPACITY));
        addRenderableWidget(new FluidWidget(() -> this.menu.getFluidInTank(4), this.leftPos + 223, this.topPos + 6, 18, 62, TANK_CAPACITY));
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float v, int i, int i1) {
        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
        graphics.blit(BACKGROUND_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        guiGraphics.blit(BACKGROUND_LOCATION, this.leftPos + 119, this.topPos + 28, 0, 232, (this.menu.getProgress() * 42) / 100, 15);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // We do not want to render the "inventory" label.
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }
}
