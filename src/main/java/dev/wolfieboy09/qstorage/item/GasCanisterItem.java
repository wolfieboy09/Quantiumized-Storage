package dev.wolfieboy09.qstorage.item;

import dev.wolfieboy09.qstorage.api.capabilities.gas.GasTank;
import dev.wolfieboy09.qstorage.api.components.GasCanisterComponent;
import dev.wolfieboy09.qstorage.api.gas.SingleGasTankHandler;
import dev.wolfieboy09.qstorage.api.registry.QSRegistries;
import dev.wolfieboy09.qstorage.component.QSDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class GasCanisterItem extends Item {
    public GasCanisterItem(@NotNull Properties properties) {
        super(properties.component(QSDataComponents.GAS_CANISTER_COMPONENT, new GasCanisterComponent(new SingleGasTankHandler(new GasTank(5000)))));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        GasCanisterComponent data = stack.get(QSDataComponents.GAS_CANISTER_COMPONENT.get());
        if (data != null && !data.getGasTank().getGas().isEmpty()) {
            String langKey = data.getGasTank().getGas().getGasHolder().getRegisteredName();
            tooltipComponents.add(Component.translatable("qstorage.gas_canister.contains_gas", Component.literal(langKey)));
            tooltipComponents.add(Component.literal(data.getGasTank().getGasAmount() + " / " + data.getGasTank().getCapacity()));
        }

        // To make jade not show this section on the tooltip
        if (!(FMLEnvironment.dist.isClient() && Minecraft.getInstance().screen == null)) {
            if (tooltipFlag.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("qstorage.gas_canister.shifted_info"));
            } else {
                tooltipComponents.add(Component.translatable("qstorage.tooltip.shift_info"));
            }
        }
    }
}
