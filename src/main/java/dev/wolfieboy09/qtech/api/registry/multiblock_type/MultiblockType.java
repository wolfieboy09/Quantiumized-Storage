package dev.wolfieboy09.qtech.api.registry.multiblock_type;

import com.mojang.serialization.Codec;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.gas.Gas;
import dev.wolfieboy09.qtech.api.registry.gas.GasBuilder;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public class MultiblockType {
    public static final Codec<MultiblockType> CODEC = QTRegistries.MULTIBLOCK_TYPE.byNameCodec();
    private String descriptionId;
    private ResourceLocation location;
    private final Holder<MultiblockType> builtInRegistryHolder;

    public static final StreamCodec<RegistryFriendlyByteBuf, MultiblockType> STREAM_CODEC = ByteBufCodecs.registry(QTRegistries.MULTIBLOCK_TYPE_KEY);

    public MultiblockType() {
        this.builtInRegistryHolder = QTRegistries.MULTIBLOCK_TYPE.wrapAsHolder(this);
    }

    public ResourceLocation getLocation() {
        if (this.location == null) {
            this.location = QTRegistries.MULTIBLOCK_TYPE.getKey(this);
        }
        return this.location;
    }

    public ResourceLocation getMultiblockType() {
        return getLocation();
    }

    public Holder<MultiblockType> getBuiltInRegistryHolder() {
        return this.builtInRegistryHolder;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("multiblock_type", QTRegistries.MULTIBLOCK_TYPE.getKey(this));
        }
        return this.descriptionId;
    }
}
