package com.iafenvoy.bhc.config;

import com.iafenvoy.bhc.BothHandsCombat;
import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
import com.iafenvoy.jupiter.config.entry.DoubleEntry;
import net.minecraft.resources.Identifier;

public final class CombatConfig extends AutoInitConfigContainer {
    public static final CombatConfig INSTANCE = new CombatConfig();
    public final Settings settings = new Settings();

    private CombatConfig() {
        super(Identifier.fromNamespaceAndPath(BothHandsCombat.MOD_ID, "common"),
                "config.both_hands_combat.title", "./config/both_hands_combat.json");
    }

    public static boolean enabled() {
        return INSTANCE.settings.enabled.getValue();
    }

    public static boolean requireLineOfSight() {
        return INSTANCE.settings.requireLineOfSight.getValue();
    }

    public static double attackTimeoutAfterSwing() {
        return INSTANCE.settings.attackTimeoutAfterSwing.getValue();
    }

    public static final class Settings extends AutoInitConfigCategoryBase {
        public final BooleanEntry enabled = BooleanEntry.builder("config.both_hands_combat.enabled", true).key("enabled").build();
        public final BooleanEntry requireLineOfSight = BooleanEntry.builder("config.both_hands_combat.require_line_of_sight", true).key("requireLineOfSight").build();
        public final DoubleEntry attackTimeoutAfterSwing = DoubleEntry.builder("config.both_hands_combat.attack_timeout_after_swing", 0.5D).key("attackTimeoutAfterSwing").build();

        private Settings() {
            super("settings", "config.both_hands_combat.settings");
        }
    }
}

