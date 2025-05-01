package dev.wolfieboy09.qtech.component;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.components.BaseStorageDisk;
import dev.wolfieboy09.qtech.api.components.FacadeComponent;
import dev.wolfieboy09.qtech.api.components.GasCanisterComponent;
import dev.wolfieboy09.qtech.api.components.ItemStorageDiskComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class QTDataComponents {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, QuantiumizedTech.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BaseStorageDisk>> BASE_STORAGE_DISK_COMPONENT = REGISTRAR.registerComponentType(
            "base_storage_disk",
            builder -> builder
                    .persistent(BaseStorageDisk.CODEC)
                    .networkSynchronized(BaseStorageDisk.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStorageDiskComponent>> ITEM_STORAGE_DISK_COMPONENT = REGISTRAR.registerComponentType(
            "item_storage_disk",
            builder -> builder
                    .persistent(ItemStorageDiskComponent.CODEC)
                    .networkSynchronized(ItemStorageDiskComponent.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GasCanisterComponent>> GAS_CANISTER_COMPONENT = REGISTRAR.registerComponentType(
      "gas_canister",
      builder -> builder
              .persistent(GasCanisterComponent.CODEC)
              .networkSynchronized(GasCanisterComponent.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FacadeComponent>> FACADE_COMPONENT = REGISTRAR.registerComponentType(
            "facade",
            builder -> builder.persistent(FacadeComponent.CODEC)
    );

    public static void register(IEventBus eventBus) {
        REGISTRAR.register(eventBus);
    }
}
