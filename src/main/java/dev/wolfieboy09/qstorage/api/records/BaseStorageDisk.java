package dev.wolfieboy09.qstorage.api.records;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qstorage.api.storage.StorageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record BaseStorageDisk(StorageType storageType){

    public static final Codec<BaseStorageDisk> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StringRepresentable.fromEnum(StorageType::values).fieldOf("type").forGetter(BaseStorageDisk::storageType)
    ).apply(instance, BaseStorageDisk::new));

    public static final StreamCodec<FriendlyByteBuf, BaseStorageDisk> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(StorageType.class), BaseStorageDisk::storageType,
            BaseStorageDisk::new
    );


    public BaseStorageDisk(StorageType storageType) {
        this.storageType = storageType;
    }
}
