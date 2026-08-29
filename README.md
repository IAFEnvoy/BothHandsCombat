# Both Hands Combat

NeoForge 26.1.2 port of [OffHandCombat](https://github.com/BunnyCinnamon/OffHandCombat).

This mod allows players to use an attackable weapon in the offhand and perform independent off-hand attacks, including
dual crossbows.

## Features

- Use an attackable weapon in the offhand and right-click an entity to perform an independent off-hand attack.
- Keep separate main-hand and off-hand attack cooldowns, including a dedicated off-hand attack indicator.
- Render the off-hand cooldown indicator above the hotbar or crosshair, following Minecraft's attack-indicator setting.
- Support dual crossbows: load the main-hand and off-hand crossbows in sequence, then fire them in sequence.
- Validate off-hand attacks on the server, including target range, line of sight, held item, and cooldown state.
- Preserve vanilla behavior for entities and items excluded through the compatibility tags.

## Configuration

Configure the mod through the in-game configuration screen supplied by Jupiter:

- `enabled`: enables off-hand combat.
- `requireLineOfSight`: blocks attacks through walls.
- `attackTimeoutAfterSwing`: main-hand cooldown fraction retained after an off-hand hit.

Compatibility exclusions are controlled with data-pack tags:

- `both_hands_combat:disabled_entities` (`data/<namespace>/tags/entity_type/disabled_entities.json`) disables
  off-hand attacks against the listed entity types and keeps vanilla interaction behavior.
- `both_hands_combat:disabled_items` (`data/<namespace>/tags/item/disabled_items.json`) disables off-hand combat
  for the listed items and keeps their vanilla use behavior.

For example, a data pack can disable off-hand combat for a specific entity type:

```json
{
  "replace": false,
  "values": [
    "minecraft:armor_stand"
  ]
}
```
