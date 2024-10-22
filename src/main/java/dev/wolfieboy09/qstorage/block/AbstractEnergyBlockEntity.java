package dev.wolfieboy09.qstorage.block;

import dev.wolfieboy09.qstorage.api.energy.ExtendedEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public abstract class AbstractEnergyBlockEntity extends GlobalBlockEntity {
    private final ExtendedEnergyStorage energyStorage;

    public AbstractEnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, int maxTransfer) {
        super(type, pos, blockState);
        this.energyStorage = new ExtendedEnergyStorage(capacity, maxTransfer);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Energy", energyStorage.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(registries, Objects.requireNonNull(tag.get("Energy")));
        }
    }
}