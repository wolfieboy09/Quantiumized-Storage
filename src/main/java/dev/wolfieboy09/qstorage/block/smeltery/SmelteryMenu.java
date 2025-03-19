package dev.wolfieboy09.qstorage.block.smeltery;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.block.ItemResultSlot;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import dev.wolfieboy09.qstorage.registries.QSMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerInvWrapper;
import org.jetbrains.annotations.NotNull;

@NothingNullByDefault
public class SmelteryMenu extends AbstractContainerMenu {
    private SmelteryBlockEntity blockEntity;
    private final Level level;
    private ContainerData data;

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    final int SLOT_X_SPACING = 18;
    final int SLOT_Y_SPACING = 18;

    public SmelteryMenu(int id, BlockPos pos, Inventory playerInventory, @NotNull Player player, ContainerData containerData) {
        super(QSMenuTypes.SMELTERY_MENU.get(), id);
        addDataSlots(containerData);
        this.level = player.level();
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        if (!(blockEntity instanceof SmelteryBlockEntity be)) return;
        this.blockEntity = be;
        this.data = containerData;

        addSlot(new SlotItemHandler(be.getInventory(), 0, 93, 6));
        addSlot(new SlotItemHandler(be.getInventory(), 1, 93, 29));
        addSlot(new SlotItemHandler(be.getInventory(), 2, 93, 52));

        addSlot(new ItemResultSlot(be.getInventory(), 3, 171, 6));
        addSlot(new ItemResultSlot(be.getInventory(), 4, 171, 52));

        createPlayerInventory(playerInventory, 48, 150);
        createPlayerHotbar(playerInventory,48,208);
    }

    private void createPlayerInventory(@NotNull Inventory playerInventory,int PLAYER_INVENTORY_XPOS, int PLAYER_INVENTORY_YPOS) {
        PlayerInvWrapper playerInvWrapper = new PlayerInvWrapper(playerInventory);

        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlot(new SlotItemHandler(playerInvWrapper, slotNumber, xpos, ypos));
            }
        }
    }

    private void createPlayerHotbar(@NotNull Inventory playerInv,int HOTBAR_XPOS,int HOTBAR_YPOS) {
        for (int col = 0; col < HOTBAR_SLOT_COUNT; ++col) {
            this.addSlot(new Slot(playerInv, col, HOTBAR_XPOS + col * SLOT_X_SPACING, HOTBAR_YPOS));
        }
    }

    public SmelteryMenu(int id, BlockPos pos, Inventory playerInventory, Player playerIn) {
        this(id, pos, playerInventory, playerIn, new SimpleContainerData(4));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        //TODO get quick move stack working
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, QSBlocks.SMELTERY.get());
    }
}
