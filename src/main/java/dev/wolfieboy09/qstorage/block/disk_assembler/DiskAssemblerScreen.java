package dev.wolfieboy09.qstorage.block.disk_assembler;

import com.mojang.blaze3d.systems.RenderSystem;
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
                this.topPos + 75 + (-this.menu.getEnergyStoredScaled() / 2) - 6,
                this.leftPos + 167,
                this.topPos + 69,
                0xFFCC2222);


    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        int energyStored = this.menu.getEnergy();
        int maxEnergy = this.menu.getMaxEnergy();

        Component text = Component.literal("Energy: " + energyStored + " / " + maxEnergy);
        if(isHovering(163, 10, 3, 62, pMouseX, pMouseY)) {
            pGuiGraphics.renderTooltip(this.font, text, pMouseX, pMouseY);
        }
    }
}
