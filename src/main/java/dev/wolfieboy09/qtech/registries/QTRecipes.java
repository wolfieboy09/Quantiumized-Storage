package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.block.circut_engraver.CircuitEngraverRecipe;
import dev.wolfieboy09.qtech.block.disk_assembler.DiskAssemblerRecipe;
import dev.wolfieboy09.qtech.block.smeltery.SmelteryRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class QTRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, QuantiumizedTech.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, QuantiumizedTech.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<DiskAssemblerRecipe>> DISK_ASSEMBLER_TYPE = registerRecipeType("disk_assembly");
    public static final DeferredHolder<RecipeSerializer<?>, DiskAssemblerRecipe.Serializer> DISK_ASSEMBLER_SERIALIZER = SERALIZERS.register("disk_assembly", DiskAssemblerRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<CircuitEngraverRecipe>> CIRCUIT_ENGRAVER_TYPE = registerRecipeType("circuit_engraver");
    public static final DeferredHolder<RecipeSerializer<?> , CircuitEngraverRecipe.Serializer> CIRCUIT_ENGRAVER_SERIALIZER = SERALIZERS.register("circuit_engraver", CircuitEngraverRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<SmelteryRecipe>> SMELTERY_RECIPE_TYPE = registerRecipeType("smeltery");
    public static final DeferredHolder<RecipeSerializer<?>, SmelteryRecipe.Serializer> SMELTERY_SERIALIZER = SERALIZERS.register("smeltery", SmelteryRecipe.Serializer::new);

    public static void register(IEventBus bus) {
        SERALIZERS.register(bus);
        TYPES.register(bus);
    }

    public static <T extends Recipe<?>> @NotNull DeferredHolder<RecipeType<?>, RecipeType<T>> registerRecipeType(String id) {
        return TYPES.register(id, () -> RecipeType.simple(ResourceHelper.asResource(id)));
    }
}
