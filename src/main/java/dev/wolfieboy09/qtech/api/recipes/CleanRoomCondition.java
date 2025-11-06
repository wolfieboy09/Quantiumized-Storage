package dev.wolfieboy09.qtech.api.recipes;

import com.mojang.serialization.Codec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

@NothingNullByDefault
public enum CleanRoomCondition implements StringRepresentable {
    NONE(0xFFFFFF),          // Any condition, does not matter
    CONTROLLED(0xA0A0A0),    // Basic filtered air. Prevents major contamination
    CLEAN(0x00FF00),         // ISO Class 8–7 equivalent, used for micro-assembly
    ULTRA_CLEAN(0x0000FF),   // ISO Class 6–5, required for precision electronics
    STERILIZED(0xFF00FF),    // ISO Class 4–1, almost particle free environment
    VACUUM(0x2B0033);        // Environment with no air or even particles. Used for recipes that require absolute isolation

    private final int color;

    public static final Codec<CleanRoomCondition> CODEC = StringRepresentable.fromEnum(CleanRoomCondition::values);
    public static final StreamCodec<FriendlyByteBuf, CleanRoomCondition> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(CleanRoomCondition.class);

    CleanRoomCondition(int color) {
        this.color = color;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }

    public String getTranslationKey() {
        return "recipe.cleanroom_condition." + getSerializedName();
    }

    public int getColor() {
        return this.color;
    }
}
