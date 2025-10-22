package dev.wolfieboy09.qtech.api.datagen;

import com.google.gson.JsonObject;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.multiblock.MultiblockBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A data gen class to create multiblock patterns
 */
@NothingNullByDefault
public abstract class MultiblockPatternProvider implements DataProvider {
    private final PackOutput output;
    private final String modId;
    private final List<MultiblockBuilder.PatternData> patterns = new ArrayList<>();

    protected MultiblockPatternProvider(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        Path outputFolder = this.output.getOutputFolder()
                .resolve("data/" + this.modId + "/multiblock_patterns");

        List<CompletableFuture<?>> futures = new ArrayList<>();

        registerPatterns();

        for (MultiblockBuilder.PatternData pattern : this.patterns) {
            // This is to get the path out from the type
            Path path = outputFolder.resolve(pattern.multiblockType().split(":")[1] + "/" + pattern.name() + ".json");
            JsonObject json = pattern.toJson();
            futures.add(DataProvider.saveStable(cachedOutput, json, path));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

    }

    protected abstract void registerPatterns();

    /**
     * Creates a new {@link MultiblockBuilder} instance
     * @param name The name to give it
     * @param type The type to give
     * @return A created {@link MultiblockBuilder}
     * @implNote This will automatically call {@link MultiblockBuilder#create(String)} with the name given. This will also call
     * {@link MultiblockBuilder#type(ResourceLocation)}, and provide the mod id in the data gen and the type
     */
    protected MultiblockBuilder create(String name, String type) {
        return MultiblockBuilder.create(name).type(ResourceLocation.fromNamespaceAndPath(this.modId, type));
    }

    /**
     * Automatically calls {@link MultiblockBuilder#build()}
     * @param builder the {@link MultiblockBuilder}
     */
    protected void add(MultiblockBuilder builder) {
        this.patterns.add(builder.build());
    }

    /**
     * Adds the pattern data to be generated
     * @param patternData The built {@link MultiblockBuilder.PatternData} from {@link MultiblockBuilder#build()}
     */
    protected void add(MultiblockBuilder.PatternData patternData) {
        this.patterns.add(patternData);
    }

    @Override
    public String getName() {
        return "Multiblock Patterns: " + modId;
    }
}
