package dev.wolfieboy09.qtech.api.multiblock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.codecs.QTExtraStreamCodecs;
import dev.wolfieboy09.qtech.api.multiblock.blocks.BaseMultiblockController;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Supplier;

/**
 * Class to build Multiblocks with
 */
@ParametersAreNonnullByDefault
public class MultiblockBuilder {
    private static final Set<Character> RESERVED_KEYS = Set.of(' ', '*', '+');

    private final String name;
    private MultiblockType multiblockType;
    private final Map<Character, BlockMatcher> keyMap = new HashMap<>();
    private BlockPos controllerPosition = BlockPos.ZERO;
    private final List<Layer> layers = new ArrayList<>();
    private ResourceLocation controller;

    private MultiblockBuilder(String name) {
        this.name = name;
    }

    /**
     * Create a new multiblock pattern builder
     * @param name Unique identifier for this pattern
     */
    @Contract("_ -> new")
    public static @NotNull MultiblockBuilder create(String name) {
        return new MultiblockBuilder(name);
    }

    /**
     * Set the multiblock type this pattern belongs to
     * @param type The {@link Supplier} of the {@link MultiblockType}
     */
    public MultiblockBuilder type(Supplier<MultiblockType> type) {
        this.multiblockType = type.get();
        return this;
    }

    /**
     * Define a key that matches a single block
     */
    public MultiblockBuilder key(char symbol, Block block) {
        validateKey(symbol);
        this.keyMap.put(symbol, BlockMatcher.single(block));
        return this;
    }

    /**
     * Define a key that matches a single block
     */
    public MultiblockBuilder key(char symbol, DeferredBlock<?> block) {
        validateKey(symbol);
        this.keyMap.put(symbol, BlockMatcher.single(block.get()));
        return this;
    }

    /**
     * Define a key that matches any of the given blocks
     */
    public MultiblockBuilder key(char symbol, Block... blocks) {
        validateKey(symbol);
        if (blocks.length == 0) throw new IllegalArgumentException("There must be at least one block defined");
        this.keyMap.put(symbol, BlockMatcher.anyOf(blocks));
        return this;
    }

    /**
     * Define a key that matches any of the given blocks
     */
    public MultiblockBuilder key(char symbol, DeferredBlock<?>... blocks) {
        validateKey(symbol);
        if (blocks.length == 0) throw new IllegalArgumentException("There must be at least one block defined");
        List<Block> fetchedBlocked = new ArrayList<>();
        for (DeferredBlock<?> b : blocks) {
            fetchedBlocked.add(b.get());
        }
        this.keyMap.put(symbol, BlockMatcher.anyOf(fetchedBlocked));
        return this;
    }

    /**
     * Define a key that matches a block tag
     */
    public MultiblockBuilder key(char symbol, TagKey<Block> tag) {
        validateKey(symbol);
        this.keyMap.put(symbol, BlockMatcher.tag(tag));
        return this;
    }

    /**
     * Define a key using a custom {@link BlockMatcher}
     */
    public MultiblockBuilder key(char symbol, BlockMatcher matcher) {
        validateKey(symbol);
        this.keyMap.put(symbol, matcher);
        return this;
    }

    /**
     * Add a layer to the pattern
     * Layers are built from bottom to top
     * @param rows Array of strings representing each row (back to front)
     */
    public MultiblockBuilder layer(String... rows) {
        Layer layer = Layer.of(rows);
        if (!layer.isValid()) {
            throw new IllegalArgumentException("All rows in a layer must have the same length");
        }
        this.layers.add(layer);
        return this;
    }

    /**
     * Add a pre-constructed layer
     */
    public MultiblockBuilder layer(Layer layer) {
        if (!layer.isValid()) {
            throw new IllegalArgumentException("All rows in a layer must have the same length");
        }
        this.layers.add(layer);
        return this;
    }

    public MultiblockBuilder controller(DeferredBlock<? extends BaseMultiblockController> controller) {
        this.controller = controller.getId();
        return this;
    }

    public MultiblockBuilder controller(BaseMultiblockController controller) {
        this.controller = controller.builtInRegistryHolder().getKey().location();
        return this;
    }

    private void validateKey(char symbol) {
        if (RESERVED_KEYS.contains(symbol)) {
            throw new IllegalArgumentException(
                    "Key '" + symbol + "' is reserved and cannot be used."
            );
        }
    }

    @Contract(" -> new")
    private @NotNull BlockPos findControllerPosition() {
        for (int y = 0; y < layers.size(); y++) {
            Layer layer = layers.get(y);
            for (int z = 0; z < layer.getDepth(); z++) {
                String row = layer.getRow(z);
                for (int x = 0; x < row.length(); x++) {
                    if (row.charAt(x) == '+') {
                        return new BlockPos(x, y, z);
                    }
                }
            }
        }
        throw new IllegalStateException(
                "Pattern must contain exactly one '+' character to mark the controller position"
        );
    }

    public PatternData build() throws IllegalStateException {
        if (this.multiblockType == null) {
            throw new IllegalStateException("Multiblock type must be set");
        }
        if (this.layers.isEmpty()) {
            throw new IllegalStateException("Pattern must have at least one layer");
        }
        if (this.controller == null) {
            throw new IllegalStateException("Controller block was not defined");
        }

        int width = this.layers.getFirst().getWidth();
        int depth = this.layers.getFirst().getDepth();
        for (Layer layer : this.layers) {
            if (layer.getWidth() != width || layer.getDepth() != depth) {
                throw new IllegalStateException("All layers must have the same dimensions");
            }
        }

        this.controllerPosition = findControllerPosition();

        return new PatternData(this.name, this.multiblockType, this.controller, this.controllerPosition, this.keyMap, this.layers);
    }

    public record PatternData(String name, MultiblockType multiblockType, ResourceLocation controller, BlockPos controllerPosition,
                              Map<Character, BlockMatcher> keyMap, List<Layer> layers) {

        public PatternData(String name, MultiblockType multiblockType, ResourceLocation controller, BlockPos controllerPosition,
                           Map<Character, BlockMatcher> keyMap, List<Layer> layers) {
            this.name = name;
            this.controllerPosition = controllerPosition;
            this.controller = controller;
            this.multiblockType = multiblockType;
            this.keyMap = new HashMap<>(keyMap);
            this.layers = List.copyOf(layers);
        }

        public static final Codec<PatternData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.STRING.fieldOf("name").forGetter(PatternData::name),
                MultiblockType.CODEC.fieldOf("multiblock_type").forGetter(PatternData::multiblockType),
                ResourceLocation.CODEC.fieldOf("controller").forGetter(PatternData::controller),
                BlockPos.CODEC.fieldOf("controller_offset").forGetter(PatternData::controllerPosition),
                Codec.unboundedMap(
                        Codec.STRING.xmap(s -> s.charAt(0), String::valueOf),
                        BlockMatcher.CODEC
                ).fieldOf("pattern").forGetter(PatternData::keyMap),
                Layer.CODEC.listOf().fieldOf("layers").forGetter(PatternData::layers)
        ).apply(inst, PatternData::new));


        public static final StreamCodec<RegistryFriendlyByteBuf, PatternData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, PatternData::name,
            MultiblockType.STREAM_CODEC, PatternData::multiblockType,
            ResourceLocation.STREAM_CODEC, PatternData::getControllerBlock,
            BlockPos.STREAM_CODEC, PatternData::controllerPosition,
            ByteBufCodecs.map(
                    HashMap::new,
                    QTExtraStreamCodecs.CHAR,
                    BlockMatcher.STREAM_CODEC
            ), PatternData::keyMap,
            Layer.STREAM_CODEC.apply(ByteBufCodecs.list()), PatternData::layers,
            PatternData::new
        );


        /**
         * Get the controller offset (relative to pattern origin at 0,0,0)
         */
        public BlockPos getControllerOffset() {
            return this.controllerPosition;
        }

        public int getWidth() {
            return this.layers.isEmpty() ? 0 : this.layers.getFirst().getWidth();
        }

        public int getHeight() {
            return this.layers.size();
        }

        public int getDepth() {
            return this.layers.isEmpty() ? 0 : this.layers.getFirst().getDepth();
        }

        public ResourceLocation getControllerBlock() {
            return this.controller;
        }

        public @NotNull JsonObject toJson() {
            JsonObject root = new JsonObject();
            root.addProperty("name", this.name);
            root.addProperty("multiblock_type", this.multiblockType.getMultiblockType().toString());

            root.addProperty("controller", this.controller.toString());

            JsonObject controllerOffset = new JsonObject();
            controllerOffset.addProperty("x", this.controllerPosition.getX());
            controllerOffset.addProperty("y", this.controllerPosition.getY());
            controllerOffset.addProperty("z", this.controllerPosition.getZ());
            root.add("controller_offset", controllerOffset);

            JsonObject keyMapJson = new JsonObject();
            for (Map.Entry<Character, BlockMatcher> pair : this.keyMap.entrySet()) {
                // Skip the reserved keys due to it being handled automatically and not needed to be added to JSON
                if (RESERVED_KEYS.contains(pair.getKey())) {
                    continue;
                }
                keyMapJson.add(pair.getKey().toString(), pair.getValue().toJson());
            }

            root.add("pattern", keyMapJson);

            JsonArray rootLayerJson = new JsonArray();
            for (Layer layer : this.layers) {
                JsonArray layerJson = new JsonArray();
                for (int i = 0; i < layer.getDepth(); i++) {
                    layerJson.add(layer.getRow(i));
                }
                rootLayerJson.add(layerJson);
            }

            root.add("layers", rootLayerJson);

            return root;
        }

        public static @NotNull PatternData fromJson(JsonObject root) {
            String name = Objects.requireNonNull(root.get("name").getAsString(), "Missing name");
            MultiblockType type = Objects.requireNonNull(QTRegistries.MULTIBLOCK_TYPE.get(ResourceLocation.parse(root.get("multiblock_type").getAsString())), "Missing multiblock_type");
            ResourceLocation controller = Objects.requireNonNull(ResourceLocation.parse(root.get("controller").getAsString()), "Missing controller");

            BlockPos controllerOffset;

            if (root.has("controller_offset")) {
                JsonObject offset = root.getAsJsonObject("controller_offset");
                if (!(offset.has("x") || offset.has("y") || offset.has("z"))) {
                    throw new NullPointerException("Invalid controller_offset");
                }
                controllerOffset = new BlockPos(offset.get("x").getAsInt(), offset.get("y").getAsInt(), offset.get("z").getAsInt());
            } else {
                throw new NullPointerException("Missing controller_offset");
            }

            Map<Character, BlockMatcher> keyMap = new HashMap<>();

            if (root.has("pattern")) {
                JsonObject patternData = root.getAsJsonObject("pattern");
                Set<String> set = patternData.keySet();
                for (String key : set) {
                    keyMap.put(key.charAt(0), BlockMatcher.fromJson(patternData.get(key)));
                }
            } else {
                throw new NullPointerException("Missing pattern");
            }

            if (!root.has("layers")) throw new NullPointerException("Missing layers");


            List<Layer> layers = root.getAsJsonArray("layers").asList().stream()
                    .map(JsonElement::getAsJsonArray)
                    .map(JsonElement::getAsString)
                    .map(Layer::of)
                    .toList();

            return new PatternData(name, type, controller, controllerOffset, keyMap, layers);
        }
    }
}
