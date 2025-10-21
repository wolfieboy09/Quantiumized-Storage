package dev.wolfieboy09.qtech.api.codecs;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Extra codecs for block states
 */
@NothingNullByDefault
public final class BlockStateExtras {

    // There's no BlockState$STREAM_CODEC so we make our own
    public static final StreamCodec<ByteBuf, BlockState> BLOCK_STATE_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public BlockState decode(ByteBuf buf) {
            return Block.stateById(buf.readInt());
        }

        @Override
        public void encode(ByteBuf buf, BlockState state) {
            buf.writeInt(Block.getId(state));
        }
    };
}
