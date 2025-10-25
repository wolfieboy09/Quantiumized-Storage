package dev.wolfieboy09.qtech.api.multiblock.blocks;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.multiblock.MultiblockBuilder;
import dev.wolfieboy09.qtech.api.registry.multiblock_type.MultiblockType;
import dev.wolfieboy09.qtech.block.AbstractBaseEntityBlock;
import dev.wolfieboy09.qtech.packets.ShowMultiblockPattern;
import dev.wolfieboy09.qtech.registries.QTBlocks;
import dev.wolfieboy09.qtech.registries.QTMultiblockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Supplier;

@NothingNullByDefault
public abstract class BaseMultiblockController extends AbstractBaseEntityBlock {
    private final Supplier<MultiblockType> type;

    public BaseMultiblockController(Properties properties, Supplier<MultiblockType> type) {
        super(properties);
        this.type = type;
    }


    public MultiblockType getType() {
        return this.type.get();
    }

    // THIS IS TEMPORARY. WILL BE MODIFIED TO BE BETTER
//    @Override
//    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
//        if (level.isClientSide()) return InteractionResult.PASS;
//
//        PacketDistributor.sendToPlayer((ServerPlayer) player, new ShowMultiblockPattern(
//                MultiblockBuilder.create("centrifuge")
//                        .type(QTMultiblockTypes.CENTRIFUGE)
//                        .controller(QTBlocks.CENTRIFUGE_CONTROLLER)
//                        .key('B', Blocks.BRICKS)
//                        .layer("BBBBB", "BBBBB", "BBBBB")
//                        .layer("BB+BB", "BBBBB", "BBBBB")
//                        .layer("BBBBB", "BBBBB", "BBBBB")
//                        .build(),
//                pos,
//                600
//        ));
//        return super.useWithoutItem(state, level, pos, player, hitResult);
//    }
}
