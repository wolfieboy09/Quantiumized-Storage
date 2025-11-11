package dev.wolfieboy09.qtech.api.recipes;

import com.mojang.serialization.Codec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

@NothingNullByDefault
public enum CleanroomCondition implements StringRepresentable {
    /**
     * Any condition/environment. Does not matter
     */
    NONE(0xFFFFFF),
    /**
     * Basic filtered air. Prevents major forms of contamination
     */
    CONTROLLED(0xA0A0A0),
    /**
     * ISO Class 8-7 equivalent. Used for micro-assembly
     */
    CLEAN(0x00FF00),
    /**
     * ISO Class 6-5 equivalent. Used for precision electronics
     */
    ULTRA_CLEAN(0x0000FF),
    /**
     * ISO Class 4-1 equivalent. Almost particle-free environment
     */
    STERILIZED(0xFF00FF),
    /**
     * Environment with no air or even particles. Used for recipes that require absolute isolation
     */
    VACUUM(0x2B0033);

    private final int color;

    public static final Codec<CleanroomCondition> CODEC = StringRepresentable.fromEnum(CleanroomCondition::values);
    public static final StreamCodec<FriendlyByteBuf, CleanroomCondition> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(CleanroomCondition.class);

    CleanroomCondition(int color) {
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

    public static CleanroomCondition fromString(String string) {
        String upper = string.toUpperCase();
        return switch (upper) {
            case "NONE" -> CleanroomCondition.NONE;
            case "CONTROLLED" -> CleanroomCondition.CONTROLLED;
            case "CLEAN" -> CleanroomCondition.CLEAN;
            case "ULTRA_CLEAN" -> CleanroomCondition.ULTRA_CLEAN;
            case "STERILIZED" -> CleanroomCondition.STERILIZED;
            case "VACUUM" -> CleanroomCondition.VACUUM;
            default -> throw new IllegalArgumentException("Unknown cleanroom condition: " + string);
        };
    }
}
