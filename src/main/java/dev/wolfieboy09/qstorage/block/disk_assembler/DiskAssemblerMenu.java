package dev.wolfieboy09.qstorage.block.disk_assembler;

import dev.wolfieboy09.qstorage.QuantiumizedStorage;
import dev.wolfieboy09.qstorage.block.AbstractEnergyContainerMenu;
import dev.wolfieboy09.qstorage.block.ItemResultSlot;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import dev.wolfieboy09.qstorage.registries.QSMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerInvWrapper;
import org.jetbrains.annotations.NotNull;

public class DiskAssemblerMenu extends AbstractEnergyContainerMenu {
    private DiskAssemblerBlockEntity blockEntity;
    private Level level;
    private ContainerData data;

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

    public DiskAssemblerMenu(int id, BlockPos pos, Inventory playerInventory, Player player, ContainerData containerData) {
        super(QSMenuTypes.DISK_ASSEMBLER.get(), id);
        addDataSlots(containerData);
        this.level = player.level();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof DiskAssemblerBlockEntity be)) return;
        this.blockEntity = be;
        this.data = containerData;

        PlayerInvWrapper playerInvWrapper = new PlayerInvWrapper(playerInventory);

        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;

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

        ItemStackHandler itemStackHandler = be.getInventory();

        addSlot(new SlotItemHandler(itemStackHandler, 0, 17, 27));
        addSlot(new SlotItemHandler(itemStackHandler, 1, 17, 45));
        addSlot(new SlotItemHandler(itemStackHandler, 2, 35, 36));

        addSlot(new SlotItemHandler(itemStackHandler, 3, 116, 27));
        addSlot(new SlotItemHandler(itemStackHandler, 4, 134, 27));
        addSlot(new SlotItemHandler(itemStackHandler, 5, 116, 45));
        addSlot(new SlotItemHandler(itemStackHandler, 6, 134, 45));

        addSlot(new ItemResultSlot(itemStackHandler, 7, 80, 36));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + 7, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + 7) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            QuantiumizedStorage.LOGGER.warn("Invalid slotIndex: {}", index);
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, QSBlocks.DISK_ASSEMBLER.get());
    }

    @Override
    public int getEnergy() {
        return this.data.get(0);
    }

    @Override
    public int getMaxEnergy() {
        return this.blockEntity.getMaxEnergyStored();
    }
}
