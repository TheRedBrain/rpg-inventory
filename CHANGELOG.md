# 2.11.1

- fixed shift-clicking to unequip items in custom slots

# 2.11.0

RPG Inventory's feature set has grown significantly and the mod is also used as a dependency for other projects. I made the decision to change the default state of the mod to be as close to vanilla as possible. Config values of existing installations should not be affected.

## Additions

- added "enable_two_handed_items_restriction" server config option, false by default

## Changes

- changed several default config values
  - server config
    - "activate_rpg_inventory_screen" is now false by default
    - "always_allow_toggling_two_handed_stance" is now true by default
    - "are_hand_items_restricted_to_item_tags" is now false by default
  - client config
    - "show_item_tooltip_equipment_slots" is now false by default
    - "show_armor_bar" is now true by default
- removed the "always_show_all_hotbar_slots" and "is_hotbar_centered" client config options. Use the corresponding config options of the "Inventory Size Attributes" mod instead.

## Fixes

- fixed item cooldowns for swapping/sheathing hand items or toggling 2-handed stance

# 2.10.0

## Additions

- added status effects that are used by various RPG Inventory features. These effects were previously implemented by the "Various Status Effects" mod, but the reasons for the separation no longer apply. This should make the mod more user friendly and easier to maintain.

## Fixes

- fixed class_item slot

# 2.9.0

## Additions

- added "rpginventory:unusable_when_low_durability" data component
- added "rpginventory:exclusive_equipment" data component, which is the more general replacement of the "unique rings system"
- added server config option to disable the RPG Inventory, this disables all custom inventory slots, including the custom hand slots (items equipped in those slots are moved into the inventory/dropped on the ground)
  - this comes with an alternative screen for the mannequin, that only shows the vanilla equipment slots
- added configurable item cooldowns to hand slot actions (swapping, sheathing, toggling two-handed stance)
- added integration with Overhauled Damage (PVP deaths now reset effect build-ups)

## Changes

- simplified "rpginventory:load_out_item" data component (is no longer a special case variant of the "rpginventory:is_kept_on_death" data component)

## Fixes

- items equipped in alternative hand slots are now moved into the inventory/dropped on the ground when alternative hand slots are disabled
- fixed quick moving (shift-clicking) items in the RPG Inventory Screen

## Technical

- changed empty slot tooltips to use the implementation from Slot Customization API
- improved several mixins to hopefully improve compatibility with Sinytra Connector

# 2.8.0

## Additions

- added "PVP Death" system, refer to the readme for a detailed explanation
- populated "relics" item tag
- added "canEquip" and "canChangeInventory" flags to Mannequin Block
- added server config options to enable/disable mannequin slots
- added server config options to enable/disable equipment slot interactions
- added new data components:
  - "rpginventory:ignores_equipment_change_restrictions"
  - "rpginventory:is_destroyed_on_death"
  - "rpginventory:is_kept_on_death"
- added "class_item" slot, can't be interacted with directly (designed to be used by class selection mods)

## Fixes

- fixed Better Combat Extension compat
- fixed Spell Engine Extension integration
- (probably) fixed a glitch where Empty Hand Weapons could enter the regular inventory

# 2.7.0

- added Numismatic Overhaul Integration
- added item slot overlay for loadout items
- fixed all known issues with the Better Combat X Hand Slot Overhaul compatibility (also requires updating Better Combat Extension)
- fixed Combat Roll integration

# 2.6.0

- added relic slot
- reworked "default_spell_slot_amount" server config option and adjusted the "generic.active_spell_slot_amount" entity attribute
- reworked game rules into server config settings
- tweaked several item tags
- tweaked default positions of several equipment slots
- ring 2 slot is now disabled by default
- refactored advancement locked items
- refactored custom item slot overlays
- refactored custom item tooltip lines
- updated sheathed_hand_item_positions default client config setting
- hand slot actions (hand swapping/hand sheathing/2-handed-stance toggling) can no longer be done in certain situations. This currently checks for:
  - Spell Engine: spell casting
  - Spell Engine Extension: after casting movement lock
  - Better Combat: attack swing
  - Combat Roll: active roll
  - Minecraft: items on cooldown
- fixed an issue where items could bind themselves to a player when they were already bound to another player

# 2.5.0

- added 'advancement locked' item component. Item stacks with this component can only be equipped and used by players who have unlocked the advancement defined by the component.
- added 'requires_stamina_cost' options for hand swapping/sheathing and toggling 2-handed stance
- scrolling to another hotbar slot can now sheathe hand items. This can be toggled in the client config
- added separate stamina costs for swapping main and offhand
- added hotkey for swapping both hand items (costing the sum of the sheathe costs * a configurable multiplier)
- added server config option to disable alternative hand slots
- improved 'disabled hand slot overhaul' feedback message to account for the feature being disabled when 'Better Combat' is present but 'Better Combat Extension' is not
- improved optional mod compatibilities
- improved configurability for item tooltips
- several internal refactors to improve maintainability
- fixed 'hand slot overhaul' not getting properly disabled when 'Better Combat' is present but 'Better Combat Extension' is not
- removed optional dependency on 'Food Overhaul', what status effects are listed in the Food list on the effect screen is now controlled by the "rpginventory:food_effects" effect tag

# 2.4.5

- fixed RPG Inventory screen Trinkets compat

# 2.4.4

- fixed RPG Inventory screen Trinkets compat

# 2.4.3

- fixed Trinkets still being required (again)

# 2.4.2

- fixed Trinkets still being required

# 2.4.1

- reworked check for when the two-handed item status effect should be applied. It is now independent of the hand slot overhaul and simply checks if the offhand slot is empty
- fixed offhand slot getting disabled when hand slot overhaul is disabled
- added server config options to disable custom equipment slots

# 2.4.0

**Important!**\
This release has a number of breaking changes. Remove items from player inventories and make back-ups of your worlds before updating!

This update removes all built-in Trinket slots. The additional slots are now a custom implementation, that is based on the vanilla equipment slots. This means that Trinkets is no longer a dependency (but it is still compatible).\
This has (among others) the following consequences:
- players on existing worlds should remove all items from their inventory **BEFORE** updating to this version
- enchantments can now directly target specific custom equipment slots
- the custom equipment slots make use of vanilla components for stuff like attribute modifiers, this unfortunately means that Trinket items might need a compatibility patch
- the custom equipment slots are registered with Spell Engine APIs for spell container source and spell ammo supply

The 'hand slot overhaul' now requires 'Better Combat Extension', which implements a fix for an item duplication glitch.\
If 'Better Combat' is not installed, this restriction does not apply.

Added first iteration of Mannequins, basically equipment load outs for players\
The inventory of these blocks can hold a complete set of equipment. Players can interact with a mannequin to equip a copy of those items. The mannequin keeps the original. This comes with a catch, tho. Items equipped via a mannequin can not be unequipped manually (only by interacting with a mannequin). These items will also not drop on death. Instead, they either vanish or are kept in the slot.

Additional Changes:
- empty hand weapons no longer drop on death and can no longer be used for casting Spell Engine spells

# 2.3.0

Important!
This release has a number of breaking changes. Remove items from player inventories and make back-ups of your worlds before updating!

- Hand slots (including the empty, sheathed and alternative variants) are no longer Trinket slots. This fixes a number of issues, like items in alternative hand slots granting their attributes.
- The drawback is, that any items on the old slots will be gone, so remove all items from your hand slots, before updating the mod!


- the "Hand slot overhaul" can now be completely disabled. This includes the main hand slot, the sheathing mechanic, the alternative hand slots and the empty hand weapons.
- the vanilla "swap hands mechanic" will only be disabled when the "hand slot overhaul" is active


- added compatibility with "RPG Crafting", the handcrafting screen can now be opened with a button on the inventory screen. (needs to be enabled in the config)
- items can now be excluded from being shown on the player when they are in a sheathed hand slot. This is powered by item tags.
- trinket slots with special placement are now config-driven
- items can now have an "owner", preventing other players from equipping or using the item. This mechanic is powered by data components. Thanks Galysso for the suggestion!
- items can now display the player who crafted it, in a tooltip. This mechanic is powered by data components.
- renamed "rpginventory:keeps_inventory_on_death" item tag to "rpginventory:sacrificed_to_keep_inventory_on_death", to better represent its functionality
- disabled hotbar slots can now be hidden
- removed dependency on Cloth Config
- added dependency on Fzzy Config


- A lot of small improvements and bug fixes. The mod now brings by default much more compatibility with other mods and allows for much more control via config options. If you are interested in the details, check the [changelog](https://github.com/TheRedBrain/rpg-inventory/blob/1.21.1/CHANGELOG.md) for the pre-releases.

# 2.3.0 Pre-Releases

Important!
This release has a number of breaking changes. Remove items from player inventories and make back-ups of your worlds before updating!

- Hand slots (including the empty, sheathed and alternative variants) are no longer Trinket slots. This fixes a number of issues, like items in alternative hand slots granting their attributes.
- The drawback is, that any items on the old slots will be gone, so remove all items from your hand slots, before updating the mod!


- the "Hand slot overhaul" can now be completely disabled. This includes the main hand slot, the sheathing mechanic, the alternative hand slots and the empty hand weapons.
- the vanilla "swap hands mechanic" will only be disabled when the "hand slot overhaul" is active


- added compatibility with "RPG Crafting", the handcrafting screen can now be opened with a button on the inventory screen. (needs to be enabled in the config)
- items can now be excluded from being shown on the player when they are in a sheathed hand slot. This is powered by item tags.
- trinket slots with special placement are now config-driven
- items can now have an "owner", preventing other players from equipping or using the item. This mechanic is powered by data components. Thanks Galysso for the suggestion!
- items can now display the player who crafted it, in a tooltip. This mechanic is powered by data components.
- disabled hotbar slots can now be hidden
- removed dependency on Cloth Config
- added dependency on Fzzy Config

Changes since Pre-Release 1
- added optional slot overlay for not owned items
- renamed "rpginventory:keeps_inventory_on_death" item tag to "rpginventory:sacrificed_to_keep_inventory_on_death", to better represent its functionality
- client and server config screens are more organized and have proper localization support
- fixed an issue where trinket slots added by RPG Inventory would not show up in tooltips
- integrated the latest "Inventory Size Attributes" version
- item bounding can now be disabled for creative mode players
- player names in the "Bound to..." and "Crafted by..." tooltips can now be formatted using a config option
- added daggers from "Rogues" to "rpginventory:offhand_items" item tag
- fixed label of crafting button

Changes since Pre-Release 2
- the "default_spell_slot_amount" server config option now actually effects the amount of spell slots
- improved descriptions of some config options
- improved sheathed item positions (Thanks @ Galysso for the contribution)
- added sheathed item positions for more items
- revamped item tags with modern collection tags. This should make many mods compatible by default

Changes since Pre-Release 3
- fixed quick move in survival inventory not always working
- fixed tools don't applying their digging speed bonus
- fixed desync issues with the pick block and drop item hotkeys
- fixed an issue where the unsheathed hand item would be an empty stack instead of the empty_hand_weapon item

Changes since Pre-Release 4
- fixed empty hand weapons getting equipped in normal hand slots

Changes since Pre-Release 5
- fixed claymore tag references
- fixed incompatibility with Scorchful

# 2.2.0

- removed feature: Attribute Screen, this feature was exported into a standalone mod called "Player Attribute Screen". The "attribute_screen_configuration" config value should work in the new mods config file.
- added compatibility with "Player Attribute Screen"
- added server config option to set default spell slot amount
- added vanilla axes and items from the "More RPG Classes" mods by @Fichte to several item tags

# 2.1.0

- update to 1.21.1
- client config is now much more organized and better documented
- added client option to hide empty hand slots in the HUD
- added client option to hide the status effect screen
- added compatibility with "Backpack Attribute"
- added compatibility with "Inventory Size Attributes"
- fixed several issues with the status effect screen
- prepared several features that are coming in the future

# 2.0.1

- fixed an issue where the client would crash when shift-clicking in the RPG inventory, on some items, while hand items where sheathed

# 2.0.0

Update to 1.21

# 1.9.0

- improved quick moving (shift-clicking) items in the RPG inventory screen
- refactored "main_hand" slots to "hand" slots. This should improve compatibility with other mods.
- fixed an issue where trinket slots from other mods where not registered correctly

# 1.8.0

- the "unusable items" feature is now properly implemented
- added various tooltips to items/inventory slots. This is customizable via the client config
- fixed an issue where connecting to a dedicated server would fail (for real this time!)

# 1.7.1

- fixed an issue where connecting to a dedicated server would fail

# 1.7.0

- added unique rings functionality (any item in the rpginventory:unique_rings item tag can only be equipped in one ring slot at a time)
- position/orientation of sheathed items is now configurable
- fixed an issue where swapping/sheathing hands would sometimes duplicate/delete items

# 1.6.0

- sheathing or swapping a hand item now plays a sound
- added HUD indicator for hand slots that are not sheathed
- The indicator for the selected hotbar slot is now only visible when the main hand is sheathed. This can be disabled in the client config.
- added server config option to allow hand slots to hold every item
- changed toggling two-handed stance to no longer fail when both hands are sheathed. The old functionality can be re-enabled in the server config.

# 1.5.3

- fixed an issue where items in the offhand were deleted when sheathing or swapping the offhand item

# 1.5.2

- fixed crash on start up

# 1.5.1

- fixed an issue with the weapon_attribute file for the default_empty_hand_weapon

# 1.5.0

RPG Inventory now supports all features of the Trinkets mod, no more restrictions!

- switched positions of the effects screen and the attributes screen
- refactored slots and slot groups to 'offhand' instead of 'off_hand'
- refactored some server config values to be more descriptive

# 1.4.0

- interactions with the offhand slot are now restricted like for all other slots
- positions of the equipment slots are now controlled by the server config
- added support for additional trinket slots

# 1.3.0

- AzureLib is no longer a required dependency
- API for rendering equipped trinkets on the player model was removed. This feature will be implemented in an addon for RPG Inventory instead.

# 1.2.2

- Stamina Attributes is no longer a required dependency
- disabled the functionality of vanillas 'Swap Item With Offhand' hotkey. Using it could lead to item duplication.

# 1.2.1

- custom belt/gloves/necklace/rings slots by default now also accept all items configured for the corresponding default Trinket slots
- spell slots now accept spell books from all mods
- added missing translation keys for keybindings
- changed default keybindings to no longer conflict with vanilla keybindings

# 1.2.0

- added a toggleable attribute screen which shows a configurable list of attributes
- added compatibility with Food Overhaul, food effects are displayed separately to other effects
- status effect description are now only displayed, when the translation key has a value assigned to it
- added client config option to hide the armor bar
- changed active spell slots amount to 1 by default

# 1.1.0

- added default config values for status effects provided by 'Various Status Effects'
- added mod compatibilities via item tags for 'Better Combat' and 'Better Combat Extension'
- added new gamerule 'destroyDroppedItemsOnDeath', false by default and has no effect when gamerule 'keepInventory' is true
- added config option for a new status effect identifier, when valid and applied to a player, acts like the 'keepInventory' gamerule

# 1.0.0

First release!

#