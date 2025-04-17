package dev.wolfieboy09.qstorage.item;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlock;
import dev.wolfieboy09.qstorage.block.pipe.BasePipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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
//        if (level.isClientSide()) return super.useOn(context);
        if (player != null && !(player.isCrouching())) return super.useOn(context);

//        Now we know the player is crouching, we can use the wrench

        double relX = clickLocation.x() - pos.getX();
        double relY = clickLocation.y() - pos.getY();
        double relZ = clickLocation.z() - pos.getZ();
        Direction targetDirection = determineTargetDirection(clickedFace, relX, relY, relZ);
        if (targetDirection == null) return super.useOn(context);

        QuantiumizedStorage.LOGGER.debug("Wrench used on {}", targetDirection);
        var state = level.getBlockState(pos);
        if (!(level.getBlockEntity(pos) instanceof BasePipeBlockEntity blockEntity)) return super.useOn(context);
        blockEntity.disconnect(targetDirection);
        level.setBlockAndUpdate(pos, state);
        return super.useOn(context);
    }

    @Nullable
    private Direction determineTargetDirection(Direction clickedFace, double relX, double relY, double relZ) {
        switch (clickedFace) {
            case DOWN: // Clicked bottom face (Y=0)
                if (relX < CENTER_THRESHOLD_MIN) return Direction.WEST;
                if (relX > CENTER_THRESHOLD_MAX) return Direction.EAST;
                if (relZ < CENTER_THRESHOLD_MIN) return Direction.NORTH;
                if (relZ > CENTER_THRESHOLD_MAX) return Direction.SOUTH;
                return Direction.DOWN; // Clicked center of bottom face

            case UP: // Clicked top face (Y=1)
                if (relX < CENTER_THRESHOLD_MIN) return Direction.WEST;
                if (relX > CENTER_THRESHOLD_MAX) return Direction.EAST;
                if (relZ < CENTER_THRESHOLD_MIN) return Direction.NORTH;
                if (relZ > CENTER_THRESHOLD_MAX) return Direction.SOUTH;
                return Direction.UP; // Clicked center of top face

            case NORTH: // Clicked north face (Z=0)
                if (relX < CENTER_THRESHOLD_MIN) return Direction.WEST;
                if (relX > CENTER_THRESHOLD_MAX) return Direction.EAST;
                if (relY < CENTER_THRESHOLD_MIN) return Direction.DOWN;
                if (relY > CENTER_THRESHOLD_MAX) return Direction.UP;
                return Direction.NORTH; // Clicked center of north face

            case SOUTH: // Clicked south face (Z=1)
                if (relX < CENTER_THRESHOLD_MIN) return Direction.WEST;
                if (relX > CENTER_THRESHOLD_MAX) return Direction.EAST;
                if (relY < CENTER_THRESHOLD_MIN) return Direction.DOWN;
                if (relY > CENTER_THRESHOLD_MAX) return Direction.UP;
                return Direction.SOUTH; // Clicked center of south face

            case WEST: // Clicked west face (X=0)
                if (relZ < CENTER_THRESHOLD_MIN) return Direction.NORTH;
                if (relZ > CENTER_THRESHOLD_MAX) return Direction.SOUTH;
                if (relY < CENTER_THRESHOLD_MIN) return Direction.DOWN;
                if (relY > CENTER_THRESHOLD_MAX) return Direction.UP;
                return Direction.WEST; // Clicked center of west face

            case EAST: // Clicked east face (X=1)
                if (relZ < CENTER_THRESHOLD_MIN) return Direction.NORTH;
                if (relZ > CENTER_THRESHOLD_MAX) return Direction.SOUTH;
                if (relY < CENTER_THRESHOLD_MIN) return Direction.DOWN;
                if (relY > CENTER_THRESHOLD_MAX) return Direction.UP;
                return Direction.EAST; // Clicked center of east face

            default:
                // Should be impossible
                return null;
        }
    }
}
