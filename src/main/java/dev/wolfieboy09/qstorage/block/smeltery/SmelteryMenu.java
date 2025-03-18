package dev.wolfieboy09.qstorage.block.smeltery;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.slots.GuiFluidSlot;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import dev.wolfieboy09.qstorage.registries.QSMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
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

    public SmelteryMenu(int id, BlockPos pos, Inventory playerInventory, @NotNull Player player, ContainerData containerData) {
        super(QSMenuTypes.SMELTERY_MENU.get(), id);
        addDataSlots(containerData);
        this.level = player.level();
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        if (!(blockEntity instanceof SmelteryBlockEntity be)) return;
        this.blockEntity = be;
        this.data = containerData;
        PlayerInvWrapper playerInvWrapper = new PlayerInvWrapper(playerInventory);
        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;

        final int HOTBAR_XPOS = 8;
        final int HOTBAR_YPOS = 142;
        final int PLAYER_INVENTORY_XPOS = 8;
        final int PLAYER_INVENTORY_YPOS = 84;

        ItemStackHandler itemStackHandler = be.getInventory();

        addSlot(new GuiFluidSlot(this.blockEntity.getInputFluid(0), 1, 1, 1));

        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlot(new SlotItemHandler(playerInvWrapper, slotNumber, xpos, ypos));
            }
        }
    }

    public SmelteryMenu(int id, BlockPos pos, Inventory playerInventory, Player playerIn) {
        this(id, pos, playerInventory, playerIn, new SimpleContainerData(4));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, QSBlocks.SMELTERY.get());
    }
}
