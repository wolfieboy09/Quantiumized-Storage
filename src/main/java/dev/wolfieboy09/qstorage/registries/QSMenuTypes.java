package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerMenu;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = QuantiumizedStorage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class QSMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, QuantiumizedStorage.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<DiskAssemblerMenu>> DISK_ASSEMBLER = MENUS.register("disk_assembler", () ->
            IMenuTypeExtension.create((id, playerInv, data) -> new DiskAssemblerMenu(id, data.readBlockPos(), playerInv, playerInv.player)));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(QSMenuTypes.DISK_ASSEMBLER.get(), DiskAssemblerScreen::new);
    }
}
