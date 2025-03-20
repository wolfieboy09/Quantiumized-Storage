package dev.wolfieboy09.qstorage.block.smeltery;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.fluids.ExtendedFluidTank;
import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.block.GlobalBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@NothingNullByDefault
public class SmelteryBlockEntity extends GlobalBlockEntity implements MenuProvider {
    public static final int TANK_CAPACITY = 10000;
    public static final int INPUT_TANKS_COUNT = 3;

    private final Component TITLE = Component.translatable("block.qstorage.smeltery");
    // Expand container data to include both fluid IDs and amounts
    // For each tank: [fluidId, amount]
    // So for 3 tanks we need 6 integers
    private final ContainerData containerData = new SimpleContainerData(INPUT_TANKS_COUNT * 2);
    private final ItemStackHandler inventory = new ItemStackHandler(5);
    private final List<ExtendedFluidTank> inputTanks = new ArrayList<>(INPUT_TANKS_COUNT);
    private final ExtendedFluidTank outputFluidTank = new ExtendedFluidTank(TANK_CAPACITY, this::setChanged);
    private final ExtendedFluidTank wasteOutputFluidTank = new ExtendedFluidTank(TANK_CAPACITY, this::setChanged);
    private final IFluidHandler fluidHandler = new IFluidHandler() {

        @Override
        public int getTanks() {
            return 5;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return switch (tank) {
                case 0 -> inputTanks.getFirst().getFluid();
                case 1 -> inputTanks.get(1).getFluid();
                case 2 -> inputTanks.get(2).getFluid();
                case 3 -> outputFluidTank.getFluid();
                case 4 -> wasteOutputFluidTank.getFluid();
                default -> FluidStack.EMPTY;
            };
        }

        @Override
        public int getTankCapacity(int tank) {
            return switch (tank) {
                case 0 -> inputTanks.getFirst().getCapacity();
                case 1 -> inputTanks.get(1).getCapacity();
                case 2 -> inputTanks.get(2).getCapacity();
                case 3 -> outputFluidTank.getCapacity();
                case 4 -> wasteOutputFluidTank.getCapacity();
                default -> 0;
            };
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return switch (tank) {
                case 0 -> inputTanks.getFirst().isFluidValid(stack);
                case 1 -> inputTanks.get(1).isFluidValid(stack);
                case 2 -> inputTanks.get(2).isFluidValid(stack);
                case 3 -> outputFluidTank.isFluidValid(stack);
                case 4 -> wasteOutputFluidTank.isFluidValid(stack);
                default -> false;
            };
        }

        @Override
        public int fill(FluidStack fluidStack, FluidAction fluidAction) {
            for (ExtendedFluidTank inputTank : inputTanks) {
                if (inputTank.getFluid().getFluid() == fluidStack.getFluid()) {
                    return inputTank.fill(fluidStack, fluidAction);
                } else if (inputTank.getFluid().isEmpty()) {
                    return inputTank.fill(fluidStack, fluidAction);
                }
            }
        //    Cant fill into the output tanks
            return 0;
        }

        @Override
        public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
            if (outputFluidTank.getFluid().getFluid() == fluidStack.getFluid()) {
                return outputFluidTank.drain(fluidStack, fluidAction);
            }
            if (wasteOutputFluidTank.getFluid().getFluid() == fluidStack.getFluid()) {
                return wasteOutputFluidTank.drain(fluidStack, fluidAction);
            }
        //    Cant extract out from the input tanks
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction fluidAction) {
            if (outputFluidTank.getFluidAmount() >= maxDrain) {
                return outputFluidTank.drain(maxDrain, fluidAction);
            }
            if (wasteOutputFluidTank.getFluidAmount() >= maxDrain) {
                return wasteOutputFluidTank.drain(maxDrain, fluidAction);
            }
        //    Cant extract out from the input tanks
            return FluidStack.EMPTY;
        }
    };

    public SmelteryBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.SMELTERY.get(), pos, blockState);
        initInputFluidTanks();
    }

    public void onContentsChanged() {
        setChanged();
        updateContainerData();
    }

    private void updateContainerData() {
        // Update container data with fluid IDs and amounts
        for (int i = 0; i < INPUT_TANKS_COUNT; i++) {
            FluidStack fluid = this.inputTanks.get(i).getFluid();
            // Store fluid registry ID as an integer
            int fluidId = BuiltInRegistries.FLUID.getId(fluid.getFluid());
            // Store amount
            int amount = fluid.getAmount();
            
            // Update container data (2 slots per tank)
            this.containerData.set(i * 2, fluidId);
            this.containerData.set(i * 2 + 1, amount);
        }
    }
    
    public void initInputFluidTanks() {
        for (int i = 0; i < INPUT_TANKS_COUNT; i++) {
            this.inputTanks.add(new ExtendedFluidTank(TANK_CAPACITY, this::onContentsChanged));
        }
    }

    public @Nullable IFluidHandler getFluidHandler() {
        return this.fluidHandler;
    }

    public List<ExtendedFluidTank> getInputTanks() {
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
        updateContainerData();
        return new SmelteryMenu(id, this.getBlockPos(), playerInv, player, this.containerData);
    }

    private CompoundTag saveFluidTank(FluidTank fluidTank, HolderLookup.Provider registries) {
        return fluidTank.writeToNBT(registries, new CompoundTag());
    }

    private void loadFluidTank(FluidTank fluidTank, CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.isEmpty()) return;
        fluidTank.readFromNBT(registries, tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag listTag = new ListTag();
        for (ExtendedFluidTank inputTank : this.inputTanks) {
            listTag.add(saveFluidTank(inputTank, registries));
        }

        tag.put("InputTanks", listTag);
        tag.put("Inventory", this.inventory.serializeNBT(registries));
        tag.put("OutputTank", saveFluidTank(this.outputFluidTank, registries));
        tag.put("WasteTank", saveFluidTank(this.wasteOutputFluidTank, registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ListTag listTag = tag.getList("InputTanks", Tag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            loadFluidTank(this.inputTanks.get(i), listTag.getCompound(i), registries);
        }

        loadFluidTank(this.outputFluidTank, tag.getCompound("OutputTank"), registries);
        loadFluidTank(this.wasteOutputFluidTank, tag.getCompound("WasteTank"), registries);
        this.inventory.deserializeNBT(registries, tag);
    }

    public void tick() {
//        Testing if the tanks function correctly
//        this.inputTanks.getFirst().fill(new FluidStack(Fluids.WATER, 10), IFluidHandler.FluidAction.EXECUTE);
//        this.inputTanks.get(1).fill(new FluidStack(Fluids.LAVA,10), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level == null || this.level.isClientSide) return;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }
}
