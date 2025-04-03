package dev.wolfieboy09.qstorage.registries;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class QSPackets {
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);
//        registrar.playToServer(
//                GasFillerModeData.TYPE,
//                GasFillerModeData.STREAM_CODEC,
//               TODO create C2S packet thing here
//        );
    }
}
