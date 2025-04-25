package dev.wolfieboy09.qtech.api.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

@NothingNullByDefault
public final class FluidUtil {

    /**
     * Renders the fluid stack given in a screen
     * @param guiGraphics The {@link GuiGraphics} to draw from
     * @param fluidStacks The {@link FluidStack}s to use for rendering
     * @param x The X pos to draw at
     * @param y The Y pos to draw at
     * @param width The width
     * @param height The height
     */
    public static void renderFluid(GuiGraphics guiGraphics, FluidStack[] fluidStacks, int x, int y, int width, int height) {
        for (FluidStack fluidStack : fluidStacks) {
            if (fluidStack.isEmpty() || height <= 0) return;

            Fluid fluid = fluidStack.getFluid();
            ResourceLocation fluidStill = IClientFluidTypeExtensions.of(fluid).getStillTexture();
            TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
            int fluidColor = IClientFluidTypeExtensions.of(fluid).getTintColor(fluidStack);

            float red = (float) (fluidColor >> 16 & 255) / 255.0F;
            float green = (float) (fluidColor >> 8 & 255) / 255.0F;
            float blue = (float) (fluidColor & 255) / 255.0F;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            RenderSystem.setShaderColor(red, green, blue, 1.0f);

            int zLevel = 0;
            float uMin = fluidStillSprite.getU0();
            float uMax = fluidStillSprite.getU1();
            float vMin = fluidStillSprite.getV0();
            float vMax = fluidStillSprite.getV1();
            int textureWidth = fluidStillSprite.contents().width();
            int textureHeight = fluidStillSprite.contents().height();

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder vertexBuffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            int yOffset = 0;
            while (yOffset < height) {
                int drawHeight = Math.min(textureHeight, height - yOffset);
                int drawY = y - yOffset - drawHeight; // Adjust for bottom-to-top drawing

                float vMaxAdjusted = vMin + (vMax - vMin) * ((float) drawHeight / textureHeight);

                int xOffset = 0;
                while (xOffset < width) {
                    int drawWidth = Math.min(textureWidth, width - xOffset);

                    float uMaxAdjusted = uMin + (uMax - uMin) * ((float) drawWidth / textureWidth);

                    vertexBuffer.addVertex(poseStack.last().pose(), x + xOffset, drawY + drawHeight, zLevel).setUv(uMin, vMaxAdjusted);
                    vertexBuffer.addVertex(poseStack.last().pose(), x + xOffset + drawWidth, drawY + drawHeight, zLevel).setUv(uMaxAdjusted, vMaxAdjusted);
                    vertexBuffer.addVertex(poseStack.last().pose(), x + xOffset + drawWidth, drawY, zLevel).setUv(uMaxAdjusted, vMin);
                    vertexBuffer.addVertex(poseStack.last().pose(), x + xOffset, drawY, zLevel).setUv(uMin, vMin);

                    xOffset += drawWidth;
                }
                yOffset += drawHeight;
            }

            BufferUploader.drawWithShader(vertexBuffer.buildOrThrow());
            poseStack.popPose();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.applyModelViewMatrix();
        }
    }

    /**
     * Renders the fluid stack given in a screen
     * @param guiGraphics The {@link GuiGraphics} to draw from
     * @param fluidStack The {@link FluidStack} to use for rendering
     * @param x The X pos to draw at
     * @param y The Y pos to draw at
     * @param width The width
     * @param height The height
     */
    public static void renderFluid(GuiGraphics guiGraphics, FluidStack fluidStack, int x, int y, int width, int height) {
        // It will iterate through one stack, so this will do
        renderFluid(guiGraphics, new FluidStack[] {fluidStack}, x, y, width, height);
    }
}
