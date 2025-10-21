package dev.wolfieboy09.qtech.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue renderFluidOnSmeltery;

    public static ModConfigSpec SPEC;

    static {
        renderFluidOnSmeltery = BUILDER
                .comment("Whether to render the fluid on the side of the smeltery")
                .define("renderFluidOnSmeltery", true);

        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {

    }
}
