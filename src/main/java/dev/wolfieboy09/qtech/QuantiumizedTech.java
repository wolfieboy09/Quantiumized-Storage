package dev.wolfieboy09.qtech;

import com.mojang.logging.LogUtils;
import dev.wolfieboy09.qtech.component.QTDataComponents;
import dev.wolfieboy09.qtech.integration.cctweaked.CCTweakedPlugin;
import dev.wolfieboy09.qtech.registries.*;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.LoadingModList;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(QuantiumizedTech.MOD_ID)
public class QuantiumizedTech {
    public static final String MOD_ID = "qtech";
    public static final Logger LOGGER = LogUtils.getLogger();

    public QuantiumizedTech(@NotNull IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);

        QTDataComponents.register(modEventBus);
        QTItems.register(modEventBus);
        QTBlockEntities.register(modEventBus);
        QTBlocks.register(modEventBus);
        QTRecipes.register(modEventBus);
        QTCreativeTab.register(modEventBus);
        QTMenuTypes.register(modEventBus);
        QTParticleTypes.register(modEventBus);
        QTGasses.register(modEventBus);
        QTEffects.register(modEventBus);
        modEventBus.addListener(QTPackets::register);
        modEventBus.addListener(QTEntities::registerRenderers);
        QTEntities.register(modEventBus);

        // Register before neoforge because wiki says so
        modEventBus.addListener(EventPriority.HIGH, QTEvents::registerRegistries);
        modEventBus.addListener(QTEvents::particle);
        modEventBus.register(QTClientEvents.class);

        // Check to see if CC: Tweaked is present when mod loading, and register the peripherals there
        if (LoadingModList.get().getModFileById("computercraft") != null) {
            CCTweakedPlugin.register();
        }

        modEventBus.addListener(QTDataMaps::register);

        //modEventBus.addListener(QTEntities::registerAttributes);
        //modEventBus.register(QTShading.class);
        // modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Lets dive into getting small");
    }
}
