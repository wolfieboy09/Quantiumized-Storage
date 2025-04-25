package dev.wolfieboy09.qtech.api.util;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class ResourceHelper {
    @Contract("_ -> new")
    public static @NotNull ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(QuantiumizedTech.MOD_ID, path);
    }
}
