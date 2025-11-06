package dev.wolfieboy09.qtech.api.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.codecs.NonNullListStreamCodec;
import dev.wolfieboy09.qtech.api.codecs.wrapper.TriEitherCodecs;
import dev.wolfieboy09.qtech.api.gas.crafting.SizedGasIngredient;
import dev.wolfieboy09.qtech.api.recipes.result.FluidStackChanceResult;
import dev.wolfieboy09.qtech.api.recipes.result.GasStackChanceResult;
import dev.wolfieboy09.qtech.api.recipes.result.ItemStackChanceResult;
import dev.wolfieboy09.qtech.api.util.TriEither;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@NothingNullByDefault
public class ProcessingRecipeParams {
    public static MapCodec<ProcessingRecipeParams> CODEC = codec(ProcessingRecipeParams::new);
    public static StreamCodec<RegistryFriendlyByteBuf, ProcessingRecipeParams> STREAM_CODEC = streamCodec(ProcessingRecipeParams::new);

    protected NonNullList<Ingredient> ingredients;
    protected NonNullList<SizedFluidIngredient> fluidIngredients;
    protected NonNullList<SizedGasIngredient> gasIngredients;

    protected NonNullList<ItemStackChanceResult> itemResults;
    protected NonNullList<FluidStackChanceResult> fluidResults;
    protected NonNullList<GasStackChanceResult> gasResults;

    protected int processingDuration;
    protected CleanRoomCondition requiredCleanRoom;

    public ProcessingRecipeParams() {
        this.ingredients = NonNullList.create();
        this.fluidIngredients = NonNullList.create();
        this.gasIngredients = NonNullList.create();
        this.itemResults = NonNullList.create();
        this.fluidResults = NonNullList.create();
        this.gasResults = NonNullList.create();
        this.processingDuration = 0;
        this.requiredCleanRoom = CleanRoomCondition.NONE;
    }

    @Contract("_ -> new")
    protected static <P extends ProcessingRecipeParams> @NotNull MapCodec<P> codec(Supplier<P> factory) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                TriEitherCodecs.either(Ingredient.CODEC, SizedFluidIngredient.FLAT_CODEC, SizedGasIngredient.FLAT_CODEC).listOf().fieldOf("ingredients").forGetter(ProcessingRecipeParams::ingredients),
                TriEitherCodecs.either(ItemStackChanceResult.CODEC, FluidStackChanceResult.CODEC, GasStackChanceResult.CODEC).listOf().fieldOf("results").forGetter(ProcessingRecipeParams::results),
                Codec.INT.fieldOf("processing_time").forGetter(ProcessingRecipeParams::processingDuration),
                CleanRoomCondition.CODEC.optionalFieldOf("cleanroom_condition", CleanRoomCondition.NONE).forGetter(ProcessingRecipeParams::requiredCleanRoom)
        ).apply(instance, (ingredients, results, processingDuration, cleanroomCondition) -> {
            P params = factory.get();
            ingredients.forEach(either -> either
                    .ifLeft(params.ingredients::add)
                    .ifMiddle(params.fluidIngredients::add)
                    .ifRight(params.gasIngredients::add));

            results.forEach(either -> either
                    .ifLeft(params.itemResults::add)
                    .ifMiddle(params.fluidResults::add)
                    .ifRight(params.gasResults::add));

            params.processingDuration = processingDuration;
            params.requiredCleanRoom = cleanroomCondition;
            return params;
        }));
    }

    @Contract(value = "_ -> new", pure = true)
    protected static <P extends ProcessingRecipeParams> @NotNull StreamCodec<RegistryFriendlyByteBuf, P> streamCodec(Supplier<P> factory) {
        return StreamCodec.of(
                (buffer, params) -> params.encode(buffer),
                buffer -> {
                    P params = factory.get();
                    params.decode(buffer);
                    return params;
                }
        );
    }

    protected void encode(RegistryFriendlyByteBuf buffer) {
        NonNullListStreamCodec.nonNullList(Ingredient.CONTENTS_STREAM_CODEC).encode(buffer, ingredients);
        NonNullListStreamCodec.nonNullList(SizedFluidIngredient.STREAM_CODEC).encode(buffer, fluidIngredients);
        NonNullListStreamCodec.nonNullList(SizedGasIngredient.STREAM_CODEC).encode(buffer, gasIngredients);

        NonNullListStreamCodec.nonNullList(ItemStackChanceResult.STREAM_CODEC).encode(buffer, itemResults);
        NonNullListStreamCodec.nonNullList(FluidStackChanceResult.STREAM_CODEC).encode(buffer, fluidResults);
        NonNullListStreamCodec.nonNullList(GasStackChanceResult.STREAM_CODEC).encode(buffer, gasResults);
        ByteBufCodecs.VAR_INT.encode(buffer, processingDuration);
        CleanRoomCondition.STREAM_CODEC.encode(buffer, requiredCleanRoom);
    }

    protected void decode(RegistryFriendlyByteBuf buffer) {
        this.ingredients = NonNullListStreamCodec.nonNullList(Ingredient.CONTENTS_STREAM_CODEC).decode(buffer);
        this.fluidIngredients = NonNullListStreamCodec.nonNullList(SizedFluidIngredient.STREAM_CODEC).decode(buffer);
        this.gasIngredients = NonNullListStreamCodec.nonNullList(SizedGasIngredient.STREAM_CODEC).decode(buffer);

        this.itemResults = NonNullListStreamCodec.nonNullList(ItemStackChanceResult.STREAM_CODEC).decode(buffer);
        this.fluidResults = NonNullListStreamCodec.nonNullList(FluidStackChanceResult.STREAM_CODEC).decode(buffer);
        this.gasResults = NonNullListStreamCodec.nonNullList(GasStackChanceResult.STREAM_CODEC).decode(buffer);
        this.processingDuration = ByteBufCodecs.VAR_INT.decode(buffer);
        this.requiredCleanRoom = CleanRoomCondition.STREAM_CODEC.decode(buffer);
    }

    protected final List<TriEither<Ingredient, SizedFluidIngredient, SizedGasIngredient>> ingredients() {
        List<TriEither<Ingredient, SizedFluidIngredient, SizedGasIngredient>> ingredients =
                new ArrayList<>(this.ingredients.size() + this.fluidIngredients.size() + this.gasIngredients.size());
        this.ingredients.forEach(ingredient -> ingredients.add(TriEither.left(ingredient)));
        this.fluidIngredients.forEach(ingredient -> ingredients.add(TriEither.middle(ingredient)));
        this.gasIngredients.forEach(ingredient -> ingredients.add(TriEither.right(ingredient)));
        return ingredients;
    }

    protected final List<TriEither<ItemStackChanceResult, FluidStackChanceResult, GasStackChanceResult>> results() {
        List<TriEither<ItemStackChanceResult, FluidStackChanceResult, GasStackChanceResult>> results =
                new ArrayList<>(this.itemResults.size() + this.fluidResults.size() + this.gasResults.size());
        this.itemResults.forEach(result -> results.add(TriEither.left(result)));
        this.fluidResults.forEach(result -> results.add(TriEither.middle(result)));
        this.gasResults.forEach(result -> results.add(TriEither.right(result)));
        return results;
    }

    protected final int processingDuration() {
        return this.processingDuration;
    }

    protected final CleanRoomCondition requiredCleanRoom() {
        return this.requiredCleanRoom;
    }
}
