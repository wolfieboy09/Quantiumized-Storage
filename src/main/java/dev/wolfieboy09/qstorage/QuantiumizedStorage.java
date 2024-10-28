package dev.wolfieboy09.qstorage;

import com.mojang.logging.LogUtils;
import dev.wolfieboy09.qstorage.component.QSDataComponents;
import dev.wolfieboy09.qstorage.registries.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(QuantiumizedStorage.MOD_ID)
public class QuantiumizedStorage {
    public static final String MOD_ID = "qstorage";
    public static final Logger LOGGER = LogUtils.getLogger();

    public QuantiumizedStorage(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);

        QSDataComponents.register(modEventBus);
        QSItems.register(modEventBus);
        QSBlockEntities.register(modEventBus);
        QSBlocks.register(modEventBus);
        QSRecipes.register(modEventBus);
        QSCreativeTab.register(modEventBus);
        QSMenuTypes.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        // modEventBus.addListener(this::addCreative);

        // modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Lets dive into getting small");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
