package dev.wolfieboy09.qtech.packets;

import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record PipeFacadeUpdate(BlockPos blockPos, BlockState state) implements CustomPacketPayload {
    public static final Type<PipeFacadeUpdate> TYPE = new CustomPacketPayload.Type<>(ResourceHelper.asResource("pipe_facade_upadte"));

//    public static final StreamCodec<FriendlyByteBuf, PipeFacadeUpdate> STREAM_CODEC = StreamCodec.composite(
//            BlockPos.STREAM_CODEC, PipeFacadeUpdate::blockPos,
//            StreamCodec.of(BlockState::), PipeFacadeUpdate::state,
//            PipeFacadeUpdate::new
//    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
