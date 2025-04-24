package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.api.packets.OneWayPacketHandler;
import dev.wolfieboy09.qstorage.block.gas_filler.GasFillerBlockEntity;
import dev.wolfieboy09.qstorage.packets.GasFillerModeData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

public final class QSPackets {
    public static void register(final @NotNull RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                GasFillerModeData.TYPE,
                GasFillerModeData.STREAM_CODEC,
                new OneWayPacketHandler<>(ServerPayloadHandler::handleGasFillerMode)
        );
    }

    public static class ServerPayloadHandler {
        public static void handleGasFillerMode(@NotNull GasFillerModeData payload, final @NotNull IPayloadContext context) {
            BlockEntity blockEntity = context.player().level().getBlockEntity(payload.blockPos());
            if (blockEntity instanceof GasFillerBlockEntity gasFiller) {
                gasFiller.setState(payload.state());
            }
        }
    }
}
