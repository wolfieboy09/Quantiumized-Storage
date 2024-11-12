package dev.wolfieboy09.qstorage.block.storage_matrix;

import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qstorage.block.disk_assembler.DiskAssemblerBlock;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StorageMatrixBlockEntity extends AbstractEnergyBlockEntity implements MenuProvider {
    private final Component TITLE = Component.translatable("block.qstorage.storage_matrix");
    private final ItemStackHandler inventory = new ItemStackHandler(8) {
        @Override
        public void onContentsChanged(int slot) {
            setChanged();
        }
    };

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
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        this.energyContainer.set(0, this.energyStorage.getEnergyStored());
        return new StorageMatrixMenu(containerId, this.getBlockPos(), playerInventory, player, this.energyContainer);
    }
    public EnergyStorage getEnergyHandler(@Nullable Direction side) {
        if (side == null) return this.getEnergyStorage();
        return side == this.getBlockState().getValue(StorageMatrixBlock.FACING).getOpposite() ? this.getEnergyStorage() : null;
    }

    @Override
    public void saveExtra(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveExtra(tag, registries);
        tag.put("Inventory", this.inventory.serializeNBT(registries));
    }

    @Override
    protected void loadExtra(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadExtra(tag, registries);
        this.inventory.deserializeNBT(registries, tag);
    }

    @Override
    public CompoundTag updateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        tag.put("Inventory", this.inventory.serializeNBT(lookupProvider));
        return super.updateTag(tag, lookupProvider);
    }

    @Override
    public void handleUpdate(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdate(tag, lookupProvider);
        this.inventory.deserializeNBT(lookupProvider, tag);
    }
}
