package dev.wolfieboy09.qstorage;

import com.mojang.logging.LogUtils;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.component.QSDataComponents;
import dev.wolfieboy09.qstorage.registries.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
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

        modEventBus.addListener(QSEvents::registerRegistries);
        modEventBus.addListener(QSEvents::particle);
        modEventBus.register(QSShading.class);

        NeoForge.EVENT_BUS.register(QuantiumizedStorage.ClientEvents.class);

        // modEventBus.addListener(this::addCreative);

        // modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Lets dive into getting small");
    }

    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
    private static class ClientEvents {
        private static ShaderInstance oxygenShader = null;
        private static boolean appliedShader = false;

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            MobEffectInstance effect = player.getEffect(QSEffects.OXYGEN_DEPRIVATION);
            if (effect != null) {
                applyShader(effect.getDuration());
            } else {
                removeShader();
            }
        }

        private static void applyShader(int duration) {
            Minecraft mc = Minecraft.getInstance();

            if (!appliedShader) {
                try {
                    if (oxygenShader == null) {
                        mc.gameRenderer.loadEffect(ResourceHelper.asResource("shaders/core/oxygen_deprivation.json"));
                        oxygenShader = mc.gameRenderer.getShader("qstorage:oxygen_deprivation");
                    }

                    if (oxygenShader != null) {
                        appliedShader = true;
                    } else {
                        System.out.println("Failed to load shader: oxygen_deprivation");
                    }
                } catch (Exception e) {
                    appliedShader = false;
                    System.out.println("Error loading shader: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            if (appliedShader && oxygenShader != null) {
                float severity = Math.min(1.0F, (600.0F - duration) / 600.0F);
                oxygenShader.safeGetUniform("Severity").set(severity);
            }
        }

        private static void removeShader() {
            if (appliedShader) {
                Minecraft.getInstance().gameRenderer.shutdownEffect();
                oxygenShader = null;
                appliedShader = false;
            }
        }
    }
}
