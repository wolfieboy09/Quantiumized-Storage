package dev.wolfieboy09.qtech.api.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.storage.ItemStorageType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record ItemStorageDiskComponent(BaseStorageDisk base, ItemStorageType itemType, NonNullList<ItemStack> contents) {
    public static final Codec<ItemStorageDiskComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BaseStorageDisk.CODEC.fieldOf("disk_info").forGetter(ItemStorageDiskComponent::base),
            StringRepresentable.fromEnum(ItemStorageType::values).fieldOf("item_type").forGetter(ItemStorageDiskComponent::itemType),
            NonNullList.codecOf(ItemStack.CODEC).fieldOf("contents").forGetter(ItemStorageDiskComponent::contents)
            ).apply(instance, ItemStorageDiskComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStorageDiskComponent> STREAM_CODEC = StreamCodec.composite(
            BaseStorageDisk.STREAM_CODEC, ItemStorageDiskComponent::base,
            NeoForgeStreamCodecs.enumCodec(ItemStorageType.class), ItemStorageDiskComponent::itemType,
            ByteBufCodecs.collection(
                    NonNullList::createWithCapacity,
                    ItemStack.STREAM_CODEC
            ), ItemStorageDiskComponent::contents,
            ItemStorageDiskComponent::new
    );

    public ItemStorageDiskComponent(BaseStorageDisk base, ItemStorageType itemType, NonNullList<ItemStack> contents) {
        this.base = base;
        this.itemType = itemType;
        this.contents = contents;
    }
}
