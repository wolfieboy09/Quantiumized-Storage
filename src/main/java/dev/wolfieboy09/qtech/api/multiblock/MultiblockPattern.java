package dev.wolfieboy09.qtech.api.multiblock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.codecs.QTExtraStreamCodecs;
import dev.wolfieboy09.qtech.api.registry.QTRegistries;
import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;

import java.util.*;
import java.util.stream.StreamSupport;

@NothingNullByDefault
public record MultiblockPattern(String name, MultiblockType multiblockType, ResourceLocation controller, BlockPos controllerPosition,
                                Map<Character, BlockMatcher> keyMap, List<Layer> layers) {
    private static final Set<Character> RESERVED_KEYS = Set.of(' ', '*', '+');

    public MultiblockPattern(String name, MultiblockType multiblockType, ResourceLocation controller, BlockPos controllerPosition,
                             Map<Character, BlockMatcher> keyMap, List<Layer> layers) {
        this.name = name;
        this.controllerPosition = controllerPosition;
        this.controller = controller;
        this.multiblockType = multiblockType;
        this.keyMap = new HashMap<>(keyMap);
        this.layers = List.copyOf(layers);
    }

    public static final Codec<MultiblockPattern> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("name").forGetter(MultiblockPattern::name),
            MultiblockType.CODEC.fieldOf("multiblock_type").forGetter(MultiblockPattern::multiblockType),
            ResourceLocation.CODEC.fieldOf("controller").forGetter(MultiblockPattern::controller),
            BlockPos.CODEC.fieldOf("controller_offset").forGetter(MultiblockPattern::controllerPosition),
            Codec.unboundedMap(
                    Codec.STRING.xmap(s -> s.charAt(0), String::valueOf),
                    BlockMatcher.CODEC
            ).fieldOf("pattern").forGetter(MultiblockPattern::keyMap),
            Layer.CODEC.listOf().fieldOf("layers").forGetter(MultiblockPattern::layers)
    ).apply(inst, MultiblockPattern::new));


    public static final StreamCodec<RegistryFriendlyByteBuf, MultiblockPattern> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, MultiblockPattern::name,
            MultiblockType.STREAM_CODEC, MultiblockPattern::multiblockType,
            ResourceLocation.STREAM_CODEC, MultiblockPattern::getControllerBlock,
            BlockPos.STREAM_CODEC, MultiblockPattern::controllerPosition,
            ByteBufCodecs.map(
                    HashMap::new,
                    QTExtraStreamCodecs.CHAR,
                    BlockMatcher.STREAM_CODEC
            ), MultiblockPattern::keyMap,
            Layer.STREAM_CODEC.apply(ByteBufCodecs.list()), MultiblockPattern::layers,
            MultiblockPattern::new
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

    public List<Block> getSupportedBlocks(BlockPos patternPos) {
        if (patternPos.getY() < 0 || patternPos.getY() >= layers.size()) {
            return List.of();
        }

        Layer layer = this.layers.get(patternPos.getY());

        if (patternPos.getZ() < 0 || patternPos.getZ() >= layer.getDepth()) {
            return List.of();
        }

        String row = layer.getRow(patternPos.getZ());
        if (patternPos.getX() < 0 || patternPos.getX() >= row.length()) {
            return List.of();
        }

        char key = row.charAt(patternPos.getX());
        BlockMatcher matcher = keyMap.get(key);

        switch (matcher) {
            case null -> {
                return List.of();
            }
            case BlockMatcher.SingleBlock(Block block) -> {
                return List.of(block);
            }
            case BlockMatcher.AnyOf(List<Block> blocks) -> {
                return List.copyOf(blocks);
            }
            case BlockMatcher.Tag(String tagId) -> {
                TagKey<Block> tagKey = TagKey.create(Registries.BLOCK, ResourceLocation.parse(tagId));
                return BuiltInRegistries.BLOCK.getTag(tagKey)
                        .map(holders -> StreamSupport.stream(holders.spliterator(), false)
                                .map(Holder::value)
                                .toList())
                        .orElse(List.of());
            }
            case BlockMatcher.Air ignored -> {
                return List.of(Blocks.AIR);
            }
            case BlockMatcher.Solid ignored -> {
                //TODO: Somehow tell the caller it's any block
                return List.of();
            }
            default -> {}
        }

        return List.of();
    }

    public boolean matches(Level level, BlockPos controllerPos, BlockState controllerState) {
        // Check if controller block is correct
        ResourceLocation controllerBlockId = BuiltInRegistries.BLOCK.getKey(controllerState.getBlock());

        if (!controllerBlockId.equals(this.controller)) {
            return false;
        }

        // Check all other positions
        Map<BlockPos, Character> positions = getAllPositions(controllerPos);

        for (Map.Entry<BlockPos, Character> entry : positions.entrySet()) {
            BlockPos pos = entry.getKey();
            char key = entry.getValue();

            if (!isPositionCorrect(level, pos, key)) {
                return false;
            }
        }

        return true;
    }


    public Map<BlockPos, Character> getAllPositions(BlockPos controllerPos) {
        Map<BlockPos, Character> positions = new HashMap<>();

        // Calculate start position (subtract controller offset to get origin)
        BlockPos startPos = controllerPos.subtract(this.controllerPosition);

        for (int y = 0; y < layers.size(); y++) {
            Layer layer = layers.get(y);

            for (int z = 0; z < layer.getDepth(); z++) {
                String row = layer.getRow(z);

                for (int x = 0; x < row.length(); x++) {
                    char key = row.charAt(x);
                    BlockPos pos = startPos.offset(x, y, z);

                    if (pos.equals(controllerPos)) {
                        continue;
                    }

                    positions.put(pos.immutable(), key);
                }
            }
        }

        return positions;
    }

    public boolean isPositionCorrect(Level level, BlockPos pos, char expectedKey) {
        BlockState state = level.getBlockState(pos);
        return switch (expectedKey) {
            case ' ' -> state.isAir();
            case '*' -> state.isSolid();
            case '+' -> state.getBlock().equals(BuiltInRegistries.BLOCK.get(this.controller));
            default -> this.keyMap.get(expectedKey) != null && this.keyMap.get(expectedKey).test(state);
        };
    }

    public JsonObject toJson() {
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

    @Contract("_ -> new")
    public static MultiblockPattern fromJson(JsonObject root) {
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

        return new MultiblockPattern(name, type, controller, controllerOffset, keyMap, layers);
    }
}