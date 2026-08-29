package com.iafenvoy.bhc.mixin;

import com.iafenvoy.bhc.combat.CombatState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    private LivingEntity bothHandsCombat$self() {
        return (LivingEntity) (Object) this;
    }

    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("HEAD"))
    private void bothHandsCombat$resetOffhandCooldown(InteractionHand hand, boolean sendToSwingingEntity, CallbackInfo ci) {
        if (hand == InteractionHand.OFF_HAND && this.bothHandsCombat$self() instanceof Player player) {
            CombatState.get(player).attackStrengthTicker = 0;
        }
    }

    @Inject(method = "stopUsingItem", at = @At("HEAD"))
    private void bothHandsCombat$rememberActiveHand(CallbackInfo ci) {
        if (this.bothHandsCombat$self() instanceof Player player && player.isUsingItem()) {
            CombatState.Data data = CombatState.get(player);
            data.ticksSinceLastActiveStack = 0;
            data.handOfLastActiveStack = player.getUsedItemHand();
        }
    }
}
