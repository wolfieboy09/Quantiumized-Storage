package dev.wolfieboy09.qstorage.block.pipe;

import dev.wolfieboy09.qstorage.block.GlobalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class BasePipeBlockEntity extends GlobalBlockEntity {
    List<Direction> disconnectedSides = new ArrayList<>(6) {
    };

    public BasePipeBlockEntity(BlockEntityType<?> blockEntityTypeFor, BlockPos pos, BlockState blockState) {
        super(blockEntityTypeFor, pos, blockState);
    }

    public void disconnect(Direction direction) {
        if (disconnectedSides.contains(direction)) return;
        var newState = getBlockState().setValue(BasePipeBlock.getPropertyFromDirection(direction), BasePipeBlock.ConnectionType.NONE);
        level.setBlock(getBlockPos(),newState,UPDATE_ALL);
        setChanged();
    }

    public void reconnect(Direction direction) {
        disconnectedSides.remove(direction);
        setChanged();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        var listTag = Direction.CODEC.listOf().encodeStart(NbtOps.INSTANCE,disconnectedSides);
        tag.put("DisconnectedSides", listTag.getOrThrow());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        disconnectedSides = Direction.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("DisconnectedSides")).getOrThrow();
    }
}