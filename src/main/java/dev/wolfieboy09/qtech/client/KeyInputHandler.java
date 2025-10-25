package dev.wolfieboy09.qtech.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.quantipedia.QuantiReader;
import dev.wolfieboy09.qtech.quantipedia.rendering.QuantipediaScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@NothingNullByDefault
@EventBusSubscriber(modid = "qtech", value = Dist.CLIENT)
public class KeyInputHandler {
    public static final KeyMapping openWiki = new KeyMapping(
            "key.qtech.open_wiki",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.qtech"
    );

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(openWiki);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (openWiki.consumeClick()) {
            Minecraft.getInstance().setScreen(
                    new QuantipediaScreen(Component.literal("hi"),
                            QuantiReader.roots.getFirst()
                    ));
        }
    }
}
