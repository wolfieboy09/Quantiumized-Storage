package dev.wolfieboy09.qtech.block.pipe.pipes.fluid;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.pipe.network.NetworkType;
import dev.wolfieboy09.qtech.block.pipe.BasePipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class FluidPipe extends BasePipeBlock<IFluidHandler> {
    public FluidPipe() {
        super(Capabilities.FluidHandler.BLOCK);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidPipeBlockEntity(blockPos, blockState);
    }

    @Override
    public NetworkType getNetworkType() {
        return NetworkType.FLUID;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : (lvl, pos, blockState, blockEntity) -> ((FluidPipeBlockEntity) blockEntity).tick();
    }
}
