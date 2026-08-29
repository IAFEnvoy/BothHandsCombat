package com.iafenvoy.bhc.mixin;

import com.iafenvoy.bhc.combat.CombatState;
import com.iafenvoy.bhc.combat.HandCombat;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Records the hand that actually completed a predicted item-use action.
 */
@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    /**
     * Minecraft tries MAIN_HAND first and stops as soon as an item consumes the click.
     * Returning PASS here leaves that loop intact while allowing the other loaded/charging
     * crossbow to receive the next use action.
     */
    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    private void bothHandsCombat$skipLastDualCrossbowHand(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        CombatState.Data data = CombatState.get(player);
        if (HandCombat.isDualCrossbowUser(player) && hand == data.handOfLastActiveStack) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method = "useItem", at = @At("RETURN"))
    private void bothHandsCombat$rememberUsedHand(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue() instanceof InteractionResult.Success) {
            CombatState.Data data = CombatState.get(player);
            data.ticksSinceLastActiveStack = 0;
            data.handOfLastActiveStack = hand;
        }
    }
}
