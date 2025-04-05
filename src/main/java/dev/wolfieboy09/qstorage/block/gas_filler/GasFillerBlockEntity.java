package dev.wolfieboy09.qstorage.block.gas_filler;

import dev.wolfieboy09.qstorage.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qstorage.api.capabilities.gas.GasTank;
import dev.wolfieboy09.qstorage.api.components.GasCanisterComponent;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import dev.wolfieboy09.qstorage.api.registry.gas.GasStack;
import dev.wolfieboy09.qstorage.block.GlobalBlockEntity;
import dev.wolfieboy09.qstorage.component.QSDataComponents;
import dev.wolfieboy09.qstorage.registries.QSBlockEntities;
import dev.wolfieboy09.qstorage.registries.QSGasses;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import static dev.wolfieboy09.qstorage.block.gas_filler.GasFillerBlock.MODE;

@NothingNullByDefault
public class GasFillerBlockEntity extends GlobalBlockEntity implements MenuProvider {
    private final Component TITLE = Component.translatable("block.qstorage.gas_filler");
    private final GasTank gasTank = new GasTank(5000, this::setChanged);

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final ContainerData containerData = new SimpleContainerData(3);

    private GasFillerState gasFillerState = GasFillerState.FILL;


    @Override
    public void setChanged() {
        super.setChanged();
        // Gas ID
        this.containerData.set(0, QSRegistries.GAS.getId(this.gasTank.getGas().getGasHolder().value()));
        // Gas Amount
        this.containerData.set(1, this.gasTank.getGasAmount());
    }

    public GasFillerBlockEntity(BlockPos pos, BlockState blockState) {
        super(QSBlockEntities.GAS_FILLER.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return this.TITLE;
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        setChanged();
        return new GasFillerMenu(i, this.getBlockPos(), inventory, player, this.containerData);
    }

    public void tick() {
        GasCanisterComponent data = this.inventory.getStackInSlot(0).get(QSDataComponents.GAS_CANISTER_COMPONENT);
        if (!this.inventory.getStackInSlot(0).isEmpty() && data != null) {
            GasTank tank = new GasTank(data.getTankCapacity());
            int amount = data.getGasTank().getGasAmount();
            if (amount < data.getTankCapacity()) {
                amount++;
            }
            tank.setGasInSlot(0, new GasStack(QSGasses.HYDROGEN.get(), amount));

            this.inventory.getStackInSlot(0).set(QSDataComponents.GAS_CANISTER_COMPONENT, new GasCanisterComponent(tank));
        }
    }

    public void setState(GasFillerState state) {
        this.gasFillerState = state;
        this.setChanged();
        this.level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(MODE, state));
    }
}
