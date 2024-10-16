package dev.wolfieboy09.qstorage.api.records;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qstorage.api.storage.StorageType;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.UUID;

public record BaseStorageDisk(UUID id, StorageType storageType){

    public static final Codec<BaseStorageDisk> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(BaseStorageDisk::id),
            StringRepresentable.fromEnum(StorageType::values).fieldOf("type").forGetter(BaseStorageDisk::storageType)
    ).apply(instance, BaseStorageDisk::new));

    public static final StreamCodec<FriendlyByteBuf, BaseStorageDisk> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, BaseStorageDisk::id,
            NeoForgeStreamCodecs.enumCodec(StorageType.class), BaseStorageDisk::storageType,
            BaseStorageDisk::new
    );


    public BaseStorageDisk(UUID id, StorageType storageType) {
        this.id = id;
        this.storageType = storageType;
    }
}
