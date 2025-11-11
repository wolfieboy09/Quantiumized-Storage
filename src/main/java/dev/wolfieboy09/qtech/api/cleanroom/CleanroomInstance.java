package dev.wolfieboy09.qtech.api.cleanroom;

import dev.wolfieboy09.qtech.api.recipes.CleanroomCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

import java.util.Set;

public record CleanroomInstance(BlockPos controllerPos,
                                Set<BlockPos> structureBlocks,
                                AABB boundingBox,
                                int airVolume,
                                CleanroomCondition condition) {
}
