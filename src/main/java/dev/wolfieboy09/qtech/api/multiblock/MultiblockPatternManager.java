package dev.wolfieboy09.qtech.api.multiblock;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.UnmodifiableView;
import org.slf4j.Logger;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NothingNullByDefault
public final class MultiblockPatternManager extends SimplePreparableReloadListener<Map<ResourceLocation, JsonObject>> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static volatile Map<MultiblockType, List<MultiblockPattern>> PATTERNS = Map.of();


    @Override
    protected Map<ResourceLocation, JsonObject> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, JsonObject> jsons = new HashMap<>();

        resourceManager.listResources("multiblock_patterns", path -> path.getPath().endsWith(".json"))
                .forEach((fileId, resource) -> {
                    String namespace = fileId.getNamespace();
                    String path = fileId.getPath();

                    if (!path.startsWith("multiblock_patterns/")) {
                        LOGGER.warn("Skipping unexpected multiblock pattern path: {}", path);
                        return;
                    }

                    String relativePath = path.substring("multiblock_patterns/".length(), path.length() - ".json".length());
                    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, relativePath);

                    try (Reader reader = resource.openAsReader()) {
                        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                        jsons.put(id, json);
                    } catch (Exception e) {
                        LOGGER.error("Failed to read multiblock pattern {} from {}", id, fileId, e);
                    }
                });

        LOGGER.info("Discovered {} multiblock pattern JSON(s)", jsons.size());
        return jsons;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<MultiblockType, List<MultiblockPattern>> loaded = new HashMap<>();

        for (var entry : jsons.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonObject json = entry.getValue();

            MultiblockPattern.CODEC.decode(JsonOps.INSTANCE, json)
                    .resultOrPartial(err -> LOGGER.error("Error decoding multiblock pattern {}: {}", id, err))
                    .ifPresent(pair -> {
                        MultiblockPattern pattern = pair.getFirst();
                        loaded.put(pattern.multiblockType(), Collections.singletonList(pattern));
                    });
        }

        PATTERNS = Map.copyOf(loaded);
        LOGGER.info("Successfully loaded {} multiblock pattern(s)", PATTERNS.size());
    }

    public static @UnmodifiableView Map<MultiblockType, List<MultiblockPattern>> getPatterns() {
        return Collections.unmodifiableMap(PATTERNS);
    }

    public static List<MultiblockPattern> getAllPatternsForType(MultiblockType type) {

//        return List.of(
//                MultiblockBuilder.create("centrifuge")
//                        .controller(QTBlocks.CENTRIFUGE_CONTROLLER)
//                        .type(QTMultiblockTypes.CENTRIFUGE)
//                        .key('B', Blocks.BRICKS)
//                        .layer(" B ") // Y = 0
//                        .layer("B+B") // Y = 1
//                        .layer(" B ") // Y = 2
//                        .build()
//        );
        return PATTERNS.get(type);
    }
}
