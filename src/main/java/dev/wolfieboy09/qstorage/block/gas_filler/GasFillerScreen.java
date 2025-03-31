package dev.wolfieboy09.qstorage.block.gas_filler;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@NothingNullByDefault
public class GasFillerScreen extends AbstractContainerScreen<GasFillerMenu> {
    private static final ResourceLocation BACKGROUND_LOCATION = ResourceHelper.asResource("textures/gui/gas_filler.png");

    public GasFillerScreen(GasFillerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        CycleButton<GasFillerState> cycler = CycleButton.<GasFillerState>builder((object) -> Component.literal("Thing1"))
                .withInitialValue(GasFillerState.FILL)
                .withValues(GasFillerState.FILL, GasFillerState.DRAIN)
                .create(0, 0, 50, 50, Component.literal("Thing2"), (button, value) -> System.out.println("Value: " + value));

        addRenderableWidget(cycler);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
