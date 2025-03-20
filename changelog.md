# V-Tweaks Changelog MC 1.21

## 4.0.18

### Added:

- New mixin tweak to make Powder Snow blocks solid
  - Disabled by default
  - Makes exploration in ambient snowy areas less annoying

## 4.0.17

### Added:

- Config to control whether baby `#c:chickens` can have their feathers plucked
    - Defaults to false; babies cannot have their feathers plucked

### Fixed:

- Ungriefing Creepers tweak not working
- Feather Plucking using the wrong Shears tag
- Feather Plucking causing the player's arm to swing even if plucking failed

### Changed:

- Updated all references to tags in the `forge` namespace to now use the `c` namespace instead
    - This change is also reflected in Config Comments for maximum clarity
- ChopDown Requires Tool setting uses a less sketchy method for tool detection

## 4.0.16

### Changed:

- The text `Durability: ` in the Durability Tooltip is now localizable!

### Fixed:

- Server Crash

## 4.0.15

Initial port to 1.21 (and 1.21.1 by proxy) using NeoForge. Tested full suite of features locally in dev server, so
hopefully there'll be minimal server-only issues, but we'll see!

Not too much has changed from a functional perspective really:

### Changed:

- Challenger Mobs' Gear and Drops now use the Component Feature to define properties like enchantments, potion data,
  etc.

### Removed:

- Food Tooltip: This was trickier to re-implement than I had time for, plus this feature gets turned off by most users
  anyways because better tools like Quark and AppleSkin exist.