package dev.wolfieboy09.qstorage.block.disk_assembler;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.AbstractEnergyBlockEntity;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import dev.wolfieboy09.qstorage.registries.QSRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DiskAssemblerBlockEntity extends AbstractEnergyBlockEntity implements MenuProvider {
    private final Component TITLE = Component.translatable("block.qstorage.disk_assembler");
    private int progress = 0;
    private int crafting_ticks = 0;
    private int energy_required = 0;
    private DiskAssemblerRecipe recipe = null;
    private boolean isValidRecipe = false;
    
    public DiskAssemblerBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.DISK_ASSEMBLER.get(), pos, blockState, 20000, 1000, 0);
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
    
    public void tick() {
        if (this.level == null || getInputContainer().isEmpty()) return;
        
        // Validate the recipe
        if (!this.isValidRecipe) return;
        
        // Check if output has space before crafting
        ItemStack outputSlotStack = this.inventory.getStackInSlot(DiskAssemblerSlot.OUTPUT_SLOT);
        ItemStack resultItem = this.recipe.getResultItem(this.level.registryAccess());
        
        boolean outputHasSpace = outputSlotStack.isEmpty() ||
            (outputSlotStack.getItem() == resultItem.getItem() &&
                outputSlotStack.getCount() + resultItem.getCount() <= outputSlotStack.getMaxStackSize());
        
        if (!outputHasSpace) return; // Exit if output slot lacks space
        
        // Check energy and progress crafting if energy is sufficient
        boolean energySufficient = this.energyStorage.getEnergyStored() >= this.recipe.energyCost();
        int timeRequired = this.recipe.timeInTicks();
        
        if (energySufficient) {
            if (this.crafting_ticks < timeRequired) {
                this.crafting_ticks++;
                this.progress = getProgress();
                this.energyStorage.removeEnergy(this.recipe.energyCost() / 100);
            }
            
            // If progress reaches 100%, complete the crafting
            if (this.progress >= 100) {
                // Consume energy and input items
                // this.energyStorage.removeEnergy(this.recipe.energyCost()); // Extract energy here upon successful crafting
                consumeInputItems();
                
                // Place result in output slot
                if (outputSlotStack.isEmpty()) {
                    this.inventory.setStackInSlot(DiskAssemblerSlot.OUTPUT_SLOT, resultItem.copy());
                } else {
                    outputSlotStack.grow(resultItem.getCount());
                }
                
                // Reset crafting progress
                resetProgress();
            }
        }
    }
    
    private int getProgress() {
        return (int) (this.crafting_ticks / (float) this.recipe.timeInTicks() * 100);
    }
    
    private void consumeInputItems() {
        this.inventory.getStackInSlot(DiskAssemblerSlot.MAIN_SLOT_1).shrink(1);
        this.inventory.getStackInSlot(DiskAssemblerSlot.MAIN_SLOT_2).shrink(1);
        this.inventory.getStackInSlot(DiskAssemblerSlot.MAIN_SLOT_3).shrink(1);
        this.inventory.getStackInSlot(DiskAssemblerSlot.EXTRA_SLOT_1).shrink(1);
        this.inventory.getStackInSlot(DiskAssemblerSlot.EXTRA_SLOT_2).shrink(1);
        this.inventory.getStackInSlot(DiskAssemblerSlot.EXTRA_SLOT_3).shrink(1);
        this.inventory.getStackInSlot(DiskAssemblerSlot.EXTRA_SLOT_4).shrink(1);
    }
    
    protected void resetProgress() {
        this.crafting_ticks = 0;
        this.progress = 0;
    }
    
    private boolean checkRecipe(){
    //    Get the input items
        if (this.level == null) return false;
        var inputHandler = new ItemStackHandler(7);
        for (int i = 0; i < 7; i++) {
            inputHandler.setStackInSlot(i, this.inventory.getStackInSlot(i));
        }
        RecipeWrapper input = new RecipeWrapper(inputHandler);
        RecipeManager recipes = this.level.getRecipeManager();
        RecipeHolder<DiskAssemblerRecipe> recipeFound = recipes.getRecipeFor(
            QSRecipes.DISK_ASSEMBLER_TYPE.get(),
                        input,
                        this.level
            ).orElse(null);
        if (recipeFound == null) return false;
        DiskAssemblerRecipe recipe = recipeFound.value();
        boolean matches = recipe.matches(input, this.level);
        if (!matches) return false;
        ItemStack result = recipe.assemble(input, this.level.registryAccess());
        if (result.isEmpty()) return false;
        this.recipe = recipe;
        return true;
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
                boolean isValidRecipe = checkRecipe();
                if (!isValidRecipe) resetProgress();
                setIsValidRecipe(isValidRecipe);
            }
            setChanged();
        }
    };
    
    private void setIsValidRecipe(boolean value){
        this.isValidRecipe = value;
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
        return this.energyStorage.canReceive();
    }

    public EnergyStorage getEnergyHandler(@Nullable Direction side) {
        if (side == null) return this.getEnergyStorage(); // for special cases
        Direction blockFacing = this.getBlockState().getValue(DiskAssemblerBlock.FACING);
        return side == blockFacing.getOpposite() ? this.getEnergyStorage() : null;
    }

    public ItemStackHandler getInventoryHandler() {
        return this.inventory;
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
    
    private void saveRecipeToNBT(CompoundTag modData, HolderLookup.Provider registries) {
        try {
            if (this.recipe instanceof DiskAssemblerRecipe t) {
                modData.put("recipe", DiskAssemblerRecipe.CODEC.encodeStart(NbtOps.INSTANCE, t).getOrThrow());
            }
        } catch (Exception e) {
            QuantiumizedStorage.LOGGER.error("Error saving recipe to NBT: {}", e.getMessage());
        }
    }
    
    private void loadRecipeFromNBT(CompoundTag recipeTag) {
        var recipe = Recipe.CODEC.parse(NbtOps.INSTANCE, recipeTag).getOrThrow();
        if (recipe instanceof DiskAssemblerRecipe diskAssemblerRecipe) {
            this.recipe = diskAssemblerRecipe;
        }
    }
    

    @Override
    public void saveExtra(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveExtra(tag, registries);
        tag.putInt("progress", this.progress);
        tag.putInt("energy_required", this.energy_required);
        tag.putInt("crafting_ticks", this.crafting_ticks);
        tag.put("inventory", this.inventory.serializeNBT(registries));
        tag.putBoolean("isValidRecipe", this.isValidRecipe);
        saveRecipeToNBT(tag, registries);
    }

    @Override
    protected void loadExtra(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadExtra(tag, registries);
        this.progress = tag.getInt("progress");
        this.energy_required = tag.getInt("energy_required");
        this.crafting_ticks = tag.getInt("crafting_ticks");
        this.inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        this.isValidRecipe = tag.getBoolean("isValidRecipe");
        // Load the recipe if it exists
        if (tag.contains("recipe")) {
            loadRecipeFromNBT(tag.getCompound("recipe"));
        }
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
