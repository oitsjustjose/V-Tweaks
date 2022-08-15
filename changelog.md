# V-Tweaks Changelog MC 1.18.x

## 3.6.4.1 (HOTFIX)

- Fixes crash on dedicated server for Ungriefed Creepers feature

## 3.6.4

### Added

- Ungriefed Creepers: When any entity with tag `#forge:creeper` explodes, blocks exploded will ploop back into place
  after a short delay! Some small world griefing (such as snow or torches) may not place back, but they'll at least be
  dropped. Works fine with Tile Entities as well!

![https://oitsjustjose-sharex.s3.us-east-2.amazonaws.com/2022/08/ezgif.com-gif-maker%281%29.gif](https://oitsjustjose-sharex.s3.us-east-2.amazonaws.com/2022/08/ezgif.com-gif-maker%281%29.gif)

- EntityCulling datapack set:
  - Allows you to disable entities by Entity Tag or Entity Name (i.e. `#minecraft:raiders` or `minecraft:cat`), filtering by dimensions and biomes (biometags included).
  - Uses datapack system to make your own rules. Datapack data is stored in `data/vtweaks/culled_entities/<YOUR_FILE>.json`
  - JSON schema looks like this:
```json5
{
  // REQUIRED: You can get this info by looking at the entity with F3 turned on,
  //     or via third party tools like KubeJS
  
  // You can use ! to negate the selection 
  //     (i.e. !minecraft:creeper would apply to any non-creeper entity), 
  //     but you cannot mix them (i.e. [!minecraft:creeper, #minecraft:skeletons] is invalid) 
  "entities": [ "minecraft:creeper", "#minecraft:skeletons" ],
  // Optional - you don't have to define the dimension
  "dimensions": ["minecraft:overworld"],
  // Optional - you don't have to define the biomes either! You can make this a global cull :)
  "biomes": ["#minecraft:is_beach", "minecraft:desert"]
}
```

## 3.6.3

### Added

- New Torch Lighting Tweak! Any item that is tagged with the tag `vtweaks:ignition_item` can be right-clicked on any
  block that has a `lit` block state property. This includes campfires, candles, and many modded blocks!

### Changed

- With world height being completely modular via datapack, the Peaceful Surface Y-Level config no longer is bound
  to [0 - 256], but [MIN_INT - MAX_INT]
- _Some_ Config Names have been changed to be more consistent. These include:

    - `cropTweaksEnabled` -> `enableCropTweaks`
    - `disablePetFriendlyFire` -> `enablePetFriendlyFireTweak`
    - `challengerMobsEnabled` -> `enableChallengerMobs`
    - `peacefulSurfaceEnabled` -> `enablePeacefulSurface`

### Removed

- Egg Hatching tweak - this was a broken feature capable of causing excessive lag

## 3.6.2

### Fixed

- Challenger Particles causing issues on Client/server
