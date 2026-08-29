package com.iafenvoy.bhc;

import com.iafenvoy.bhc.config.BHCConfig;
import com.iafenvoy.jupiter.render.screen.ConfigSelectScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(Dist.CLIENT)
public final class BothHandsCombatClient {
    @SubscribeEvent
    public static void onInit(FMLClientSetupEvent event) {
        event.getContainer().registerExtensionPoint(IConfigScreenFactory.class, (_, parent) -> ConfigSelectScreen.builder("config.both_hands_combat.title", parent).common(BHCConfig.INSTANCE).build());
    }
}
