package com.iafenvoy.bhc.screen;

import com.iafenvoy.bhc.BothHandsCombat;
import com.iafenvoy.bhc.combat.HandCombat;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(Dist.CLIENT)
public final class GuiRenderer {
    private static final Identifier HOTBAR_LAYER = Identifier.fromNamespaceAndPath(BothHandsCombat.MOD_ID, "offhand_hotbar_indicator");
    private static final Identifier CROSSHAIR_LAYER = Identifier.fromNamespaceAndPath(BothHandsCombat.MOD_ID, "offhand_crosshair_indicator");
    private static final Identifier HOTBAR_BACKGROUND = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_background");
    private static final Identifier HOTBAR_PROGRESS = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_progress");
    private static final Identifier CROSSHAIR_BACKGROUND = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_background");
    private static final Identifier CROSSHAIR_PROGRESS = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_progress");

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, HOTBAR_LAYER, GuiRenderer::renderHotbar);
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, CROSSHAIR_LAYER, GuiRenderer::renderCrosshair);
    }

    private static void renderHotbar(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.attackIndicator().get() != AttackIndicatorStatus.HOTBAR
                || !HandCombat.canSwingHand(minecraft.player, InteractionHand.OFF_HAND)) return;
        float scale = HandCombat.getOffhandAttackStrengthScale(minecraft.player);
        if (scale >= 1.0F) return;
        int center = graphics.guiWidth() / 2;
        HumanoidArm offhandArm = minecraft.player.getMainArm().getOpposite();
        int x = offhandArm == HumanoidArm.RIGHT ? center + 97 : center - 113;
        int y = graphics.guiHeight() - 20;
        int progress = (int) (scale * 19.0F);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_BACKGROUND, x, y, 18, 18);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_PROGRESS, 18, 18, 0, 18 - progress,
                x, y + 18 - progress, 18, progress);
    }

    private static void renderCrosshair(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.attackIndicator().get() != AttackIndicatorStatus.CROSSHAIR
                || !HandCombat.canSwingHand(minecraft.player, InteractionHand.OFF_HAND)) return;
        float scale = HandCombat.getOffhandAttackStrengthScale(minecraft.player);
        if (scale >= 1.0F) return;
        int x = graphics.guiWidth() / 2 - 8;
        int y = graphics.guiHeight() / 2 + 25;
        int progress = (int) (scale * 17.0F);
        graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_BACKGROUND, x, y, 16, 4);
        graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_PROGRESS, 16, 4, 0, 0, x, y, progress, 4);
    }
}
