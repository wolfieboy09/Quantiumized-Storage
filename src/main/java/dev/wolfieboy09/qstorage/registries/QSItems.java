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
    public static final ItemEntry<Item> SILICONE = simpleItem("silicone");

    public static final ItemEntry<ItemStorageDisk> TEST_DISK = register("test", props -> new ItemStorageDisk(props.stacksTo(1), ItemStorageType.BASIC)).register();

    public static void init() {
        QuantiumizedStorage.LOGGER.info("Item Registry Initialized");
    }

    private static @NotNull ItemEntry<Item> simpleItem(String name) {
        return register(name, Item::new).register();
    }


    private static <T extends Item> ItemBuilder<T, QSRegistrate> register(String name, NonNullFunction<Item.Properties, T> factory) {
        return REGISTRATE.item(name, factory);
    }
}
