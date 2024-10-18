package dev.wolfieboy09.qstorage;

import com.mojang.logging.LogUtils;
import dev.wolfieboy09.qstorage.component.QSDataComponents;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import dev.wolfieboy09.qstorage.registries.QSItems;
import dev.wolfieboy09.qstorage.registries.QSRegistrate;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(QuantiumizedStorage.MOD_ID)
public class QuantiumizedStorage {
    public static final String MOD_ID = "qstorage";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final QSRegistrate REGISTRATE = QSRegistrate.create(MOD_ID);

    public QuantiumizedStorage(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);
        QSDataComponents.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        QSItems.init();
        QSBlockEntities.init();
        QSBlocks.init();

        NeoForge.EVENT_BUS.register(this);

        // modEventBus.addListener(this::addCreative);

        // modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Lets dive into getting small");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

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
