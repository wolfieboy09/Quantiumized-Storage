package dev.wolfieboy09.qtech.api.codecs;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

public final class DimensionCodecs {
    private DimensionCodecs() {}

    public static final Codec<ResourceKey<DimensionType>> CODEC = ResourceKey.codec(Registries.DIMENSION_TYPE);

    public static final StreamCodec<ByteBuf, ResourceKey<DimensionType>> STREAM_CODEC = ResourceKey.streamCodec(Registries.DIMENSION_TYPE);
}
