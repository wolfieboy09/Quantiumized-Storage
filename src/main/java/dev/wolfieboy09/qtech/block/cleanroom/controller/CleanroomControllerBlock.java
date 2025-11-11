package dev.wolfieboy09.qtech.block.cleanroom.controller;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.cleanroom.CleanroomTracker;
import dev.wolfieboy09.qtech.block.AbstractBaseEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class CleanroomControllerBlock extends AbstractBaseEntityBlock {
    public static final MapCodec<CleanroomControllerBlock> CODEC = simpleCodec(CleanroomControllerBlock::new);

    public CleanroomControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CleanroomControllerBlockEntity(pos, state);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof CleanroomControllerBlockEntity controller) {
            CleanroomTracker.unregister(level, controller.getCurrentInstance());
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.PASS;
        if (level.getBlockEntity(pos) instanceof CleanroomControllerBlockEntity controller && !controller.isFullyClean()) {
            controller.tryFormCleanroom();
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
