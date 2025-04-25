package dev.wolfieboy09.qtech.api.widgets;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.util.FluidUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;

import java.text.DecimalFormat;
import java.util.function.Supplier;

import static net.minecraft.client.gui.screens.Screen.hasShiftDown;

@NothingNullByDefault
public class FluidWidget extends AbstractWidget {
    private final Supplier<FluidStack> fluidSupplier;
    private final int maxCapacity;

    public FluidWidget(Supplier<FluidStack> fluidSupplier, int x, int y, int width, int height, int maxCapacity) {
        super(x, y, width, height, Component.empty());
        this.fluidSupplier = fluidSupplier;
        this.maxCapacity = maxCapacity;
    }

    public static String formatted(int value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(value);
    }

    public static String withSuffix(int count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f", count / Math.pow(1000, exp));
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        FluidStack fluid = this.fluidSupplier.get();
        if (fluid.isEmpty()) return;
        int fluidFilled = fluid.getAmount() * 100 / this.maxCapacity;
        renderFluid(graphics,getX(),getY()+this.height,this.width, (int) (this.height * (float) fluidFilled / 100f));
        if (isHovered()) {
            Component hoverName = fluid.getHoverName();
            var amount = fluid.getAmount();
            if (hasShiftDown()){
                graphics.renderTooltip(Minecraft.getInstance().font,
                        Component.translatable("qtech.screen.fluid",hoverName, formatted(amount), formatted(this.maxCapacity)),
                        mouseX, mouseY);
            } else {
                graphics.renderTooltip(Minecraft.getInstance().font,
                        Component.translatable("qtech.screen.fluid",hoverName, withSuffix(amount), withSuffix(this.maxCapacity)),
                        mouseX, mouseY);
            }

        }
    }

    public void renderFluid(GuiGraphics guiGraphics, int startX, int startY, int width, int height) {
        FluidUtil.renderFluid(guiGraphics, this.fluidSupplier.get(), startX, startY, width, height);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
