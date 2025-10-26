package dev.wolfieboy09.qtech.registries;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import dev.wolfieboy09.qtech.api.packets.OneWayPacketHandler;
import dev.wolfieboy09.qtech.block.gas_canister.GasCanisterBlockEntity;
import dev.wolfieboy09.qtech.block.pipe.BasePipeBlockEntity;
import dev.wolfieboy09.qtech.client.render.MultiblockGhostRenderer;
import dev.wolfieboy09.qtech.packets.GasCanisterModeData;
import dev.wolfieboy09.qtech.packets.HideMultiblockPattern;
import dev.wolfieboy09.qtech.packets.PipeFacadeUpdate;
import dev.wolfieboy09.qtech.packets.ShowMultiblockPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@NothingNullByDefault
public final class QTPackets {
    public static void register(final RegisterPayloadHandlersEvent event) {
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

        registrar.playToClient(
                ShowMultiblockPattern.TYPE,
                ShowMultiblockPattern.STREAM_CODEC,
                new OneWayPacketHandler<>(ClientPayloadHandler::handleShowMultiblockPattern)
        );
        registrar.playToClient(
                HideMultiblockPattern.TYPE,
                HideMultiblockPattern.STREAM_CODEC,
                new OneWayPacketHandler<>(ClientPayloadHandler::handleHideMultiblockPattern)
        );
    }

    public static class ServerPayloadHandler {
        public static void handleGasCanisterMode(GasCanisterModeData payload, final IPayloadContext context) {
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

        public static void handleShowMultiblockPattern(ShowMultiblockPattern payload, IPayloadContext context) {
            context.enqueueWork(() -> MultiblockGhostRenderer.show(
                    payload.controllerPos(),
                    payload.pattern(),
                    payload.facing(),
                    payload.duration()
            ));
        }

        public static void handleHideMultiblockPattern(HideMultiblockPattern payload, IPayloadContext context) {
            context.enqueueWork(MultiblockGhostRenderer::hide);
        }
    }
}
