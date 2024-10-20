package dev.wolfieboy09.qstorage.registries;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static dev.wolfieboy09.qstorage.QuantiumizedStorage.REGISTRATE;

public class QSCreativeTab {
    private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, QuantiumizedStorage.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_CREATIVE_TAB = addTab("main_creative_tab", "Quantiumized Storage", QSItems.SILICON::asStack);

    public static @NotNull DeferredHolder<CreativeModeTab, CreativeModeTab> addTab(String id, String name, Supplier<ItemStack> icon) {
        String itemGroupId = "item_group." + QuantiumizedStorage.MOD_ID + "." + id;
        REGISTRATE.addRawLang(itemGroupId, name);

        return REGISTER.register(id, () -> {
            CreativeModeTab.Builder tabBuilder = CreativeModeTab.builder()
                    .icon(icon)
                    .displayItems((parameters, populator) -> {
                        populator.accept(QSItems.SILICON.asItem());
                    })
                    .title(Component.translatable(itemGroupId));
            return tabBuilder.build();
        });
    }




    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
