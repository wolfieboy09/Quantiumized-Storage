package dev.wolfieboy09.qstorage.registries.tags;

import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class QSItemTags {
    // Global tags
    public static final TagKey<Item> STEEL_INGOT = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/steel"));
    public static final TagKey<Item> SILICON = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "silicon"));

    // Mod Tags
    public static final TagKey<Item> DATA_CRYSTAL = ItemTags.create(ResourceHelper.asResource("data_crystals"));
    public static final TagKey<Item> ITEM_PORT = ItemTags.create(ResourceHelper.asResource("item_ports"));
}
