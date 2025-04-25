package dev.wolfieboy09.qtech.item;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

@NothingNullByDefault
public class UpgradeItem extends Item {
    private final String langKey;

    public UpgradeItem(Properties properties, String tooltipLangKey) {
        super(properties);
        this.langKey = tooltipLangKey;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable(this.langKey));
    }
}
