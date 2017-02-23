1.4.9.12:
* Added: Sleeping Bags; any bed item renamed "Sleeping Bag" (capitalization doesn't matter) will allow you to right-click to sleep anywhere! If on Multiplayer, at least 50% of the players (including yourself) must be asleep.
* Changed: AxeLeafBlower -> LeafEater. Config file has changed too; if you disabled it before you'll have to again under the new name :(

1.4.9.11:
* Added: Axe Leaf Blower feature; axes break an AOE of leaves (without consuming extra durability).
	
1.4.9.10:
* Added: Durability Tooltips & Config Options

1.4.9.9:
* Added: Death Point, a small chat message telling you where you died!
* Added: Config options for Sheep Dye Fix
* Changed: TONS of huge refactors - nothing that effects players
* Changed: Players can no longer use the config to change the names of Challenger Mobs
* Changed: All config options have been tested to see which ones require a restart, and now ONLY the restart-requiring config options have such requirement.
* Fixed: Right-clicking sheep with empty hand causing crash

1.4.9.8:
* Changed: Minecraft Version to 1.11
* Changed: Had to re-write Torch Helper. Hopefully it works as well as it used to!
* Removed: Item despawning stuff completely. This includes egg hatching and sapling planting

1.4.9.7:
* Added: Lava Loss Prevention; makes it safe to mine Obsidian (or Chisel's Basalt, the "Raw" variant only) when it's hovering above a block of lava.
* Changed: Defaults for egg hatching.... they were far too high.
* Changed: Defaulted config for egg hatching to OFF. How hard are chicken cookers anyway?

1.4.9.6:
* Added: Organic Sapling Feature; instead of despawning, saplings will try to plant themselves first!
* Added: Egg Hatching; eggs will hatch (configurable chance) instead of despawning!
* Added: Despawn Timer Override; disabled by default (mostly meant for debug, but might be helpful!), lets you adjust how long item entities stay in the world

1.4.9.5:
* Improved: Challenger Mob Equipment / Class Spawning - should be less equipment class meshing
* Improved: Easy Crop Harvesting method
* Added: Blacklist for classes which Easy Crop Harvesting should not work for
* Fixed: Step Boost enchantment doing weird things with step height and Baubles

1.4.9.4:
* Fixed: Step Height Issues. Jesus I'm a tard

1.4.9.3:
* Officially ported to 1.10.2 (will work on 1.9.4, 1.10 and 1.10.2 though)
* Fixed: Challenger Mob config parsing
* Fixed: Sheep Dying fix inter-mod compat with BiomesOPlenty
* Fixed: Hanging Item Fix doing anything with TiCon Frames

1.4.9.2:
* Removed: Guide Book, Dragon Respawn
* Added: One-time chat notifier of V-Tweaks wiki link

1.4.9.1:
* Ported to 1.9.4 :D

1.4.8.5:
* Added: Config Options for level costs
* Added: Anvil Recipe for Step-Boost: 16 stairs of any kind (mod included) plus Book & Quill
* Changed: Lots of config stuff - you should probably delete and re-configure your V-Tweaks config

1.4.8.4:
* Added: Config to add custom block registry entries to the Autosmelt + Fortune interaction list
* Changed: Autosmelt algorithm: functionality is still identical

1.4.8.3:
* Fixed: Derpy enchantment names

1.4.8.2:
* Hotfix: Fixed issue with Challenger Mobs' configurable drops

1.4.8.1:
* Removed: Door placement sound. Reflection worked in dev environment but never after compilation

1.4.8:
* Fixed: Hanging item compatibility with Tinkers' Construct
* Fixed: Massive crashing with door placement sound fixer
* Fixed: Crash with crophelper + DenseOres (or other blocks too, possibly)

1.4.7:
* Added: 100% Configurable Loot Table for Challenger Mobs
* Changed: Door placement sound fixer now dynamically grabs the sound :D
* Changed: Mob Tweak config options to default to false when the config is generated
* Changed: Early Game config option to default to false when the config is generated
* Changed: Many small refactors
* Fixed: Somewhat fixed the Torch Placement helper.. it won't be perfect, but it's darn good!
* Fixed: OreDictionary Dye fixer for sheep not reducing stacksize

1.4.6:
* Added: Feather Plucker! Pluck feathers from chickens using shears - has a cooldown, but doesn't actually damage the chicken
* Added: Sheep Dye Fix - nonconfigurable fix allowing oreDict dyes to color sheep
* Fixed: Torch Helper interaction issues
* Fixed: Jukebox Disc ejection inventory update issue

1.4.5.5:
* Fixed: Fall damage negation when wearing no boots

1.4.5.4:
* Added: Option for smelting more vanilla items that are made of wood
* Changed: Easy Harvesting now can be done with almost any item in-hand

1.4.5.3:
* Removed: Sign editor tweak. Was not working, and caused client crashes D:
* Added: Changelog book also given instead of a whole new guide, since the guide has a recipe

1.4.5.2:
* Fixed: Torch Helper placing torches on blocks with a GUI

1.4.5.1:
* Changed: tiny tweak to how better feather falling works: now applies fall damage negated to your boots

1.4.5:
* Added: Pet Armor!! Read my book or the config file :D
* Added: Recipe for V-Tweaks guide if you need one for whatever reason
* Added: Config for disabled V-Tweaks guide given to players
* Changed: Wither Whitelisting now gives the player back their resources
* Changed: Challenger Mobs spawn with more health, rather than resistance potion effect
* Fixed: Challenger Mob Spawn Rates

1.4.4:
* Added: My own implementation for hung item fixes
* Added: Ability to limit Wither spawning to other non-overworld dimensions
* Added: JEI recipe compatibility
* Added: Console-spammy implementation to hush villages (off by default, I'd suggest that)
* Changed: Tons of refactors

1.4.3:
* Added: Ability to edit signs via shift R-click with empty hand
* Added: Placement sound for doors (now all sound fixes are generalized in the config)
* Added: Fix for Light / Heavy Pressure Plate step sounds (see above for config info)
* Changed: Great optimizations to Easy Crop Harvesting drops
* Changed: Torch + Tool interaction now supports any item in your inventory with the localized version of 
	the name "Torch" - this offers multilingual support, so if your language it isn't named "Torch", 
	it'll still work
* Changed: Crop Harvesting now requires an empty hand to activate - temporary fix for weirdness with
	right-click interaction with your currently equipped item
* Fixed: Nether Wart "bonemealing" tweak allowing you to waste blaze powder on a fully grown crop

1.4:
* Changed: Completely rewrote configuration file and in-game mod config. All settings will need to be re-changed.
* Changed: Many small internal refactors. Affects nothing.
* Added: Ability to disable Lightning (or technically, thunderstorms). Lightning on my server causes a lot of stupidity.