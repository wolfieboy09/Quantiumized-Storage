package dev.wolfieboy09.qtech.block.gas_filler;

import dev.wolfieboy09.qtech.api.QTConstants;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.slots.GasSlot;
import dev.wolfieboy09.qtech.registries.QTBlocks;
import dev.wolfieboy09.qtech.registries.QTMenuTypes;
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
import org.jetbrains.annotations.UnknownNullability;

import static dev.wolfieboy09.qtech.block.gas_filler.GasFillerBlock.MODE;

@NothingNullByDefault
@SuppressWarnings("SameParameterValue")
public class GasFillerMenu extends AbstractContainerMenu {
    @UnknownNullability
    private GasFillerBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public GasFillerMenu(int id, BlockPos pos, Inventory playerInventory, Player player) {
        this(id, pos, playerInventory, player, new SimpleContainerData(2));
    }

    public GasFillerMenu(int id, BlockPos pos, Inventory playerInventory, @NotNull Player player, ContainerData containerData) {
        super(QTMenuTypes.GAS_FILLER_MENU.get(), id);
        addDataSlots(containerData);
        this.level = player.level();
        this.data = containerData;
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        if (!(blockEntity instanceof GasFillerBlockEntity be)) return;
        this.blockEntity = be;
        addSlot(new GasSlot(be.getInventory(), 0, 129, 36));

        createPlayerInventory(playerInventory, 8, 87);
        createPlayerHotbar(playerInventory, 8, 145);
    }

    private void createPlayerInventory(@NotNull Inventory playerInventory, int inventoryXPos, int inventoryYPos) {
        PlayerInvWrapper playerInvWrapper = new PlayerInvWrapper(playerInventory);

        for (int y = 0; y < QTConstants.PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < QTConstants.PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = QTConstants.HOTBAR_SLOT_COUNT + y * QTConstants.PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = inventoryXPos + x * QTConstants.SLOT_X_SPACING;
                int ypos = inventoryYPos + y * QTConstants.SLOT_Y_SPACING;
                addSlot(new SlotItemHandler(playerInvWrapper, slotNumber, xpos, ypos));
            }
        }
    }

    private void createPlayerHotbar(@NotNull Inventory playerInv, int hotbarXPos, int hotbarYPos) {
        for (int col = 0; col < QTConstants.HOTBAR_SLOT_COUNT; ++col) {
            this.addSlot(new Slot(playerInv, col, hotbarXPos + col * QTConstants.SLOT_X_SPACING, hotbarYPos));
        }
    }

    public GasFillerState getFillState() {
        return this.blockEntity.getBlockState().getValue(MODE);
    }

    public void updateMode(GasFillerState newState) {
        this.blockEntity.setState(newState);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, this.blockEntity.getBlockPos()), player, QTBlocks.GAS_FILLER.get());
    }

    public GasFillerBlockEntity getBE() {
        return this.blockEntity;
    }
}
