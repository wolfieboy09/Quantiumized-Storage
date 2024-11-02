package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerBlockEntity;
import dev.wolfieboy09.qstorage.block.storage_matrix.StorageMatrixBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = QuantiumizedStorage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class QSCapabilities {
    @SubscribeEvent
    public static void register(@NotNull RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, QSBlockEntities.DISK_ASSEMBLER.get(), DiskAssemblerBlockEntity::getEnergyHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, QSBlockEntities.DISK_ASSEMBLER.get(), (block, dir) -> block.getInventoryHandler());

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, QSBlockEntities.STORAGE_MATRIX.get(), StorageMatrixBlockEntity::getEnergyHandler);
    }
}
