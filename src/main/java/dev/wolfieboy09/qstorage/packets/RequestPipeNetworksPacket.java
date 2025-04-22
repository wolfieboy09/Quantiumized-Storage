package dev.wolfieboy09.qstorage.packets;

import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RequestPipeNetworksPacket() implements CustomPacketPayload {
    public static final Type<SyncPipeNetworksPacket> TYPE = new CustomPacketPayload.Type<>(ResourceHelper.asResource("request_pipe_networks"));

    public static final StreamCodec<ByteBuf, RequestPipeNetworksPacket> STREAM_CODEC = StreamCodec.of((a, b) -> {}, c -> new RequestPipeNetworksPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

