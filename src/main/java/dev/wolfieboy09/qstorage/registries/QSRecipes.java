package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class QSRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, QuantiumizedStorage.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, QuantiumizedStorage.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<DiskAssemblerRecipe>> DISK_ASSEMBLER_TYPE = registerRecipeType("disk_assembly");
    public static final DeferredHolder<RecipeSerializer<?>, DiskAssemblerRecipe.Serializer> DISK_ASSEMBLER_SERIALIZER = SERALIZERS.register("disk_assembly", DiskAssemblerRecipe.Serializer::new);

    public static void init(IEventBus bus) {
        SERALIZERS.register(bus);
        TYPES.register(bus);
    }

    public static <T extends Recipe<?>> @NotNull DeferredHolder<RecipeType<?>, RecipeType<T>> registerRecipeType(String id) {
        return TYPES.register(id, () -> RecipeType.simple(ResourceHelper.asResource(id)));
    }
}
