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
}