package dev.wolfieboy09.qstorage.api.util;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class ResourceHelper {
    @Contract("_ -> new")
    public static @NotNull ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(QuantiumizedStorage.MOD_ID, path);
    }
}
