package dev.wolfieboy09.qtech.api.codecs;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Extra codecs for block states
 */
@NothingNullByDefault
public final class BlockStateExtras {

    // There's no BlockState$STREAM_CODEC so we make our own
    public static final StreamCodec<FriendlyByteBuf, BlockState> BLOCK_STATE_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public BlockState decode(FriendlyByteBuf buf) {
            return Block.stateById(buf.readInt());
        }

        @Override
        public void encode(FriendlyByteBuf buf, BlockState state) {
            buf.writeInt(Block.getId(state));
        }
    };
}
