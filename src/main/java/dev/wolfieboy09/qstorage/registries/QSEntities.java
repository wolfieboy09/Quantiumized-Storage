package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.entity.GasCloudEntity;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class QSEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, QuantiumizedStorage.MOD_ID);

    public static final Supplier<EntityType<GasCloudEntity>> GAS_CLOUD =
            ENTITY_TYPES.register("gas_cloud",
                    () -> EntityType.Builder.of(GasCloudEntity::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .fireImmune()
                            .canSpawnFarFromPlayer()
                            .build(QuantiumizedStorage.MOD_ID + ":gas_cloud"));

    public static void registerAttributes(@NotNull EntityAttributeCreationEvent event) {
        //event.put(QSEntities.GAS_CLOUD.get(), GasCloudEntity.createAttributes().build());
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(QSEntities.GAS_CLOUD.get(), NoopRenderer::new);
    }

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
