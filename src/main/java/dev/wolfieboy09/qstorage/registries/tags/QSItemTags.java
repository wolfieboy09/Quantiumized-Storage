package dev.wolfieboy09.qstorage.registries.tags;

import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class QSItemTags {
    // Global tags
    public static final TagKey<Item> STEEL_INGOT = globalTag("ingots/steel");
    public static final TagKey<Item> SILICON = globalTag("silicon");

    public static final TagKey<Item> BASIC_CIRCUIT = globalTag("circuits/basic");
    public static final TagKey<Item> ADVANCED_CIRCUIT = globalTag("circuits/advanced");
    public static final TagKey<Item> ELITE_CIRCUIT = globalTag("circuits/elite");
    public static final TagKey<Item> ULTIMATE_CIRCUIT = globalTag("circuits/ultimate");


    // Mod Tags
    public static final TagKey<Item> DATA_CRYSTAL = ItemTags.create(ResourceHelper.asResource("data_crystals"));
    public static final TagKey<Item> ITEM_PORT = ItemTags.create(ResourceHelper.asResource("item_ports"));

    private static @NotNull TagKey<Item> globalTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
