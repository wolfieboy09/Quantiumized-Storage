package dev.wolfieboy09.qtech.api.recipes;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.util.StringRepresentable;

@NothingNullByDefault
public enum CleanRoomCondition implements StringRepresentable {
    NONE(0xFFFFFF),          // Any condition, does not matter
    CONTROLLED(0xA0A0A0),    // Basic filtered air. Prevents major contamination
    CLEAN(0x00FF00),         // ISO Class 8–7 equivalent, used for micro-assembly
    ULTRA_CLEAN(0x0000FF),   // ISO Class 6–5, required for precision electronics
    STERILIZED(0xFF00FF),    // ISO Class 4–1, almost particle free environment
    VACUUM(0x2B0033);        // Environment with no air or even particles. Used for recipes that require absolute isolation

    private final int color;

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
