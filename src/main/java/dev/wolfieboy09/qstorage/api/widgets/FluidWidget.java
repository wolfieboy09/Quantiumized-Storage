package dev.wolfieboy09.qstorage.api.widgets;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

@NothingNullByDefault
public class FluidWidget extends AbstractWidget {
    private final Fluid fluid;

    public FluidWidget(Fluid fluid, int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.fluid = fluid;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int i, int i1, float v) {
        int guiWidth = graphics.guiWidth();
        int guiHeight = graphics.guiHeight();

        TextureAtlasSprite textureAtlasSprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(IClientFluidTypeExtensions.of(this.fluid).getStillTexture());
        graphics.innerBlit(TextureAtlas.LOCATION_BLOCKS, getX(), getX() + this.width, getY(), getY() + this.height, 0, textureAtlasSprite.getU0(), textureAtlasSprite.getU1(), textureAtlasSprite.getV0(), textureAtlasSprite.getV1());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
