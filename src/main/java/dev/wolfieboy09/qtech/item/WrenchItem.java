package dev.wolfieboy09.qtech.item;

import dev.wolfieboy09.qtech.QuantiumizedTech;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.pipe.network.ConnectionState;
import dev.wolfieboy09.qtech.api.pipe.network.PipeNetworkManager;
import dev.wolfieboy09.qtech.block.pipe.BasePipeBlock;
import dev.wolfieboy09.qtech.block.pipe.BasePipeBlockEntity;
import dev.wolfieboy09.qtech.packets.PipeFacadeUpdate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

@NothingNullByDefault
public class WrenchItem extends Item {
    public WrenchItem() {
        super(new Item.Properties().stacksTo(1));
    }

    private static final double CONNECTION_THRESHOLD = 0.4;
    private static final double CENTER_THRESHOLD_MIN = CONNECTION_THRESHOLD;
    private static final double CENTER_THRESHOLD_MAX = 1.0 - CONNECTION_THRESHOLD;

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction clickedFace = context.getClickedFace();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Vec3 clickLocation = context.getClickLocation();
        Player player = context.getPlayer();
        if (level.isClientSide() || player == null) return InteractionResult.PASS;

        // Now we know the player is crouching, we can use the wrench
        double relX = clickLocation.x() - pos.getX();
        double relY = clickLocation.y() - pos.getY();
        double relZ = clickLocation.z() - pos.getZ();
        Direction targetDirection = determineTargetDirection(clickedFace, relX, relY, relZ);

        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof BasePipeBlock<?>)) return InteractionResult.PASS;

        // Check if the side is currently connected
        ConnectionState connectionState = PipeNetworkManager.getConnectionState(level, pos, targetDirection);
        boolean isCrouching = player.isCrouching();
        boolean isConnection = connectionState == ConnectionState.CONNECTED_TO_PIPE || connectionState == ConnectionState.CONNECTED_TO_BLOCK;
        boolean isDisabled = connectionState == ConnectionState.MANUALLY_DISABLED;
        boolean isExtraction = connectionState == ConnectionState.CONNECTED_TO_BLOCK_TO_EXTRACT;
        boolean isBlockConnection = connectionState == ConnectionState.CONNECTED_TO_BLOCK;
        if (!isCrouching && state.getValue(BasePipeBlock.FACADING) && level.getBlockEntity(pos) instanceof BasePipeBlockEntity<?> pipe) {
            level.setBlock(pos, state.setValue(BasePipeBlock.FACADING, false), BasePipeBlock.UPDATE_ALL);
            pipe.updateFacadeBlock(BasePipeBlockEntity.NO_FACADE_STATE);
            PacketDistributor.sendToAllPlayers(new PipeFacadeUpdate(pos, state.setValue(BasePipeBlock.FACADING, false)));
            Containers.dropContents(level, pos, new SimpleContainer(FacadeItem.createFacade(pipe.getFacadeState().getBlock())));
        }
        if (isCrouching && isConnection) {
            QuantiumizedTech.LOGGER.debug("WrenchItem: Disconnecting side {} at {}", targetDirection, pos);
            PipeNetworkManager.disableConnection(level, pos, targetDirection);}
        else if (!isExtraction && !isCrouching && isBlockConnection) {
            QuantiumizedTech.LOGGER.debug("WrenchItem: Extraction Enable side {} at {}", targetDirection, pos);
            PipeNetworkManager.enableExtraction(level, pos, targetDirection);
        } else if (isCrouching && isDisabled){
            QuantiumizedTech.LOGGER.debug("WrenchItem: Reconnecting side {} at {}", targetDirection, pos);
            PipeNetworkManager.enableConnection(level, pos, targetDirection);
        } else if (!isCrouching && isExtraction){
            QuantiumizedTech.LOGGER.debug("WrenchItem: Extraction Disable side {} at {}", targetDirection, pos);
            PipeNetworkManager.disableExtraction(level, pos, targetDirection);
        }
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
