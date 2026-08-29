package com.iafenvoy.bhc.combat;

import com.iafenvoy.bhc.config.CombatConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

/**
 * Server-side counterpart to the original packet/player mixins.
 */
public final class OffhandCombat {
    private OffhandCombat() {
    }

    public static void attack(ServerPlayer player, int targetId) {
        if (!CombatConfig.enabled() || player.isSpectator() || player.isUsingItem()
                || !HandCombat.canSwingHand(player, InteractionHand.OFF_HAND)) return;

        Entity target = player.level().getEntity(targetId);
        if (target == null || target == player || player.distanceToSqr(target) > 36.0D
                || HandCombat.isDisabledEntity(target)
                || CombatConfig.requireLineOfSight() && !player.hasLineOfSight(target)) return;

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offhand = player.getOffhandItem();
        CombatState.Data data = CombatState.get(player);
        int mainTicker = player.attackStrengthTicker;

        // Player.attack() now reads LivingEntity equipment, so switch the actual slots for this call.
        HandCombat.setItemStackToSlot(player, EquipmentSlot.MAINHAND, offhand);
        HandCombat.setItemStackToSlot(player, EquipmentSlot.OFFHAND, mainHand);
        HandCombat.makeActive(player, offhand, mainHand);
        player.attackStrengthTicker = data.attackStrengthTicker;
        try {
            player.attack(target);
            player.swing(InteractionHand.OFF_HAND, true);
        } finally {
            data.attackStrengthTicker = 0;
            player.attackStrengthTicker = Math.min(mainTicker,
                    (int) (player.getCurrentItemAttackStrengthDelay() * CombatConfig.attackTimeoutAfterSwing()));
            HandCombat.setItemStackToSlot(player, EquipmentSlot.OFFHAND, offhand);
            HandCombat.setItemStackToSlot(player, EquipmentSlot.MAINHAND, mainHand);
            HandCombat.makeInactive(player, offhand, mainHand);
        }
    }
}
