package dev.wolfieboy09.qtech.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.multiblock.MultiblockPattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@EventBusSubscriber(value = Dist.CLIENT, modid = QuantiumizedTech.MOD_ID)
public class MultiblockGhostRenderer {

    private static BlockPos controllerPos = null;
    private static MultiblockPattern pattern = null;
    private static Direction facing = Direction.NORTH;
    private static long hideTime = 0;
    private static final float GHOST_SCALE = 0.9f;
    private static final float GHOST_ALPHA = 0.8f;

    /**
     * Show ghost blocks for a pattern
     * @param pos Controller position
     * @param targetPattern Pattern to display
     * @param controllerFacing Direction the controller is facing
     * @param durationTicks How long to show (in ticks)
     */
    public static void show(BlockPos pos, MultiblockPattern targetPattern, Direction controllerFacing, int durationTicks) {
        controllerPos = pos;
        pattern = targetPattern;
        facing = controllerFacing;
        hideTime = System.currentTimeMillis() + (durationTicks * 50L);
    }

    public static void hide() {
        controllerPos = null;
        pattern = null;
        facing = Direction.NORTH;
        hideTime = 0;
    }

    public static boolean isShowing() {
        return controllerPos != null && pattern != null && System.currentTimeMillis() < hideTime;
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        if (!isShowing()) {
            return;
        }

        if (System.currentTimeMillis() * 0.05 > hideTime) {
            hide();
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level == null) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        Vec3 camPos = event.getCamera().getPosition();

        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);

        renderGhostBlocks(poseStack, bufferSource, level);

        poseStack.popPose();
        bufferSource.endBatch();
    }

    private static void renderGhostBlocks(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Level level) {
        // Get positions with rotation applied using the pattern's method
        Map<BlockPos, Character> positions = pattern.getAllPositions(controllerPos, facing);

        for (Map.Entry<BlockPos, Character> entry : positions.entrySet()) {
            BlockPos worldPos = entry.getKey();

            if (worldPos.equals(controllerPos)) {
                continue;
            }

            // Use the pattern's method to get supported blocks at this world position
            List<Block> blocks = pattern.getSupportedBlocksAtWorld(controllerPos, facing, worldPos);

            renderGhostBlock(poseStack, bufferSource, worldPos, blocks.isEmpty() ? null : blocks.getFirst(), level);
        }
    }


    private static void renderGhostBlock(PoseStack poseStack, MultiBufferSource bufferSource,
                                         BlockPos pos, @Nullable Block block, Level level) {
        poseStack.pushPose();

        poseStack.translate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        poseStack.scale(GHOST_SCALE, GHOST_SCALE, GHOST_SCALE);
        poseStack.translate(-0.5, -0.5, -0.5);

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        if (block != null) {
            int light = LevelRenderer.getLightColor(level, pos);
            dispatcher.renderSingleBlock(block.defaultBlockState(), poseStack, bufferSource, light, 0);
        }

        poseStack.popPose();
    }

    private static void renderTranslucentBox(PoseStack poseStack, VertexConsumer consumer, int packedLight,
                                             double minX, double minY, double minZ,
                                             double maxX, double maxY, double maxZ,
                                             float r, float g, float b, float a) {
        var matrix = poseStack.last().pose();

        vertex(consumer, matrix, packedLight, (float)minX, (float)minY, (float)minZ, r, g, b, a, 0, -1, 0);
        vertex(consumer, matrix, packedLight, (float)maxX, (float)minY, (float)minZ, r, g, b, a, 0, -1, 0);
        vertex(consumer, matrix, packedLight, (float)maxX, (float)minY, (float)maxZ, r, g, b, a, 0, -1, 0);
        vertex(consumer, matrix, packedLight, (float)minX, (float)minY, (float)maxZ, r, g, b, a, 0, -1, 0);

        // Top face (Y+)
        vertex(consumer, matrix, packedLight, (float)minX, (float)maxY, (float)maxZ, r, g, b, a, 0, 1, 0);
        vertex(consumer, matrix, packedLight, (float)maxX, (float)maxY, (float)maxZ, r, g, b, a, 0, 1, 0);
        vertex(consumer, matrix, packedLight, (float)maxX, (float)maxY, (float)minZ, r, g, b, a, 0, 1, 0);
        vertex(consumer, matrix, packedLight, (float)minX, (float)maxY, (float)minZ, r, g, b, a, 0, 1, 0);

        // North face (Z-)
        vertex(consumer, matrix, packedLight, (float)minX, (float)minY, (float)minZ, r, g, b, a, 0, 0, -1);
        vertex(consumer, matrix, packedLight, (float)minX, (float)maxY, (float)minZ, r, g, b, a, 0, 0, -1);
        vertex(consumer, matrix, packedLight, (float)maxX, (float)maxY, (float)minZ, r, g, b, a, 0, 0, -1);
        vertex(consumer, matrix, packedLight, (float)maxX, (float)minY, (float)minZ, r, g, b, a, 0, 0, -1);

        // South face (Z+)
        vertex(consumer, matrix, packedLight, (float)maxX, (float)minY, (float)maxZ, r, g, b, a, 0, 0, 1);
        vertex(consumer, matrix, packedLight, (float)maxX, (float)maxY, (float)maxZ, r, g, b, a, 0, 0, 1);
        vertex(consumer, matrix, packedLight, (float)minX, (float)maxY, (float)maxZ, r, g, b, a, 0, 0, 1);
        vertex(consumer, matrix, packedLight, (float)minX, (float)minY, (float)maxZ, r, g, b, a, 0, 0, 1);

        // West face (X-)
        vertex(consumer, matrix, packedLight, (float)minX, (float)minY, (float)maxZ, r, g, b, a, -1, 0, 0);
        vertex(consumer, matrix, packedLight, (float)minX, (float)maxY, (float)maxZ, r, g, b, a, -1, 0, 0);
        vertex(consumer, matrix, packedLight, (float)minX, (float)maxY, (float)minZ, r, g, b, a, -1, 0, 0);
        vertex(consumer, matrix, packedLight, (float)minX, (float)minY, (float)minZ, r, g, b, a, -1, 0, 0);

        // East face (X+)
        vertex(consumer, matrix, packedLight, (float)maxX, (float)minY, (float)minZ, r, g, b, a, 1, 0, 0);
        vertex(consumer, matrix, packedLight, (float)maxX, (float)maxY, (float)minZ, r, g, b, a, 1, 0, 0);
        vertex(consumer, matrix, packedLight, (float)maxX, (float)maxY, (float)maxZ, r, g, b, a, 1, 0, 0);
        vertex(consumer, matrix, packedLight, (float)maxX, (float)minY, (float)maxZ, r, g, b, a, 1, 0, 0);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix,
                               int packedLight,
                               float x, float y, float z,
                               float r, float g, float b, float a,
                               float nx, float ny, float nz) {
        consumer.addVertex(matrix, x, y, z)
                .setColor(r, g, b, a)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(nx, ny, nz);
    }
}
