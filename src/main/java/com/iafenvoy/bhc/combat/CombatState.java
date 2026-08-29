package com.iafenvoy.bhc.combat;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/**
 * Runtime state used by the independent hand cooldown and swing animations.
 */
public final class CombatState {
    private static final Map<UUID, Data> SWING = new ConcurrentHashMap<>();
    private static final Map<UUID, Data> SWING_LOCAL = new ConcurrentHashMap<>();

    private CombatState() {
    }

    public static Data get(Player player) {
        Map<UUID, Data> states = player.isLocalPlayer() ? SWING_LOCAL : SWING;
        return states.computeIfAbsent(player.getUUID(), ignored -> new Data());
    }

    public static final class Data {
        public int attackStrengthTicker;
        public int ticksSinceLastActiveStack;
        public InteractionHand handOfLastActiveStack;
    }
}
