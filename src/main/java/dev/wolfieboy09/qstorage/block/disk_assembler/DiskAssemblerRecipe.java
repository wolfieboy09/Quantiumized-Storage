package dev.wolfieboy09.qstorage.block.disk_assembler;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qstorage.registries.QSRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public record DiskAssemblerRecipe(
        Ingredient diskPort,
        Ingredient diskCasing,
        Ingredient screws,
        List<Ingredient> extras,
        int energyCost,
        int timeInTicks,
        ItemStack result
) implements Recipe<RecipeWrapper>, RecipeType<DiskAssemblerRecipe> {

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        ItemStack[] mainItems = {recipeWrapper.getItem(0), recipeWrapper.getItem(1), recipeWrapper.getItem(2)};
        List<Predicate<ItemStack>> toTest = Arrays.asList(diskPort, diskCasing, screws);

        for (Predicate<ItemStack> test : toTest) {
            if (Arrays.stream(mainItems).noneMatch(test)) {
                return false;
            }
        }

        for (int i = 3; i <= 6; i++) {
            ItemStack extraItem = recipeWrapper.getItem(i);
            if (extras.stream().noneMatch(extra -> extra.test(extraItem))) {
                return false;
            }
        }
        return true;
    }


    @Override
    public @NotNull ItemStack assemble(RecipeWrapper recipeWrapper, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return QSRecipes.DISK_ASSEMBLER_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return QSRecipes.DISK_ASSEMBLER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<DiskAssemblerRecipe> {
        private static final MapCodec<DiskAssemblerRecipe> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                        Ingredient.CODEC.fieldOf("port").forGetter(DiskAssemblerRecipe::diskPort),
                        Ingredient.CODEC.fieldOf("casing").forGetter(DiskAssemblerRecipe::diskCasing),
                        Ingredient.CODEC.fieldOf("screws").forGetter(DiskAssemblerRecipe::screws),
                        Ingredient.CODEC.listOf().fieldOf("extras").forGetter(DiskAssemblerRecipe::extras),
                        Codec.INT.fieldOf("energy").forGetter(DiskAssemblerRecipe::energyCost),
                        Codec.INT.fieldOf("ticks").forGetter(DiskAssemblerRecipe::timeInTicks),
                        ItemStack.CODEC.fieldOf("result").forGetter(DiskAssemblerRecipe::result)
                ).apply(builder, DiskAssemblerRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, DiskAssemblerRecipe> STREAM_CODEC = NeoForgeStreamCodecs.composite(
                Ingredient.CONTENTS_STREAM_CODEC, DiskAssemblerRecipe::diskPort,
                Ingredient.CONTENTS_STREAM_CODEC, DiskAssemblerRecipe::diskCasing,
                Ingredient.CONTENTS_STREAM_CODEC, DiskAssemblerRecipe::screws,
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list(4)), DiskAssemblerRecipe::extras,
                ByteBufCodecs.INT, DiskAssemblerRecipe::energyCost,
                ByteBufCodecs.INT, DiskAssemblerRecipe::timeInTicks,
                ItemStack.STREAM_CODEC, DiskAssemblerRecipe::result,
                DiskAssemblerRecipe::new
        );

        @Override
        public @NotNull MapCodec<DiskAssemblerRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, DiskAssemblerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
