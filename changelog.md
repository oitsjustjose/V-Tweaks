# V-Tweaks Changelog MC 1.16.x

## 3.5.2

### Added

- Chopdown - this was removed for 3.5.1. Added back in for 3.5.2 😀

## 3.5.0

### Added

- ChopDown: A new feature where trees straight up fall down (**DISABLED BY DEFAULT**)
  - Trees don't just disappear into items, but they fall instead! [Credit to Tersnip's original implementation](oitsjustjo.se/u/5cCfruy1x)
  - Acknowledges lumbering, has smarter tree detection than the original as well as some other small optimizations overall
  - Configurable tree detection (checks for at least 1 leaf and x many logs above the original, where x is a config option)

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
  - If you change your challenger mob drops, you'll need to run the `/refresh` command to reload them as a side-effect of this fix
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
- Non-imperishable tools will reserve 1 durability on your tool so it doesn't get destroyed via lumbering and you can repair it

## 3.3.2

### Added

- New config option for limiting lumbering to the initial wood type you broke
  See [this tweet](https://twitter.com/oitsjustjose/status/1344366952509284353): the first 3 lumbers are with `lumberingIsStrictAboutWood=true`, the last is with `lumberingIsStrictAboutWood=false`

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
