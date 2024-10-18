package dev.wolfieboy09.qstorage.registries;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.storage.ItemStorageType;
import dev.wolfieboy09.qstorage.item.ItemStorageDisk;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import static dev.wolfieboy09.qstorage.QuantiumizedStorage.REGISTRATE;

public class QSItems {
    public static final ItemEntry<Item> SILICON = simpleItem("silicon");
    public static final ItemEntry<Item> STEEL_CASING = simpleItem("steel_casing");
    public static final ItemEntry<Item> DATA_CORE = simpleItem("data_core");

    public static final ItemEntry<Item> STEEL_SCREW = simpleItem("steel_screw");
    public static final ItemEntry<Item> STEEL_INGOT = simpleItem("steel_ingot");

    public static final ItemEntry<Item> ITEM_PORT = simpleItem("item_port");

    public static final ItemEntry<ItemStorageDisk> BASIC_ITEM_DISK = registerItemStorageDisk("basic_storage_disk", ItemStorageType.BASIC);
    public static final ItemEntry<ItemStorageDisk> ADVANCED_ITEM_DISK = registerItemStorageDisk("advanced_storage_disk", ItemStorageType.ADVANCED);
    public static final ItemEntry<ItemStorageDisk> SUPERIOR_ITEM_DISK = registerItemStorageDisk("superior_storage_disk", ItemStorageType.SUPERIOR);
    public static final ItemEntry<ItemStorageDisk> QUANTUM_ITEM_DISK = registerItemStorageDisk("quantum_storage_disk", ItemStorageType.QUANTUM);
    public static final ItemEntry<ItemStorageDisk> MULTI_DIMENSIONAL_ITEM_DISK = registerItemStorageDisk("multi_dimensional_storage_disk", ItemStorageType.MULTI_DIMENSIONAL);

    public static void init() {
        QuantiumizedStorage.LOGGER.info("Item Registry Initialized");
    }

    private static @NotNull ItemEntry<ItemStorageDisk> registerItemStorageDisk(String name, ItemStorageType type) {
        return register(name, props -> new ItemStorageDisk(type)).register();
    }

    private static @NotNull ItemEntry<Item> simpleItem(String name) {
        return register(name, Item::new).register();
    }


    private static <T extends Item> @NotNull ItemBuilder<T, QSRegistrate> register(String name, NonNullFunction<Item.Properties, T> factory) {
        return REGISTRATE.item(name, factory);
    }
}
