package dev.wolfieboy09.qstorage.api.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record SmelteryFuel(int burnTime, int temperature) {
    public static final Codec<SmelteryFuel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("burn_time").forGetter(SmelteryFuel::burnTime),
            ExtraCodecs.POSITIVE_INT.fieldOf("temperature").forGetter(SmelteryFuel::temperature)
    ).apply(instance, SmelteryFuel::new));
}
