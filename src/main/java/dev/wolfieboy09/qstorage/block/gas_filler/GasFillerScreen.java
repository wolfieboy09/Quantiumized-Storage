package dev.wolfieboy09.qstorage.block.gas_filler;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.packets.GasFillerModeData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import static dev.wolfieboy09.qstorage.block.gas_filler.GasFillerBlock.MODE;

@NothingNullByDefault
public class GasFillerScreen extends AbstractContainerScreen<GasFillerMenu> {
    private static final ResourceLocation BACKGROUND_LOCATION = ResourceHelper.asResource("textures/gui/gas_filler.png");

    public GasFillerScreen(GasFillerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.titleLabelX = 8;
        this.titleLabelY = 3;

        this.inventoryLabelX = 8;
        this.inventoryLabelY = 76;
        this.imageHeight += 2;
    }

    @Override
    protected void init() {
        super.init();
        CycleButton<GasFillerState> cycler = CycleButton.<GasFillerState>builder((gasFillerState) -> Component.literal(gasFillerState.name()))
                .withInitialValue(GasFillerState.FILL)
                .withValues(GasFillerState.FILL, GasFillerState.DRAIN)
                .create(this.width / 2 - 25, this.topPos + this.imageHeight - 110, 75, 15, Component.literal("Mode"), (button, value) -> {
                    PacketDistributor.sendToServer(new GasFillerModeData(this.menu.getBE().getBlockPos(), button.getValue()));
                    this.menu.updateMode(button.getValue());
                });
        cycler.setValue(this.menu.getFillState());

        addRenderableWidget(cycler);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float v, int i, int i1) {
        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
        graphics.blit(BACKGROUND_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        if (this.menu.getFillState() == GasFillerState.FILL) {
            guiGraphics.blit(BACKGROUND_LOCATION, this.leftPos + this.imageWidth/2 - 15, this.topPos + 35, 176, 0, 50, 15);
        } else {
            guiGraphics.blit(BACKGROUND_LOCATION, this.leftPos + this.imageWidth/2 - 15, this.topPos + 35, 176, 15, 50, 15);
        }
    }
}
