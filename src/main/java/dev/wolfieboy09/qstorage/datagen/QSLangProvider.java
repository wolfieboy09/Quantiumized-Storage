package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.util.NamingUtil;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import dev.wolfieboy09.qstorage.registries.QSCreativeTab;
import dev.wolfieboy09.qstorage.registries.QSGasses;
import dev.wolfieboy09.qstorage.registries.QSItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class QSLangProvider extends LanguageProvider {
    public QSLangProvider(PackOutput output) {
        super(output, QuantiumizedStorage.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        final Map<String, String> toGenerate = getStringStringMap();

        QSItems.ITEMS.getEntries().forEach(
                item -> addItem(item, NamingUtil.toHumanReadable(item.getRegisteredName().split(":")[1]))
        );
        QSBlocks.BLOCKS.getEntries().forEach(
                block -> addBlock(block, NamingUtil.toHumanReadable(block.getRegisteredName().split(":")[1]))
        );
        QSCreativeTab.REGISTER.getEntries().forEach(
                tab -> add(tab.get().getDisplayName().getString(), QSCreativeTab.nameForLangGen.get(tab.get().getDisplayName().getString()))
        );
        QSGasses.GASSES.getEntries().forEach(
                tab -> add(tab.get().getName().getString(), NamingUtil.toHumanReadable(tab.get().getResourceLocation().getPath()))
        );
        toGenerate.forEach(this::add);
    }

    private static @NotNull Map<String, String> getStringStringMap() {
        final Map<String, String> toGenerate = new HashMap<>();
        toGenerate.put("qstorage.oxygen_deprivation", "%s could not breath on land");
        toGenerate.put("effect.qstorage.oxygen_deprivation", "Oxygen Deprivation");
        toGenerate.put("tooltip.qstorage.max_energy_upgrade", "Adds more energy capacity");
        toGenerate.put("tooltip.qstorage.speed_upgrade", "Makes the process faster");

        toGenerate.put("qstorage.screen.fluid", "%s: %s/%s");
        toGenerate.put("qstorage.tooltip.shift_info", "Press §6SHIFT§r for more info");
        toGenerate.put("qstorage.gas_canister.shifted_info", "Supports containing gasses inside the container");
        toGenerate.put("qstorage.gas_canister.contains_gas", "Storing §e%s§r gU");
        return toGenerate;
    }
}
