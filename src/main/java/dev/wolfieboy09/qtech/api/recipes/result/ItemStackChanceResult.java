package dev.wolfieboy09.qtech.api.recipes.result;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemStackChanceResult extends ChanceResult<ItemStack> {
    public static final ItemStackChanceResult EMPTY = new ItemStackChanceResult(ItemStack.EMPTY, 1);

    public static final Codec<ItemStackChanceResult> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(r ->
                    BuiltInRegistries.ITEM.getKey(r.getResult().getItem())
            ),
            Codec.INT.optionalFieldOf("count", 1).forGetter(r -> r.getResult().getCount()),
            Codec.FLOAT.optionalFieldOf("chance", 1f).forGetter(ItemStackChanceResult::getChance)
    ).apply(instance, (id, count, chance) ->
            new ItemStackChanceResult(new ItemStack(
                    BuiltInRegistries.ITEM.getOptional(id).orElseThrow(
                            () -> new IllegalArgumentException("Unknown item: " + id)
                    ), count),
                    chance
            )
    ));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackChanceResult> STREAM_CODEC =
            StreamCodec.composite(
                    ItemStack.STREAM_CODEC, ItemStackChanceResult::getResult,
                    ByteBufCodecs.FLOAT, ItemStackChanceResult::getChance,
                    ItemStackChanceResult::new
            );

    public ItemStackChanceResult(ItemStack result, float chance) {
        super(result, chance);
    }

    public ItemStackChanceResult(ItemStack result) {
        this(result, 1);
    }

    @Override
    protected ItemStack copyResult() {
        return this.result.copy();
    }

    @Override
    public Codec<? extends ChanceResult<ItemStack>> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ? extends ChanceResult<ItemStack>> streamCodec() {
        return STREAM_CODEC;
    }
}
