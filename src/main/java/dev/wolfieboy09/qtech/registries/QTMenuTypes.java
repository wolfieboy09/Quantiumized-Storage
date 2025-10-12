package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.block.disk_assembler.DiskAssemblerMenu;
import dev.wolfieboy09.qtech.block.disk_assembler.DiskAssemblerScreen;
import dev.wolfieboy09.qtech.block.gas_canister.GasCanisterMenu;
import dev.wolfieboy09.qtech.block.gas_canister.GasCanisterScreen;
import dev.wolfieboy09.qtech.block.smeltery.SmelteryMenu;
import dev.wolfieboy09.qtech.block.smeltery.SmelteryScreen;
import dev.wolfieboy09.qtech.block.storage_matrix.StorageMatrixMenu;
import dev.wolfieboy09.qtech.block.storage_matrix.StorageMatrixScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = QuantiumizedTech.MOD_ID)
public class QTMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, QuantiumizedTech.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<DiskAssemblerMenu>> DISK_ASSEMBLER = MENUS.register("disk_assembler", () ->
            IMenuTypeExtension.create((id, playerInv, data) -> new DiskAssemblerMenu(id, data.readBlockPos(), playerInv, playerInv.player)));

    public static final DeferredHolder<MenuType<?>, MenuType<StorageMatrixMenu>> STORAGE_MATRIX = MENUS.register("storage_matrix", () ->
            IMenuTypeExtension.create((id, playerInv, data) -> new StorageMatrixMenu(id, data.readBlockPos(), playerInv, playerInv.player)));

    public static final DeferredHolder<MenuType<?>, MenuType<SmelteryMenu>> SMELTERY_MENU = MENUS.register("smeltery", () ->
            IMenuTypeExtension.create((id, playerInv, data) -> new SmelteryMenu(id, data.readBlockPos(), playerInv, playerInv.player)));

    public static final DeferredHolder<MenuType<?>, MenuType<GasCanisterMenu>> GAS_CANISTER_MENU = MENUS.register("gas_filler", () ->
            IMenuTypeExtension.create((id, playerInv, data) -> new GasCanisterMenu(id, data.readBlockPos(), playerInv, playerInv.player)));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }

    @SubscribeEvent
    public static void registerScreens(@NotNull RegisterMenuScreensEvent event) {
        event.register(QTMenuTypes.DISK_ASSEMBLER.get(), DiskAssemblerScreen::new);
        event.register(QTMenuTypes.STORAGE_MATRIX.get(), StorageMatrixScreen::new);
        event.register(QTMenuTypes.SMELTERY_MENU.get(), SmelteryScreen::new);
        event.register(QTMenuTypes.GAS_CANISTER_MENU.get(), GasCanisterScreen::new);
    }
}
