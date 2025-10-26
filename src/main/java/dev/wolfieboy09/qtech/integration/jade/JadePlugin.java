package dev.wolfieboy09.qtech.integration.jade;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.multiblock.blocks.BaseMultiblockController;
import dev.wolfieboy09.qtech.integration.jade.multiblocks.MultiblockComponentProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
@NothingNullByDefault
public class JadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(MultiblockComponentProvider.INSTANCE, BaseMultiblockController.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(MultiblockComponentProvider.INSTANCE, BaseMultiblockController.class);
    }
}
