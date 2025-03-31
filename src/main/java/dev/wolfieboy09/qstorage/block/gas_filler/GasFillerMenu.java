package dev.wolfieboy09.qstorage.block.gas_filler;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.slots.GasSlot;
import dev.wolfieboy09.qstorage.registries.QSBlocks;
import dev.wolfieboy09.qstorage.registries.QSMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

@NothingNullByDefault
public class GasFillerMenu extends AbstractContainerMenu {
    private GasFillerBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public GasFillerMenu(int id, BlockPos pos, Inventory playerInventory, Player player) {
        this(id, pos, playerInventory, player, new SimpleContainerData(3));
    }

    public GasFillerMenu(int id, BlockPos pos, Inventory playerInventory, @NotNull Player player, ContainerData containerData) {
        super(QSMenuTypes.GAS_FILLER_MENU.get(), id);
        addDataSlots(containerData);
        this.level = player.level();
        this.data = containerData;
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        if (!(blockEntity instanceof GasFillerBlockEntity be)) return;
        this.blockEntity = be;
        addSlot(new GasSlot(be.getInventory(), 0, 0, 0));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, this.blockEntity.getBlockPos()), player, QSBlocks.GAS_FILLER.get());
    }
}
