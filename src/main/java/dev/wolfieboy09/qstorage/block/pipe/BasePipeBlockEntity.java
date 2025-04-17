package dev.wolfieboy09.qstorage.block.pipe;

import dev.wolfieboy09.qstorage.block.GlobalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class BasePipeBlockEntity extends GlobalBlockEntity {
    List<Direction> disconnectedSides = new ArrayList<>(6);

    public BasePipeBlockEntity(BlockEntityType<?> blockEntityTypeFor, BlockPos pos, BlockState blockState) {
        super(blockEntityTypeFor, pos, blockState);
    }

    public void disconnect(Direction direction) {
        if (this.disconnectedSides.contains(direction) || this.level == null || this.level.isClientSide()) return;
        BlockState newState = getBlockState().setValue(BasePipeBlock.getPropertyFromDirection(direction), ConnectionType.NONE);
        this.level.setBlockAndUpdate(getBlockPos(), newState);
        setChanged();
    }

    public void reconnect(Direction direction) {
        this.disconnectedSides.remove(direction);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        var listTag = Direction.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.disconnectedSides);
        tag.put("DisconnectedSides", listTag.getOrThrow());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.disconnectedSides = Direction.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("DisconnectedSides")).getOrThrow();
    }
}