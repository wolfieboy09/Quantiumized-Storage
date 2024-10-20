package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static dev.wolfieboy09.qstorage.QuantiumizedStorage.REGISTRATE;

public class QSCreativeTab {
    private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, QuantiumizedStorage.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_CREATIVE_TAB = addTab("main_creative_tab", "Quantiumized Storage", QSItems.DATA_CRYSTAL::asStack);

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> addTab(String id, String name, Supplier<ItemStack> icon) {
        String itemGroupId = "itemGroup." + QuantiumizedStorage.MOD_ID + "." + id;
        REGISTRATE.addRawLang(itemGroupId, name);
        CreativeModeTab.Builder tabBuilder = CreativeModeTab.builder()
                .icon(icon)
                .displayItems((pParameters, populator) -> {
                    populator.accept(QSItems.SILICON.get());
                })
                .title(Component.translatable(itemGroupId));
        return REGISTER.register(id, tabBuilder::build);
    }

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
