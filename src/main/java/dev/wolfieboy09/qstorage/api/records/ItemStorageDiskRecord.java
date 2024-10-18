package dev.wolfieboy09.qstorage.api.records;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qstorage.api.storage.ItemStorageType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record ItemStorageDiskRecord(BaseStorageDisk base, ItemStorageType itemType, NonNullList<ItemStack> contents) {
    public static final Codec<ItemStorageDiskRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BaseStorageDisk.CODEC.fieldOf("disk_info").forGetter(ItemStorageDiskRecord::base),
            StringRepresentable.fromEnum(ItemStorageType::values).fieldOf("item_type").forGetter(ItemStorageDiskRecord::itemType),
            NonNullList.codecOf(ItemStack.CODEC).fieldOf("contents").forGetter(ItemStorageDiskRecord::contents)
            ).apply(instance, ItemStorageDiskRecord::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStorageDiskRecord> STREAM_CODEC = StreamCodec.composite(
            BaseStorageDisk.STREAM_CODEC, ItemStorageDiskRecord::base,
            NeoForgeStreamCodecs.enumCodec(ItemStorageType.class), ItemStorageDiskRecord::itemType,
            ByteBufCodecs.collection(
                    NonNullList::createWithCapacity,
                    ItemStack.STREAM_CODEC
            ), ItemStorageDiskRecord::contents,
            ItemStorageDiskRecord::new
    );

    public ItemStorageDiskRecord(BaseStorageDisk base, ItemStorageType itemType, NonNullList<ItemStack> contents) {
        this.base = base;
        this.itemType = itemType;
        this.contents = contents;
    }
}
