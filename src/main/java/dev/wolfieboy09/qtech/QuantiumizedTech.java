package dev.wolfieboy09.qtech;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.component.QTDataComponents;
import dev.wolfieboy09.qtech.config.ClientConfig;
import dev.wolfieboy09.qtech.integration.cctweaked.CCTweakedPlugin;
import dev.wolfieboy09.qtech.quantipedia.QuantiReader;
import dev.wolfieboy09.qtech.registries.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@Mod(QuantiumizedTech.MOD_ID)
public class QuantiumizedTech {
    public static final String MOD_ID = "qtech";
    public static final Logger LOGGER = LogUtils.getLogger();

    public QuantiumizedTech(@NotNull IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerKeys);
        modEventBus.addListener(this::reload);

        QTDataComponents.register(modEventBus);
        QTItems.register(modEventBus);
        QTBlockEntities.register(modEventBus);
        QTMultiblockTypes.register(modEventBus);
        QTBlocks.register(modEventBus);
        QTRecipes.register(modEventBus);
        QTCreativeTab.register(modEventBus);
        QTMenuTypes.register(modEventBus);
        QTParticleTypes.register(modEventBus);
        QTGasses.register(modEventBus);
        QTEffects.register(modEventBus);
        QTEntities.register(modEventBus);

        modEventBus.addListener(QTPackets::register);
        modEventBus.addListener(QTEntities::registerRenderers);

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

        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Lets dive into getting small");
    }
    public static KeyMapping testKey;

    private void registerKeys(@NotNull RegisterKeyMappingsEvent event) {
        testKey = new KeyMapping(
                "key.qtech.test",             // Translation key
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,              // Default key (G)
                "key.categories.qtech"        // Category
        );

        event.register(testKey);
    }

    @NothingNullByDefault
    private void reload(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new SimplePreparableReloadListener<Void>() {
            @Override
            protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
                return null;
            }

            @Override
            protected void apply(Void o, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
                QuantiReader.loadAllWikiEntries(resourceManager);
            }
        });
    }
}
