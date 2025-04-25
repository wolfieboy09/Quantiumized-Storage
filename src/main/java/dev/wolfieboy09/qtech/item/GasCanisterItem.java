package dev.wolfieboy09.qtech.item;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.capabilities.gas.GasTank;
import dev.wolfieboy09.qtech.api.components.GasCanisterComponent;
import dev.wolfieboy09.qtech.api.util.ColorUtil;
import dev.wolfieboy09.qtech.component.QTDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@NothingNullByDefault
public class GasCanisterItem extends Item {
    public GasCanisterItem(@NotNull Properties properties) {
        super(properties.component(QTDataComponents.GAS_CANISTER_COMPONENT, new GasCanisterComponent((new GasTank(5000)))));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        GasCanisterComponent data = stack.get(QTDataComponents.GAS_CANISTER_COMPONENT);
        if (data != null && !data.getGasTank().getGas().isEmpty()) {
            tooltipComponents.add(Component.translatable("qtech.gas_canister.contains_gas", data.getGasTank().getGas().getGas().getName()));
            tooltipComponents.add(Component.literal(data.getGasTank().getGasAmount() + " / " + data.getGasTank().getCapacity() + " gU"));
        }

        // To make jade not show this section on the tooltip
        if (!(FMLEnvironment.dist.isClient() && Minecraft.getInstance().screen == null)) {
            if (tooltipFlag.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("qtech.gas_canister.shifted_info"));
            } else {
                tooltipComponents.add(Component.translatable("qtech.tooltip.shift_info"));
            }
        }
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        GasCanisterComponent data = stack.get(QTDataComponents.GAS_CANISTER_COMPONENT);
        if (data != null) {
            float fillRatio = (float) data.getGasTank().getGasAmount() / data.getTankCapacity();
            return Math.round(fillRatio * 13.0F);
        } else {
            return 0;
        }
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return ColorUtil.fromArgb(1, 86, 3, 252);
    }
}
