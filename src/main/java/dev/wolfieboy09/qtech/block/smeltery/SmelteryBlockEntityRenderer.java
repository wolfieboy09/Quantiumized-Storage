package dev.wolfieboy09.qtech.block.smeltery;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

@NothingNullByDefault
public class SmelteryBlockEntityRenderer implements BlockEntityRenderer<SmelteryBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public SmelteryBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    // Thanks to Random (random832) on the NeoForge server for making this code so much better
    @Override
    public void render(SmelteryBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        IFluidHandler handler = blockEntity.getFluidHandler();
        Level level = blockEntity.getLevel();

        if (handler == null || level == null) return;

        FluidStack inputFluid1 = handler.getFluidInTank(0);
        FluidStack inputFluid2 = handler.getFluidInTank(1);
        FluidStack inputFluid3 = handler.getFluidInTank(2);
        FluidStack resultFluid = handler.getFluidInTank(3);
        FluidStack wasteFluid = handler.getFluidInTank(4);
        //TODO Get all 4 directions showing the fluid correctly
//        int[] rotationAngle = switch (blockEntity.getBlockState().getValue(SmelteryBlock.FACING)) {
//            case SOUTH -> new int[] {180, 90};
//            default -> new int[] {90, 270};
//        };

        BlockPos pos = blockEntity.getBlockPos();
//        drawFluid(blockEntity, poseStack, multiBufferSource, packedLight, packedOverlay, inputFluid1, level, pos, 0.625f, 0.188f, 0.25f, 0.75f, 0.2f, -1.24f, 0.487f, rotationAngle[0], handler.getTankCapacity(0));
//        drawFluid(blockEntity, poseStack, multiBufferSource, packedLight, packedOverlay, inputFluid2, level, pos, 0.625f, -0.13f, 0.25f, 0.76f, -0.115f, -1.24f, 0.487f, rotationAngle[0], handler.getTankCapacity(1));
//        drawFluid(blockEntity, poseStack, multiBufferSource, packedLight, packedOverlay, inputFluid3, level, pos, 0.625f, -0.44f, 0.25f, 0.75f, -0.43f, -1.24f, 0.487f, rotationAngle[0], handler.getTankCapacity(2));
//        drawFluid(blockEntity, poseStack, multiBufferSource, packedLight, packedOverlay, resultFluid, level, pos, -0.24f, -0.44f, 0.27f, 0.88f, -0.75f, -0.22f, 0.3f, rotationAngle[1], handler.getTankCapacity(3));
//        drawFluid(blockEntity, poseStack, multiBufferSource, packedLight, packedOverlay, wasteFluid, level, pos, -0.24f, 0.06f, 0.27f, 0.88f, -1.3f, -0.22f, 0.3f, rotationAngle[1], handler.getTankCapacity(4));

        drawFluid(blockEntity, poseStack, multiBufferSource, packedLight, packedOverlay, inputFluid1, level, pos, 0.625f, 0.188f, 0.25f, 0.75f, 0.2f, -1.24f, 0.487f, 90, handler.getTankCapacity(0));
        drawFluid(blockEntity, poseStack, multiBufferSource, packedLight, packedOverlay, inputFluid2, level, pos, 0.625f, -0.13f, 0.25f, 0.76f, -0.115f, -1.24f, 0.487f, 90, handler.getTankCapacity(1));
        drawFluid(blockEntity, poseStack, multiBufferSource, packedLight, packedOverlay, inputFluid3, level, pos, 0.625f, -0.44f, 0.25f, 0.75f, -0.43f, -1.24f, 0.487f, 90, handler.getTankCapacity(2));
        drawFluid(blockEntity, poseStack, multiBufferSource, packedLight, packedOverlay, resultFluid, level, pos, -0.24f, -0.44f, 0.27f, 0.88f, -0.75f, -0.22f, 0.3f, 270, handler.getTankCapacity(3));
        drawFluid(blockEntity, poseStack, multiBufferSource, packedLight, packedOverlay, wasteFluid, level, pos, -0.24f, 0.06f, 0.27f, 0.88f, -1.3f, -0.22f, 0.3f, 270, handler.getTankCapacity(4));
    }

    private static void drawFluid(SmelteryBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, FluidStack fluid, BlockAndTintGetter level, BlockPos pos, float x0, float z0, float x1, float z2, float x3, float z3, float x4, int rotationAngle, int tankCapacity) {
        if (!fluid.isEmpty()) {
            FluidState state = fluid.getFluid().defaultFluidState();
            IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluid.getFluid());
            ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluid);
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(stillTexture);
            int tintColor = fluidTypeExtensions.getTintColor(state, level, pos);
            VertexConsumer builder = multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(state));
            float height = (((float) fluid.getAmount() / tankCapacity) * 0.635f) + 0.26f;

            if(fluid.getAmount() < tankCapacity) {
                poseStack.pushPose();
                poseStack.translate(x0, 0.05f, z0);
                drawQuad(builder, poseStack, x1, height, 0.5f, 0.365f, height, z2, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
                poseStack.popPose();
            }

            poseStack.pushPose();
            poseStack.mulPose(Axis.YN.rotationDegrees(rotationAngle));
            poseStack.translate(x3, 0.05f, z3);
            drawQuad(builder, poseStack, x4, 0, 0.25f, 0.74f, height, 0.25f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), packedLight, packedOverlay, tintColor);
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