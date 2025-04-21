package dev.wolfieboy09.qstorage.packets;

import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record HandlePipePos(BlockPos pos, boolean remove) implements CustomPacketPayload {
    public static final Type<HandlePipePos> TYPE = new CustomPacketPayload.Type<>(ResourceHelper.asResource("handle_pipe_pos"));

    public static final StreamCodec<FriendlyByteBuf, HandlePipePos> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, HandlePipePos::pos,
            ByteBufCodecs.BOOL, HandlePipePos::remove,
            HandlePipePos::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
