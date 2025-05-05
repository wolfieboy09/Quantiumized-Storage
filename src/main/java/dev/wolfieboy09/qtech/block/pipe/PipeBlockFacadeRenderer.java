package dev.wolfieboy09.qtech.block.pipe;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class PipeBlockFacadeRenderer implements BlockEntityRenderer<BasePipeBlockEntity<?>> {
    private final BlockEntityRendererProvider.Context context;

    public PipeBlockFacadeRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(BasePipeBlockEntity<?> basePipeBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = basePipeBlockEntity.getFacadeState();
        if (state != BasePipeBlockEntity.NO_FACADE_STATE) {
            dispatcher.renderSingleBlock(state, poseStack, multiBufferSource, packedLight, packedOverlay);
        }
    }
}
