package dev.wolfieboy09.qstorage;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.logging.LogUtils;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.slf4j.Logger;

import java.io.IOException;

@EventBusSubscriber(value = Dist.CLIENT, modid = QuantiumizedStorage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class QSShading {
    private static ShaderInstance shaderInstance;
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ShaderInstance getShaderInstance() {
        return shaderInstance;
    }

    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceHelper.asResource("oxygen_deprivation"), DefaultVertexFormat.BLIT_SCREEN), instance -> shaderInstance = instance);
        } catch (IOException e) {
            LOGGER.error("Failed to create shader of type 'oxygen_deprivation'");
            throw new RuntimeException(e);
        }
    }

}
