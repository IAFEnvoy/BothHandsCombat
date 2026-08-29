package com.iafenvoy.bhc.combat;

import com.iafenvoy.bhc.config.BHCConfig;
import com.iafenvoy.bhc.registry.tag.BhcTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

public final class HandCombat {
    private HandCombat() {
    }

    public static boolean canSwingHand(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(BhcTags.DISABLED_ITEMS)) return false;
        EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        boolean[] hasAttackDamage = {false};
        stack.forEachModifier(slot, (attribute, ignored) -> hasAttackDamage[0] |= attribute.equals(Attributes.ATTACK_DAMAGE));
        if (!hasAttackDamage[0]) {
            stack.forEachModifier(EquipmentSlot.MAINHAND, (attribute, ignored) -> hasAttackDamage[0] |= attribute.equals(Attributes.ATTACK_DAMAGE));
        }
        return hasAttackDamage[0];
    }

    public static boolean isDisabledEntity(Entity entity) {
        return entity.is(BhcTags.DISABLED_ENTITIES);
    }

    public static boolean isDualCrossbowUser(Player player) {
        return player.getMainHandItem().getItem() instanceof CrossbowItem
                && player.getOffhandItem().getItem() instanceof CrossbowItem
                && !player.getMainHandItem().is(BhcTags.DISABLED_ITEMS)
                && !player.getOffhandItem().is(BhcTags.DISABLED_ITEMS);
    }

    public static void resetAttackStrengthTickerOffHand(Player player) {
        CombatState.Data data = CombatState.get(player);
        data.attackStrengthTicker = 0;
        if (canSwingHand(player, InteractionHand.MAIN_HAND)) {
            int half = (int) (BHCConfig.attackTimeoutAfterSwing() * player.getCurrentItemAttackStrengthDelay());
            if (player.attackStrengthTicker > half) player.attackStrengthTicker = half;
        }
    }

    public static float getOffhandAttackStrengthScale(Player player) {
        ItemStack offhand = player.getOffhandItem();
        ItemStack mainHand = player.getMainHandItem();
        makeActive(player, offhand, mainHand);
        float delay = player.getCurrentItemAttackStrengthDelay();
        makeInactive(player, offhand, mainHand);
        return Mth.clamp(CombatState.get(player).attackStrengthTicker / delay, 0.0F, 1.0F);
    }

    public static void makeActive(Player player, ItemStack offhand, ItemStack mainHand) {
        updateModifiers(player, mainHand, EquipmentSlot.MAINHAND, false);
        updateModifiers(player, offhand, EquipmentSlot.OFFHAND, false);
        updateModifiers(player, offhand, EquipmentSlot.MAINHAND, true);
        updateModifiers(player, mainHand, EquipmentSlot.OFFHAND, true);
    }

    public static void makeInactive(Player player, ItemStack offhand, ItemStack mainHand) {
        updateModifiers(player, mainHand, EquipmentSlot.OFFHAND, false);
        updateModifiers(player, offhand, EquipmentSlot.MAINHAND, false);
        updateModifiers(player, mainHand, EquipmentSlot.MAINHAND, true);
        updateModifiers(player, offhand, EquipmentSlot.OFFHAND, true);
    }

    public static void setItemStackToSlot(Player player, EquipmentSlot slot, ItemStack stack) {
        player.setItemSlot(slot, stack);
    }

    private static void updateModifiers(Player player, ItemStack stack, EquipmentSlot slot, boolean add) {
        stack.forEachModifier(slot, (attribute, modifier) -> {
            AttributeInstance instance = player.getAttributes().getInstance(attribute);
            if (instance == null) return;
            if (add) {
                // Item modifiers retain stable IDs, so GUI extraction may encounter an already-active one.
                instance.removeModifier(modifier.id());
                instance.addTransientModifier(modifier);
            } else {
                instance.removeModifier(modifier);
            }
        });
    }
}
