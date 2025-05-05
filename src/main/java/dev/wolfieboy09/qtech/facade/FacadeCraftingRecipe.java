package dev.wolfieboy09.qtech.facade;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.item.FacadeItem;
import dev.wolfieboy09.qtech.registries.QTRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;

public class FacadeCraftingRecipe extends CustomRecipe {
    public FacadeCraftingRecipe() {
        super(CraftingBookCategory.MISC);
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        boolean foundFacade = false;
        boolean foundBlock = false;
        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof FacadeItem facade) {
                if (foundFacade || FacadeItem.hasBlock(stack)) return false;
                foundFacade = true;
            } else if (stack.getItem() instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                if (!isCubeLike(block) || foundBlock) return false;
                foundBlock = true;
            } else {
                return false;
            }
        }
        return foundFacade && foundBlock;
    }


    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        ItemStack facadeItem = ItemStack.EMPTY;
        Block representedBlock = null;
        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof FacadeItem) {
                facadeItem = stack;
            } else if (stack.getItem() instanceof BlockItem blockItem) {
                representedBlock = blockItem.getBlock();
            }
        }
        if (facadeItem.isEmpty() || representedBlock == null) return ItemStack.EMPTY;
        return FacadeItem.createFacade(representedBlock);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return QTRecipes.FACADE_SERIALIZER.get();
    }

    public static boolean isCubeLike(Block block) {
        return block.defaultBlockState().getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) == Shapes.block();
    }

    public static class Serializer implements RecipeSerializer<FacadeCraftingRecipe> {
        // WARNING TO ANYONE
        // Do not try to "merge" this varable.
        // It prevents Minecraft from failing with recipes, aka
        // it holds together the mod.
        private static final FacadeCraftingRecipe SINGLETON = new FacadeCraftingRecipe();
        public static final MapCodec<FacadeCraftingRecipe> CODEC = MapCodec.unit(() -> SINGLETON);
        public static final StreamCodec<RegistryFriendlyByteBuf, FacadeCraftingRecipe> STREAM_CODEC = StreamCodec.unit(SINGLETON);

        @Override
        public MapCodec<FacadeCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FacadeCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
