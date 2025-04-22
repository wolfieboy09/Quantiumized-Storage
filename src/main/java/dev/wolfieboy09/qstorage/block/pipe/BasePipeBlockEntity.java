package dev.wolfieboy09.qstorage.block.pipe;

import com.mojang.serialization.DataResult;
import dev.wolfieboy09.qstorage.block.GlobalBlockEntity;
import dev.wolfieboy09.qstorage.block.pipe.network.PipeNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class BasePipeBlockEntity extends GlobalBlockEntity {
    private List<Direction> disconnectedSides = new ArrayList<>(6);

    public BasePipeBlockEntity(BlockEntityType<?> blockEntityTypeFor, BlockPos pos, BlockState blockState) {
        super(blockEntityTypeFor, pos, blockState);
    }

    public void disconnect(Direction direction) {
        if (this.disconnectedSides.contains(direction) || this.level == null || this.level.isClientSide()) return;
        this.disconnectedSides.add(direction);
        PipeNetworkManager.sideDisconnected(this.level, getBlockPos(), direction);
        setChanged();
    }

    public List<Direction> getDisconnectedSides() {
        return this.disconnectedSides;
    }

    public boolean allowConnectionFromDirection(Direction direction) {
        return !this.disconnectedSides.contains(direction);
    }

    public void reconnect(Direction direction) {
        if (this.level == null || this.level.isClientSide()) return;
        this.disconnectedSides.remove(direction);
        PipeNetworkManager.sideConnected(this.level, getBlockPos(), direction);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        DataResult<Tag> listTag = Direction.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.disconnectedSides);
        tag.put("DisconnectedSides", listTag.getOrThrow());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.disconnectedSides = Direction.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("DisconnectedSides")).getOrThrow();
    }
}