package dev.wolfieboy09.qtech.integration.jade.cleanroom;

import dev.wolfieboy09.qtech.api.recipes.CleanRoomCondition;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.block.cleanroom.controller.CleanroomControllerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum CleanroomComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("CleanroomCondition")) {
            CleanRoomCondition condition = CleanRoomCondition.fromString(blockAccessor.getServerData().getString("CleanroomCondition"));
            tooltip.add(Component.translatable(condition.getTranslationKey()));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        CleanroomControllerBlockEntity controller = (CleanroomControllerBlockEntity) blockAccessor.getBlockEntity();
        compoundTag.putString("CleanroomCondition", controller.getCleanroomCondition().getSerializedName());
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceHelper.asResource("cleanroom_controller");
    }
}
