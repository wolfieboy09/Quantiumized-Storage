package dev.wolfieboy09.qtech.block.cleanroom.controller;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.cleanroom.CleanroomInstance;
import dev.wolfieboy09.qtech.api.cleanroom.CleanroomTracker;
import dev.wolfieboy09.qtech.api.recipes.CleanRoomCondition;
import dev.wolfieboy09.qtech.block.GlobalBlockEntity;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

@NothingNullByDefault
public class CleanroomControllerBlockEntity extends GlobalBlockEntity {

    private CleanRoomCondition selectedCondition = CleanRoomCondition.NONE;
    @Nullable
    private CleanroomInstance currentInstance;

    private boolean fullyClean = false;

    public CleanroomControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(QTBlockEntities.CLEANROOM_CONTROLLER.get(), pos, blockState);
    }

    public void updateCleanroomCondition(CleanRoomCondition newCondition) {
        if (newCondition != this.selectedCondition) {
            this.selectedCondition = newCondition;
            setChanged();
            syncToClient();
        }
    }

    public CleanRoomCondition getCleanroomCondition() {
        return this.selectedCondition;
    }

    @Nullable
    public CleanroomInstance getCurrentInstance() {
        return currentInstance;
    }

    public boolean isFullyClean() {
        return this.fullyClean;
    }

    public CleanRoomCondition getDetectedCondition() {
        return currentInstance != null ? currentInstance.condition() : CleanRoomCondition.NONE;
    }

    public void tryFormCleanroom() {
        if (level == null || level.isClientSide) return;

        CleanroomTracker.discover(level, worldPosition).ifPresentOrElse(instance -> {
            this.currentInstance = instance;
            setChanged();
            syncToClient();
        }, () -> {
            this.currentInstance = null;
            setChanged();
            syncToClient();
        });
    }

    public void onCleanroomBroken() {
        this.currentInstance = null;
        this.fullyClean = false;
        setChanged();
        syncToClient();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("CleanroomCondition", this.selectedCondition.getSerializedName());
        tag.putBoolean("FullyClean", this.fullyClean);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.selectedCondition = CleanRoomCondition.fromString(tag.getString("CleanroomCondition"));
        this.fullyClean = tag.getBoolean("FullyClean");
    }


    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}