package dev.wolfieboy09.qtech.block.multiblock.centrifuge;

import com.mojang.serialization.MapCodec;
import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.multiblock.blocks.BaseMultiblockController;
import dev.wolfieboy09.qtech.api.multiblock.blocks.BaseMultiblockEntityController;
import dev.wolfieboy09.qtech.registries.QTMultiblockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;

@NothingNullByDefault
public class CentrifugeController extends BaseMultiblockController {
    public static final MapCodec<CentrifugeController> CODEC = simpleCodec(CentrifugeController::new);

    public CentrifugeController(Properties properties) {
        super(properties, QTMultiblockTypes.CENTRIFUGE);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BaseMultiblockEntityController newMultiblockController(BlockPos pos, BlockState state) {
        return new CentrifugeBlockEntityController(pos, state);
    }
}
