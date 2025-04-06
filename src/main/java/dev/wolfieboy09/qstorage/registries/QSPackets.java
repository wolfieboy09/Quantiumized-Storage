package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.api.packets.OneWayPacketHandler;
import dev.wolfieboy09.qstorage.block.gas_filler.GasFillerBlockEntity;
import dev.wolfieboy09.qstorage.packets.GasFillerModeData;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class QSPackets {
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                GasFillerModeData.TYPE,
                GasFillerModeData.STREAM_CODEC,
                new OneWayPacketHandler<>(ServerPayloadHandler::handleGasFillerMode)
        );
    }

    public static class ServerPayloadHandler {
        public static void handleGasFillerMode(GasFillerModeData payload, final IPayloadContext context) {
            if (context.player().level().getBlockEntity(payload.blockPos()) instanceof GasFillerBlockEntity gasFiller) {
                gasFiller.setState(payload.state());
            }
        }
    }
}
