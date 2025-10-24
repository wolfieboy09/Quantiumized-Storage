package dev.wolfieboy09.qtech.api.multiblock;

import dev.wolfieboy09.qtech.api.multiblock.blocks.BaseMultiblockController;
import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import net.minecraft.core.BlockPos;
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

    public MultiblockPattern build() throws IllegalStateException {
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

        return new MultiblockPattern(this.name, this.multiblockType, this.controller, this.controllerPosition, this.keyMap, this.layers);
    }
}
