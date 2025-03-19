package dev.wolfieboy09.qstorage.block.smeltery;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.block.GlobalBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@NothingNullByDefault
public class SmelteryBlockEntity extends GlobalBlockEntity implements MenuProvider {
    private final Component TITLE = Component.translatable("block.qstorage.smeltery");
    private final ContainerData containerData = new SimpleContainerData(3);
    private final ItemStackHandler inventory = new ItemStackHandler(5);
    private final List<FluidTank> inputTanks = new ArrayList<>(3);
    private final FluidTank outputFluidTank = new FluidTank(10000);
    private final FluidTank wasteOutputFluidTank = new FluidTank(10000);

    public SmelteryBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.SMELTERY.get(), pos, blockState);
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public IFluidHandler getInputFluid(int index) {
        return this.inputTanks.get(index);
    }

    private void updateContainer() {

    }

    @Override
    public Component getDisplayName() {
        return this.TITLE;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new SmelteryMenu(id, this.getBlockPos(), playerInv, player, this.containerData);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag listTag = new ListTag();
        listTag.add(this.inputTanks.getFirst().writeToNBT(registries, tag));
        listTag.add(this.inputTanks.get(1).writeToNBT(registries, tag));
        listTag.add(this.inputTanks.get(2).writeToNBT(registries, tag));

        tag.put("InputTanks", listTag);
        tag.put("Inventory", this.inventory.serializeNBT(registries));
        tag.put("OutputTank", this.outputFluidTank.writeToNBT(registries, tag));
        tag.put("WasteTank", this.wasteOutputFluidTank.writeToNBT(registries, tag));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag from = tag.getList("InputTanks", Tag.TAG_COMPOUND);
        this.inputTanks.getFirst().readFromNBT(registries, from.getCompound(0));
        this.inputTanks.get(1).readFromNBT(registries, from.getCompound(1));
        this.inputTanks.get(2).readFromNBT(registries, from.getCompound(2));

        this.outputFluidTank.readFromNBT(registries, tag);
        this.wasteOutputFluidTank.readFromNBT(registries, tag);
        this.inventory.deserializeNBT(registries, tag);
    }
}
