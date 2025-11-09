package dev.wolfieboy09.qtech.api.cleanroom;

import dev.wolfieboy09.qtech.api.recipes.CleanRoomCondition;
import dev.wolfieboy09.qtech.block.cleanroom.controller.CleanroomControllerBlockEntity;
import dev.wolfieboy09.qtech.registries.tags.QTBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CleanroomTracker {
    private static final Map<Level, Map<BlockPos, CleanroomInstance>> CLEANROOMS = new ConcurrentHashMap<>();

    public static Optional<CleanroomInstance> getCleanroomAt(Level level, BlockPos pos) {
        if (level == null) return Optional.empty();
        Map<BlockPos, CleanroomInstance> map = CLEANROOMS.get(level);
        return map == null ? Optional.empty() : Optional.ofNullable(map.get(pos));
    }

    public static void register(Level level, CleanroomInstance instance) {
        if (level == null || instance == null) return;
        Map<BlockPos, CleanroomInstance> levelMap = CLEANROOMS.computeIfAbsent(level, l -> new ConcurrentHashMap<>());
        for (BlockPos pos : instance.structureBlocks()) {
            levelMap.put(pos, instance);
        }
    }

    public static void unregister(Level level, CleanroomInstance instance) {
        if (level == null || instance == null) return;
        Map<BlockPos, CleanroomInstance> levelMap = CLEANROOMS.get(level);
        if (levelMap != null) {
            levelMap.entrySet().removeIf(e -> e.getValue().equals(instance));
        }
    }

    public static void clearLevel(Level level) {
        CLEANROOMS.remove(level);
    }

    /**
     * Attempts to discover and register a cleanroom structure starting from the controller.
     * Returns the created instance if successful.
     * @apiNote The max range is hard-coded to 40 blocks
     */
    public static Optional<CleanroomInstance> discover(Level level, BlockPos controllerPos) {
        if (level == null) return Optional.empty();

        int radius = 40;
        BlockPos min = controllerPos.offset(-radius, -radius, -radius);
        BlockPos max = controllerPos.offset(radius, radius, radius);

        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        Set<BlockPos> structureBlocks = new HashSet<>();
        Set<BlockPos> airInside = new HashSet<>();

        queue.add(controllerPos.below());

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            if (!visited.add(current)) continue;
            if (!withinBounds(current, min, max)) continue;

            BlockState state = level.getBlockState(current);

            if (state.isAir()) {
                airInside.add(current);
                for (var dir : net.minecraft.core.Direction.values()) {
                    queue.add(current.relative(dir));
                }
            } else if (state.is(QTBlockTags.CLEANROOM_TILE)) {
                structureBlocks.add(current);
            }
        }

        if (airInside.isEmpty() || structureBlocks.isEmpty()) return Optional.empty();

        AABB box = makeAABBFromPositions(structureBlocks);

        if (!isEnclosed(level, airInside)) return Optional.empty();

        CleanroomInstance instance = new CleanroomInstance(controllerPos, structureBlocks, box, airInside.size(), evaluateCondition(level, structureBlocks));
        register(level, instance);
        return Optional.of(instance);
    }

    private static boolean withinBounds(@NotNull BlockPos pos, @NotNull BlockPos min, BlockPos max) {
        return pos.getX() >= min.getX() && pos.getX() <= max.getX()
                && pos.getY() >= min.getY() && pos.getY() <= max.getY()
                && pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
    }

    @Contract("_ -> new")
    private static @NotNull AABB makeAABBFromPositions(@NotNull Set<BlockPos> positions) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (BlockPos pos : positions) {
            minX = Math.min(minX, pos.getX());
            minY = Math.min(minY, pos.getY());
            minZ = Math.min(minZ, pos.getZ());
            maxX = Math.max(maxX, pos.getX());
            maxY = Math.max(maxY, pos.getY());
            maxZ = Math.max(maxZ, pos.getZ());
        }
        return new AABB(minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1);
    }

    private static boolean isEnclosed(Level level, @NotNull Set<BlockPos> air) {
        for (BlockPos pos : air) {
            for (var dir : net.minecraft.core.Direction.values()) {
                BlockPos neighbor = pos.relative(dir);
                if (!level.isLoaded(neighbor)) return false;
                BlockState state = level.getBlockState(neighbor);
                if (state.isAir()) continue;
                if (!state.is(QTBlockTags.CLEANROOM_TILE)) {
                    return false;
                }
            }
        }
        return true;
    }

    @SubscribeEvent
    public static void onBlockBreak(@NotNull BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();

        Map<BlockPos, CleanroomInstance> levelMap = CLEANROOMS.get(level);
        if (levelMap == null) return;

        CleanroomInstance instance = levelMap.get(pos);
        if (instance == null) return;

        if (level.getBlockEntity(instance.controllerPos()) instanceof CleanroomControllerBlockEntity controller) {
            controller.onCleanroomBroken();
        }

        unregister(level, instance);
    }

    private static CleanRoomCondition evaluateCondition(Level level, @NotNull Set<BlockPos> structureBlocks) {
        boolean hasFilter = false;
        boolean hasSterilizer = false;
        boolean hasVacuumPump = false;

        for (BlockPos pos : structureBlocks) {
            BlockState state = level.getBlockState(pos);

            if (state.is(QTBlockTags.CLEANROOM_FILTER)) hasFilter = true;
            if (state.is(QTBlockTags.CLEANROOM_STERILIZER)) hasSterilizer = true;
            if (state.is(QTBlockTags.CLEANROOM_VACUUM)) hasVacuumPump = true;
        }

        if (hasVacuumPump) return CleanRoomCondition.VACUUM;
        if (hasSterilizer) return CleanRoomCondition.STERILIZED;
        if (hasFilter) return CleanRoomCondition.ULTRA_CLEAN;
        return CleanRoomCondition.CONTROLLED;
    }

}
