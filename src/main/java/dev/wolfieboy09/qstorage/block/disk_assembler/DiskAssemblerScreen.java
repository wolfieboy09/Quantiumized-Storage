package dev.wolfieboy09.qstorage.block.disk_assembler;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.wolfieboy09.qstorage.api.util.FormattingUtil;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class DiskAssemblerScreen extends AbstractContainerScreen<DiskAssemblerMenu> {
    private static final ResourceLocation BACKGROUND_LOCATION = ResourceHelper.asResource("textures/gui/disk_assembler.png");

    public DiskAssemblerScreen(DiskAssemblerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float v, int i, int i1) {
        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
        graphics.blit(BACKGROUND_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        graphics.fill(
                this.leftPos + 163,
                this.topPos + 69 + (-this.menu.getEnergyStoredScaled() / 2),
                this.leftPos + 167,
                this.topPos + 69,
                0xFFCC2222);

        int progress = this.menu.getCurrentProgress();
        int recipeTotalProgress = this.menu.getTotalProgress();
        int width = recipeTotalProgress == 0 ? 0 : progress * 30 / recipeTotalProgress;
        graphics.blit(BACKGROUND_LOCATION, this.leftPos + 52, this.topPos + 25, 176, 0, width, 34);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        int progress = this.menu.getCurrentProgress();
        int recipeTotalProgress = this.menu.getTotalProgress();

        graphics.blit(BACKGROUND_LOCATION, 52, 25, 176, 0, (progress * 30)  / 100, 35);

        graphics.blit(BACKGROUND_LOCATION, 95, 25, 176, 36, (progress * 21) / 100, 32);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(graphics, pMouseX, pMouseY);

        int energyStored = this.menu.getEnergy();
        int maxEnergy = this.menu.getMaxEnergy();

        Component text = Component.literal("Energy: " + FormattingUtil.formatNumber(energyStored) + " / " + FormattingUtil.formatNumber(maxEnergy) + "FE");
        if(isHovering(163, 10, 3, 62, pMouseX, pMouseY)) {
            graphics.renderTooltip(this.font, text, pMouseX, pMouseY);
        }
    }
}
