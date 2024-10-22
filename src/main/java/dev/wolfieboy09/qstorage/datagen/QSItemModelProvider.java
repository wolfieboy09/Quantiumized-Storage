package dev.wolfieboy09.qstorage.datagen;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.registries.QSItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class QSItemModelProvider extends ItemModelProvider {
    public QSItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, QuantiumizedStorage.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        QSItems.ITEMS.getEntries()
                .forEach(item -> basicItem(item.get()));
    }
}
