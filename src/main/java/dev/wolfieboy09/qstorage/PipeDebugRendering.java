package dev.wolfieboy09.qstorage;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.wolfieboy09.qstorage.packets.HandlePipePos;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PipeDebugRendering {
    private static final List<BlockPos> debugPipePositions = new ArrayList<>();

    public static void removePos(BlockPos pos) {
        if (Minecraft.getInstance().level != null && !Minecraft.getInstance().level.isClientSide) {
            PacketDistributor.sendToAllPlayers(new HandlePipePos(pos, true));
        }
        debugPipePositions.remove(pos);
    }

    public static void addPos(BlockPos pos) {
        if (Minecraft.getInstance().level != null && !Minecraft.getInstance().level.isClientSide) {
            PacketDistributor.sendToAllPlayers(new HandlePipePos(pos, false));
        }
        debugPipePositions.add(pos);
    }

    public static List<BlockPos> getPosses() {
        return debugPipePositions;
    }

    public static void render(@NotNull RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;

        Camera camera = event.getCamera();
        Vec3 cameraPos = camera.getPosition();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableDepthTest();
        RenderSystem.lineWidth(2.0F);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);

        for (BlockPos pos : debugPipePositions) {
            AABB box = new AABB(pos).inflate(1).move(-cameraPos.x, -cameraPos.y, -cameraPos.z);
            try {
                BufferUploader.drawWithShader(drawBox(buffer, box, 1.0F, 0.0F, 0.0F, 1.0F));
            } catch (Exception e) {
                System.out.println("Error caught: " + e.getMessage());
            }
        }

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }



    private static MeshData drawBox(@NotNull BufferBuilder buffer, @NotNull AABB box, float r, float g, float b, float a) {
        float x1 = (float) box.minX, y1 = (float) box.minY, z1 = (float) box.minZ;
        float x2 = (float) box.maxX, y2 = (float) box.maxY, z2 = (float) box.maxZ;

        buffer.addVertex(x1, y1, z1).setColor(r, g, b, a);
        buffer.addVertex(x2, y1, z1).setColor(r, g, b, a);

        buffer.addVertex(x2, y1, z1).setColor(r, g, b, a);
        buffer.addVertex(x2, y1, z2).setColor(r, g, b, a);

        buffer.addVertex(x2, y1, z2).setColor(r, g, b, a);
        buffer.addVertex(x1, y1, z2).setColor(r, g, b, a);

        buffer.addVertex(x1, y1, z2).setColor(r, g, b, a);
        buffer.addVertex(x1, y1, z1).setColor(r, g, b, a);

        buffer.addVertex(x1, y2, z1).setColor(r, g, b, a);
        buffer.addVertex(x2, y2, z1).setColor(r, g, b, a);

        buffer.addVertex(x2, y2, z1).setColor(r, g, b, a);
        buffer.addVertex(x2, y2, z2).setColor(r, g, b, a);

        buffer.addVertex(x2, y2, z2).setColor(r, g, b, a);
        buffer.addVertex(x1, y2, z2).setColor(r, g, b, a);

        buffer.addVertex(x1, y2, z2).setColor(r, g, b, a);
        buffer.addVertex(x1, y2, z1).setColor(r, g, b, a);

        buffer.addVertex(x1, y1, z1).setColor(r, g, b, a);
        buffer.addVertex(x1, y2, z1).setColor(r, g, b, a);

        buffer.addVertex(x2, y1, z1).setColor(r, g, b, a);
        buffer.addVertex(x2, y2, z1).setColor(r, g, b, a);

        buffer.addVertex(x2, y1, z2).setColor(r, g, b, a);
        buffer.addVertex(x2, y2, z2).setColor(r, g, b, a);

        buffer.addVertex(x1, y1, z2).setColor(r, g, b, a);
        buffer.addVertex(x1, y2, z2).setColor(r, g, b, a);

        return buffer.build();
    }
}
