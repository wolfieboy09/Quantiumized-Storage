package dev.wolfieboy09.qtech.datagen;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.registries.QTItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class QTItemModelProvider extends ItemModelProvider {
    public QTItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, QuantiumizedTech.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        QTItems.ITEMS.getEntries()
                .forEach(item -> basicItem(item.get()));
    }
}
