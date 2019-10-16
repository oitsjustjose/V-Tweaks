# V-Tweaks Changelog MC 1.14.x

## 3.0.3b 

### Fixed

* *Actually* fixed that Peaceful Surface server-side crash 🤔

## 3.0.3

### Added

* Arm swing when using the Crop Tweak to harvest a crop (this was work, but I felt like it was worth it)
* Crop re-plant sound when using the Crop Tweak to harvest a crop
* Sound when using an imperishable tool with only 1 durability left
* New sound when when striking with an imperishable foe with only 1 durability left
* New text when you go to attack with a broken sword that is saved by imperishable
    * As always this is localizable 😄

### Fixed

* Crop tweak not working on Cocoa or Nether Wart
* Imperishable armor with durability > 1 not taking damage ever
* Imperishable sword being able to break
* Tool efficiency allowing your tool to break a block with Imperishable and 1 durability left
* Lumbering taking *insane* durability from your axe
* Tool Durability tooltip being 1 higher than actual
* Server crash with Peaceful Surface

### Changed

* Lumbering now only breaks what logs it can before the tool breaks. It will leave your tool with 1 durability and the tree entact.
* Cutting leaves with lumbering doesn't take durability

## 3.0.2

### Added

* Config Option for disabling any modifications to despawn timers at all

### Fixed

* Items despawning if Item Despawn Timer set to -1

### Changed

* Config options for item despawning to be more descriptive

## 3.0.1

### Fixed

* Imperishable enchant causing weird visual glitch on tools
* Imperishable enchant not working on sword attacks
* Imperishable enchant not working on sword breaking blocks
* Imperishable enchant not working on armor

## 3.0.0

### Initial Port

#### What won't be coming

* Sheep Dye Fix - may return if still needed
* Horse Armor Recipes

#### What's ready but doesn't work yet

* Cake drops - they're implemented but forge isn't firing the event that it utilizes

#### What's changed with the port

* Crop Harvest Tweak doesn't swing arm (I can't figure this out w/o using packets)
* Imperishable - I can't figure out how to add the Book to nether fortress loot, so the recipe is an anvil recipe of **Unbreaking III books** on both sides.
* All texts are now translatable - this includes ChallengerMobs' names, the welcome text on first world join, and more.
