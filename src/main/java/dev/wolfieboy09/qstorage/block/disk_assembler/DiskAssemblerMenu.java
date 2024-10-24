package dev.wolfieboy09.qstorage.block.disk_assembler;

import dev.wolfieboy09.qstorage.api.energy.ExtendedEnergyStorage;
import dev.wolfieboy09.qstorage.block.AbstractEnergyContainerMenu;
import dev.wolfieboy09.qstorage.registries.QSMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerInvWrapper;
import org.jetbrains.annotations.NotNull;

public class DiskAssemblerMenu extends AbstractEnergyContainerMenu {
    private DiskAssemblerBlockEntity blockEntity;

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_ROW_COUNT = 2;
    private static final int TE_INVENTORY_COLUMN_COUNT = 3;

    public DiskAssemblerMenu(int id, BlockPos pos, Inventory playerInventory, Player playerIn) {
        this(id, pos, playerInventory, playerIn, new SimpleContainerData(4));
    }

    public DiskAssemblerMenu(int id, BlockPos pos, Inventory playerInventory, Player playerIn, ContainerData containerData) {
        super(QSMenuTypes.DISK_ASSEMBLER.get(), id);
        addDataSlots(containerData);
        DiskAssemblerBlockEntity blockEntity = (DiskAssemblerBlockEntity) playerIn.getCommandSenderWorld().getBlockEntity(pos);
        if (blockEntity == null) return;
        this.blockEntity = blockEntity;

        PlayerInvWrapper playerInvWrapper = new PlayerInvWrapper(playerInventory);

        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int TE_SLOT_Y_SPACING = 48;
        final int TILE_INVENTORY_XPOS = 70;
        final int TILE_INVENTORY_YPOS = 12;

        final int HOTBAR_XPOS = 8;
        final int HOTBAR_YPOS = 142;
        final int PLAYER_INVENTORY_XPOS = 8;
        final int PLAYER_INVENTORY_YPOS = 84;

        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            addSlot(new SlotItemHandler(playerInvWrapper, x, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
        }

        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlot(new SlotItemHandler(playerInvWrapper, slotNumber, xpos, ypos));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public int getEnergy() {
        return this.blockEntity.getEnergyStored();
    }

    @Override
    public int getMaxEnergy() {
        return this.blockEntity.getMaxEnergyStored();
    }
}
