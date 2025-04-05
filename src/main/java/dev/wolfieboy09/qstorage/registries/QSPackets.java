package dev.wolfieboy09.qstorage.registries;

import dev.wolfieboy09.qstorage.block.gas_filler.GasFillerBlockEntity;
import dev.wolfieboy09.qstorage.packets.GasFillerModeData;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class QSPackets {
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                GasFillerModeData.TYPE,
                GasFillerModeData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleData,
                        ServerPayloadHandler::handleData
                )
        );
    }

    public static class ClientPayloadHandler {
        public static void handleData(GasFillerModeData payload, final IPayloadContext context) {
            // NOOP
        }
    }

    public static class ServerPayloadHandler {
        public static void handleData(GasFillerModeData payload, final IPayloadContext context) {
            var be = context.player().level().getBlockEntity(payload.blockPos());
            if (be instanceof GasFillerBlockEntity gasFiller) {
                gasFiller.setState(payload.state());
            }
        }
    }
}
