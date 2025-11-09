package dev.wolfieboy09.qtech;

import com.mojang.blaze3d.shaders.Uniform;
import dev.wolfieboy09.qtech.block.pipe.PipeBlockFacadeRenderer;
import dev.wolfieboy09.qtech.block.smeltery.SmelteryBlockEntityRenderer;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import dev.wolfieboy09.qtech.registries.QTEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
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


    private static ShaderInstance oxygenShader = null;
    private static boolean appliedShader = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        MobEffectInstance effect = player.getEffect(QTEffects.OXYGEN_DEPRIVATION);
        if (effect != null) {
            applyShader(effect.getDuration());
        } else {
            removeShader();
        }
    }

    private static void applyShader(int duration) {
        Minecraft mc = Minecraft.getInstance();

        if (!appliedShader) {
            try {
                if (oxygenShader == null) {
                    mc.gameRenderer.loadEffect(ResourceLocation.withDefaultNamespace("shaders/core/oxygen_deprivation.json"));
                    oxygenShader = mc.gameRenderer.getShader("oxygen_deprivation");
                }

                if (oxygenShader != null) {
                    appliedShader = true;
                } else {
                    System.out.println("Failed to load shader: oxygen_deprivation");
                }
            } catch (Exception e) {
                appliedShader = false;
                QuantiumizedTech.LOGGER.error("Error loading shader: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }

        if (appliedShader && oxygenShader != null) {
            float severity = Math.min(1.0F, (600.0F - duration) / 600.0F);

            // Update Severity uniform
            Uniform severityUniform = oxygenShader.getUniform("Severity");
            if (severityUniform != null) {
                severityUniform.set(severity);
            }

            float spreadFactorValue = 2.0F; //TODO change dynamically?
            Uniform spreadUniform = oxygenShader.getUniform("SpreadFactor");
            if (spreadUniform != null) {
                spreadUniform.set(spreadFactorValue);
            }
        }
    }

    private static void removeShader() {
        if (appliedShader) {
            Minecraft.getInstance().gameRenderer.shutdownEffect();
            oxygenShader = null;
            appliedShader = false;
        }
    }
}