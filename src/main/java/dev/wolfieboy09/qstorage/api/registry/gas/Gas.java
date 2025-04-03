package dev.wolfieboy09.qstorage.api.registry.gas;

import com.mojang.serialization.Codec;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public class Gas implements GasLike {
    public static final Codec<Gas> CODEC = QSRegistries.GAS.byNameCodec();
    private String descriptionId;
    private ResourceLocation location;
    private final Holder<Gas> builtInRegistryHolder;

    public static final StreamCodec<RegistryFriendlyByteBuf, Gas> STREAM_CODEC = ByteBufCodecs.registry(QSRegistries.GAS_KEY);

    protected final GasBuilder gasBuilder;

    public Gas(GasBuilder builder) {
        this.gasBuilder = builder;
        this.builtInRegistryHolder = QSRegistries.GAS.wrapAsHolder(this);
    }

    public GasInfo getGasData() {
        return this.gasBuilder;
    }

    protected String getOrCreateDescriptionId() {
       if (this.descriptionId == null) {
           this.descriptionId = Util.makeDescriptionId("gas", QSRegistries.GAS.getKey(this));
       }
       return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getName() {
        return Component.translatable(this.getDescriptionId());
    }

    public Gas copy() {
        return new Gas(this.gasBuilder);
    }

    public ResourceLocation getResourceLocation() {
        if (this.location == null) {
            this.location = QSRegistries.GAS.getKey(this);
        }
        return this.location;
    }

    @Override
    public Gas asGas() {
        return this;
    }

    public Holder<Gas> builtInRegistryHolder() {
        return this.builtInRegistryHolder;
    }
}
