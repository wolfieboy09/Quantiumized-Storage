package dev.wolfieboy09.qtech.packets;

import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import dev.wolfieboy09.qtech.block.gas_canister.GasCanisterState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record GasCanisterModeData(BlockPos blockPos, GasCanisterState state) implements CustomPacketPayload {
    public static final Type<GasCanisterModeData> TYPE = new CustomPacketPayload.Type<>(ResourceHelper.asResource("gas_filler_state"));

    public static final StreamCodec<FriendlyByteBuf, GasCanisterModeData> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            GasCanisterModeData::blockPos,
            NeoForgeStreamCodecs.enumCodec(GasCanisterState.class), GasCanisterModeData::state,
            GasCanisterModeData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
