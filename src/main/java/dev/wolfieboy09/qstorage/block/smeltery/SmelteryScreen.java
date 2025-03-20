package dev.wolfieboy09.qstorage.block.smeltery;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.api.widgets.FluidWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static dev.wolfieboy09.qstorage.block.smeltery.SmelteryBlockEntity.TANK_CAPACITY;

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
        Supplier<FluidStack> tank1Supplier = () -> this.menu.getFluidInTank(0);
        Supplier<FluidStack> tank2Supplier = () -> this.menu.getFluidInTank(1);
        Supplier<FluidStack> tank3Supplier = () -> this.menu.getFluidInTank(2);

        addRenderableWidget(new FluidWidget(tank1Supplier, this.leftPos + 8, this.topPos + 6, 18, 62, TANK_CAPACITY));
        addRenderableWidget(new FluidWidget(tank2Supplier, this.leftPos + 38, this.topPos + 6, 18, 62, TANK_CAPACITY));
        addRenderableWidget(new FluidWidget(tank3Supplier, this.leftPos + 67, this.topPos + 6, 18, 62, TANK_CAPACITY));
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
    }
}
