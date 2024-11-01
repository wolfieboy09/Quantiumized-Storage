package dev.wolfieboy09.qstorage.block.storage_matrix;

import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StorageMatrixBlockEntity extends AbstractEnergyBlockEntity implements MenuProvider {
    private final Component TITLE = Component.translatable("block.qstorage.storage_matrix");
    public StorageMatrixBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.STORAGE_MATRIX.get(), pos, blockState, 20000, 1000, 0);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return null;
    }
}
