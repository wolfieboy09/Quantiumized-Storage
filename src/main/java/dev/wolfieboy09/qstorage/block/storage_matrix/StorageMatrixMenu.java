package dev.wolfieboy09.qstorage.block.storage_matrix;

import dev.wolfieboy09.qstorage.block.AbstractEnergyContainerMenu;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import dev.wolfieboy09.qstorage.registries.QSMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class StorageMatrixMenu extends AbstractEnergyContainerMenu {
    private final Level level;
    private StorageMatrixBlockEntity blockEntity;
    private ContainerData data;

    public StorageMatrixMenu(int id, BlockPos pos, Inventory playerInventory, Player playerIn) {
        this(id, pos, playerInventory, playerIn, new SimpleContainerData(4));
    }

    public StorageMatrixMenu(int id, BlockPos pos, Inventory playerInventory, Player player, ContainerData containerData) {
        super(QSMenuTypes.STORAGE_MATRIX.get(), id);
        this.level = player.level();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof StorageMatrixBlockEntity be)) return;
        this.blockEntity = be;
        this.data = containerData;
    }

    @Override
    public int getEnergy() {
        return this.data.get(0);
    }

    @Override
    public int getMaxEnergy() {
        return this.blockEntity.getMaxEnergyStored();
    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, QSBlocks.STORAGE_MATRIX.get());
    }
}
