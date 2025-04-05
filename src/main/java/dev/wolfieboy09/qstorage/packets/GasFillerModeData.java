package dev.wolfieboy09.qstorage.packets;

import dev.wolfieboy09.qstorage.api.util.ResourceHelper;
import dev.wolfieboy09.qstorage.block.gas_filler.GasFillerState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

public record GasFillerModeData(BlockPos blockPos,GasFillerState state) implements CustomPacketPayload {
    public static final Type<GasFillerModeData> TYPE = new CustomPacketPayload.Type<>(ResourceHelper.asResource("gas_filler_state"));

    public static final StreamCodec<FriendlyByteBuf, GasFillerModeData> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            GasFillerModeData::blockPos,
            NeoForgeStreamCodecs.enumCodec(GasFillerState.class), GasFillerModeData::state,
            GasFillerModeData::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
