package dev.wolfieboy09.qstorage.packets;

import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @param levelKey The resource key level for the client to know when to render
 * @param posList All block posses of any network
 */
public record SyncPipeNetworksPacket(ResourceKey<Level> levelKey, List<BlockPos> posList) implements CustomPacketPayload {
    public static final Type<SyncPipeNetworksPacket> TYPE = new CustomPacketPayload.Type<>(ResourceHelper.asResource("sync_pipe_networks"));

    public static final StreamCodec<FriendlyByteBuf, SyncPipeNetworksPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), SyncPipeNetworksPacket::levelKey,
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncPipeNetworksPacket::posList,
            SyncPipeNetworksPacket::new
    );

    @Override
    public Type<SyncPipeNetworksPacket> type() {
        return TYPE;
    }
}
