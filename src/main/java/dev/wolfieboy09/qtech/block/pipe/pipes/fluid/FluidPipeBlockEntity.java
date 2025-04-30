package dev.wolfieboy09.qtech.block.pipe.pipes.fluid;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.block.pipe.BasePipeBlockEntity;
import dev.wolfieboy09.qtech.registries.QTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

@NothingNullByDefault
public class FluidPipeBlockEntity extends BasePipeBlockEntity<IFluidHandler> {
    public FluidPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(QTBlockEntities.FLUID_PIPE.get(), Capabilities.FluidHandler.BLOCK, pos, blockState);
    }

    @Override
    protected boolean canExtract(IFluidHandler source) {
        for (int i = 0; i < source.getTanks(); i++) {
            if (!source.drain(1, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean tryTransfer(IFluidHandler source, IFluidHandler target) {
        for (int i = 0; i < source.getTanks(); i++) {
            FluidStack simulatedDrain = source.drain(Math.min(source.getFluidInTank(i).getAmount(), 100), IFluidHandler.FluidAction.SIMULATE);
            if (simulatedDrain.isEmpty()) continue;

            int accepted = target.fill(simulatedDrain, IFluidHandler.FluidAction.SIMULATE);
            if (accepted > 0) {
                FluidStack actualDrain = source.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
                if (!actualDrain.isEmpty()) {
                    target.fill(actualDrain, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            }
        }
        return false;
    }
}
