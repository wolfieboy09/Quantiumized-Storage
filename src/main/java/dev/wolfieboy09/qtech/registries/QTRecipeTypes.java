package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.recipes.IRecipeTypeInfo;
import dev.wolfieboy09.qtech.api.recipes.StandardProcessingRecipe;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.block.disk_assembler.NewDiskAssemblerRecipe;
import dev.wolfieboy09.qtech.block.disk_assembler.recipe.DiskAssemblerRecipeParams;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

@NothingNullByDefault
@SuppressWarnings("unchecked")
public enum QTRecipeTypes implements IRecipeTypeInfo, StringRepresentable {
    DISK_ASSEMBLY(params -> new NewDiskAssemblerRecipe((DiskAssemblerRecipeParams) params));

    public final ResourceLocation id;
    public final Supplier<RecipeSerializer<?>> serializerSupplier;
    private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> serializerObject;
    @Nullable
    private final DeferredHolder<RecipeType<?>, RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    QTRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
        String name = ResourceHelper.asResource(name()).toString();
        id = ResourceHelper.asResource(name);
        this.serializerSupplier = serializerSupplier;
        this.serializerObject = QTRecipes.SERALIZERS.register(name, serializerSupplier);
        if (registerType) {
            typeObject = QTRecipes.TYPES.register(name, typeSupplier);
            type = typeObject;
        } else {
            this.typeObject = null;
            this.type = typeSupplier;
        }
    }

    QTRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = ResourceHelper.asResource(name()).toString();
        this.id = ResourceHelper.asResource(name);
        this.serializerSupplier = serializerSupplier;
        this.serializerObject = QTRecipes.SERALIZERS.register(name, serializerSupplier);
        this.typeObject = QTRecipes.TYPES.register(name, () -> RecipeType.simple(id));
        this.type = typeObject;
    }

    QTRecipeTypes(StandardProcessingRecipe.Factory<?> processingFactory) {
        this(() -> new StandardProcessingRecipe.Serializer<>(processingFactory));
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @Override
    public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
        return (RecipeType<R>) this.type.get();
    }

    public <I extends RecipeInput, R extends Recipe<I>> Optional<RecipeHolder<R>> find(I inv, Level world) {
        return world.getRecipeManager()
                .getRecipeFor(getType(), inv, world);
    }

    @Override
    public String getSerializedName() {
        return id.toString();
    }
}
