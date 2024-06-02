# RPG Inventory

Adds new equipment slots powered by the [Trinkets](https://modrinth.com/mod/trinkets) mod. These slots are integrated into a new inventory screen.

## New equipment slots
The new slots accessible in the inventory are:
- 1 belt slot
- 1 necklace slot
- 2 ring slots
- 1 gloves slot
- 1 shoulders slot
- 8 spell slots, by default 1 of them can be accessed\
The new entity attribute "generic.active_spell_slot_amount" controls how many active spell slots the player has available.
These spell slots are intended to be used with spell books powered by Spell Engine, but they should work with other items as well.
- 1 main hand slot
- 1 alternative main hand slot
- 1 alternative offhand slot

The mod also adds additional slots which are not directly accessible. They are used in mechanics which are explained later.
- 1 sheathed main hand slot
- 1 sheathed offhand slot
- 1 empty main hand slot
- 1 empty offhand slot

## New Keybindings and mechanics
- Swap main hand: Swaps the item in the main hand slot with the item in the alternative main hand slot
- Swap offhand: Swaps the item in the offhand slot with the item in the alternative offhand slot\


- Sheathe Weapons: Puts the items in the main hand and the offhand slot into their corresponding sheathed hand slots. When pressed again, swaps the items back
- Toggle Two-handing Stance: Puts the items in the offhand slot into the sheathed offhand slot. When pressed again, swaps the item back. This is not possible, when the main hand item is in the "non_two_handed_items" item tag.
When the main hand item is sheathed, the main hand slot contains the item in the selected hotbar slot, like in vanilla.
When the offhand item is sheathed, the offhand slot contains an empty item stack. On its own this is not very useful, it's designed to be used in combination with other mods like [Better Combat Extension](https://modrinth.com/mod/bettercombat-extension).

When a hand item is not sheathed, but the corresponding slot contains no item, the players hand is not empty. The item in the corresponding empty hand slot is held instead.
The empty hand slots always contain a item called "Empty Hand Weapon". This is technically a weapon. When Better Combat is installed, this allows for unarmed combat.

## Status Effect screen
Active and visible status effects are listed on the left side of the inventory screen. They are sorted by their category (harmful beneficial and neutral). When the mod [Food Overhaul](https://modrinth.com/mod/food-overhaul) is installed, its food effects are listed in a separate list.
Effects can have a description (added by assigning a value to <effect_translation_key>.description in the lang files), which is also displayed.

## Attribute Screen
Attributes and their values are listed on the right side of the inventory. This can be toggled on/off with a button.
The client config allows extensive customization of the attribute screen.

## Additional settings and features
Swapping hand items, sheathing weapons and Toggling two-handing stance can all be configured to require stamina above 0. They also cost a configurable amount of stamina.

The 2x2 crafting grid in the player inventory can be disabled.

Items in the "two_handed_items" item tag can only be used when the offhand is sheathed.

When "needs_two_handing_status_effect_identifier" is a valid status effect identifier, that status effect is applied when the item in the main hand is in the "two_handed_items" item tag and the offhand is not sheathed.

When "no_attack_item_status_effect_identifier" is a valid status effect identifier, that status effect is applied when the item in the main hand is not in the 'attack_items' item tag and the 'allow_attacking_with_non_attack_items' option is set to false.

When "building_mode_status_effect_identifier" is a valid status effect identifier and the player has that status effect, several mechanics are ignored.
- every item can be used to attack and to break blocks
- both main and offhand behave like they are sheathed, so the main hand slot contains the item in the selected hotbar slot, like in vanilla.

The gamerule "canChangeEquipment" controls, whether items can be put into or removed from equipment slots.

When "civilisation_status_effect_identifier" is a valid status effect identifier and the player has that status effect items can be put into or removed from equipment slots, regardless of the gamerule "canChangeEquipment".

When "wilderness_status_effect_identifier" is a valid status effect identifier and the player has that status effect items can not be put into or removed from equipment slots, regardless of the gamerule "canChangeEquipment".

When the gamerule "destroyDroppedItemsOnDeath" is true and the vanilla gamerule "keepInventory" is false, the items in the players inventory are not dropped when they die. They are destroyed instead.

When "keep_inventory_status_effect_identifier" is a valid status effect identifier, that status effect is applied when an item in the "keeps_inventory_on_death" item tag is equipped (in an equipment, trinket or the offhand slot).
When the player dies while having that status effect, all equipped items in the "keeps_inventory_on_death" item tag are destroyed. The rest of the inventory is kept, regardless of game rules and stuff like "Curse of Vanishing".

This mod also provides an API for trinket items which are rendered on the player model.