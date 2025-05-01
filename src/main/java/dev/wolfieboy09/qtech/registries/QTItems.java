package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.storage.ItemStorageType;
import dev.wolfieboy09.qtech.item.GasCanisterItem;
import dev.wolfieboy09.qtech.item.ItemStorageDisk;
import dev.wolfieboy09.qtech.item.UpgradeItem;
import dev.wolfieboy09.qtech.item.WrenchItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class QTItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(QuantiumizedTech.MOD_ID);

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
    public static final DeferredItem<Item> QUANTUM_CIRCUIT = simpleItem("quantum_circuit");

    public static final DeferredItem<Item> SULFUR = simpleItem("sulfur");

    public static final DeferredItem<UpgradeItem> MAX_ENERGY_UPGRADE = registerUpgradeItem("max_energy_upgrade");
    public static final DeferredItem<UpgradeItem> SPEED_UPGRADE = registerUpgradeItem("speed_upgrade");

    public static final DeferredItem<ItemStorageDisk> BASIC_ITEM_DISK = registerItemStorageDisk("basic_storage_disk", ItemStorageType.BASIC);
    public static final DeferredItem<ItemStorageDisk> ADVANCED_ITEM_DISK = registerItemStorageDisk("advanced_storage_disk", ItemStorageType.ADVANCED);
    public static final DeferredItem<ItemStorageDisk> SUPERIOR_ITEM_DISK = registerItemStorageDisk("superior_storage_disk", ItemStorageType.SUPERIOR);
    public static final DeferredItem<ItemStorageDisk> QUANTUM_ITEM_DISK = registerItemStorageDisk("quantum_storage_disk", ItemStorageType.QUANTUM);
    public static final DeferredItem<ItemStorageDisk> MULTI_DIMENSIONAL_ITEM_DISK = registerItemStorageDisk("multi_dimensional_storage_disk", ItemStorageType.MULTI_DIMENSIONAL);

    public static final DeferredItem<WrenchItem> WRENCH = ITEMS.register("wrench", WrenchItem::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    private static @NotNull DeferredItem<ItemStorageDisk> registerItemStorageDisk(String name, ItemStorageType storageType) {
        return ITEMS.register(name, () -> new ItemStorageDisk(storageType));
    }

    private static @NotNull DeferredItem<UpgradeItem> registerUpgradeItem(String name) {
        return ITEMS.register(name, () -> new UpgradeItem(new Item.Properties(), "tooltip.qtech." + name));
    }

    private static @NotNull DeferredItem<Item> simpleItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }
}
