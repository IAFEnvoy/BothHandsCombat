package com.iafenvoy.bhc.mixin;

import com.iafenvoy.bhc.combat.CombatState;
import com.iafenvoy.bhc.combat.HandCombat;
import com.iafenvoy.bhc.config.CombatConfig;
import com.iafenvoy.bhc.network.payload.OffhandAttackC2SPayload;
import com.iafenvoy.bhc.registry.tag.BhcTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Alternates the hand selected by vanilla item-use for rapid repeated right clicks (e.g. dual crossbows).
 */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    public LocalPlayer player;
    @Shadow
    public HitResult hitResult;

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    private void bothHandsCombat$startUseItem(CallbackInfo ci) {
        if (this.player == null || this.player.isHandsBusy() || this.player.isCrouching() || !CombatConfig.enabled()
                || !HandCombat.canSwingHand(this.player, InteractionHand.OFF_HAND) || this.hitResult == null) {
            return;
        }
        if (this.hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) this.hitResult;
            if (HandCombat.isDisabledEntity(entityHit.getEntity())) return;
            ClientPacketDistributor.sendToServer(new OffhandAttackC2SPayload(entityHit.getEntity().getId()));
            this.player.swing(InteractionHand.OFF_HAND);
            ci.cancel();
        }
    }

    @ModifyVariable(
            method = "startUseItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1),
            name = "heldItem")
    private ItemStack bothHandsCombat$alternateUseHand(ItemStack heldItem) {
        if (this.player == null) return heldItem;
        // A tagged stack keeps vanilla's normal hand selection/use behavior.
        if (heldItem.is(BhcTags.DISABLED_ITEMS)
                || this.player.getMainHandItem().is(BhcTags.DISABLED_ITEMS)
                || this.player.getOffhandItem().is(BhcTags.DISABLED_ITEMS)) return heldItem;
        CombatState.Data data = CombatState.get(this.player);
        InteractionHand hand = this.player.getItemInHand(InteractionHand.MAIN_HAND) == heldItem
                ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        boolean shouldAlternate = HandCombat.isDualCrossbowUser(this.player)
                || data.ticksSinceLastActiveStack < 3;
        return shouldAlternate && data.handOfLastActiveStack == hand
                ? ItemStack.EMPTY : heldItem;
    }
}
