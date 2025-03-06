# V-Tweaks Changelog MC 1.21

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