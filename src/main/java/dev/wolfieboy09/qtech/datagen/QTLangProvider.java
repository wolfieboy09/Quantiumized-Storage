package dev.wolfieboy09.qtech.datagen;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.util.NamingUtil;
import dev.wolfieboy09.qtech.registries.QTBlocks;
import dev.wolfieboy09.qtech.registries.QTCreativeTab;
import dev.wolfieboy09.qtech.registries.QTGasses;
import dev.wolfieboy09.qtech.registries.QTItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class QTLangProvider extends LanguageProvider {
    public QTLangProvider(PackOutput output) {
        super(output, QuantiumizedTech.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        final Map<String, String> toGenerate = getStringStringMap();

        QTItems.ITEMS.getEntries().forEach(
                item -> addItem(item, NamingUtil.toHumanReadable(item.getRegisteredName().split(":")[1]))
        );
        QTBlocks.BLOCKS.getEntries().forEach(
                block -> addBlock(block, NamingUtil.toHumanReadable(block.getRegisteredName().split(":")[1]))
        );
        QTCreativeTab.REGISTER.getEntries().forEach(
                tab -> add(tab.get().getDisplayName().getString(), QTCreativeTab.nameForLangGen.get(tab.get().getDisplayName().getString()))
        );
        QTGasses.GASSES.getEntries().forEach(
                tab -> add(tab.get().getName().getString(), NamingUtil.toHumanReadable(tab.get().getResourceLocation().getPath()))
        );
        toGenerate.forEach(this::add);
    }

    private static @NotNull Map<String, String> getStringStringMap() {
        final Map<String, String> toGenerate = new HashMap<>();
        toGenerate.put("qtech.oxygen_deprivation", "%s could not breath on land");
        toGenerate.put("effect.qtech.oxygen_deprivation", "Oxygen Deprivation");
        toGenerate.put("tooltip.qtech.max_energy_upgrade", "Adds more energy capacity");
        toGenerate.put("tooltip.qtech.speed_upgrade", "Makes the process faster");

        toGenerate.put("qtech.screen.fluid", "%s: %s/%s");
        toGenerate.put("qtech.tooltip.shift_info", "Press §6SHIFT§r for more info");
        toGenerate.put("qtech.gas_canister.shifted_info", "Supports containing gasses inside the container");
        toGenerate.put("qtech.gas_canister.contains_gas", "Storing §e%s§r");
        return toGenerate;
    }
}
