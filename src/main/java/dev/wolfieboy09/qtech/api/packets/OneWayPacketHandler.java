package dev.wolfieboy09.qtech.api.packets;

import dev.wolfieboy09.qtech.api.annotation.NothingNullByDefault;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

@NothingNullByDefault
public record OneWayPacketHandler<T extends CustomPacketPayload>(IPayloadHandler<T> packet) implements IPayloadHandler<T> {
    @Override
    public void handle(T payload, IPayloadContext iPayloadContext) {
        this.packet.handle(payload, iPayloadContext);
    }
}
