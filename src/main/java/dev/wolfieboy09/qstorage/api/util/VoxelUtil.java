package dev.wolfieboy09.qstorage.api.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public final class VoxelUtil {
    public static @NotNull VoxelShape rotateVoxelShape(@NotNull VoxelShape shape, Direction direction) {
        return Shapes.or(Shapes.empty(), shape.toAabbs().stream()
                .map(box -> Shapes.create(new AABB(
                        rotatePointAroundCenter(box.minX, box.minY, box.minZ, direction),
                        rotatePointAroundCenter(box.maxX, box.maxY, box.maxZ, direction)
                ))).toArray(VoxelShape[]::new));
    }

    public static @NotNull Vec3 rotatePointAroundCenter(double x, double y, double z, @NotNull Direction direction) {
        return switch (direction) {
            case NORTH -> new Vec3(x, y, z);
            case SOUTH -> new Vec3(1 - x, y, 1 - z);
            case EAST -> new Vec3(1 - z, y, x);
            case WEST -> new Vec3(z, y, 1 - x);
            case DOWN -> new Vec3(x, z, 1 - y);
            case UP -> new Vec3(x, 1 - z, y);
        };
    }
}
