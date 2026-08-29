Both Hands Combat
=================

NeoForge 26.1.2 port of [OffHandCombat](https://github.com/BunnyCinnamon/OffHandCombat).

Hold a weapon with an attack-damage modifier in the off hand and right-click an entity to attack with it. The server validates the target, distance, line of sight, weapon, and a separate off-hand cooldown before executing the attack.

Configuration
-------------

`config/both_hands_combat-common.toml` provides these options:

- `enabled`: enables off-hand combat.
- `requireLineOfSight`: blocks attacks through walls.
- `attackTimeoutAfterSwing`: main-hand cooldown fraction retained after an off-hand hit.

Build
-----

Run `gradlew.bat build`. The output jar is written to `build/libs`.

Credits
-------

Gameplay behavior is ported from OffHandCombat by Arekkuusu and BunnyCinnamon, distributed under the MIT License. This project targets Minecraft 26.1.2 and NeoForge 26.1.2.99.
