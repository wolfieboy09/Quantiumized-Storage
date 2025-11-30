package dev.wolfieboy09.qtech.api.recipes;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record RecipeIgnorance(boolean ignoresCleanoom, boolean ignoresProcessingTime, boolean ignoresEnergyCost) {
    @Contract(value = " -> new", pure = true)
    public static @NotNull RecipeIgnoranceBuilder create() {
        return new RecipeIgnoranceBuilder();
    }

    public static RecipeIgnorance createBlank() {
        return create().set();
    }

    public static class RecipeIgnoranceBuilder {
        private boolean ignoresCleanoom;
        private boolean ignoresProcessingTime;
        private boolean ignoresEnergyCost;

        public RecipeIgnoranceBuilder ignoresCleanroom() {
            this.ignoresCleanoom = true;
            return this;
        }

        public RecipeIgnoranceBuilder ignoresProcessingTime() {
            this.ignoresProcessingTime = true;
            return this;
        }

        public RecipeIgnoranceBuilder ignoresEnergyCost() {
            this.ignoresEnergyCost = true;
            return this;
        }

        public RecipeIgnorance set() {
            return new RecipeIgnorance(this.ignoresCleanoom, this.ignoresProcessingTime, this.ignoresEnergyCost);
        }
    }
}
