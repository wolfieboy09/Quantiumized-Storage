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
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public record DiskAssemblerRecipe(
        List<Ingredient> mainItems,
        List<Ingredient> extras,
        int energyCost,
        int timeInTicks,
        ItemStack result
) implements Recipe<RecipeWrapper>, RecipeType<DiskAssemblerRecipe> {

    @Override
    public boolean matches(RecipeWrapper input, Level level) {
        boolean extrasMatch = !this.extras.isEmpty();
        boolean ingredientsMatch = !this.mainItems.isEmpty();
        for (Ingredient extra : this.extras) {
            if (!extra.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.EXTRA_SLOT_1))
                && !extra.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.EXTRA_SLOT_2))
                && !extra.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.EXTRA_SLOT_3))
                && !extra.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.EXTRA_SLOT_4))) {
                extrasMatch = false;
            }
        }

        for (Ingredient extra : this.mainItems) {
            if (!extra.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.MAIN_SLOT_1))
                    && !extra.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.MAIN_SLOT_2))
                    && !extra.test(input.getItem(DiskAssemblerBlockEntity.DiskAssemblerSlot.MAIN_SLOT_3))) {
                ingredientsMatch = false;
            }
        }
        
        return extrasMatch && ingredientsMatch;
    }

    @Override
    public @NotNull ItemStack assemble(RecipeWrapper recipeWrapper, HolderLookup.Provider provider) {
        return getResultItem(provider);
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
                        Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(DiskAssemblerRecipe::mainItems),
                        Ingredient.CODEC.listOf().fieldOf("extras").forGetter(DiskAssemblerRecipe::extras),
                        Codec.INT.fieldOf("energy").forGetter(DiskAssemblerRecipe::energyCost),
                        Codec.INT.fieldOf("ticks").forGetter(DiskAssemblerRecipe::timeInTicks),
                        ItemStack.CODEC.fieldOf("result").forGetter(DiskAssemblerRecipe::result)
                ).apply(builder, DiskAssemblerRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, DiskAssemblerRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list(3)), DiskAssemblerRecipe::mainItems,
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
