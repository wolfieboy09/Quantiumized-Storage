package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.api.packets.OneWayPacketHandler;
import dev.wolfieboy09.qtech.block.gas_canister.GasCanisterBlockEntity;
import dev.wolfieboy09.qtech.block.pipe.BasePipeBlockEntity;
import dev.wolfieboy09.qtech.packets.GasCanisterModeData;
import dev.wolfieboy09.qtech.packets.PipeFacadeUpdate;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

public final class QTPackets {
    public static void register(final @NotNull RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                GasCanisterModeData.TYPE,
                GasCanisterModeData.STREAM_CODEC,
                new OneWayPacketHandler<>(ServerPayloadHandler::handleGasCanisterMode)
        );

        registrar.playToClient(
                PipeFacadeUpdate.TYPE,
                PipeFacadeUpdate.STREAM_CODEC,
                new OneWayPacketHandler<>(ClientPayloadHandler::handleFacadeUpdate)
        );
    }

    public static class ServerPayloadHandler {
        public static void handleGasCanisterMode(@NotNull GasCanisterModeData payload, final @NotNull IPayloadContext context) {
            BlockEntity blockEntity = context.player().level().getBlockEntity(payload.blockPos());
            if (blockEntity instanceof GasCanisterBlockEntity gasCanister) {
                gasCanister.setState(payload.state());
            }
        }
    }

    public static class ClientPayloadHandler {
        public static void handleFacadeUpdate(PipeFacadeUpdate payload, IPayloadContext context) {
            BlockEntity blockEntity = context.player().level().getBlockEntity(payload.blockPos());
            if (blockEntity instanceof BasePipeBlockEntity<?> pipeBlock) {
                pipeBlock.updateFacadeBlock(payload.state());
            }
        }
    }
}
