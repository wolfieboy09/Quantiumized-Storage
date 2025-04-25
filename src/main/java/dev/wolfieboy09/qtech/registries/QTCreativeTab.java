package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Supplier;

public class QTCreativeTab {
    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, QuantiumizedTech.MOD_ID);

    public static HashMap<String, String> nameForLangGen = new HashMap<>();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS = addTab("item_group", "QT Items", QTItems.ITEMS, () -> new ItemStack(QTItems.SILICON.get()));
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = addTab("block_group", "QT Blocks", QTBlocks.BLOCK_ITEMS, () -> new ItemStack(QTBlocks.DISK_ASSEMBLER.get()));

    public static @NotNull DeferredHolder<CreativeModeTab, CreativeModeTab> addTab(String id, String name, DeferredRegister.Items entries, Supplier<ItemStack> icon) {
        String displayKey = "creative_tab." + QuantiumizedTech.MOD_ID + "." + id;
        nameForLangGen.put(displayKey, name);

        CreativeModeTab.Builder tabBuilder = CreativeModeTab.builder()
                .icon(icon)
                .displayItems((parameters, populator) -> entries.getEntries().forEach(item -> populator.accept(item.get())))
                .title(Component.translatable(displayKey));
        return REGISTER.register(id, tabBuilder::build);
    }

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
