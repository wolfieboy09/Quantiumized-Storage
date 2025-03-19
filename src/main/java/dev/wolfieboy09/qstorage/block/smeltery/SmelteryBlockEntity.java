package dev.wolfieboy09.qstorage.block.smeltery;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.block.GlobalBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.state.BlockState;
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
    private final List<FluidTank> inputTanks = new ArrayList<>();
    private final FluidTank outputFluidTank = new FluidTank(10000);
    private final FluidTank wasteOutputFluidTank = new FluidTank(10000);

    public SmelteryBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.SMELTERY.get(), pos, blockState);
        initInputFluidTanks();
    }
    
    public void initInputFluidTanks() {
        int INPUT_TANKS = 3;
        int INPUT_TANK_CAPACITY = 10000;
        for (int i = 0; i < INPUT_TANKS; i++) {
            this.inputTanks.add(new FluidTank(INPUT_TANK_CAPACITY));
        }
    }

    public List<FluidTank> getInputTanks() {
        return this.inputTanks;
    }


    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public Component getDisplayName() {
        return this.TITLE;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new SmelteryMenu(id, this.getBlockPos(), playerInv, player, this.containerData);
    }

    private CompoundTag saveFluidTank(FluidTank fluidTank,HolderLookup.Provider registries) {
        return fluidTank.writeToNBT(registries, new CompoundTag());
    }

    private void loadFluidTank(FluidTank fluidTank,CompoundTag tag,HolderLookup.Provider registries) {
        if (tag.isEmpty()) return;
        fluidTank.readFromNBT(registries, tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
//        Save here all the stuff
        ListTag listTag = new ListTag();
        listTag.add(saveFluidTank(this.inputTanks.get(0),registries));
        listTag.add(saveFluidTank(this.inputTanks.get(1),registries));
        listTag.add(saveFluidTank(this.inputTanks.get(2),registries));

        tag.put("InputTanks", listTag);
        tag.put("Inventory", this.inventory.serializeNBT(registries));
        tag.put("OutputTank", saveFluidTank(this.outputFluidTank, registries));
        tag.put("WasteTank", saveFluidTank(this.wasteOutputFluidTank, registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        loadFluidTank(this.inputTanks.get(0), tag.getCompound("InputTank1"), registries);
        loadFluidTank(this.inputTanks.get(1), tag.getCompound("InputTank2"), registries);
        loadFluidTank(this.inputTanks.get(2), tag.getCompound("InputTank3"), registries);

        loadFluidTank(this.outputFluidTank, tag.getCompound("OutputTank"), registries);
        loadFluidTank(this.wasteOutputFluidTank, tag.getCompound("WasteTank"), registries);
        this.inventory.deserializeNBT(registries, tag);
    }

    public void tick() {

    }
}
