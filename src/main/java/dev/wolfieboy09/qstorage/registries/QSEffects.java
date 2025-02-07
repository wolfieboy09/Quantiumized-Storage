package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.effects.OxygenDeprivationEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class QSEffects {
        public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, QuantiumizedStorage.MOD_ID);

        public static final Holder<MobEffect> OXYGEN_DEPRIVATION = MOB_EFFECTS.register("oxygen_deprivation", () -> new OxygenDeprivationEffect(MobEffectCategory.HARMFUL, 0x00FF00)
                .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.withDefaultNamespace("effect.slowness"), -0.2F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        public static void register(IEventBus bus) {
                MOB_EFFECTS.register(bus);
        }
}
