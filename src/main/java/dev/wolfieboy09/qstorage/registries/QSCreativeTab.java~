package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.function.Supplier;

public class QSCreativeTab {
    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, QuantiumizedStorage.MOD_ID);

    public static HashMap<String, String> nameForLangGen = new HashMap<>();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS = addTab("item_group", "QS Items", () -> new ItemStack(QSItems.SILICON.get()));
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = addTab("block_group", "QS Blocks", () -> new ItemStack(QSBlocks.DISK_ASSEMBLER.get()));

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> addTab(String id, String name, Supplier<ItemStack> icon) {
        String displayKey = "creative_tab." + QuantiumizedStorage.MOD_ID + "." + id;
        nameForLangGen.put(displayKey, name);

        CreativeModeTab.Builder tabBuilder = CreativeModeTab.builder()
                .icon(icon)
                .displayItems((parameters, populator) -> QSItems.ITEMS.getEntries().forEach(item -> populator.accept(item.get())))
                .title(Component.translatable(displayKey));
        return REGISTER.register(id, tabBuilder::build);
    }

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
