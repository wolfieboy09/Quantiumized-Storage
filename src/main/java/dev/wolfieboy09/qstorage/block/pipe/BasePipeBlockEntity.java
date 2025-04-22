package dev.wolfieboy09.qstorage.block.pipe;

import dev.wolfieboy09.qstorage.api.nbt.MapBoolNBT;
import dev.wolfieboy09.qstorage.block.GlobalBlockEntity;
import dev.wolfieboy09.qstorage.block.pipe.network.PipeNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public class BasePipeBlockEntity extends GlobalBlockEntity {
    private final MapBoolNBT<Direction> sideState = new MapBoolNBT<>(Direction.class);

    public BasePipeBlockEntity(BlockEntityType<?> blockEntityTypeFor, BlockPos pos, BlockState blockState) {
        super(blockEntityTypeFor, pos, blockState);
    }

    public void disconnect(Direction direction) {
        if (this.sideState.containsKey(direction) || this.level == null || this.level.isClientSide()) return;
        this.sideState.setValue(direction, true);
        PipeNetworkManager.sideDisconnected(this.level, getBlockPos(), direction);
        setChanged();
    }

    public Map<Direction, Boolean> getSides() {
        return this.sideState.getMap();
    }

    public boolean allowConnectionFromDirection(Direction direction) {
        return !this.sideState.containsKey(direction);
    }

    public void reconnect(Direction direction) {
        if (this.level == null || this.level.isClientSide()) return;
        this.sideState.setValue(direction, false);
        PipeNetworkManager.sideConnected(this.level, getBlockPos(), direction);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("DisconnectedSides", this.sideState.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.sideState.deserializeNBT(registries, tag.getCompound("DisconnectedSides"));
    }
}