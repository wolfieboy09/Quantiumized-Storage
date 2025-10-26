package dev.wolfieboy09.qtech.packets;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.multiblock.MultiblockPattern;
import dev.wolfieboy09.qtech.api.util.ResourceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.Contract;

@NothingNullByDefault
public record ShowMultiblockPattern(MultiblockPattern pattern, BlockPos controllerPos, Direction facing, int duration) implements CustomPacketPayload {
    public static final Type<ShowMultiblockPattern> TYPE = new Type<>(ResourceHelper.asResource("show_multiblock_pattern"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShowMultiblockPattern> STREAM_CODEC = StreamCodec.composite(
            MultiblockPattern.STREAM_CODEC, ShowMultiblockPattern::pattern,
            BlockPos.STREAM_CODEC, ShowMultiblockPattern::controllerPos,
            Direction.STREAM_CODEC, ShowMultiblockPattern::facing,
            ByteBufCodecs.INT, ShowMultiblockPattern::duration,
            ShowMultiblockPattern::new
    );

    @Contract(pure = true)
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
