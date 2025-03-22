package dev.wolfieboy09.qstorage.api.datagen;

import dev.wolfieboy09.qstorage.api.registry.gas.Gas;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GasTagProvider extends TagsProvider<Gas> {
    protected final Map<ResourceLocation, TagBuilder> subclassBuilders = new LinkedHashMap<>();

    @Contract("_, _, _, _, _ -> new")
    public static @NotNull TagsProvider<Gas> create(PackOutput output, ResourceKey<Registry<Gas>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        return new GasTagProvider(output, registryKey, lookupProvider, modId, existingFileHelper);
    }

    protected GasTagProvider(PackOutput output, ResourceKey<Registry<Gas>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registryKey, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected @NotNull TagBuilder getOrCreateRawBuilder(@NotNull TagKey<Gas> tag) {
        super.getOrCreateRawBuilder(tag);
        return this.subclassBuilders.computeIfAbsent(tag.location(), (key) -> TagBuilder.create());
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        this.builders.putAll(this.subclassBuilders);
    }
}
