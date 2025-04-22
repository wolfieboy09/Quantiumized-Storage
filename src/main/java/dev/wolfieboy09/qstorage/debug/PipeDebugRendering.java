package dev.wolfieboy09.qstorage.debug;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PipeDebugRendering {
    public static final List<BlockPos> pipes = new ArrayList<>();

    public static void render(RenderLevelStageEvent event) {
        pipes.forEach(pos -> renderBoxAroundBlockPos(event.getPoseStack(), pos));
    }

    public static void renderBoxAroundBlockPos(PoseStack matrixStack, BlockPos blockPos) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null) return;

        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.getBlock() == Blocks.AIR) {
            return;
        }

        AABB aabb = blockState.getShape(level, blockPos).bounds().move(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        matrixStack.pushPose();
        RenderSystem.setShaderColor(1.0F, 0.0F, 0.0F, 0.5F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.LINES);

        LevelRenderer.renderLineBox(matrixStack, vertexConsumer, aabb, 1, 0, 0, 0.5f);
        bufferSource.endBatch();
        matrixStack.popPose();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }
}
