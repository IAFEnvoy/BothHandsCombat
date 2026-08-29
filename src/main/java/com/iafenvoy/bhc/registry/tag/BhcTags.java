package com.iafenvoy.bhc.registry.tag;

import com.iafenvoy.bhc.BothHandsCombat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

/**
 * Data-pack extensible compatibility exclusions.
 */
public final class BhcTags {
    public static final TagKey<EntityType<?>> DISABLED_ENTITIES = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(BothHandsCombat.MOD_ID, "disabled_entities"));
    public static final TagKey<Item> DISABLED_ITEMS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(BothHandsCombat.MOD_ID, "disabled_items"));
}
