package dev.wolfieboy09.qstorage.item;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlock;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlockEntity;
import dev.wolfieboy09.qstorage.block.pipe.ConnectionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@NothingNullByDefault
public class WrenchItem extends Item {
    public WrenchItem() {
        super(new Item.Properties());
    }

    private static final double CONNECTION_THRESHOLD = 0.25;
    private static final double CENTER_THRESHOLD_MIN = CONNECTION_THRESHOLD;
    private static final double CENTER_THRESHOLD_MAX = 1.0 - CONNECTION_THRESHOLD;

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction clickedFace = context.getClickedFace();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Vec3 clickLocation = context.getClickLocation();
        Player player = context.getPlayer();
        if ((player != null && !player.isCrouching()) && !level.isClientSide()) return InteractionResult.PASS;

        //        Now we know the player is crouching, we can use the wrench

        double relX = clickLocation.x() - pos.getX();
        double relY = clickLocation.y() - pos.getY();
        double relZ = clickLocation.z() - pos.getZ();
        Direction targetDirection = determineTargetDirection(clickedFace, relX, relY, relZ);

        BlockState state = level.getBlockState(pos);
        if (!(level.getBlockEntity(pos) instanceof BasePipeBlockEntity blockEntity)) return InteractionResult.PASS;
        blockEntity.disconnect(targetDirection);
        level.setBlockAndUpdate(blockEntity.getBlockPos(),
                state.setValue(BasePipeBlock.getPropertyFromDirection(targetDirection), ConnectionType.NONE));
        return InteractionResult.SUCCESS;
    }

    private Direction determineTargetDirection(Direction clickedFace, double relX, double relY, double relZ) {
        return switch (clickedFace) {
            case UP, DOWN -> getDirectionFromXZ(clickedFace, relX, relZ);
            case NORTH, SOUTH -> getDirectionFromXY(clickedFace, relX, relY);
            case WEST, EAST -> getDirectionFromYZ(clickedFace, relZ, relY);
        };
    }

    private Direction getDirectionFromXZ(Direction face, double relX, double relZ) {
        if (relX < CENTER_THRESHOLD_MIN) return Direction.WEST;
        if (relX > CENTER_THRESHOLD_MAX) return Direction.EAST;
        if (relZ < CENTER_THRESHOLD_MIN) return Direction.NORTH;
        if (relZ > CENTER_THRESHOLD_MAX) return Direction.SOUTH;
        return face;
    }

    private Direction getDirectionFromXY(Direction face, double relX, double relY) {
        if (relX < CENTER_THRESHOLD_MIN) return Direction.WEST;
        if (relX > CENTER_THRESHOLD_MAX) return Direction.EAST;
        if (relY < CENTER_THRESHOLD_MIN) return Direction.DOWN;
        if (relY > CENTER_THRESHOLD_MAX) return Direction.UP;
        return face;
    }

    private Direction getDirectionFromYZ(Direction face, double relZ, double relY) {
        if (relZ < CENTER_THRESHOLD_MIN) return Direction.NORTH;
        if (relZ > CENTER_THRESHOLD_MAX) return Direction.SOUTH;
        if (relY < CENTER_THRESHOLD_MIN) return Direction.DOWN;
        if (relY > CENTER_THRESHOLD_MAX) return Direction.UP;
        return face;
    }
}
