package dev.wolfieboy09.qtech.block.cleanroom.controller;

import dev.wolfieboy09.qtech.block.GlobalBlockEntity;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CleanroomControllerBlockEntity extends GlobalBlockEntity {
    public CleanroomControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(QTBlockEntities.CLEANROOM_CONTROLLER.get(), pos, blockState);
    }
}
