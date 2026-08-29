package com.iafenvoy.bhc.mixin;

import com.iafenvoy.bhc.combat.CombatState;
import com.iafenvoy.bhc.combat.HandCombat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Unique
    private ItemStack bothHandsCombat$lastItemInOffHand = ItemStack.EMPTY;

    @Inject(method = "tick", at = @At("TAIL"))
    private void bothHandsCombat$tickOffhandCooldown(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        CombatState.Data data = CombatState.get(player);
        data.attackStrengthTicker++;
        data.ticksSinceLastActiveStack++;
        ItemStack offhand = player.getOffhandItem();
        if (!ItemStack.matches(this.bothHandsCombat$lastItemInOffHand, offhand)) {
            if (!ItemStack.isSameItem(this.bothHandsCombat$lastItemInOffHand, offhand)
                    && HandCombat.canSwingHand(player, InteractionHand.OFF_HAND))
                HandCombat.resetAttackStrengthTickerOffHand(player);
            this.bothHandsCombat$lastItemInOffHand = offhand.copy();
        }
    }
}
