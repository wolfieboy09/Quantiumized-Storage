package dev.wolfieboy09.qtech.block.cleanroom.controller;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.block.AbstractBaseEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
}
