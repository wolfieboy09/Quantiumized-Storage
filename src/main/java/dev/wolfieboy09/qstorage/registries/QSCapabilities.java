package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerBlockEntity;
import dev.wolfieboy09.qstorage.item.ItemStorageDisk;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = QuantiumizedStorage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class QSCapabilities {
    @SubscribeEvent
    public static void register(@NotNull RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, QSBlockEntities.DISK_ASSEMBLER.get(), DiskAssemblerBlockEntity::getEnergyHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, QSBlockEntities.DISK_ASSEMBLER.get(), (block, dir) -> block.getInventoryHandler());

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, QSBlockEntities.CREATIVE_ENERGY_BLOCK.get(), (block, dir) -> block.getEnergyHandler());

        for (DeferredHolder<Item, ? extends Item> item : QSItems.ITEMS.getEntries()) {
            if (item.get() instanceof ItemStorageDisk) {
                event.registerItem(
                        Capabilities.ItemHandler.ITEM,
                        (stack, ctx) -> ((ItemStorageDisk) stack.getItem()).getInventoryHandler(stack),
                        item.get()
                );
            }
        }
    }
}
