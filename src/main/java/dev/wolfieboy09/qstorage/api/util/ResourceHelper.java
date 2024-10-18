package dev.wolfieboy09.qstorage.api.util;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import net.minecraft.resources.ResourceLocation;

public class ResourceHelper {
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(QuantiumizedStorage.MOD_ID, path);
    }
}
