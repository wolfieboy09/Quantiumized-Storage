package dev.wolfieboy09.qtech.quantipedia;

import dev.wolfieboy09.qtech.quantipedia.api.QuanEntry;
import dev.wolfieboy09.qtech.quantipedia.api.QuanRoot;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuantiReader {
    public static List<QuanRoot> roots = new ArrayList<>();

    public static void loadAllWikiEntries(ResourceManager manager) {
        roots.clear();
        try {
            Map<ResourceLocation, Resource> resources = manager.listResources(
                    "quantipedia",
                    path -> path.getPath().endsWith(".md")
            );

            for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
                ResourceLocation id = entry.getKey();
                Resource resource = entry.getValue();
                QuanRoot root = new QuanRoot(id.getNamespace());

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.open(), StandardCharsets.UTF_8))) {

                    List<String> content = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.add(line);
                    }

                    root.addEntry(QuanEntry.create(content));
                    roots.add(root);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
