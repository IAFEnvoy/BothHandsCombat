package com.iafenvoy.bhc.network;

import com.iafenvoy.bhc.combat.OffhandCombat;
import com.iafenvoy.bhc.network.payload.OffhandAttackC2SPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ServerPayloadHandlers {
    public static void handle(OffhandAttackC2SPayload payload, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) OffhandCombat.attack(player, payload.targetId());
    }
}
