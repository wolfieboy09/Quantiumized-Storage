package dev.wolfieboy09.qtech.integration.kubejs.events;

import dev.wolfieboy09.qtech.api.events.void_crafting.VoidCraftingEvent;
import dev.wolfieboy09.qtech.integration.kubejs.events.handler.VoidCraftingEventHandler;
import net.neoforged.bus.api.SubscribeEvent;

public class KubeEventHandlers {
    @SubscribeEvent
    public static void onVoidCrafting(VoidCraftingEvent event) {
        if (QTKubeEvents.VOID_CRAFTING.hasListeners()) {
            if (QTKubeEvents.VOID_CRAFTING.post(new VoidCraftingEventHandler(event)).interruptFalse()) {
                event.setCanceled(true);
            }
        }
    }
}
