package dev.wolfieboy09.qtech.api.multiblock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.codecs.ByteBufStrings;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;

import java.util.List;

/**
 * Defined how blocks should be matched with a multiblock pattern
 */
@NothingNullByDefault
public sealed interface BlockMatcher {

    /**
     * Match a single specific block
     */
    @Contract("_ -> new")
    static BlockMatcher single(Block block) {
        return new SingleBlock(block);
    }

    /**
     * Match any of the given blocks
     */
    @Contract("_ -> new")
    static BlockMatcher anyOf(Block... blocks) {
        return new AnyOf(List.of(blocks));
    }

    /**
     * Match any of the given blocks
     */
    @Contract("_ -> new")
    static BlockMatcher anyOf(List<Block> blocks) {
        return new AnyOf(blocks);
    }

    /**
     * Match blocks in a tag
     */
    @Contract("_ -> new")
    static BlockMatcher tag(TagKey<Block> tag) {
        return new Tag(tag.location().toString());
    }

    /**
     * Match air blocks
     */
    static BlockMatcher air() {
        return Air.INSTANCE;
    }

    /**
     * Match any solid block
     */
    static BlockMatcher solid() {
        return Solid.INSTANCE;
    }


    /**
     * Used to generate JSON for data generation
     *
     * @return The JSON for a data generator to use
     */
    JsonElement toJson();

    Codec<?> codec();

    StreamCodec<? super RegistryFriendlyByteBuf, ?> streamCodec();

    boolean test(BlockState state);

    record SingleBlock(Block block) implements BlockMatcher {
        public static final Codec<SingleBlock> CODEC = BuiltInRegistries.BLOCK.byNameCodec().xmap(SingleBlock::new, SingleBlock::block);

         public static final StreamCodec<RegistryFriendlyByteBuf, SingleBlock> STREAM_CODEC =
                ByteBufCodecs.registry(Registries.BLOCK).map(SingleBlock::new, SingleBlock::block);

        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", MatchingType.SINGLE.getSerializedName());
            json.addProperty("value", getBlockId());
            return json;
        }

        @Override
        public Codec<?> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ?> streamCodec() {
            return STREAM_CODEC;
        }

        public String getBlockId() {
            return BuiltInRegistries.BLOCK.getKey(this.block).toString();
        }

        @Override
        public boolean test(BlockState state) {
            return state.is(this.block);
        }
    }

    record AnyOf(List<Block> blocks) implements BlockMatcher {
        public static final Codec<AnyOf> CODEC = BuiltInRegistries.BLOCK.byNameCodec()
                .listOf()
                .xmap(AnyOf::new, AnyOf::blocks);

        public static final StreamCodec<RegistryFriendlyByteBuf, AnyOf> STREAM_CODEC =
                ByteBufCodecs.registry(Registries.BLOCK)
                        .apply(ByteBufCodecs.list())
                        .map(AnyOf::new, AnyOf::blocks);


        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", MatchingType.ANY_OF.getSerializedName());
            JsonArray array = new JsonArray();
            this.blocks.stream()
                    .map(BuiltInRegistries.BLOCK::getKey)
                    .map(ResourceLocation::toString)
                    .forEach(array::add);
            json.add("value", array);
            return json;
        }

        @Override
        public Codec<?> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ?> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public boolean test(BlockState state) {
            return this.blocks.stream().anyMatch(state::is);
        }
    }

    record Tag(String tagId) implements BlockMatcher {
        public static final Codec<Tag> CODEC = Codec.STRING.xmap(Tag::new, Tag::tagId);

        public static final StreamCodec<ByteBuf, Tag> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, Tag::tagId,
                Tag::new
        );

        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", MatchingType.TAG.getSerializedName());
            json.addProperty("value", tagId);
            return json;
        }

        @Override
        public Codec<?> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<ByteBuf, ?> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public boolean test(BlockState state) {
            return state.is(TagKey.create(Registries.BLOCK, ResourceLocation.parse(this.tagId)));
        }
    }

    final class Air implements BlockMatcher {
        static final Air INSTANCE = new Air();
        public static final Codec<Air> CODEC = Codec.unit(INSTANCE);
        public static final StreamCodec<ByteBuf, Air> STREAM_CODEC = StreamCodec.unit(INSTANCE);

        private Air() {}

        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", MatchingType.AIR.getSerializedName());
            return json;
        }

        @Override
        public Codec<?> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<ByteBuf, ?> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public boolean test(BlockState state) {
            return state.isAir();
        }
    }

    final class Solid implements BlockMatcher {
        static final Solid INSTANCE = new Solid();
        public static final Codec<Solid> CODEC = Codec.unit(INSTANCE);
        public static final StreamCodec<ByteBuf, Solid> STREAM_CODEC = StreamCodec.unit(INSTANCE);


        private Solid() {}

        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", MatchingType.SOLID.getSerializedName());
            return json;
        }

        @Override
        public Codec<?> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<ByteBuf, ?> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public boolean test(BlockState state) {
            return state.isSolid();
        }
    }

    enum MatchingType implements StringRepresentable {
        SINGLE, ANY_OF, TAG, AIR, SOLID;

        public static final Codec<MatchingType> CODEC = StringRepresentable.fromEnum(MatchingType::values);


        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

    // Anything below this line is for codec stuff... yeah...

    Codec<BlockMatcher> CODEC = MatchingType.CODEC.dispatch(
            BlockMatcher::matcherType,
            type -> switch (type) {
                case SINGLE -> SingleBlock.CODEC.fieldOf("value").xmap(b -> (BlockMatcher) b, m -> (SingleBlock) m);
                case ANY_OF -> AnyOf.CODEC.fieldOf("value").xmap(b -> (BlockMatcher) b, m -> (AnyOf) m);
                case TAG -> Tag.CODEC.fieldOf("value").xmap(b -> (BlockMatcher) b, m -> (Tag) m);
                case AIR -> Air.CODEC.fieldOf("value").xmap(b -> (BlockMatcher) b, m -> (Air) m);
                case SOLID -> Solid.CODEC.fieldOf("value").xmap(b -> (BlockMatcher) b, m -> (Solid) m);
            }
    );

    StreamCodec<RegistryFriendlyByteBuf, BlockMatcher> STREAM_CODEC =
            StreamCodec.of(
                    (buf, matcher) -> {
                        MatchingType type = matcherType(matcher);
                        ByteBufStrings.writeString(buf, type.getSerializedName());
                        switch (type) {
                            case SINGLE -> encodeWith(SingleBlock.STREAM_CODEC, buf, (SingleBlock) matcher);
                            case ANY_OF -> encodeWith(AnyOf.STREAM_CODEC, buf, (AnyOf) matcher);
                            case TAG    -> encodeWith(Tag.STREAM_CODEC, buf, (Tag) matcher);
                            case AIR    -> encodeWith(Air.STREAM_CODEC, buf, Air.INSTANCE);
                            case SOLID  -> encodeWith(Solid.STREAM_CODEC, buf, Solid.INSTANCE);
                        }
                    },
                    buf -> {
                        MatchingType type = MatchingType.valueOf(ByteBufStrings.readString(buf).toUpperCase());
                        return switch (type) {
                            case SINGLE -> decodeWith(SingleBlock.STREAM_CODEC, buf);
                            case ANY_OF -> decodeWith(AnyOf.STREAM_CODEC, buf);
                            case TAG    -> decodeWith(Tag.STREAM_CODEC, buf);
                            case AIR    -> decodeWith(Air.STREAM_CODEC, buf);
                            case SOLID  -> decodeWith(Solid.STREAM_CODEC, buf);
                        };
                    }
            );

    private static <T> void encodeWith(StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
                                       RegistryFriendlyByteBuf buf,
                                       T value) {
        codec.encode(buf, value);
    }

    private static <T> T decodeWith(StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
                                    RegistryFriendlyByteBuf buf) {
        return codec.decode(buf);
    }

    private static MatchingType matcherType(BlockMatcher matcher) {
        return switch (matcher) {
            case SingleBlock ignored -> MatchingType.SINGLE;
            case AnyOf ignored -> MatchingType.ANY_OF;
            case Tag ignored -> MatchingType.TAG;
            case Air ignored -> MatchingType.AIR;
            case Solid ignored -> MatchingType.SOLID;
        };
    }

    static BlockMatcher fromJson(JsonElement json) {
        return switch (json.getAsJsonObject().get("type").getAsString()) {
            case "single" -> new SingleBlock(BuiltInRegistries.BLOCK.get(ResourceLocation.parse(json.getAsJsonObject().get("value").getAsString())));
            case "any_of" -> new AnyOf(
                    json.getAsJsonObject().getAsJsonArray("value").asList().stream()
                            .map(JsonElement::getAsString)
                            .map(ResourceLocation::parse)
                            .map(BuiltInRegistries.BLOCK::get)
                            .toList()
            );
            case "tag" -> new Tag(json.getAsJsonObject().get("value").getAsString());
            case "air" -> new Air();
            case "solid" -> new Solid();

            default -> throw new IllegalArgumentException("Unknown BlockMatcher type: " + json.getAsJsonObject().get("type").toString());
        };
    }
}
