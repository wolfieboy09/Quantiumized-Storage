package dev.wolfieboy09.qtech.api.multiblock.tracking;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.multiblock.blocks.controller.BaseMultiblockController;
import dev.wolfieboy09.qtech.api.multiblock.blocks.controller.BaseMultiblockControllerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = QuantiumizedTech.MOD_ID)
public class MultiblockTracker {

    // Level -> (Block Position -> Controller Position)
    private static final Map<Level, Map<BlockPos, BlockPos>> TRACKED_BLOCKS = new ConcurrentHashMap<>();

    @Contract("_, _ -> new")
    public static Optional<BlockPos> getControllerPos(Level level, BlockPos pos) {
        return level == null ? Optional.empty() : Optional.ofNullable(TRACKED_BLOCKS.get(level).get(pos));
    }

    public static void registerMultiblock(Level level, BlockPos controllerPos, Set<BlockPos> positions) {
        if (level == null) return;

        Map<BlockPos, BlockPos> levelMap = TRACKED_BLOCKS.computeIfAbsent(level, k -> new ConcurrentHashMap<>());

        for (BlockPos pos : positions) {
            levelMap.put(pos, controllerPos);
        }
    }

    public static void unregisterMultiblock(Level level, BlockPos controllerPos) {
        if (level == null) return;

        Map<BlockPos, BlockPos> levelMap = TRACKED_BLOCKS.get(level);
        if (levelMap == null) return;

        levelMap.entrySet().removeIf(entry -> entry.getValue().equals(controllerPos));
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.@NotNull BreakEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos brokenPos = event.getPos();

        Map<BlockPos, BlockPos> levelMap = TRACKED_BLOCKS.get(level);
        if (levelMap == null) return;

        BlockPos controllerPos = levelMap.get(brokenPos);
        if (controllerPos == null) return;

        if (level.getBlockEntity(controllerPos) instanceof BaseMultiblockControllerEntity controller) {
            controller.breakMultiblock();
            // Update block state
            BlockState state = controller.getBlockState();
            if (state.hasProperty(BaseMultiblockController.FORMED)) {
                level.setBlock(controllerPos, state.setValue(BaseMultiblockController.FORMED, false), 3);
            }
        }

        levelMap.remove(brokenPos);
    }

    public static void clearLevel(Level level) {
        TRACKED_BLOCKS.remove(level);
    }
}
