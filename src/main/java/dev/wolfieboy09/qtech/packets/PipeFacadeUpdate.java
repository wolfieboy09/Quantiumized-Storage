package dev.wolfieboy09.qtech.packets;

import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record PipeFacadeUpdate(BlockPos blockPos, BlockState state) implements CustomPacketPayload {
    public static final Type<PipeFacadeUpdate> TYPE = new CustomPacketPayload.Type<>(ResourceHelper.asResource("pipe_facade_upadte"));

    // There's no BlockState.STREAM_CODEC, and you would have to do really strange stuff
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

    public static final StreamCodec<FriendlyByteBuf, PipeFacadeUpdate> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PipeFacadeUpdate::blockPos,
            PipeFacadeUpdate.BLOCK_STATE_STREAM_CODEC, PipeFacadeUpdate::state,
            PipeFacadeUpdate::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
