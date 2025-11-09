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
}