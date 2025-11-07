package dev.wolfieboy09.qtech.api.recipes;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record ProcessingRecipeConstrains(int maxItemInputs, int maxFluidInputs, int maxGasInputs, int maxItemOutputs, int maxFluidOutputs, int maxGasOutputs) {
    @Contract(" -> new")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {
        int maxItemInputs = 0;
        int maxFluidInputs = 0;
        int maxGasInputs = 0;
        int maxItemOutputs = 0;
        int maxFluidOutputs = 0;
        int maxGasOutputs = 0;

        public Builder maxItemInputs(int maxItemInputs) {
            this.maxItemInputs = maxItemInputs;
            return this;
        }

        public Builder maxFluidInputs(int maxFluidInputs) {
            this.maxFluidInputs = maxFluidInputs;
            return this;
        }

        public Builder maxGasInputs(int maxGasInputs) {
            this.maxGasInputs = maxGasInputs;
            return this;
        }

        public Builder maxItemOutputs(int maxItemOutputs) {
            this.maxItemOutputs = maxItemOutputs;
            return this;
        }

        public Builder maxFluidOutputs(int maxFluidOutputs) {
            this.maxFluidOutputs = maxFluidOutputs;
            return this;
        }

        public Builder maxGasOutputs(int maxGasOutputs) {
            this.maxGasOutputs = maxGasOutputs;
            return this;
        }

        public Builder maxItemIO(int maxItemInputs, int maxItemOutputs) {
            this.maxItemInputs = maxItemInputs;
            this.maxItemOutputs = maxItemOutputs;
            return this;
        }

        public Builder maxFluidIO(int maxFluidInputs, int maxFluidOutputs) {
            this.maxFluidInputs = maxFluidInputs;
            this.maxFluidOutputs = maxFluidOutputs;
            return this;
        }

        public Builder maxGasIO(int maxGasInputs, int maxGasOutputs) {
            this.maxGasInputs = maxGasInputs;
            this.maxGasOutputs = maxGasOutputs;
            return this;
        }

        public ProcessingRecipeConstrains build() {
            return new ProcessingRecipeConstrains(maxItemInputs, maxFluidInputs, maxGasInputs, maxItemOutputs, maxFluidOutputs, maxGasOutputs);
        }

    }
}
