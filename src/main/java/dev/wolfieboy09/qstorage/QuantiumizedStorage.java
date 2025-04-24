package dev.wolfieboy09.qstorage;

import com.mojang.logging.LogUtils;
import dev.wolfieboy09.qstorage.component.QSDataComponents;
import dev.wolfieboy09.qstorage.registries.*;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(QuantiumizedStorage.MOD_ID)
public class QuantiumizedStorage {
    public static final String MOD_ID = "qstorage";
    public static final Logger LOGGER = LogUtils.getLogger();

    public QuantiumizedStorage(@NotNull IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);

        QSDataComponents.register(modEventBus);
        QSItems.register(modEventBus);
        QSBlockEntities.register(modEventBus);
        QSBlocks.register(modEventBus);
        QSRecipes.register(modEventBus);
        QSCreativeTab.register(modEventBus);
        QSMenuTypes.register(modEventBus);
        QSParticleTypes.register(modEventBus);
        QSGasses.register(modEventBus);
        QSEffects.register(modEventBus);
        modEventBus.addListener(QSPackets::register);
        //modEventBus.addListener(QSEntities::registerAttributes);
        modEventBus.addListener(QSEntities::registerRenderers);
        QSEntities.register(modEventBus);

        // Register before neoforge because wiki says so
        modEventBus.addListener(EventPriority.HIGH, QSEvents::registerRegistries);
        modEventBus.addListener(QSEvents::particle);
        modEventBus.register(QSClientEvents.class);

        modEventBus.addListener(QSDataMaps::register);

//        modEventBus.register(QSShading.class);

        // modEventBus.addListener(this::addCreative);

        // modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Lets dive into getting small");
    }
}
