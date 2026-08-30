# Arrow Fletching

A NeoForge mod that adds **30 craftable arrows**, each with custom art and its own effect.
Fire any of them from a bow or crossbow.

- **Minecraft:** Java Edition 26.2
- **Loader:** NeoForge 26.2.0.69
- **Version:** 1.1.0
- Ships as a single `.jar`.

## Arrows

### Effect arrows
| Arrow | Recipe | Effect |
|---|---|---|
| TNT | 8 arrows + TNT | Small explosion (radius 2.0) on impact |
| Water | 8 arrows + water bucket | Places a water source where it lands |
| Lava | 8 arrows + lava bucket | Places a lava source where it lands |
| Ender | 8 arrows + ender pearl | Teleports the shooter to the landing spot |
| Bee | 8 arrows + honeycomb | Spawns 3 bees; if it hit a mob, they swarm it |
| Wind | 8 arrows + wind charge | Launches nearby entities ~10 blocks up |
| Lightning | 8 arrows + lightning rod | Calls a lightning strike |
| Dripstone | 8 arrows + pointed dripstone | Drops a 5×5 volley of falling, damaging stalactites from 20 blocks up |
| Cage | 8 arrows + iron bars | Boxes the struck mob in a 3×3 iron-bar cage |
| Slime | 8 arrows + slime ball | Slowness VI for 5s |
| Fishing Rod | 8 arrows + fishing rod | Reels nearby items **and** mobs back toward you |
| Lichen | 8 arrows + glow lichen | Grows glow lichen + a bright hidden light (removed if the lichen is broken) |
| Membrane | 8 arrows + phantom membrane | Homing: keeps its launch speed and curves toward the nearest mob |

### Ore damage tiers (crafted like a vanilla arrow: material / stick / feather → 4)
Stone → Flint → Copper → Iron → Gold → Diamond → Netherite, each hitting harder than the last.

### Fish tiers (material / stick / feather → 4)
Cod → Salmon → Pufferfish. These fly through water with **no slowdown**.

### Mining tiers (8 arrows + the matching pickaxe → 8)
Excavate an N×N×N cube and respect that pickaxe's harvest level (won't break ores it's too weak for —
those are left floating). Heavier tiers also cost more bow durability per shot.

| Tier | Cube | Harvests up to | Extra bow wear |
|---|---|---|---|
| Wooden | 1³ | wood level | +0 |
| Stone | 2³ | stone level | +1 |
| Copper | 3³ | copper level | +2 |
| Iron | 4³ | iron level | +3 |
| Gold | 5³ | iron level (bumped to match cost) | +4 |
| Diamond | 7³ | diamond level | +6 |
| Netherite | 11³ | all harvestable blocks | +10 |

## Building

Requires **JDK 25** (Minecraft/NeoForge 26.2 needs it). From the project folder:

```bash
./gradlew build
```

Output: `build/libs/arrow_fletching-1.1.0.jar`. To run a dev client:

```bash
./gradlew runClient
```

> Note: launch the client with `./gradlew runClient`, not the IDE Run button — Gradle
> builds fresh output, while an IDE's `bin/` folder can go stale.

## Installing

1. Install NeoForge 26.2.0.69 (https://neoforged.net) and run the game once.
2. Put `arrow_fletching-1.1.0.jar` in your `mods/` folder
   (`%appdata%\.minecraft\mods` on Windows).
3. Launch the NeoForge profile. Arrows appear in the Combat creative tab and craft in survival.

On servers, both the server and every player need the mod.

## Versions

Targets Minecraft `26.2` / NeoForge `26.2.0.69`, built with ModDevGradle `2.0.144`.
All version strings live in `gradle.properties`.
