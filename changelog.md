# VTweaks Changelog

## 2.0.6b

### Fixed

- **HOTFIX**: Remove debug console logs when lumbering

## 2.0.6

### Changed

- Lumbering
  - Lumbering will now break tools again
  - Lumbering _won't_ break tools with Imperishable
  - Lumbering respects Forge Energy capabilities
  - Lumbering respects CoFH Energy and other mods using the Energy NBT tag.

## 2.0.5:

### Fixed

- Lumbering (Backport from 1.14 version)

## 2.0.4:

### Fixed

- Crash if ChallengerMobs Rarity less than or equal to 0
- Debug output when throwing concrete powder with ConcreteTweaks enabled

## 2.0.3:

### Added

- ConcreteTweaks: Throwing a concrete powder item from your inventory into water will turn it into a concrete block! This also works with dispensers.

## 2.0.2:

### Added

- Config option for PeacefulSurface Y requirements
- Config for whether or not lumbering chops down leaves with the tree

### CHANGED

- Lumbering wood-chopping algorithm: \* It now cuts down all of **_most_** trees! No more static pattern, now uses recursion to detect trees better

### Fixed

- Updater URL (for the Mods GUI screen)

## 2.0.1:

### Added

- Adventurer's Toolbox compat to Lumbering recipe

### CHANGED

- Peaceful Surface feature to use Sea Level instead of "surface" guesstimate.

## 2.0.0c:

- Peaceful Surface feature now does a better guesstimate at what's the 'surface' of a world

## 2.0.0b:

- Fix crash on dedicated server with Peaceful Surface feature.

## 2.0.0:

**This is the "Cleanup" update; it removes a lot of features that were weird, useless, or just didn't flow right with MC or Modded MC**
**You'll need to re-generate your config file**

### Removed

- Multifaceted, Hypermending, Autosmelt and Stepboost enchantments
- Loss prevention features (the ones where obisidian / cactus / others would just _pop_ into your inventory)
- Stack Tweaks
- Leaf Eater
- Glitching Item Fixes
- Mob Killer (For pigmen, bats, overworld withers)
- Extra smeltables
- Torch Helper
- Ping Damage Protection

### Changed

- Challenger Mobs now only drop _one_ item - Not sure what ever convinced me they should drop two...

### Added

- Peaceful Surface: prevents enemies from spawning on the surface, except for nights of a new moon

## 1.4.11.4:

**The 1.10.x branch is now only under a "as-needed" updating basis; only widespread game-crashing bugs will be Fixed**

### Fixed

- Loss-prevented blocks not being picked up by fake-player type breaking blocks.

## 1.4.11.3:

### Added

- Config option for blacklisting entities for Challenger Mobs.

### Fixed

- Stepboost sometimes not working (see below):

### Removed

- Stepboost sneak toggle. It's not something I could necessarily get working. PR's accepted if you propose a fix
- Unused Sleeping Bag config
- Overall Enchantment system restructure. No user-visible changes.

## 1.4.11.2:

### Changed

- Change everything to the new Forge Registry system

## 1.4.11.1:

**The 1.11.x branch is now only under a "as-needed" updating basis; my current code focus is 1.10.2 and 1.12**

### Added

- 1.12 version! Most things work short of JEI integration (pending)

### Removed

- Sleeping Bags. Dumb feature, didn't work well, and wasn't done well by me. "Sleeping Bag" beds should work as plain beds.
