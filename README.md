# Let's Do Create Compat

![Let's Do Create Compat icon](docs/assets/icon.png)

An unofficial NeoForge compatibility add-on that makes [Let's Do] Vinery wine production work cleanly with Create automation.

## Features

- Adds real pumpable fluids for Vinery apple juice and grapejuice variants.
- Adds Create Mechanical Press + Basin recipes for Vinery grapes into juice.
- Makes Create Mechanical Harvesters collect grapes from mature Vinery grape bushes instead of seeds.
- Adds a Create Mixer recipe for apples into Vinery apple mash.
- Adds Create Mechanical Press + Basin recipes for apples and apple mash into apple juice.
- Adds Create Spout filling recipes for Vinery juice bottles.
- Adds Create Item Drain recipes to turn Vinery juice bottles back into fluid and empty wine bottles.
- Lets Create pumps and pipes fill Vinery Fermentation Barrels directly.
- Adds a Spout recipe for `vinery:wine_bottle` plus honey fluid into `minecraft:honey_bottle`.

## Requirements

- Minecraft `1.21.1`
- NeoForge `21.1.x`
- Create `6.0.0+`
- `[Let's Do] Vinery` for Minecraft `1.21.1`

Vinery may also require Architectury depending on the version you install.

## Install

Download the jar from the GitHub Releases page and put it in your instance's `mods` folder alongside Create and Vinery.

If you manually drop the jar into a CurseForge profile before the mod is published on CurseForge, the CurseForge app may not list it like a normal managed mod. The jar should still load in-game, and NeoForge's Mods screen should show it.

## Build From Source

```powershell
.\gradlew.bat build
```

On Linux or macOS:

```bash
./gradlew build
```

The built jar appears in `build/libs/`.

## Automation Flow

This mod intentionally bypasses Vinery's manual Grapevine Pot workflow with Create-native recipes:

`grapes -> Mechanical Press over Basin -> juice fluid -> tank/pipe/spout/barrel`

## Notes

This is an unofficial compatibility add-on and is not affiliated with Create, NeoForge, Minecraft, or the Let's Do/Vinery team.
