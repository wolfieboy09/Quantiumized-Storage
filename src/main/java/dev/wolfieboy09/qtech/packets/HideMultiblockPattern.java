package dev.wolfieboy09.qtech.packets;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.Contract;
@NothingNullByDefault
public record HideMultiblockPattern(BlockPos controllerPos) implements CustomPacketPayload {

    public static final Type<HideMultiblockPattern> TYPE = new Type<>(ResourceHelper.asResource("hide_multiblock_pattern"));

    public static final StreamCodec<RegistryFriendlyByteBuf, HideMultiblockPattern> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, HideMultiblockPattern::controllerPos,
            HideMultiblockPattern::new
    );

    @Contract(pure = true)
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}