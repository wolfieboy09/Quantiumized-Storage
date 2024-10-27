package dev.wolfieboy09.qstorage.component;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.records.BaseStorageDisk;
import dev.wolfieboy09.qstorage.api.records.ItemStorageDiskRecord;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class QSDataComponents {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, QuantiumizedStorage.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BaseStorageDisk>> BASE_STORAGE_DISK_COMPONENT = REGISTRAR.registerComponentType(
            "base_storage_disk",
            builder -> builder
                    .persistent(BaseStorageDisk.CODEC)
                    .networkSynchronized(BaseStorageDisk.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStorageDiskRecord>> ITEM_STORAGE_DISK_COMPONENT = REGISTRAR.registerComponentType(
            "item_storage_disk",
            builder -> builder
                    .persistent(ItemStorageDiskRecord.CODEC)
                    .networkSynchronized(ItemStorageDiskRecord.STREAM_CODEC)
    );

    public static void register(IEventBus eventBus) {
        REGISTRAR.register(eventBus);
    }
}
