package dev.wolfieboy09.qtech.client;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.quantipedia.QuantiReader;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

@NothingNullByDefault
public class ClientReloadListener extends SimplePreparableReloadListener<Void> {
    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        return null;
    }

    @Override
    protected void apply(Void theVoid, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        QuantiReader.loadAllWikiEntries(resourceManager);
    }
}
