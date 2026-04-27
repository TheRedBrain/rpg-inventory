<a id="document_start"></a>
# RPG Inventory

Adds a new inventory screen with more equipment slots and other equipment related mechanics.

- [Expanded Inventory Screen](#inventory_screen)
- [Additional Features](#additional_features)
- [Integrations and Compatibility With Other Mods](#mod_compatibility)

<a id="inventory_screen"></a>
# Expanded Inventory Screen

The expanded inventory screen is the most prominent feature of this mod.

However, it can be disabled in the server config. This will also disable all other features that depend on it.

## New equipment slots

These new equipment slots work similar to the existing four armor slots.

All slots can be configured individually. These options include:
- whether the slot is enabled
- whether the slot is visible
- whether items can be manually placed into the slot
- whether items can be manually taken out of the slot
- the position of the slot

The new slots accessible in the inventory are:
- 1 belt slot
- 1 necklace slot
- 2 ring slots (the second one is disabled by default)
- 1 gloves slot
- 1 shoulders slot
- 1 relic slot
- 1 class item slot (not visible or manually interactable by default)

### "Spell" Slots

There are also 8 "spell" slots, which are directly controlled by the "rpginventory:active_spell_slot_amount" entity attribute. Its value determines how many slots are enabled.

The "default_spell_slot_amount" server config option controls the amount of spell slots each player has active by default.

_The spell slots were originally designed to be used with spell books powered by Spell Engine, but they work with other items as well._

## Hand Slot Overhaul

This is an extension to the existing offhand slot. The new "hand slot" is "replacing" the currently selected hotbar slot.

Items in both hand slots can be "sheathed" (by pressing a hotkey), which gives access to the hotbar slots again. When pressed again, teh items are unsheathed.

Sheathed items are rendered on the player model. The exact position can be configured in the client config and items in the "rpginventory:not_shown_when_in_sheathed_hand" and "rpginventory:not_shown_when_in_sheathed_offhand" item tags are not rendered when in those slots.

> The vanilla "Swap Hands" hotkey is disabled while the Hand Slot Overhaul is active. Swapping inventory items into the hotbar using the number keys still works.

### Two-handed Stance

This is a special case, where only the offhand is sheathed. This leads to the unique situation where the mainhand slot is occupied, but the offhand slot is empty.
The two-handed stance can be toggled by pressing a hotkey. This is not possible, if the item in the main hand slot is iin the "rpginventory:non_two_handed_items" item tag.

The two-handed stance enables no new functionality on its own, but mods like [Better Combat Extension](https://modrinth.com/mod/bettercombat-extension) can utilise it.

### Empty hand items

When a hand is not sheathed, but the corresponding slot contains no item, the players hand is not empty. The item in the corresponding "empty hand slot" is held instead.

The empty hand slots always contain a item called "Empty Hand Weapon". This is technically a weapon. When Better Combat is installed, this allows for unarmed combat.

### Alternative Hand Slots

These two slots can hold the same items as the regular hand slots, but the held items do not count as "equipped" (no attribute modifiers/enchantments/etc take effect).

Items in the hand slots can be swapped with the items in the corresponding alternative hand slots by pressing hotkeys. There are individual keys for the main and the offhand. There is also a hotkey that swaps both hands at the same time.

### Technical equipment slots

The mechanics of the Hand Slot Overhaul use various "hidden" equipment slots:
- 1 sheathed hand slot
- 1 sheathed offhand slot
- 1 empty hand slot
- 1 empty offhand slot
- 1 alternative hand slot
- 1 alternative offhand slot

### Additional Hotkey Settings

All hotkey actions of the Hand Slot Overhaul have configurable, additional effects:
- applying cooldowns to the items in the corresponding slot
- stamina requirements/costs (if [Stamina Attributes](https://modrinth.com/mod/stamina-attributes) is installed)

## Status Effect screen

Active and visible status effects are listed on the right side of the inventory screen. They are sorted by their category (harmful, beneficial and neutral). Effects can have a description (added by assigning a value to <effect_translation_key>.description in the lang files), which is also displayed.

Effects in the "rpginventory:food_effects" effect tag are displayed in a separate list.

## Slot Tooltips

Equipment slots can now have a tooltip. It is only shown when the slot and the cursor stack are empty. This feature can be disabled in the client config.

## Additional settings and features

The 2x2 crafting grid in the player inventory can be disabled.

---

<a id="additional_features"></a>
# Additional Features

[Back to start](#document_start)

These features work independently of the Expanded Inventory Screen.

## Advancement Locked Items

An item stack that has the "rpginventory:advancement_locked" component has one of 3 different 'status' modes, which is saved in the component.

The different modes are:
- 'not_unlocked'
- 'unlocked', this has the lowest priority. The item can only be equipped and used if the item is in this mode.
- 'locked', this has the highest priority

The "advancement_locked" component has 5 string fields:
- "unlock_advancement": the id of the 'unlock advancement'. If the player has this advancement unlocked, the status is changed to 'unlocked', if not the status is 'not_unlocked'. If this field is an empty string, the item can't be 'not_unlocked'.
- "lock_advancement": the id of the 'lock advancement'. If the player has this advancement unlocked, the status is 'locked'. If this field is an empty string, the item can't be 'locked'.

- "not_unlocked_tooltip_text": this string is optionally displayed in the item tooltip, when the item status is 'not_unlocked'. This supports localization.
- "tooltip_text": this string is optionally  displayed in the item tooltip, when the item status is 'not_unlocked'. This supports localization.
- "locked_tooltip_text": this string is optionally displayed in the item tooltip, when the item status is 'not_unlocked'. This supports localization.

Inventory slots containing an item with the 'not_unlocked' or 'locked' status modes, can optionally display a slot overlay.

## Exclusive Equipment

Equipment items can be part of multiple "exclusive equipment groups", which are defined as strings saved in the "rpginventory:exclusive_equipment" data component. Each group may only be present on one equipped item at any time. If a group is detected on additional items, those items are either moved into the regular inventory or dropped on the ground.

## Player Crafted Items

Item stacks that have the "rpginventory:saves_crafting_player" component will save the player, that crafted them. That player is then displayed in a line in the item tooltip (Can be disabled in the client config).

This is purely cosmetic.

## Player Bound Items

Items can be bound to a player. Player bound items can only be used by that player. Player bound items have an additional tooltip line that shows the player name. (Can be disabled in the client config)

Inventory slots that contain items bound to another player have an overlay of a configurable color. This can be disabled in the client config.

### How to bind an item to a player

If an item stack has the "rpginventory:bounds_to_player" component, the item stack will bind itself to a player when it is placed in a player inventory.
At that point, the "rpginventory:bounds_to_player" component will be removed and the "rpginventory:player_bound" component will be added instead. This component saves a player profile.

## Restricted Equipment Changes

The server config setting "allow_equipment_changes" controls, whether items can be put into or removed from equipment slots.

When a player has the "rpginventory:civilisation" status effect items can be put into or removed from equipment slots, regardless of the config setting.

When a player has the "rpginventory:wilderness" status effect items can not be put into or removed from equipment slots, regardless of the config setting.

Item stacks with the "rpginventory:ignores_equipment_change_restrictions" ignore these restrictions.

## Unusable Items

Items in the "unusable_when_low_durability" item tag or with the "rpginventory:unusable_when_low_durability" data component have the same behavior as elytra. Instead of getting destroyed when losing all durability, they become unusable until they are repaired. Unusable items have a different translation key (default one + "_broken").

Inventory slots that contain unusable items have an overlay of a configurable color. This can be disabled in the client config.

## Mannequins and Load Out Items

Mannequins are blocks that have storage slots similar to the players equipment slots. Items placed in those slots form a 'load out', which can be equipped by players. Equipping a load out fills the players equipment slots with copies of the load out items. Only slots that are empty or contain a load out item are filled.

Changing and/or equipping load out items can be disabled for non-creative players using block entity data.

### Load out items

Depending on a server config setting either the "rpginventory:is_destroyed_on_death" or the "rpginventory:is_kept_on_death" data component is applied to load out items.

These items can also normally not be removed from a slot, only when interacting with a mannequin.

## Additional Item Tooltips

These can be configured in the client config, including the position.
- load out items
- player bound items (disabled for load out items)
- player crafted items (disabled for load out items)
- advancement locked items (disabled for load out items)
- if an item is in the "two_handed_items" item tag.
- the slots an item can be equipped in, this is controlled by item tags.

## PVP Deaths

When the health of a players with the "rpginventory:pvp" status effect reaches zero, they are not killed, but trigger this feature instead.

Effects include:
- remove all status effects (except the pvp effect and all entries of the "rpginventory:kept_on_pvp_death" status effect tag)
- reset players resources (health, mana, stamina, etc.)
- reduce the amplifier of the "pvp effect" by one
- if the new amplifier is greater or equal to zero:
  - teleport player to a specific location (spawn point/world spawn by default, changes to Script Blocks' "location access position" when installed)
- if the new amplifier is smaller than zero:
  - teleport player to their spawn point/world spawn

## Inventory changes on death

RPG Inventory adds several mechanics that influence what happens with the items in the player inventory when the player dies.

> When the vanilla "keep_inventory" game rule is set to true, all items are kept.

The "rpginventory:keep_inventory" status effect is applied when an item in the "sacrificed_to_keep_inventory_on_death" item tag is equipped (in an equipment, trinket or the offhand slot).

When the player dies while having that status effect, all equipped items in the "sacrificed_to_keep_inventory_on_death" item tag are destroyed. The rest of the inventory is kept, regardless of game rules and stuff like "Curse of Vanishing".

> The vanilla enchantment "Curse of Vanishing" is applied only when no item was sacrificed to keep the inventory.

Remaining items are kept under these conditions:
- they have the "rpginventory:is_kept_on_death" component
- they are in the "rpginventory:empty_hand_weapons" item tag

Items that are not kept, get destroyed under these conditions:
- the server config setting "destroy_dropped_items_on_death" is set to true
- they have the "rpginventory:is_destroyed_on_death" component

Remaining items are dropped like normal.

## Status Effects

When "building_mode_status_effect_identifier" is a valid status effect identifier and the player has that status effect, several mechanics are ignored.
- every item can be used to attack and to break blocks
- both hands behave like they are sheathed, so the hand slot contains the item in the selected hotbar slot, like in vanilla.

---

<a id="mod_compatibility"></a>
# Integrations and Compatibility With Other Mods

[Back to start](#document_start)

## Backpack Attribute

A button that opens the Backpack Screen can be added to the inventory screens. The 2x2 crafting grid has to be disabled.

## Inventory Size Attributes

The hotbar in the HUD can be configured to only show enabled hot bar slots.

## Spell Engine (1.21.1 only)

Spell Engine's status effect API is used for several mechanics that apply "hidden status effects" to the player when certain conditions are met.

- when the item in the main hand is in the "rpginventory:two_handed_items" item tag and the offhand slot is not empty/sheated, the player can't attack, use the item or cast spells.
- when the item in the main hand is not in the "rpginventory:attack_items" item tag and the 'allow_attacking_with_non_attack_items' server config option is set to false, the player can't attack.

## Player Attribute Screen (1.21.1 only)

When the "Player Attribute Screen" mod is installed, a button to toggle the attribute screen is active in the RPG Inventory screen.

## RPG Crafting (1.21.1 only)

A button that opens the Hand Crafting Screen can be added to the inventory screens. The 2x2 crafting grid has to be disabled.

## Numismatic Overhaul (1.21.1 only)

The purse widget is displayed on the RPG Inventory screen.

## Trinkets (1.21.1 only)

Trinket slots are displayed on the RPG Inventory screen.

All trinket slots have a tooltip. It can be set in the lang file with this schema:

```json
{
	"slot.tooltip.<group_name>.<slot_name>": "Test Slot"
}
```

When the string is empty, no tooltip will be shown.
