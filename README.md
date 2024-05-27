# RPG Inventory

Adds new equipment slots powered by the Trinkets mod. These slots are integrated into a new inventory screen.

## New equipment slots
The new slots accessible in the inventory are:
- 1 belt slot
- 1 necklace slot
- 2 ring slots
- 1 gloves slot
- 1 shoulders slot
- 8 spell slots, by default 2 of them can be accessed\
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
- Toggle Two-handing Stance: Puts the items in the main hand slot into the sheathed main hand slot. When pressed again, swaps the item back
When the main hand item is sheathed, the main hand slot contains the item in the selected hotbar slot, like in vanilla.
When the offhand item is sheathed, the offhand slot contains an empty item stack. On its own this is not very useful, it's designed to be used in combination with other mods like [Better Combat Extension](https://modrinth.com/mod/bettercombat-extension).

When a hand item is not sheathed, but the corresponding slot contains no item, the players hand is not empty. The item in the corresponding empty hand slot is held instead.
The empty hand slots always contain a item called "Empty Hand Weapon". This is technically a weapon. When Better Combat is installed, this allows for unarmed combat.

## Additional settings and features
Swapping hand items, sheathing weapons and Toggling two-handing stance can all be configured to require stamina above 0. They also cost a configurable amount of stamina.

The 2x2 crafting grid in the player inventory can be disabled.

Items in the "two_handed_items" item tag can only be used when the offhand is sheathed.

When the item in the main hand is in the "non_two_handed_items" item tag and "needs_two_handing_status_effect_identifier" is a valid status effect identifier, that status effect is applied.

When "allow_attacking_with_non_attack_items" is set to false, only items in the "attack_items" item tag can be used to attack and to break blocks.

When "building_mode_status_effect_identifier" is a valid status effect identifier and the player has that status effect, several mechanics are ignored.
- every item can be used to attack and to break blocks
- both main and offhand behave like they are sheathed, so the main hand slot contains the item in the selected hotbar slot, like in vanilla.

This mod also provides an API for trinket items which are rendered on the player model.