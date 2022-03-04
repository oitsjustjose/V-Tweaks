# V-Tweaks Changelog MC 1.16.x

## 3.6.1

### Fixed

- Client crash

## 3.6.0

### Challenger Mobs Rewrite

- The Challenger Mobs feature has been completely rewritten from the ground-up to be completely data-driven. This means
  that you can add your own, modify existing or remove existing Challenger Mobs all via Datapack! To add your own class,
  you'll need to add a translation yourself via Resource Pack. For example, if you add a new variant named `nitro`, you
  would need to create / modify `assets/vtweaks/lang/en_us.json` (and one for every language you wish to support) and
  add the following:

    ```json
    {
      "vtweaks.nitro.challenger.mob": "Nitro %s"
    }
    ```

  This is case sensitive; using `"name": "Nitro"` means that you would need to name it `vtweaks.Nitro.challenger.mob`,
  rather than with a lower-case `n`.

  A Challenger mob has the following properties:

  ```json5
  {
    "weight": integer // The chance for this variant to be chosen for the allowed entities
    "gear": { // nothing in here is required, if you want no gear you can leave this as {}
      "mainHand": { // optional
        "item": string,
        "count": integer // optional
        "nbt": NBT Object // optional 
      },
      "offHand": { // optional
        "item": string,
        "count": integer // optional
        "nbt": NBT Object // optional 
      },
      "helmet": { // optional
        "item": string,
        "count": integer // optional
        "nbt": NBT Object // optional 
      },
      "chestplate": { // optional
        "item": string,
        "count": integer // optional
        "nbt": NBT Object // optional 
      },
      "leggings": { // optional
        "item": string,
        "count": integer // optional
        "nbt": NBT Object // optional 
      },
      "boots": { // optional
        "item": string,
        "count": integer // optional
        "nbt": NBT Object // optional 
      } 
    },
    "unlocalizedName": string, // used for localization, internal referencing
    "healthMultiplier": float,
    "speedMultiplier": float,
    "entityFilter": {
      "isBlacklist": boolean,
      "entities": [
        string where string is any entity's resource location (the one used in /summon command)
      ]
    },
    "effectsOnAttack": [ // effects inflicted to player when attacked by this entity
      {
        "name": string, (same resource location used in /effect command)
        "duration": integer //optional, in terms of ticks (remember 20 ticks in a second)
        "amplifier": integer //optional, 0-indexed (amp == 0 means Level 1 potion eff)
      }
    ],
    "loot": [
      {
        "weight": integer, // chance of this loot being dropped
        "item": {
          "item": string,
          "count": integer // optional
          "nbt": NBT Object // optional 
        }
      }
    }
  }
- ```

### Added

- No feature `disableBabyZombies` which disables baby zombies and zombified piglins, replacing them with adult variants.

### Changed

- Food Tooltips now use colors differently:
    - Green means normal food
    - Magenta means food with beneficial effects
    - Red means food with negative effects
    - If a food has both negative and beneficial effects, the negative effects will take take priority.
- Food tooltips' prefixes can now be localized to your language

## 3.5.0

### Added

- ChopDown: A new feature where trees straight up fall down (**DISABLED BY DEFAULT**)
    - Trees don't just disappear into items, but they fall
      instead! [Credit to Tersnip's original implementation](oitsjustjo.se/u/5cCfruy1x)
    - Acknowledges lumbering, has smarter tree detection than the original as well as some other small optimizations
      overall
    - Configurable tree detection (checks for at least 1 leaf and x many logs above the original, where x is a config
      option)

### Fixed

- Smol Bees config not working -- this config option is now in the Client Config as well
- Enchantments being able to be applied on top of each other
- New Lumbering config option to set max log limit fixes StackOverflow in dense forests with unbreakable/electric tools

## 3.4.2

### Added

- SMOL BEES
    - They're smol. What else do you want!
    - Oh yeah, they're bounding box is the same so it's easy to interact with them still

### Fixed

- Challenger Mob Loot not working
    - If you change your challenger mob drops, you'll need to run the `/refresh` command to reload them as a side-effect
      of this fix
- PetArmory configs being completely useless
- NoPetFriendlyFire now prevents other damage forms such as Sweeping Edge, splash potions and more
    - A hurt animation/sound will still inevitably be played, but rest assured 0 damage is done.

## 3.4.1.1

### Fixed

- Low health sound playing for every player, not just the associated player.

## 3.4.1

### Added

- New Feature: No Pet Friendly Fire!
    - This feature prevents either (configurable) the owner of a pet, or all players, from attacking tamed pets!

## 3.4.0.1

### Fixed

- Strict lumbering being _too_ strict with BlockStates
- Extra break sound being played when lumbering

## 3.4.0

### Added

- Low-health heartbeat noise - fully configurable, fully client-side
- Anvil tweak that prevents your tools from increasing in XP cost to repair!

### Changed

- Lumbering now is more FPS friendly by reducing particles and sounds

## 3.3.3b

### Fixed

(LOGICAL ERROR): Lumbering not working with electrical tools and possibly others

## 3.3.3

### Fixed

Lumbering:

- Tool damage from lumbering will now make sense again
- Imperishable will always save a tool while lumbering again
- Non-imperishable tools will reserve 1 durability on your tool so it doesn't get destroyed via lumbering and you can
  repair it

## 3.3.2

### Added

- New config option for limiting lumbering to the initial wood type you broke
  See [this tweet](https://twitter.com/oitsjustjose/status/1344366952509284353): the first 3 lumbers are
  with `lumberingIsStrictAboutWood=true`, the last is with `lumberingIsStrictAboutWood=false`

## 3.3.1

### Fixed

- Challenger mobs still lingering after the mob has died/despawned (I HOPE)
- Challenger mob particles not rendering on servers, at all

## 3.3.0

### Fixed

- Lumbering not working on electrical tools (for the most part)
- Particles from Challenger Mobs lingering after the mob has died/despawned

### Changed

- `enablePeacefulSurface` now defaults to `false`

## 3.2.0

Change compiler options to support 1.16.3 and 1.16.4 (and hopefully .1 and .2 too?)

## 3.1.9

**Initial port to 1.15! I think[?] everything's working!**
