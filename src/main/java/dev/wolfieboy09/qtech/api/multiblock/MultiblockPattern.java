package dev.wolfieboy09.qtech.api.multiblock;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.codecs.QTExtraStreamCodecs;
import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        Direction facing = Direction.NORTH; // Default
        if (controllerState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            facing = controllerState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        }

        Map<BlockPos, Character> positions = getAllPositions(controllerPos, facing);

        for (Map.Entry<BlockPos, Character> entry : positions.entrySet()) {
            BlockPos pos = entry.getKey();
            char key = entry.getValue();

            if (!isPositionCorrect(level, pos, key)) {
                return false;
            }
        }

        return true;
    }


    public Map<BlockPos, Character> getAllPositions(BlockPos controllerPos, Direction facing) {
        Map<BlockPos, Character> positions = new HashMap<>();

        // Rotate the controller offset first
        BlockPos rotatedOffset = rotatePosition(this.controllerPosition, facing);

        // Calculate start position (subtract rotated controller offset to get origin)
        BlockPos startPos = controllerPos.subtract(rotatedOffset);

        for (int y = 0; y < layers.size(); y++) {
            Layer layer = layers.get(y);

            for (int z = 0; z < layer.getDepth(); z++) {
                String row = layer.getRow(z);

                for (int x = 0; x < row.length(); x++) {
                    char key = row.charAt(x);

                    // Create relative position in pattern space
                    BlockPos relativePos = new BlockPos(x, y, z);

                    // Rotate the relative position based on facing
                    BlockPos rotatedRelative = rotatePosition(relativePos, facing);

                    BlockPos worldPos = startPos.offset(rotatedRelative);

                    if (worldPos.equals(controllerPos)) {
                        continue;
                    }

                    positions.put(worldPos.immutable(), key);
                }
            }
        }

        return positions;
    }

    private BlockPos rotatePosition(BlockPos pos, Direction facing) {
        return switch (facing) {
            case EAST -> new BlockPos(-pos.getZ(), pos.getY(), pos.getX()); // 90* CW
            case SOUTH -> new BlockPos(-pos.getX(), pos.getY(), -pos.getZ()); // 180*
            case WEST -> new BlockPos(pos.getZ(), pos.getY(), -pos.getX()); // 270* CW
            default -> pos; // Includes north
        };
    }

    /**
     * Reverse rotation - from facing back to NORTH orientation
     * Used when you need to convert world positions back to pattern space
     */
    private BlockPos unrotatePosition(BlockPos pos, Direction facing) {
        return switch (facing) {
            case NORTH -> pos; // No rotation
            case EAST -> new BlockPos(pos.getZ(), pos.getY(), -pos.getX()); // 90* CCW
            case SOUTH -> new BlockPos(-pos.getX(), pos.getY(), -pos.getZ()); // 180*
            case WEST -> new BlockPos(-pos.getZ(), pos.getY(), pos.getX()); // 270* CCW
            default -> pos;
        };
    }

    /**
     * Get supported blocks at a world position, accounting for rotation
     * @param controllerPos The controller's world position
     * @param facing The direction the controller is facing
     * @param worldPos The world position to check
     * @return List of blocks that can be at this position
     */
    public List<Block> getSupportedBlocksAtWorld(BlockPos controllerPos, Direction facing, BlockPos worldPos) {
        // Convert world position back to pattern space
        BlockPos rotatedOffset = rotatePosition(this.controllerPosition, facing);
        BlockPos startPos = controllerPos.subtract(rotatedOffset);
        BlockPos rotatedRelative = worldPos.subtract(startPos);
        BlockPos patternPos = unrotatePosition(rotatedRelative, facing);

        return getSupportedBlocks(patternPos);
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

    public JsonElement toJson() {
        return CODEC.encodeStart(JsonOps.INSTANCE,this).getOrThrow();
    }
}