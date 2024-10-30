package dev.wolfieboy09.qstorage.block.disk_assembler;

import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DiskAssemblerBlockEntity extends AbstractEnergyBlockEntity implements MenuProvider {
    private final Component TITLE = Component.translatable("block.qstorage.disk_assembler");
    private int progress = 0;
    private int crafting_ticks = 0;
    private int energy_required = 0;

    public DiskAssemblerBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.DISK_ASSEMBLER.get(), pos, blockState, getEnergyCapacity(), 1000, 0);
    }
    
    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }
    
    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        this.energyData.set(0, this.energyStorage.getEnergyStored());
        return new DiskAssemblerMenu(id, this.getBlockPos(), playerInv, player, this.energyData);
    }
    
    public static class DiskAssemblerSlot {
        public static final int MAIN_SLOT_1 = 0;
        public static final int MAIN_SLOT_2 = 1;
        public static final int MAIN_SLOT_3 = 2;
        public static final int EXTRA_SLOT_1 = 3;
        public static final int EXTRA_SLOT_2 = 4;
        public static final int EXTRA_SLOT_3 = 5;
        public static final int EXTRA_SLOT_4 = 6;
        public static final int OUTPUT_SLOT = 7;
    }

    private final ItemStackHandler inventory = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            if (slot < 7) {
                resetProgress();
            }
            setChanged();
        }
    };

    protected void resetProgress() {
        if (this.progress != 0) {
            this.progress = 0;
        }
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    public static int getEnergyCapacity() {
        return 20000;
    }

    public EnergyStorage getEnergyHandler(@Nullable Direction side) {
        if (side == null) return this.getEnergyStorage(); // for special cases
        Direction blockFacing = this.getBlockState().getValue(DiskAssemblerBlock.FACING);
        return side == blockFacing.getOpposite() ? this.getEnergyStorage() : null;
    }

    public ItemStackHandler getInventoryHandler() {
        return this.inventory;
    }


    // Crafting land
    public void craftDisk() {
        if (level == null) return;
        ItemStack stack = inventory.getStackInSlot(0);

    }

    private boolean isCrafting() {
        return this.crafting_ticks > 0;
    }

    private void resetCrafting() {
        crafting_ticks = 0;
        energy_required = 0;
    }

    public SimpleContainer getInputContainer() {
        SimpleContainer container = new SimpleContainer(7);
        for (int i = 0; i < 7; i++) {
            container.setItem(i, inventory.getStackInSlot(i));
        }
        return container;
    }

    public SimpleContainer getOutputContainer() {
        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, inventory.getStackInSlot(inventory.getSlots() - 1));
        return container;
    }

    @Override
    public void saveExtra(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveExtra(tag, registries);
        tag.putInt("progress", this.progress);
        tag.putInt("energy_required", this.energy_required);
        tag.putInt("crafting_ticks", this.crafting_ticks);
        tag.put("inventory", this.inventory.serializeNBT(registries));
    }

    @Override
    protected void loadExtra(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadExtra(tag, registries);
        this.progress = tag.getInt("progress");
        this.energy_required = tag.getInt("energy_required");
        this.crafting_ticks = tag.getInt("crafting_ticks");
        this.inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    public CompoundTag updateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        tag.putInt("energy_required", this.energy_required);
        tag.putInt("crafting_ticks", this.crafting_ticks);
        return super.updateTag(tag, lookupProvider);
    }

    @Override
    public void handleUpdate(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdate(tag, lookupProvider);
        this.energy_required = tag.getInt("energy_required");
        this.crafting_ticks = tag.getInt("crafting_ticks");
    }
}
