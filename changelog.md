# V-Tweaks Changelog MC 1.18.x

## 3.6.3

### Added

- New Torch Lighting Tweak! Any item that is tagged with the tag `vtweaks:ignition_item` can be right-clicked on any block that has a `lit` block state property. This includes campfires, candles, and many modded blocks!

### Changed

- With world height being completely modular via datapack, the Peaceful Surface Y-Level config no longer is bound to [0 - 256], but [MIN_INT - MAX_INT]
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
