package dev.wolfieboy09.qtech.integration.jade.multiblocks;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.multiblock.blocks.controller.BaseMultiblockControllerEntity;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@NothingNullByDefault
public enum MultiblockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
        if (accessor.getServerData().contains("Formed")) {
            boolean isFormed = accessor.getServerData().getBoolean("Formed");
            if (isFormed) {
                tooltip.add(Component.translatable("multiblock.qtech.formed").withStyle(ChatFormatting.GREEN));
            } else {
                tooltip.add(Component.translatable("multiblock.qtech.unformed").withStyle(ChatFormatting.RED));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        BaseMultiblockControllerEntity controller = (BaseMultiblockControllerEntity) blockAccessor.getBlockEntity();
        compoundTag.putBoolean("Formed", controller.isFormed());
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceHelper.asResource("multiblock_controller");
    }
}
