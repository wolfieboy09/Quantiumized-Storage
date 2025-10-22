package dev.wolfieboy09.qtech.client;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.quantipedia.QuantiReader;
import dev.wolfieboy09.qtech.quantipedia.rendering.QuantipediaScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = "qtech", value = Dist.CLIENT)
public class KeyInputHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (QuantiumizedTech.testKey.consumeClick()) {
            Minecraft.getInstance().setScreen(
                    new QuantipediaScreen(Component.literal("hi"),
                            QuantiReader.roots.getFirst()
                    ));
        }
    }
}
