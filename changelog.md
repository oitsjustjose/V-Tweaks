# V-Tweaks Changelog MC 1.19.x

## 3.6.6

- Added new optional Anvil Recipe attributes `cpFromLeft` and `cpFromRight`. Assumes to be `false` if not included. `cpFrom[Left|Right]` will copy all NBT **except** for those defined in the output, to the output. You can choose to set BOTH to true, but any overlap in NBT will be overwritten by the **RIGHT** input. 

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