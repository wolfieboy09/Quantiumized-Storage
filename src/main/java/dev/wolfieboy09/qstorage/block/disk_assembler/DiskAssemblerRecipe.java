package dev.wolfieboy09.qstorage.block.disk_assembler;


import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qstorage.registries.QSRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record DiskAssemblerRecipe(
        Ingredient diskPort,
        Ingredient diskCasing,
        Ingredient screws,
        int energyCost,
        int ticks,
        ItemStack result
) implements Recipe<RecipeWrapper>, RecipeType<DiskAssemblerRecipe> {

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        return true;
    }

    @Override
    public ItemStack assemble(RecipeWrapper recipeWrapper, HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return QSRecipes.DISK_ASSEMBLER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<DiskAssemblerRecipe> {

        @Override
        public MapCodec<DiskAssemblerRecipe> codec() {
            return null;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DiskAssemblerRecipe> streamCodec() {
            return null;
        }
    }
}
