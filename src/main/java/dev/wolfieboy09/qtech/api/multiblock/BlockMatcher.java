package dev.wolfieboy09.qtech.api.multiblock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
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

    record SingleBlock(Block block) implements BlockMatcher {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", MatchingType.SINGLE.getSerializedName());
            json.addProperty("value", getBlockId());
            return json;
        }

        public String getBlockId() {
            return BuiltInRegistries.BLOCK.getKey(this.block).toString();
        }
    }

    record AnyOf(List<Block> blocks) implements BlockMatcher {
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
    }

    record Tag(String tagId) implements BlockMatcher {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", MatchingType.TAG.getSerializedName());
            json.addProperty("value", tagId);
            return json;
        }
    }

    final class Air implements BlockMatcher {
        static final Air INSTANCE = new Air();
        private Air() {}

        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", MatchingType.AIR.getSerializedName());
            return json;
        }
    }

    final class Solid implements BlockMatcher {
        static final Solid INSTANCE = new Solid();
        private Solid() {}

        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("type", MatchingType.SOLID.getSerializedName());
            return json;
        }
    }

    enum MatchingType implements StringRepresentable {
        SINGLE, ANY_OF, TAG, AIR, SOLID;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
