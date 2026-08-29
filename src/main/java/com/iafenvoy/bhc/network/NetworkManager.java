package com.iafenvoy.bhc.network;

import com.iafenvoy.bhc.network.payload.OffhandAttackC2SPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;

@EventBusSubscriber
public final class NetworkManager {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToServer(OffhandAttackC2SPayload.TYPE, OffhandAttackC2SPayload.STREAM_CODEC, new MainThreadPayloadHandler<>(ServerPayloadHandlers::handle));
    }
}
