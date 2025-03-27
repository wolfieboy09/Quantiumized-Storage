package dev.wolfieboy09.qstorage.block.smeltery;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

@NothingNullByDefault
public class SmelteryBlockEntityRenderer implements BlockEntityRenderer<SmelteryBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public SmelteryBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

//    private void renderInputFluid1(PoseStack poseStack) {
//
//    }


    @Override
    public void render(SmelteryBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getFluidHandler() == null) return;
        FluidStack inputFluid1 = blockEntity.getFluidHandler().getFluidInTank(0);
        FluidStack inputFluid2 = blockEntity.getFluidHandler().getFluidInTank(1);
        FluidStack inputFluid3 = blockEntity.getFluidHandler().getFluidInTank(2);

        FluidStack resultFluid = blockEntity.getFluidHandler().getFluidInTank(3);
        FluidStack wasteFluid = blockEntity.getFluidHandler().getFluidInTank(4);

        Level level = blockEntity.getLevel();

        if (level == null) return;

        BlockPos pos = blockEntity.getBlockPos();
        if (!inputFluid1.isEmpty()) {
            FluidState state = inputFluid1.getFluid().defaultFluidState();
            IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(inputFluid1.getFluid());
            ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(inputFluid1);
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
            int tintColor = fluidTypeExtensions.getTintColor(state, level, pos);
            VertexConsumer builder = multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));
            float height = (((float) blockEntity.getFluidHandler().getFluidInTank(0).getAmount() / blockEntity.getFluidHandler().getTankCapacity(0)) * 0.635f) + 0.26f;

            if(blockEntity.getFluidHandler().getFluidInTank(0).getAmount() < blockEntity.getFluidHandler().getTankCapacity(0)) {
                poseStack.pushPose();
                poseStack.translate(0.625,0.05,0.188);
                drawQuad(builder, poseStack, 0.25f, height, 0.5f, 0.365f, height, 0.75f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
                poseStack.popPose();
            }

            poseStack.pushPose();
            poseStack.mulPose(Axis.YN.rotationDegrees(90));
            poseStack.translate(0.2, 0.05, -1.24);
            drawQuad(builder, poseStack, 0.487f, 0, 0.25f, 0.74f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
            poseStack.popPose();
        }

        if (!inputFluid2.isEmpty()) {
            FluidState state = inputFluid2.getFluid().defaultFluidState();
            IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(inputFluid2.getFluid());
            ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(inputFluid2);
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
            int tintColor = fluidTypeExtensions.getTintColor(state, level, pos);
            VertexConsumer builder = multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));
            float height = (((float) blockEntity.getFluidHandler().getFluidInTank(1).getAmount() / blockEntity.getFluidHandler().getTankCapacity(1)) * 0.635f) + 0.26f;

            if(blockEntity.getFluidHandler().getFluidInTank(1).getAmount() < blockEntity.getFluidHandler().getTankCapacity(1)) {
                poseStack.pushPose();
                poseStack.translate(0.625,0.05,-0.13);
                drawQuad(builder, poseStack, 0.25f, height, 0.5f, 0.365f, height, 0.75f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
                poseStack.popPose();
            }

            poseStack.pushPose();
            poseStack.mulPose(Axis.YN.rotationDegrees(90));
            poseStack.translate(-0.115, 0.05, -1.24);
            drawQuad(builder, poseStack, 0.487f, 0, 0.25f, 0.74f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
            poseStack.popPose();
        }

        if (!inputFluid3.isEmpty()) {
            FluidState state = inputFluid3.getFluid().defaultFluidState();
            IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(inputFluid3.getFluid());
            ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(inputFluid3);
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
            int tintColor = fluidTypeExtensions.getTintColor(state, level, pos);
            VertexConsumer builder = multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));
            float height = (((float) blockEntity.getFluidHandler().getFluidInTank(2).getAmount() / blockEntity.getFluidHandler().getTankCapacity(2)) * 0.635f) + 0.26f;

            if(blockEntity.getFluidHandler().getFluidInTank(2).getAmount() < blockEntity.getFluidHandler().getTankCapacity(2)) {
                poseStack.pushPose();
                poseStack.translate(0.625,0.05,-0.44);
                drawQuad(builder, poseStack, 0.25f, height, 0.5f, 0.365f, height, 0.75f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
                poseStack.popPose();
            }

            poseStack.pushPose();
            poseStack.mulPose(Axis.YN.rotationDegrees(90));
            poseStack.translate(-0.43, 0.05, -1.24);
            drawQuad(builder, poseStack, 0.487f, 0, 0.25f, 0.74f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
            poseStack.popPose();
        }

        if (!resultFluid.isEmpty()) {
            FluidState state = resultFluid.getFluid().defaultFluidState();
            IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(resultFluid.getFluid());
            ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(resultFluid);
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
            int tintColor = fluidTypeExtensions.getTintColor(state, level, pos);
            VertexConsumer builder = multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));
            float height = (((float) blockEntity.getFluidHandler().getFluidInTank(3).getAmount() / blockEntity.getFluidHandler().getTankCapacity(3)) * 0.635f) + 0.26f;

            if(blockEntity.getFluidHandler().getFluidInTank(3).getAmount() < blockEntity.getFluidHandler().getTankCapacity(3)) {
                poseStack.pushPose();
                poseStack.translate(-0.24,0.05,-0.44);
                drawQuad(builder, poseStack, 0.27f, height, 0.5f, 0.365f, height, 0.88f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
                poseStack.popPose();
            }

            poseStack.pushPose();
            poseStack.mulPose(Axis.YN.rotationDegrees(270));
            poseStack.translate(-0.75, 0.05, -0.22);
            drawQuad(builder, poseStack, 0.3f, 0, 0.25f, 0.74f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
            poseStack.popPose();
        }

        if (!wasteFluid.isEmpty()) {
            FluidState state = wasteFluid.getFluid().defaultFluidState();
            IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(wasteFluid.getFluid());
            ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(wasteFluid);
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
            int tintColor = fluidTypeExtensions.getTintColor(state, level, pos);
            VertexConsumer builder = multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));
            float height = (((float) blockEntity.getFluidHandler().getFluidInTank(4).getAmount() / blockEntity.getFluidHandler().getTankCapacity(4)) * 0.635f) + 0.26f;

            if(blockEntity.getFluidHandler().getFluidInTank(4).getAmount() < blockEntity.getFluidHandler().getTankCapacity(4)) {
                poseStack.pushPose();
                poseStack.translate(-0.24,0.05,-0.44);
                drawQuad(builder, poseStack, 0.27f, height, 0.5f, 0.365f, height, 0.88f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
                poseStack.popPose();
            }

            poseStack.pushPose();
            poseStack.mulPose(Axis.YN.rotationDegrees(270));
            poseStack.translate(-0.75, 0.05, -0.22);
            drawQuad(builder, poseStack, 0.3f, 0, 0.25f, 0.74f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
            poseStack.popPose();
        }
    }

    private static void drawVertex(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int packedLight, int packedOverlay, int color) {
        builder.addVertex(poseStack.last().pose(), x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setUv2(packedLight, packedOverlay)
                .setNormal(1, 0, 0);
    }

    private static void drawQuad(VertexConsumer builder, PoseStack poseStack, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int packedLight, int packedOverlay, int color) {
        drawVertex(builder, poseStack, x0, y0, z0, u0, v0, packedLight, packedOverlay, color);
        drawVertex(builder, poseStack, x0, y1, z1, u0, v1, packedLight, packedOverlay, color);
        drawVertex(builder, poseStack, x1, y1, z1, u1, v1, packedLight, packedOverlay, color);
        drawVertex(builder, poseStack, x1, y0, z0, u1, v0, packedLight, packedOverlay, color);
    }
}
