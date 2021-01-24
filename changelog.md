# V-Tweaks Changelog MC 1.16.x

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