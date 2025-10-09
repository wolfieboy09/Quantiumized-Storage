package dev.wolfieboy09.qtech;

import dev.wolfieboy09.qtech.block.pipe.PipeBlockFacadeRenderer;
import dev.wolfieboy09.qtech.block.smeltery.SmelteryBlockEntityRenderer;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class QTClientEvents {

    @SubscribeEvent
    public static void blockEntityRenders(EntityRenderersEvent.@NotNull RegisterRenderers event) {
        event.registerBlockEntityRenderer(QTBlockEntities.SMELTERY.get(), SmelteryBlockEntityRenderer::new);
        List.of(QTBlockEntities.ITEM_PIPE, QTBlockEntities.FLUID_PIPE, QTBlockEntities.ENERGY_PIPE)
                .forEach(pipe ->
                        event.registerBlockEntityRenderer(pipe.get(), PipeBlockFacadeRenderer::new)
        );
    }


//    private static ShaderInstance oxygenShader = null;
//    private static boolean appliedShader = false;
//
//    @SubscribeEvent
//    public static void onClientTick(ClientTickEvent.Post event) {
//        Player player = Minecraft.getInstance().player;
//        if (player == null) return;
//
//        MobEffectInstance effect = player.getEffect(QTEffects.OXYGEN_DEPRIVATION);
//        if (effect != null) {
//            applyShader(effect.getDuration());
//        } else {
//            removeShader();
//        }
//    }
//
//    private static void applyShader(int duration) {
//        Minecraft mc = Minecraft.getInstance();
//
//        if (!appliedShader) {
//            try {
//                if (oxygenShader == null) {
//                    mc.gameRenderer.loadEffect(ResourceLocation.withDefaultNamespace("shaders/core/post_hypoxia.json"));
//                    oxygenShader = mc.gameRenderer.getShader("oxygen_deprivation");
//                }
//
//                if (oxygenShader != null) {
//                    appliedShader = true;
//                } else {
//                    System.out.println("Failed to load shader: oxygen_deprivation");
//                }
//            } catch (Exception e) {
//                appliedShader = false;
//                System.out.println("Error loading shader: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//
//        if (appliedShader && oxygenShader != null) {
//            float severity = Math.min(1.0F, (600.0F - duration) / 600.0F);
//            Uniform serv = oxygenShader.getUniform("Severity");
//            if (serv != null) {
//                serv.set(severity);
//            }
//        }
//    }
//
//    private static void removeShader() {
//        if (appliedShader) {
//            Minecraft.getInstance().gameRenderer.shutdownEffect();
//            oxygenShader = null;
//            appliedShader = false;
//        }
//    }
}