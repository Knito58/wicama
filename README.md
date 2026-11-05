# WICAMA - Wither Cage Material

Fabric mod for Minecraft 26.3 (Fabric Loader >= 0.19.5, Java 25, Fabric API).

Two building blocks for a safe Wither cage:

| Block | Recipe (yields 4) |
|---|---|
| `wicama:building_block_opaque` | `C S C / S O S / C S C` |
| `wicama:building_block_transparent` | `C G C / S O S / C G C` |

`C` = iron chain, `S` = smooth stone, `O` = obsidian, `G` = any glass block (`#c:glass_blocks`).

- In the `minecraft:wither_immune` tag and explosion proof (blast resistance of bedrock).
- Hardness of smooth stone. Any tool that can mine obsidian (tag `needs_diamond_tool`) mines them at smooth-stone speed and gets the drop. Weaker tools are as slow as on obsidian and drop nothing.

## Build

    ./gradlew build          # jar in build/libs
    ./gradlew runGameTest    # automated in-game tests (immunity, mining, explosions)

## Licence and disclosure

MIT, see `LICENSE`. This mod was written with the help of Claude AI (Anthropic).
