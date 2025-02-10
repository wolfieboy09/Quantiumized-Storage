package dev.wolfieboy09.qstorage;

import com.mojang.blaze3d.shaders.Uniform;
import dev.wolfieboy09.qstorage.registries.QSEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

//@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class QSClientEvents {
    private static ShaderInstance oxygenShader = null;
    private static boolean appliedShader = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        MobEffectInstance effect = player.getEffect(QSEffects.OXYGEN_DEPRIVATION);
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
                    mc.gameRenderer.loadEffect(ResourceLocation.withDefaultNamespace("shaders/core/post_hypoxia.json"));
                    oxygenShader = mc.gameRenderer.getShader("oxygen_deprivation");
                }

                if (oxygenShader != null) {
                    appliedShader = true;
                } else {
                    System.out.println("Failed to load shader: oxygen_deprivation");
                }
            } catch (Exception e) {
                appliedShader = false;
                System.out.println("Error loading shader: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (appliedShader && oxygenShader != null) {
            float severity = Math.min(1.0F, (600.0F - duration) / 600.0F);
            Uniform serv = oxygenShader.getUniform("Severity");
            if (serv != null) {
                serv.set(severity);
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