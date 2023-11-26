# V-Tweaks Changelog MC 1.20.1

## 4.0.13:

### Added:

- Full ingredient support for Anvil and Fluid Conversion Recipes
    - This means you can now define multiple inputs using Tags or Array Inputs. For example, the following recipe allows
      you to throw either 1 stick, or any colored wool, to convert it to white wool.
  ```json
  {
    "type": "vtweaks:fluid_conversion",
    "input": [{
      "item": "minecraft:stick"
    }, {
      "tag": "minecraft:wool"
    }],
    "output": {
      "item": "minecraft:white_wool"
    },
    "fluid": "minecraft:water"
    }

  ```

### Changed:

- Chopdown Config now has its own subsection - **previous settings will need to be re-applied**

### Removed:

- Accidentally left in a test JSON recipe for Anvil Crafting - removed that!

### Fixed:

- [Crash on Item Pickup](https://github.com/oitsjustjose/V-Tweaks/issues/110)


## 4.0.12:

### Changed:

**Anvil Recipe**:

- The setting `cpFromLeft` has been renamed to `copyTagsFromLeft`
- The setting `cpFromRight` has been renamed to `copyTagsFromRight`
- Added a new optional setting `strict`. When set to false, the inputs on both sides of the recipe will ignore NBT;
  otherwise the inputs will need to match the NBT specified in the datapack entry

**Water Conversion Recipes**:

- Fixed background not working in JEI
- Added catalysts for the JEI tab so that it would appear as a "use" for Dispensers, Splash Water Bottles & Water
  Buckets

## 4.0.11:

### Fixed:

- Actually fix feather falling tweak -_-

## 4.0.10

### Added:

- Configuration for which moon phases Peaceful Surface should apply to (defaults to all but the New Moon)
- [Trader Stonks (Enabled by default)](https://twitter.com/oitsjustjose/status/1687305938816872448)
    - This feature, inspired by SimplySarc's idea, makes it so that Wandering Traders may sell any "valuable" items that
      you've lost recently
    - This functionality is in a beta state - please report bugs if you find any!!
    - Technical details:
        - Uses the tag `vtweaks:valuable`
            - If configured (this setting is on by default), if a `vtweaks:valuable` item _can_ be enchanted, it'll only
              save the item _if_ it's enchanted.
        - The chance that an item is added is configurable
        - The Emerald cost is configurable, but it'll always cost Emeralds
        - The item that gets added is random, and it might be yours _or_ some other players'!
        - A silly message is shown when chatting with a Wanderer (can be disabled)

### Fixed:

- Peaceful Surface outright not working
- Feather Falling Tweak not working as intended, negating _all_ damage _except_ for fall damage 🤦‍♂️
- Splash potions of \<fluid\> not working on FluidConversionRecipes

### Changed:

- Re-organized a lot of configs - please check your `vtweaks-common.toml` file to verify that your previous settings are
  what they should be

## 4.0.9

### Fixed:

- Hard dependency on JEI

## 4.0.8

### Fixed:

- `FluidConversionTweak` not working for many reasons

## 4.0.7

### Added:

- `ItemFrameTweak`: Allows any `vtweaks:clear_glass`, or glow ink sac, to be activated on an item frame while sneaking
  to make the item frame transparent or glowing respectively. [Demo](https://dv2ls.com/vt-itemframetweak)
- `SplashPotionTweak`: Allows splash potions of water to behave the same as throwing an item in water, but using
  V-Tweaks' <u>Fluid Conversion Recipes</u>. Works as an alternative if you'd like to set `enableFluidConversionRecipes`
  to `false`, but **only works with blocks** instead of blocks and items. [Demo](https://dv2ls.com/vt-splash)

### Changed:

- Mixin configs are now a part of `vtweaks-common.toml`  - if you previously changed `enableCactusItemProtection`,
  you'll need to change it again but in `vtweaks-common.toml` 🙁

## 4.0.6: INITIAL PORT TO 1.19.4

### Added:

- All new configs for Food Tooltips:
    - `useOriginalFoodTooltipColor (bool)`: if food has a custom color such as via rarity or NBT, use that color
      instead of the other colors defined below for the *hunger bar*. If food has no given custom color, then the
      default color (or the buff/debuff color) will be
      used. [Demo](https://oitsjustjose-sharex.s3.us-east-2.amazonaws.com/2023/04/java_04-02-23%2014-21-23-940.mp4)
    - `foodTooltipColor (string)`: the HEX code for a normal food that gives no Positive / Negative potion effects on
      consumption
    - `foodTooltipPositiveColor (string)`: the HEX code for food that gives **NO Negative Potion Effects** and at
      least one Positive potion effect on consumption
    - `foodTooltipNegativeColor (string)`: the HEX code for food that gives at least one Negative potion effect on
      consumption
    - `foodTooltipMultiplier (float)`: similar to health, 1 hunger is equivalent to half a hunger haunch. This setting
      controls a multiplier for this in terms of the hunger tooltip on foods.
    - `foodTooltipSaturationColor (string)`: the HEX code for the saturation a food gives

### Fixed (Hopefully 🤞)

- Hopefully patched up a crash on world join with the
  error `java.lang.IllegalStateException: Accessing LegacyRandomSource from multiple threads`. I've looked into this
  issue for most of my day today and I have no earthly idea what I'm doing wrong to cause this, but hopefully a small
  change I made may have resolved this.

## 4.0.5

### Potential memory leak resolutions

## 4.0.4****

### Micro-Optimized server and world tick performance

## 4.0.3

### Fixed

- Feather Falling Tweaks not breaking boots when at max damage

## 4.0.2

### Fixed

- Feather Falling Tweaks not working

## 4.0.1

### Fixed

- Crash on dedicated servers (Thanks Apache 😒)

## 4.0.0

This rewrite includes a couple of major changes:

## Removed Features:

- All Enchantments: Chopdown somewhat removes the need for Lumbering and I always hated the Imperishable implementation
- Bonemeal Tweaks: Bonemealing non-bonemealable blocks
- Drop Tweaks: Configurable despawn timers, egg hatching, sapling planting - these were all bad.
- Pet Armory: Throw gear at tamed pets - this was poorly implemented and was never worth the time to learn rendering
- Death Point: This really isn't necessary now with the new echo compass thing in 1.19
- Low Health Sound: Not really fitting for this mod, and its implementation was very data-modification heavy
- Chat message welcoming you to V-Tweaks is removed, this was annoying. I'll be implementing an optional Patchouli book
  for this soon :)

## Added Features

- Corpse Drops Tweak - Items dropped by the player upon death will no longer despawn like other items.
- Cactus Item Protection - prevents items from being eaten up by Cacti. **Configuration change requires restart**

## Overhauls

- Fluid Conversion Recipes - this _used_ to be the Concrete Tweak, but now they're Datapack based and you can modify the
  defaults (currently they just include all concrete powder -> concrete conversions)! You can specify the fluid, input
  and output and the recipe will automatically propagate in JEI with the custom plugin I made.
- Falling blocks caused by the Chop Down tweak will no longer drop leaves, but instead drop the block's corresponding
  drops
- `[CODE]` The Tweak system has been introduced. Create a new tweak by extending `VTweak` and using the `@Tweak`
  annotation. The config system will automatically grab onto what configs you need based on this annotation, and the
  Tweak Registry will automatically grab and fire all custom tweaks' events
- `[CODE]` Config System has been overhauled, allowing each tweak to house its own configs rather than making them
  static members of some conglomerate config class. The `category` param in the `@Tweak` annotation tells the config
  system what category to put the tweak's configs in - if it's not one of the ones listed in `CommonConfig.java`
  or `ClientConfig.java`, then it will be skipped.

## 3.6.8

- Chopdown will no longer consider player-placed leaves as part of a tree
- Hopefully fixed failure to start due to FastUtils `it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap.rehash`
  error

## 3.6.7

- At long last, the chopdown feature is _no longer experimental_ and has been finished to a point where I am content
  with it. This update fixes the offsets when breaking trees, as well as rotates logs as they fall.

## 3.6.6.2

- Fixes to NBT breakages!!

## 3.6.6

- Added new optional Anvil Recipe attributes `cpFromLeft` and `cpFromRight`. Assumes to be `false` if not
  included. `cpFrom[Left|Right]` will copy all NBT **except** for those defined in the output, to the output. You can
  choose to set BOTH to true, but any overlap in NBT will be overwritten by the **RIGHT** input.

## Should work on 1.19.1 and .2, but this has not been tested.

## 3.6.5.1

- Hopefully resolved issues with `Accessing LegacyRandomSource from multiple threads` errors.

## 3.6.5

### Added

- New `vtweaks:anvil` recipe type -- see below!

### Changed

- Huge internal refactor of code
- Lumbering and Imperishable will not disappear when disabled, but will not function
- Recipes for Enchanted Books now use `vtweaks:anvil` recipes to control inputs and cost

### `vtweaks:anvil`

Inputs AND outputs can have NBT defined for them! This recipe type performs a **_soft_** NBT comparison.
Example: if your item is defined as `{"item": "minecraft:stick", "nbt": { "Damage": 0 }}`, then if you input a stick
with enchantments or other NBT it will **succeed** so long as the NBT matches at the bare minimum the NBT defined in the
recipe (in this case, `{Damage:0}`).

`imperishable.json`:

```json
{
  "type": "vtweaks:anvil",
  "left": {
    "item": "minecraft:enchanted_book",
    "nbt": {
      "StoredEnchantments": [
        {
          "id": "minecraft:unbreaking",
          "lvl": 3
        }
      ]
    }
  },
  "right": {
    "item": "minecraft:enchanted_book",
    "nbt": {
      "StoredEnchantments": [
        {
          "id": "minecraft:unbreaking",
          "lvl": 3
        }
      ]
    }
  },
  "result": {
    "item": "minecraft:enchanted_book",
    "nbt": {
      "StoredEnchantments": [
        {
          "id": "vtweaks:imperishable",
          "lvl": 1
        }
      ]
    }
  },
  "cost": 24
}
```

`lumbering.json`:

```json
{
  "type": "vtweaks:anvil",
  "left": {
    "item": "minecraft:writable_book"
  },
  "right": {
    "item": "minecraft:golden_axe",
    "nbt": {
      "Damage": 0
    }
  },
  "result": {
    "item": "minecraft:enchanted_book",
    "nbt": {
      "StoredEnchantments": [
        {
          "id": "vtweaks:lumbering",
          "lvl": 1
        }
      ]
    }
  },
  "cost": 16
}
```
