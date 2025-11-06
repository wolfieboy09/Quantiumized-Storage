//package dev.wolfieboy09.qtech.api.recipes;
//
//import com.mojang.serialization.MapCodec;
//import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.crafting.RecipeInput;
//import net.minecraft.world.item.crafting.RecipeSerializer;
//
//@NothingNullByDefault
//public abstract class StandardProcessingRecipe<T extends RecipeInput> extends ProcessingRecipe<T, ProcessingRecipeParams> {
//    public StandardProcessingRecipe(IRecipeTypeInfo typeInfo, ProcessingRecipeParams params) {
//        super(typeInfo, params);
//    }
//
//    @FunctionalInterface
//    public interface Factory<R extends StandardProcessingRecipe<?>> extends ProcessingRecipe.F<ProcessingRecipeParams, R> {
//        R create(ProcessingRecipeParams params);
//    }
//
//    public static class Builder<R extends StandardProcessingRecipe<?>>
//            extends ProcessingRecipeBuilder<ProcessingRecipeParams, R, Builder<R>> {
//
//        public Builder(Factory<R> factory, ResourceLocation recipeId) {
//            super(factory, recipeId);
//        }
//
//        @Override
//        protected ProcessingRecipeParams createParams() {
//            return new ProcessingRecipeParams();
//        }
//
//        @Override
//        public Builder<R> self() {
//            return this;
//        }
//    }
//
//    public static class Serializer<R extends StandardProcessingRecipe<?>> implements RecipeSerializer<R> {
//        private final Factory<R> factory;
//        private final MapCodec<R> codec;
//        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;
//
//        public Serializer(Factory<R> factory) {
//            this.factory = factory;
//            this.codec = ProcessingRecipe.codec(factory, ProcessingRecipeParams.CODEC);
//            this.streamCodec = ProcessingRecipe.streamCodec(factory, ProcessingRecipeParams.STREAM_CODEC);
//        }
//
//        @Override
//        public MapCodec<R> codec() {
//            return codec;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
//            return streamCodec;
//        }
//
//        public Factory<R> factory() {
//            return factory;
//        }
//    }
//}
