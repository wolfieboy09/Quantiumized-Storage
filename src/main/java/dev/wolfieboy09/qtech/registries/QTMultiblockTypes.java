package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class QTMultiblockTypes {
    public static final DeferredRegister<MultiblockType> TYPES = DeferredRegister.create(QTRegistries.MULTIBLOCK_TYPE, QuantiumizedTech.MOD_ID);


    private static @NotNull Supplier<MultiblockType> create(String id) {
        return TYPES.register(id, MultiblockType::new);
    }

    public static void register(IEventBus bus) {
        TYPES.register(bus);
    }
}
