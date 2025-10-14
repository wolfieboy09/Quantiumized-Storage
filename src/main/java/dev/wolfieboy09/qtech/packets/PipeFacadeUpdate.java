package dev.wolfieboy09.qtech.packets;

import dev.wolfieboy09.qtech.api.codecs.BlockStateExtras;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public record PipeFacadeUpdate(BlockPos blockPos, BlockState state) implements CustomPacketPayload {
    public static final Type<PipeFacadeUpdate> TYPE = new Type<>(ResourceHelper.asResource("pipe_facade_update"));


    public static final StreamCodec<FriendlyByteBuf, PipeFacadeUpdate> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PipeFacadeUpdate::blockPos,
            BlockStateExtras.BLOCK_STATE_STREAM_CODEC, PipeFacadeUpdate::state,
            PipeFacadeUpdate::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
