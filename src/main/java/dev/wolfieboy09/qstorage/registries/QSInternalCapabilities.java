package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.capabilities.QSCapabilities;
import dev.wolfieboy09.qstorage.api.capabilities.gas.IGasHandlerItem;
import dev.wolfieboy09.qstorage.api.components.GasCanisterComponent;
import dev.wolfieboy09.qstorage.api.registry.gas.GasStack;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerBlockEntity;
import dev.wolfieboy09.qstorage.component.QSDataComponents;
import dev.wolfieboy09.qstorage.item.ItemStorageDisk;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = QuantiumizedStorage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class QSInternalCapabilities {
    @SubscribeEvent
    public static void register(@NotNull RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, QSBlockEntities.DISK_ASSEMBLER.get(), DiskAssemblerBlockEntity::getEnergyHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, QSBlockEntities.DISK_ASSEMBLER.get(), (block, dir) -> block.getInventoryHandler());

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, QSBlockEntities.CREATIVE_ENERGY_BLOCK.get(), (block, dir) -> block.getEnergyHandler());

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, QSBlockEntities.SMELTERY.get(), (block, dir) -> block.getInventory());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, QSBlockEntities.SMELTERY.get(), (block, dir) -> block.getFluidHandler());

        event.registerItem(
                QSCapabilities.GasStorage.ITEM,
                (itemStack, context) -> {
                    GasCanisterComponent handler = itemStack.get(QSDataComponents.GAS_CANISTER_COMPONENT.get());
                    return handler == null ? null : new GasItemHandlerWrapper(itemStack, handler);
                },
                QSItems.GAS_CANISTER
        );

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

    public record GasItemHandlerWrapper(ItemStack itemStack, GasCanisterComponent handler) implements IGasHandlerItem {
        @Override
        public ItemStack getContainer() {
            return this.itemStack;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public GasStack getGasInTank(int index) {
            return this.handler.getGasTank().getGas();
        }

        @Override
        public int getTankCapacity(int index) {
            return this.handler.getGasTank().getCapacity();
        }

        @Override
        public boolean isGasValid(int index, GasStack gasStack) {
            return this.handler.getGasTank().isGasValid(index, gasStack);
        }


        // In order to prevent quantum entanglement with the canisters, we must copy the data component

        @Override
        public int fill(GasStack gasStack, boolean simulate) {
            GasCanisterComponent component = new GasCanisterComponent(this.handler.gasTankHandler());
            int fill = component.getGasTank().fill(gasStack, simulate);
            this.itemStack.set(QSDataComponents.GAS_CANISTER_COMPONENT.get(), component);
            return fill;
        }

        @Override
        public GasStack drain(GasStack gasStack, boolean simulate) {
            GasCanisterComponent component = new GasCanisterComponent(this.handler.gasTankHandler());
            GasStack stack = component.getGasTank().drain(gasStack, simulate);
            this.itemStack.set(QSDataComponents.GAS_CANISTER_COMPONENT.get(), component);
            return stack;
        }

        @Override
        public GasStack drain(int maxDrain, boolean simulate) {
            GasCanisterComponent component = new GasCanisterComponent(this.handler.gasTankHandler());
            GasStack stack = component.getGasTank().drain(maxDrain, simulate);
            this.itemStack.set(QSDataComponents.GAS_CANISTER_COMPONENT.get(), component);
            return stack;
        }
    }
}
