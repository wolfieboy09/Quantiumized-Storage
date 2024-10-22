package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.util.NamingUtil;
import dev.wolfieboy09.qstorage.registries.QSItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class QSLangProvider extends LanguageProvider {
    public QSLangProvider(PackOutput output, String locale) {
        super(output, QuantiumizedStorage.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        QSItems.ITEMS.getEntries().forEach(
                item -> addItem(item, NamingUtil.toHumanReadable(item.getRegisteredName().split(":")[1]))
        );
    }
}
