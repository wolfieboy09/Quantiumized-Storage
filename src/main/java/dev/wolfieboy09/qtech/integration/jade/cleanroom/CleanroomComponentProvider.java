package dev.wolfieboy09.qtech.integration.jade.cleanroom;

import dev.wolfieboy09.qtech.api.recipes.CleanroomCondition;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.block.cleanroom.controller.CleanroomControllerBlockEntity;
import net.minecraft.ChatFormatting;
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
            CleanroomCondition condition = CleanroomCondition.fromString(blockAccessor.getServerData().getString("CleanroomCondition"));
            boolean isSuperDuperClean = blockAccessor.getServerData().getBoolean("IsSuperDuperClean");
            tooltip.add(Component.translatable("cleanroom.qtech.condition", Component.translatable(condition.getTranslationKey()).withColor(condition.getColor())));
            tooltip.add(Component.translatable("cleanroom.qtech.clean_status", isSuperDuperClean ? Component.translatable("cleanroom.qtech.clean").withStyle(ChatFormatting.GREEN) : Component.translatable("cleanroom.qtech.contaminated").withStyle(ChatFormatting.RED)));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        CleanroomControllerBlockEntity controller = (CleanroomControllerBlockEntity) blockAccessor.getBlockEntity();
        compoundTag.putString("CleanroomCondition", controller.getCleanroomCondition().getSerializedName());
        compoundTag.putBoolean("IsSuperDuperClean", controller.isFullyClean());
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceHelper.asResource("cleanroom_controller");
    }
}
