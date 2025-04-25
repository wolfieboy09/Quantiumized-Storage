package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.util.ColorUtil;
import dev.wolfieboy09.qtech.effects.OxygenDeprivationEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class QTEffects {
        public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, QuantiumizedTech.MOD_ID);

        public static final Holder<MobEffect> OXYGEN_DEPRIVATION = MOB_EFFECTS.register("oxygen_deprivation", () -> new OxygenDeprivationEffect(MobEffectCategory.HARMFUL, ColorUtil.fromArgb(128, 0, 256, 0))
                .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.withDefaultNamespace("effect.slowness"), -0.6F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        public static void register(IEventBus bus) {
                MOB_EFFECTS.register(bus);
        }
}
