package dev.wolfieboy09.qtech.api.datagen.framework;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.IRecipeTypeInfo;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipe;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeBuilder;
import dev.wolfieboy09.qtech.api.recipes.ProcessingRecipeParams;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@NothingNullByDefault
public abstract class ProcessingRecipeGen<P extends ProcessingRecipeParams, R extends ProcessingRecipe<?, P>, B extends ProcessingRecipeBuilder<P, R, B>> extends BaseRecipeProvider {
    public ProcessingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String namespace) {
        super(output, registries, namespace);
    }

    protected GeneratedRecipe create(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<B> transform) {
        GeneratedRecipe generatedRecipe = c -> {
            ItemLike itemLike = singleIngredient.get();
            transform
                    .apply(getBuilder(ResourceLocation.fromNamespaceAndPath(namespace, getKeyOrThrow(itemLike.asItem()).getPath())).withItemIngredients(Ingredient.of(itemLike)))
                    .build(c);
        };
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    protected GeneratedRecipe create(Supplier<ItemLike> singleIngredient, UnaryOperator<B> transform) {
        return create(modId, singleIngredient, transform);
    }

    protected GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<B> transform) {
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(getBuilder(name.get()))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    protected GeneratedRecipe create(ResourceLocation name, UnaryOperator<B> transform) {
        return createWithDeferredId(() -> name, transform);
    }

    protected GeneratedRecipe create(String name, UnaryOperator<B> transform) {
        return create(asResource(name), transform);
    }

    protected abstract IRecipeTypeInfo getRecipeType();

    protected abstract B getBuilder(ResourceLocation id);

    protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
        return () -> {
            ResourceLocation registryName = getKeyOrThrow(item.get()
                    .asItem());
            return asResource(registryName.getPath() + suffix);
        };
    }

    private ResourceLocation getKeyOrThrow(Item item) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKeyOrNull(item);
        if (location == null) throw new IllegalArgumentException("Invalid item: " + item.getDescriptionId());
        return location;
    }
}
