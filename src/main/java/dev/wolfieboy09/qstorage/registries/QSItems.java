package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.storage.ItemStorageType;
import dev.wolfieboy09.qstorage.item.ItemStorageDisk;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class QSItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(QuantiumizedStorage.MOD_ID);

    public static final DeferredItem<Item> SILICON = simpleItem("silicon");
    public static final DeferredItem<Item> STEEL_CASING = simpleItem("steel_casing");
    public static final DeferredItem<Item> DATA_CRYSTAL = simpleItem("data_crystal");

    public static final DeferredItem<Item> STEEL_SCREW = simpleItem("steel_screw");
    public static final DeferredItem<Item> STEEL_INGOT = simpleItem("steel_ingot");

    public static final DeferredItem<Item> ITEM_PORT = simpleItem("item_port");

    public static final DeferredItem<Item> BASIC_CIRCUIT = simpleItem("basic_circuit");
    public static final DeferredItem<Item> ADVANCED_CIRCUIT = simpleItem("advanced_circuit");
    public static final DeferredItem<Item> ELITE_CIRCUIT = simpleItem("elite_circuit");
    public static final DeferredItem<Item> ULTIMATE_CIRCUIT = simpleItem("ultimate_circuit");

    public static final DeferredItem<ItemStorageDisk> BASIC_ITEM_DISK = registerItemStorageDisk("basic_storage_disk", ItemStorageType.BASIC);
    public static final DeferredItem<ItemStorageDisk> ADVANCED_ITEM_DISK = registerItemStorageDisk("advanced_storage_disk", ItemStorageType.ADVANCED);
    public static final DeferredItem<ItemStorageDisk> SUPERIOR_ITEM_DISK = registerItemStorageDisk("superior_storage_disk", ItemStorageType.SUPERIOR);
    public static final DeferredItem<ItemStorageDisk> QUANTUM_ITEM_DISK = registerItemStorageDisk("quantum_storage_disk", ItemStorageType.QUANTUM);
    public static final DeferredItem<ItemStorageDisk> MULTI_DIMENSIONAL_ITEM_DISK = registerItemStorageDisk("multi_dimensional_storage_disk", ItemStorageType.MULTI_DIMENSIONAL);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    private static @NotNull DeferredItem<ItemStorageDisk> registerItemStorageDisk(String name, ItemStorageType storageType) {
        return ITEMS.register(name, () -> new ItemStorageDisk(storageType));
    }

    private static @NotNull DeferredItem<Item> simpleItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

}
