package com.iafenvoy.bhc;

import com.iafenvoy.bhc.config.BHCConfig;
import com.iafenvoy.jupiter.ConfigManager;
import net.neoforged.fml.common.Mod;

@Mod(BothHandsCombat.MOD_ID)
public final class BothHandsCombat {
    public static final String MOD_ID = "both_hands_combat";

    public BothHandsCombat() {
        ConfigManager.getInstance().registerConfigHandler(BHCConfig.INSTANCE);
    }
}

