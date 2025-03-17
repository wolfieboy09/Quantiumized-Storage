package dev.wolfieboy09.qstorage.api.upgrades;

import net.minecraft.world.level.block.entity.BlockEntity;

public class UpgradeManager {
    private final BlockEntity blockEntity;

    public int ADDON_SLOT_COUNT = 4;

    public UpgradeManager(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }
}
