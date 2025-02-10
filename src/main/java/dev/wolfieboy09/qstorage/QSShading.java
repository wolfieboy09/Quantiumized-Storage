package dev.wolfieboy09.qstorage;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.function.Function;

//@EventBusSubscriber(value = Dist.CLIENT, modid = QuantiumizedStorage.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class QSShading {
    private static ShaderInstance shaderInstance;
    private static final Logger LOGGER = LogUtils.getLogger();


    public static RenderType brightSolid(ResourceLocation texture)
    {
        return CustomRenderType.OXYGEN_DEPRIVATION.apply(texture);
    }


    public static ShaderInstance getShaderInstance() {
        return shaderInstance;
    }

    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceHelper.asResource("oxygen_deprivation"), DefaultVertexFormat.BLIT_SCREEN), instance -> CustomRenderType.holdingShader = instance);
        } catch (IOException e) {
            LOGGER.error("Failed to create shader of type 'oxygen_deprivation'");
            throw new RuntimeException(e);
        }
    }

    private static class CustomRenderType extends RenderType {
        private static ShaderInstance holdingShader;
        private static final RenderStateShard.ShaderStateShard RENDERTYPE_OXYGEN_DEPRIVATION_SHADER = new RenderStateShard.ShaderStateShard(() -> holdingShader);
        public static Function<ResourceLocation, RenderType> OXYGEN_DEPRIVATION = Util.memoize(CustomRenderType::oxygenDeprivation);

        private CustomRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
            super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
        }

        private static @NotNull RenderType oxygenDeprivation(ResourceLocation location) {
            RenderType.CompositeState state = RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_OXYGEN_DEPRIVATION_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(location, false, false))
                    .setTransparencyState(NO_TRANSPARENCY)
                    .setLightmapState(NO_LIGHTMAP)
                    .setOverlayState(NO_OVERLAY)
                    .createCompositeState(true);
            return create("oxygen_deprivation", DefaultVertexFormat.BLIT_SCREEN, VertexFormat.Mode.QUADS, 256, true, false, state);
        }
    }

}
