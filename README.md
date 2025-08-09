# RPG Inventory

Adds a new inventory screen with more equipment slots and other equipment related mechanics.

## New equipment slots

The new slots accessible in the inventory are:
- 1 belt slot
- 1 necklace slot
- 2 ring slots
- 1 gloves slot
- 1 shoulders slot
- 8 spell slots
  - The value "default_spell_slot_amount" in the server config file controls the amount of spell slots each player has active by default.\
    The new entity attribute "generic.active_spell_slot_amount" controls how many spell slots are added to/removed from the default active amount. This is 0 by default, but with entity attribute modifiers (EAMs) it can be changed.
These spell slots are intended to be used with spell books powered by Spell Engine, but they should work with other items as well.

If the "hand slot overhaul" is enabled in the server config, these slots become available too:
- 1 hand slot
- 1 alternative hand slot
- 1 alternative offhand slot

The mod also adds additional slots which are not directly accessible. They are used in mechanics which are explained later.
- 1 sheathed hand slot
- 1 sheathed offhand slot
- 1 empty hand slot
- 1 empty offhand slot

## New Keybindings and mechanics

### Swap hand

Swaps the item in the hand slot with the item in the alternative hand slot

### Swap offhand

Swaps the item in the offhand slot with the item in the alternative offhand slot

### Sheathe Weapons

Puts the items in the hand and the offhand slot into their corresponding sheathed hand slots. When pressed again, swaps the items back

Items in the sheathed hand slots are rendered on the player model. The exact position can be configured and items in the "rpginventory:not_shown_when_in_sheathed_hand" and "rpginventory:not_shown_when_in_sheathed_offhand" item tags are not rendered when in those slots.

### Toggle Two-handing Stance

Puts the items in the offhand slot into the sheathed offhand slot. When pressed again, swaps the item back. This is not possible, when the hand item is in the "non_two_handed_items" item tag.
When the hand item is sheathed, the hand slot contains the item in the selected hotbar slot, like in vanilla.
When the offhand item is sheathed, the offhand slot contains an empty item stack. On its own this is not very useful, it's designed to be used in combination with other mods like [Better Combat Extension](https://modrinth.com/mod/bettercombat-extension).

When a hand item is not sheathed, but the corresponding slot contains no item, the players hand is not empty. The item in the corresponding empty hand slot is held instead.
The empty hand slots always contain a item called "Empty Hand Weapon". This is technically a weapon. When Better Combat is installed, this allows for unarmed combat.

### Stamina Attributes Compatibility

Installing [Stamina Attributes](https://modrinth.com/mod/stamina-attributes) allows for swapping. sheathing and toggling the 2-handed stance to have configurable stamina costs/requirements.

### Note: The vanilla 'Swap Item With Offhand' hotkey is disabled when the "hand slot overhaul" is enabled

Using it when items where sheathed could duplicate items. Swapping items into the hotbar using the number keys still works.

## Status Effect screen

Active and visible status effects are listed on the right side of the inventory screen. They are sorted by their category (harmful, beneficial and neutral). Effects can have a description (added by assigning a value to <effect_translation_key>.description in the lang files), which is also displayed.

### Food Overhaul Compatibility

[Food Overhauls](https://modrinth.com/mod/food-overhaul) food effects are displayed in a separate list.

## Unusable Items

Items in the "unusable_when_low_durability" item tag have the same behaviour as elytra. Instead of getting destroyed when losing all durability, they become unusable until they are repaired. Unusable items have a different translation key (default one + "_broken").
Inventory slots that contain unusable items have an overlay of a configurable colour. This can be disabled in the client config.

## Player Bound Items

Items can be bound to a player. Player bound items can only be used by that player. Player bound items have an additional tooltip line that shows the player name. (Can be disabled in the client config)
Inventory slots that contain items bound to another player have an overlay of a configurable colour. This can be disabled in the client config.

### How to bind an item to a player

If an item stack has the "rpginventory:bounds_to_player" component, the item stack will bind itself to a player when it is placed in a player inventory.
At that point, the "rpginventory:bounds_to_player" component will be removed and the "rpginventory:player_bound" component will be added instead. This component saves a player profile.

## Player Crafted Items

Item stacks that have the "rpginventory:saves_crafting_player" component will save the player, that crafted them. That player is then displayed in a line in the item tooltip (Can be disabled in the client config).

This is purely cosmetic.

## Additional Item Tooltips

These can be configured in the client config, including the position.
- load out items
- player bound items (disabled for load out items)
- player crafted items (disabled for load out items)
- advancement locked items (disabled for load out items)
- if an item is in the "two_handed_items" item tag.
- the slots an item can be equipped in, this is controlled by item tags.

## Slot Tooltips

Equipment and trinket slots now have a tooltip. It is only shown when the slot and the cursor stack are empty. This feature can be disabled in the client config.

When the string is empty, no tooltip will be shown.

## Mannequins and Load Out Items

Mannequins are blocks that have storage slots similar to the players equipment slots. Items placed in those slots form a 'load out', which can be equipped by players. Equipping a load out fills the players equipment slots with copies of the load out items. Only slots that are empty or contain a load out item are filled.

### Load out items

Load out items are never dropped, they either vanish or are kept on death (this can be configured in the server config).

These items can also normally not be removed from a slot, only when interacting with a mannequin.

## Additional settings and features

The 2x2 crafting grid in the player inventory can be disabled.

Items in the "two_handed_items" item tag can only be used when the offhand is sheathed.

When "needs_two_handing_status_effect_identifier" is a valid status effect identifier, that status effect is applied when the item in the hand is in the "two_handed_items" item tag and the offhand is not sheathed.

When "no_attack_item_status_effect_identifier" is a valid status effect identifier, that status effect is applied when the item in the hand is not in the 'attack_items' item tag and the 'allow_attacking_with_non_attack_items' option is set to false.

When "building_mode_status_effect_identifier" is a valid status effect identifier and the player has that status effect, several mechanics are ignored.
- every item can be used to attack and to break blocks
- both hands behave like they are sheathed, so the hand slot contains the item in the selected hotbar slot, like in vanilla.

The game rule "canChangeEquipment" controls, whether items can be put into or removed from equipment slots.

When "civilisation_status_effect_identifier" is a valid status effect identifier and the player has that status effect items can be put into or removed from equipment slots, regardless of the gamerule "canChangeEquipment".

When "wilderness_status_effect_identifier" is a valid status effect identifier and the player has that status effect items can not be put into or removed from equipment slots, regardless of the gamerule "canChangeEquipment".

When the gamerule "destroyDroppedItemsOnDeath" is true and the vanilla gamerule "keepInventory" is false, the items in the players inventory are not dropped when they die. They are destroyed instead.

When "keep_inventory_status_effect_identifier" is a valid status effect identifier, that status effect is applied when an item in the "sacrificed_to_keep_inventory_on_death" item tag is equipped (in an equipment, trinket or the offhand slot).
When the player dies while having that status effect, all equipped items in the "sacrificed_to_keep_inventory_on_death" item tag are destroyed. The rest of the inventory is kept, regardless of game rules and stuff like "Curse of Vanishing".

### Various Status Effects Integration

All status effect identifier options default to status effects implemented by [Various Status Effects](https://modrinth.com/mod/various-status-effects).

### Player Attribute Screen Integration

When the "Player Attribute Screen" mod is installed, a button to toggle the attribute screen is active in the RPG Inventory screen.

### Inventory Size Attributes Integration

The hotbar in the HUD can be configured to only show enabled hot bar slots.

### RPG Crafting Integration

A button that opens the Hand Crafting Screen can be added to the inventory screens. The 2x2 crafting grid has to be disabled.

### Backpack Attribute Integration

A button that opens the Backpack Screen can be added to the inventory screens. The 2x2 crafting grid has to be disabled.

### Advancement Locked Items

An item stack that has the "rpginventory:advancement_locked" component has one of 3 different 'status' modes, saved in the component and updated when opening the inventory screen.

The different modes are:
- 'not_unlocked'
- 'unlocked', this has the lowest priority. The item can only be equipped and used if the item is in this mode.
- 'locked', this has the highest priority

The "advancement_locked" component has 5 string fields:
- "unlock_advancement": the id of the 'unlock advancement'. If the player has this advancement unlocked, the status is changed to 'unlocked', if not the status is 'not_unlocked'. If this field is an empty string, the item can't be 'not_unlocked'.
- "lock_advancement": the id of the 'lock advancement'. If the player has this advancement unlocked, the status is 'locked'. If this field is an empty string, the item can't be 'locked'.

- "not_unlocked_tooltip_text": this string is optionally  displayed in the item tooltip, when the item status is 'not_unlocked'. This supports localization.
- "tooltip_text": this string is optionally  displayed in the item tooltip, when the item status is 'not_unlocked'. This supports localization.
- "locked_tooltip_text": this string is optionally displayed in the item tooltip, when the item status is 'not_unlocked'. This supports localization.

Inventory slots containing an item with the 'not_unlocked' or 'locked' status modes, can optionally display a slot overlay.

### Trinket Compatibility

Trinket slots are displayed on the RPG Inventory screen.

All trinket slots have a tooltip. It can be set in the lang file with this schema:

```json
{
	"slot.tooltip.<group_name>.<slot_name>": "Test Slot"
}
```

When the string is empty, no tooltip will be shown.
