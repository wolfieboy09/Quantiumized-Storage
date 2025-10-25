package dev.wolfieboy09.qtech.quantipedia;

import dev.wolfieboy09.qtech.quantipedia.api.QuanEntry;
import dev.wolfieboy09.qtech.quantipedia.api.QuanRoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class QuantiReader {
    private static final Logger LOGGER = LoggerFactory.getLogger("Quantipedia");
    public static final List<QuanRoot> roots = new ArrayList<>();

    public static void loadAllWikiEntries(ResourceManager manager) {
        roots.clear();
        long start = System.currentTimeMillis();

        try {
            Map<ResourceLocation, Resource> resources = manager.listResources(
                    "quantipedia",
                    path -> path.getPath().endsWith(".md")
            );

            if (resources.isEmpty()) {
                return;
            }

            for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
                ResourceLocation id = entry.getKey();
                Resource resource = entry.getValue();

                // find or create a root for this namespace
                QuanRoot root = roots.stream()
                        .filter(r -> r.modId().equals(id.getNamespace()))
                        .findFirst()
                        .orElseGet(() -> {
                            QuanRoot newRoot = new QuanRoot(id.getNamespace());
                            roots.add(newRoot);
                            return newRoot;
                        });

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.open(), StandardCharsets.UTF_8))) {

                    List<String> content = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.add(line);
                    }

                    if (content.isEmpty()) {
                        LOGGER.warn("Skipping empty Markdown file: {}", id);
                        continue;
                    }

                    QuanEntry entryObj = QuanEntry.create(content);

                    try {
                        root.addEntry(entryObj);
                        LOGGER.debug("Added entry '{}' from {}", entryObj.header(), id);
                    } catch (Exception ex) {
                        LOGGER.error("Failed to add entry from {}: {}", id, ex.getMessage(), ex);
                    }

                } catch (Exception e) {
                    LOGGER.error("Error reading {}: {}", id, e.getMessage(), e);
                }
            }

            int totalRoots = roots.size();
            int totalEntries = roots.stream().mapToInt(r -> r.entries().size()).sum();
            long duration = System.currentTimeMillis() - start;

            LOGGER.info("Loaded {} namespaces and {} entries in {} ms.",
                    totalRoots, totalEntries, duration);

        } catch (Exception e) {
            LOGGER.error("Failed to load wiki entries", e);
            throw new RuntimeException("Failed to load Quantipedia entries", e);
        }
    }
}
