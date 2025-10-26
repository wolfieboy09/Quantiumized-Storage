package dev.wolfieboy09.qtech.api.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record FacadeComponent(String id) {
    public static final Codec<FacadeComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("facade_id").forGetter(FacadeComponent::id)
    ).apply(instance, FacadeComponent::new));

    public @NotNull ResourceLocation parsedId() {
        return ResourceLocation.parse(this.id);
    }
}
