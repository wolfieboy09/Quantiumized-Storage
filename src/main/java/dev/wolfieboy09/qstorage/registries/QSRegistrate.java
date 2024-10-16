package dev.wolfieboy09.qstorage.registries;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.ItemBuilder;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.records.BaseStorageDisk;
import dev.wolfieboy09.qstorage.api.records.ItemStorageDiskRecord;
import dev.wolfieboy09.qstorage.item.ItemStorageDisk;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class QSRegistrate extends AbstractRegistrate<QSRegistrate> {

    protected QSRegistrate(String mod_id) {
        super(mod_id);
    }

    public static QSRegistrate create(String mod_id) {
        return new QSRegistrate(mod_id);
    }

    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(QuantiumizedStorage.MOD_ID);

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
}