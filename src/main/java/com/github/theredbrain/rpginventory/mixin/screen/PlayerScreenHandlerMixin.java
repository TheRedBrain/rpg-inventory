package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.screen.DuckPlayerScreenHandlerMixin;
import com.github.theredbrain.rpginventory.screen.DuckSlotMixin;
import com.github.theredbrain.rpginventory.screen.slot.AdditionalEquipmentSlot;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(value = PlayerScreenHandler.class, priority = 1050)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler implements DuckPlayerScreenHandlerMixin {
	@Unique
	private static final Identifier EMPTY_HAND_SLOT = Identifier.ofVanilla("item/empty_slot_sword");
	@Unique
	private static final Identifier EMPTY_BELT_SLOT = RPGInventory.identifier("item/empty_slot_belt");
	@Unique
	private static final Identifier EMPTY_GLOVES_SLOT = RPGInventory.identifier("item/empty_slot_gloves");
	@Unique
	private static final Identifier EMPTY_NECKLACE_SLOT = RPGInventory.identifier("item/empty_slot_necklace");
	@Unique
	private static final Identifier EMPTY_RING_1_SLOT = RPGInventory.identifier("item/empty_slot_ring_1");
	@Unique
	private static final Identifier EMPTY_RING_2_SLOT = RPGInventory.identifier("item/empty_slot_ring_2");
	@Unique
	private static final Identifier EMPTY_SHOULDERS_SLOT = RPGInventory.identifier("item/empty_slot_shoulders");
	@Unique
	private static final Identifier EMPTY_SPELL_1_SLOT = RPGInventory.identifier("item/empty_slot_spell_1");
	@Unique
	private static final Identifier EMPTY_SPELL_2_SLOT = RPGInventory.identifier("item/empty_slot_spell_2");
	@Unique
	private static final Identifier EMPTY_SPELL_3_SLOT = RPGInventory.identifier("item/empty_slot_spell_3");
	@Unique
	private static final Identifier EMPTY_SPELL_4_SLOT = RPGInventory.identifier("item/empty_slot_spell_4");
	@Unique
	private static final Identifier EMPTY_SPELL_5_SLOT = RPGInventory.identifier("item/empty_slot_spell_5");
	@Unique
	private static final Identifier EMPTY_SPELL_6_SLOT = RPGInventory.identifier("item/empty_slot_spell_6");
	@Unique
	private static final Identifier EMPTY_SPELL_7_SLOT = RPGInventory.identifier("item/empty_slot_spell_7");
	@Unique
	private static final Identifier EMPTY_SPELL_8_SLOT = RPGInventory.identifier("item/empty_slot_spell_8");

//	@Shadow
//	@Final
//	private PlayerEntity owner;
//
//	@Unique
//	private final Map<SlotGroup, Integer> groupNums = new HashMap<>();
//	@Unique
//	private final Map<SlotGroup, Point> groupPos = new HashMap<>();
//	@Unique
//	private final Map<SlotGroup, List<Point>> slotHeights = new HashMap<>();
//	@Unique
//	private final Map<SlotGroup, List<SlotType>> slotTypes = new HashMap<>();
//	@Unique
//	private final Map<SlotGroup, Integer> slotWidths = new HashMap<>();
//	@Unique
//	private int trinketSlotStart = 0;
//	@Unique
//	private int trinketSlotEnd = 0;
//	@Unique
//	private int groupCount = 0;
//	@Unique
//	private PlayerInventory inventory;

	@Unique
	private boolean isAttributeScreenVisible = false;

	public PlayerScreenHandlerMixin() {
		super(null, 0);
	}

	/**
	 * @author TheRedBrain
	 */
	@Inject(method = "<init>", at = @At("TAIL"))
	public void PlayerScreenHandler(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
//		this.inventory = inventory;

		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				((SlotCustomization) this.slots.get(j + (i + 1) * 9)).slotcustomizationapi$setY(138 + i * 18);
			}
		}
		for (int i = 0; i < 9; ++i) {
			((SlotCustomization) this.slots.get(i + 36)).slotcustomizationapi$setY(196);
		}

		if (serverConfig.inventorySlots.disable_inventory_crafting_slots.get()) {
			((SlotCustomization) this.slots.get(0)).slotcustomizationapi$setDisabledOverride(true);
			((SlotCustomization) this.slots.get(1)).slotcustomizationapi$setDisabledOverride(true);
			((SlotCustomization) this.slots.get(2)).slotcustomizationapi$setDisabledOverride(true);
			((SlotCustomization) this.slots.get(3)).slotcustomizationapi$setDisabledOverride(true);
			((SlotCustomization) this.slots.get(4)).slotcustomizationapi$setDisabledOverride(true);
		} else {
			((SlotCustomization) this.slots.get(0)).slotcustomizationapi$setX(serverConfig.inventorySlots.inventory_crafting_slots_x_offset.get() + 56);
			((SlotCustomization) this.slots.get(0)).slotcustomizationapi$setY(serverConfig.inventorySlots.inventory_crafting_slots_y_offset.get() + 10);
			((SlotCustomization) this.slots.get(1)).slotcustomizationapi$setX(serverConfig.inventorySlots.inventory_crafting_slots_x_offset.get());
			((SlotCustomization) this.slots.get(1)).slotcustomizationapi$setY(serverConfig.inventorySlots.inventory_crafting_slots_y_offset.get());
			((SlotCustomization) this.slots.get(2)).slotcustomizationapi$setX(serverConfig.inventorySlots.inventory_crafting_slots_x_offset.get() + 18);
			((SlotCustomization) this.slots.get(2)).slotcustomizationapi$setY(serverConfig.inventorySlots.inventory_crafting_slots_y_offset.get());
			((SlotCustomization) this.slots.get(3)).slotcustomizationapi$setX(serverConfig.inventorySlots.inventory_crafting_slots_x_offset.get());
			((SlotCustomization) this.slots.get(3)).slotcustomizationapi$setY(serverConfig.inventorySlots.inventory_crafting_slots_y_offset.get() + 18);
			((SlotCustomization) this.slots.get(4)).slotcustomizationapi$setX(serverConfig.inventorySlots.inventory_crafting_slots_x_offset.get() + 18);
			((SlotCustomization) this.slots.get(4)).slotcustomizationapi$setY(serverConfig.inventorySlots.inventory_crafting_slots_y_offset.get() + 18);
		}

		// reposition vanilla armor slots
		// head
		((SlotCustomization) this.slots.get(5)).slotcustomizationapi$setX(serverConfig.inventorySlots.head_slot_x_offset.get());
		((SlotCustomization) this.slots.get(5)).slotcustomizationapi$setY(serverConfig.inventorySlots.head_slot_y_offset.get());
		// chest
		((SlotCustomization) this.slots.get(6)).slotcustomizationapi$setX(serverConfig.inventorySlots.chest_slot_x_offset.get());
		((SlotCustomization) this.slots.get(6)).slotcustomizationapi$setY(serverConfig.inventorySlots.chest_slot_y_offset.get());
		// legs
		((SlotCustomization) this.slots.get(7)).slotcustomizationapi$setX(serverConfig.inventorySlots.legs_slot_x_offset.get());
		((SlotCustomization) this.slots.get(7)).slotcustomizationapi$setY(serverConfig.inventorySlots.legs_slot_y_offset.get());
		// feet
		((SlotCustomization) this.slots.get(8)).slotcustomizationapi$setX(serverConfig.inventorySlots.feet_slot_x_offset.get());
		((SlotCustomization) this.slots.get(8)).slotcustomizationapi$setY(serverConfig.inventorySlots.feet_slot_y_offset.get());

		// reposition vanilla offhand slot
		((SlotCustomization) this.slots.get(45)).slotcustomizationapi$setX(serverConfig.inventorySlots.offhand_slot_x_offset.get());
		((SlotCustomization) this.slots.get(45)).slotcustomizationapi$setY(serverConfig.inventorySlots.offhand_slot_y_offset.get());

		// main hand slot
		this.addSlot(new Slot(inventory, 41, serverConfig.inventorySlots.hand_slot_x_offset.get(), serverConfig.inventorySlots.hand_slot_y_offset.get()) {
			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (owner.getServer() != null) {
					bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());

				return (stack.isIn(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isOwnedByPlayer(stack, owner.getGameProfile()) && (hasCivilisationEffect || owner.isCreative() || (bl && !hasWildernessEffect)) && !((DuckPlayerEntityMixin) owner).rpginventory$isHandStackSheathed();
			}

			@Override
			public boolean isEnabled() {
				return RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get() && !((DuckPlayerEntityMixin) owner).rpginventory$isHandStackSheathed();
			}

			@Override
			public Pair<Identifier, Identifier> getBackgroundSprite() {
				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandlerMixin.EMPTY_HAND_SLOT);
			}
		});

		// sheathed main hand slot
		this.addSlot(new Slot(inventory, 42, serverConfig.inventorySlots.hand_slot_x_offset.get(), serverConfig.inventorySlots.hand_slot_y_offset.get()) {
			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (owner.getServer() != null) {
					bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());

				return (stack.isIn(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isOwnedByPlayer(stack, owner.getGameProfile()) && (hasCivilisationEffect || owner.isCreative() || (bl && !hasWildernessEffect)) && ((DuckPlayerEntityMixin) owner).rpginventory$isHandStackSheathed();
			}

			@Override
			public boolean isEnabled() {
				return RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get() && ((DuckPlayerEntityMixin) owner).rpginventory$isHandStackSheathed();
			}

			@Override
			public Pair<Identifier, Identifier> getBackgroundSprite() {
				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandlerMixin.EMPTY_HAND_SLOT);
			}
		});

		// sheathed offhand slot
		this.addSlot(new Slot(inventory, 43, serverConfig.inventorySlots.offhand_slot_x_offset.get(), serverConfig.inventorySlots.offhand_slot_y_offset.get()) {
			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (owner.getServer() != null) {
					bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());

				return (stack.isIn(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isOwnedByPlayer(stack, owner.getGameProfile()) && (hasCivilisationEffect || owner.isCreative() || (bl && !hasWildernessEffect)) && ((DuckPlayerEntityMixin) owner).rpginventory$isOffhandStackSheathed();
			}

			@Override
			public boolean isEnabled() {
				return RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get() && ((DuckPlayerEntityMixin) owner).rpginventory$isOffhandStackSheathed();
			}

			@Override
			public Pair<Identifier, Identifier> getBackgroundSprite() {
				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT);
			}
		});

		// alternative main hand slot
		this.addSlot(new Slot(inventory, 46, serverConfig.inventorySlots.alternative_hand_slot_x_offset.get(), serverConfig.inventorySlots.alternative_hand_slot_y_offset.get()) {
			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (owner.getServer() != null) {
					bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());

				return (stack.isIn(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isOwnedByPlayer(stack, owner.getGameProfile()) && (hasCivilisationEffect || owner.isCreative() || (bl && !hasWildernessEffect));
			}

			@Override
			public boolean isEnabled() {
				return RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get();
			}

			@Override
			public Pair<Identifier, Identifier> getBackgroundSprite() {
				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandlerMixin.EMPTY_HAND_SLOT);
			}
		});

		// alternative offhand slot
		this.addSlot(new Slot(inventory, 47, serverConfig.inventorySlots.alternative_offhand_slot_x_offset.get(), serverConfig.inventorySlots.alternative_offhand_slot_y_offset.get()) {
			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (owner.getServer() != null) {
					bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());

				return (stack.isIn(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isOwnedByPlayer(stack, owner.getGameProfile()) && (hasCivilisationEffect || owner.isCreative() || (bl && !hasWildernessEffect));
			}

			@Override
			public boolean isEnabled() {
				return RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get();
			}

			@Override
			public Pair<Identifier, Identifier> getBackgroundSprite() {
				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT);
			}
		});

		// belt slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 48, owner, ExtendedEquipmentSlot.BELT, Tags.BELTS, serverConfig.inventorySlots.belt_slot_x_offset.get(), serverConfig.inventorySlots.belt_slot_y_offset.get(), EMPTY_BELT_SLOT, List.of(Text.translatable("slot.tooltip.belt"))));

		// gloves slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 49, owner, ExtendedEquipmentSlot.GLOVES, Tags.GLOVES, serverConfig.inventorySlots.gloves_slot_x_offset.get(), serverConfig.inventorySlots.gloves_slot_y_offset.get(), EMPTY_GLOVES_SLOT, List.of(Text.translatable("slot.tooltip.gloves"))));

		// necklace slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 50, owner, ExtendedEquipmentSlot.NECKLACE, Tags.NECKLACES, serverConfig.inventorySlots.necklace_slot_x_offset.get(), serverConfig.inventorySlots.necklace_slot_y_offset.get(), EMPTY_NECKLACE_SLOT, List.of(Text.translatable("slot.tooltip.necklace"))));

		// ring 1 slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 51, owner, ExtendedEquipmentSlot.RING_1, Tags.RINGS, serverConfig.inventorySlots.ring_1_slot_x_offset.get(), serverConfig.inventorySlots.ring_1_slot_y_offset.get(), EMPTY_RING_1_SLOT, List.of(Text.translatable("slot.tooltip.ring_1"))));

		// ring 2 slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 52, owner, ExtendedEquipmentSlot.RING_2, Tags.RINGS, serverConfig.inventorySlots.ring_2_slot_x_offset.get(), serverConfig.inventorySlots.ring_2_slot_y_offset.get(), EMPTY_RING_2_SLOT, List.of(Text.translatable("slot.tooltip.ring_2"))));

		// shoulders slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 53, owner, ExtendedEquipmentSlot.SHOULDERS, Tags.SHOULDERS, serverConfig.inventorySlots.shoulders_slot_x_offset.get(), serverConfig.inventorySlots.shoulders_slot_y_offset.get(), EMPTY_SHOULDERS_SLOT, List.of(Text.translatable("slot.tooltip.shoulders"))));

		// spell 1 slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 54, owner, ExtendedEquipmentSlot.SPELL_1, Tags.SPELLS, serverConfig.inventorySlots.spell_1_slot_x_offset.get(), serverConfig.inventorySlots.spell_1_slot_y_offset.get(), EMPTY_SPELL_1_SLOT, List.of(Text.translatable("slot.tooltip.spell_1"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 1;
			}

		});

		// spell 2 slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 55, owner, ExtendedEquipmentSlot.SPELL_2, Tags.SPELLS, serverConfig.inventorySlots.spell_2_slot_x_offset.get(), serverConfig.inventorySlots.spell_2_slot_y_offset.get(), EMPTY_SPELL_2_SLOT, List.of(Text.translatable("slot.tooltip.spell_2"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 2;
			}

		});

		// spell 3 slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 56, owner, ExtendedEquipmentSlot.SPELL_3, Tags.SPELLS, serverConfig.inventorySlots.spell_3_slot_x_offset.get(), serverConfig.inventorySlots.spell_3_slot_y_offset.get(), EMPTY_SPELL_3_SLOT, List.of(Text.translatable("slot.tooltip.spell_3"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 3;
			}

		});

		// spell 4 slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 57, owner, ExtendedEquipmentSlot.SPELL_4, Tags.SPELLS, serverConfig.inventorySlots.spell_4_slot_x_offset.get(), serverConfig.inventorySlots.spell_4_slot_y_offset.get(), EMPTY_SPELL_4_SLOT, List.of(Text.translatable("slot.tooltip.spell_4"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 4;
			}

		});

		// spell 5 slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 58, owner, ExtendedEquipmentSlot.SPELL_5, Tags.SPELLS, serverConfig.inventorySlots.spell_5_slot_x_offset.get(), serverConfig.inventorySlots.spell_5_slot_y_offset.get(), EMPTY_SPELL_5_SLOT, List.of(Text.translatable("slot.tooltip.spell_5"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 5;
			}

		});

		// spell 6 slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 59, owner, ExtendedEquipmentSlot.SPELL_6, Tags.SPELLS, serverConfig.inventorySlots.spell_6_slot_x_offset.get(), serverConfig.inventorySlots.spell_6_slot_y_offset.get(), EMPTY_SPELL_6_SLOT, List.of(Text.translatable("slot.tooltip.spell_6"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 6;
			}

		});

		// spell 7 slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 60, owner, ExtendedEquipmentSlot.SPELL_7, Tags.SPELLS, serverConfig.inventorySlots.spell_7_slot_x_offset.get(), serverConfig.inventorySlots.spell_7_slot_y_offset.get(), EMPTY_SPELL_7_SLOT, List.of(Text.translatable("slot.tooltip.spell_7"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 7;
			}

		});

		// spell 8 slot
		this.addSlot(new AdditionalEquipmentSlot(inventory, 61, owner, ExtendedEquipmentSlot.SPELL_8, Tags.SPELLS, serverConfig.inventorySlots.spell_8_slot_x_offset.get(), serverConfig.inventorySlots.spell_8_slot_y_offset.get(), EMPTY_SPELL_8_SLOT, List.of(Text.translatable("slot.tooltip.spell_8"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 8;
			}

		});

		// adding slot tooltips
		List<Text> list5 = new ArrayList<>();
		list5.add(Text.translatable("slot.tooltip.head"));
		((DuckSlotMixin) this.slots.get(5)).rpginventory$setSlotTooltipText(list5);

		List<Text> list6 = new ArrayList<>();
		list6.add(Text.translatable("slot.tooltip.chest"));
		((DuckSlotMixin) this.slots.get(6)).rpginventory$setSlotTooltipText(list6);

		List<Text> list7 = new ArrayList<>();
		list7.add(Text.translatable("slot.tooltip.legs"));
		((DuckSlotMixin) this.slots.get(7)).rpginventory$setSlotTooltipText(list7);

		List<Text> list8 = new ArrayList<>();
		list8.add(Text.translatable("slot.tooltip.feet"));
		((DuckSlotMixin) this.slots.get(8)).rpginventory$setSlotTooltipText(list8);

		List<Text> list45 = new ArrayList<>();
		list45.add(Text.translatable("slot.tooltip.offhand"));
		((DuckSlotMixin) this.slots.get(45)).rpginventory$setSlotTooltipText(list45);

		List<Text> list46 = new ArrayList<>();
		list46.add(Text.translatable("slot.tooltip.hand"));
		((DuckSlotMixin) this.slots.get(46)).rpginventory$setSlotTooltipText(list46);

		List<Text> list47 = new ArrayList<>();
		list47.add(Text.translatable("slot.tooltip.hand"));
		((DuckSlotMixin) this.slots.get(47)).rpginventory$setSlotTooltipText(list47);

		List<Text> list48 = new ArrayList<>();
		list48.add(Text.translatable("slot.tooltip.offhand"));
		((DuckSlotMixin) this.slots.get(48)).rpginventory$setSlotTooltipText(list48);

		List<Text> list49 = new ArrayList<>();
		list49.add(Text.translatable("slot.tooltip.alternative_hand"));
		((DuckSlotMixin) this.slots.get(49)).rpginventory$setSlotTooltipText(list49);

		List<Text> list50 = new ArrayList<>();
		list50.add(Text.translatable("slot.tooltip.alternative_offhand"));
		((DuckSlotMixin) this.slots.get(50)).rpginventory$setSlotTooltipText(list50);

//		trinkets$updateTrinketSlots(true);
	}

//	/**
//	 * Modified and expanded code by @Emi
//	 */
//	@Override
//	public void trinkets$updateTrinketSlots(boolean slotsChanged) {
//
//		TrinketsApi.getTrinketComponent(owner).ifPresent(trinkets -> {
//			if (slotsChanged) trinkets.update();
//			Map<String, SlotGroup> groups = trinkets.getGroups();
//			groupPos.clear();
//			while (trinketSlotStart < trinketSlotEnd) {
//				slots.remove(trinketSlotStart);
//				((ScreenHandlerAccessor) (this)).getTrackedStacks().remove(trinketSlotStart);
//				((ScreenHandlerAccessor) (this)).getPreviousTrackedStacks().remove(trinketSlotStart);
//				trinketSlotEnd--;
//			}
//
//			int groupNum = 0;
//			int extraGroupCount = 0;
//
//			for (SlotGroup group : groups.values().stream().sorted(Comparator.comparing(SlotGroup::getOrder)).toList()) {
//				if (!rpginventory$hasSlots(trinkets, group)) {
//					continue;
//				}
//				String groupName = group.getName();
//				int id = group.getSlotId();
//				if (id != -1) {
//					if (this.slots.size() > id) {
//						Slot slot = this.slots.get(id);
//						if (!(slot instanceof SurvivalTrinketSlot)) {
//							groupPos.put(group, new Point(slot.x, slot.y));
//							groupNums.put(group, -id);
//						}
//					}
//				} else {
//					int x;
//					int y;
//					ServerConfig.InventorySlots.SlotGroupPosition slotGroupPosition = RPGInventory.SERVER_CONFIG.inventorySlots.slot_group_positions.get(groupName);
//					if (slotGroupPosition != null) {
//						x = slotGroupPosition.survival_x;
//						y = slotGroupPosition.survival_y;
//					} else {
//						x = -14 - (extraGroupCount / 4) * 18;
//						y = 8 + (extraGroupCount % 4) * 18;
//						extraGroupCount++;
//					}
//					groupPos.put(group, new Point(x, y));
//					groupNums.put(group, groupNum);
//					groupNum++;
//				}
//			}
//			groupCount = extraGroupCount;
//			trinketSlotStart = slots.size();
//			slotWidths.clear();
//			slotHeights.clear();
//			slotTypes.clear();
//
//			for (Map.Entry<String, Map<String, TrinketInventory>> entry : trinkets.getInventory().entrySet()) {
//				String groupId = entry.getKey();
//				SlotGroup group = groups.get(groupId);
//				int groupOffset = 1;
//
//				if (group.getSlotId() != -1) {
//					groupOffset++;
//				}
//				int width = 0;
//				Point pos = trinkets$getGroupPos(group);
//				if (pos == null) {
//					continue;
//				}
//				for (Map.Entry<String, TrinketInventory> slot : entry.getValue().entrySet().stream().sorted((a, b) ->
//						Integer.compare(a.getValue().getSlotType().getOrder(), b.getValue().getSlotType().getOrder())).toList()) {
//					TrinketInventory stacks = slot.getValue();
//					if (stacks.size() == 0) {
//						continue;
//					}
//					int slotOffset = 1;
//					int x = (int) ((groupOffset / 2) * 18 * Math.pow(-1, groupOffset));
//					slotHeights.computeIfAbsent(group, (k) -> new ArrayList<>()).add(new Point(x, stacks.size()));
//					slotTypes.computeIfAbsent(group, (k) -> new ArrayList<>()).add(stacks.getSlotType());
//					for (int i = 0; i < stacks.size(); i++) {
//						int y = (int) (pos.y() + (slotOffset / 2) * 18 * Math.pow(-1, slotOffset));
//						this.addSlot(new SurvivalTrinketSlot(stacks, i, x + pos.x(), y, group, stacks.getSlotType(), i, groupOffset == 1 && i == 0));
//						slotOffset++;
//					}
//					groupOffset++;
//
//					width++;
//				}
//				slotWidths.put(group, width);
//			}
//
//			trinketSlotEnd = slots.size();
//		});
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@Unique
//	private boolean rpginventory$hasSlots(TrinketComponent comp, SlotGroup group) {
//		for (TrinketInventory inv : comp.getInventory().get(group.getName()).values()) {
//			if (inv.size() > 0) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@Override
//	public int trinkets$getGroupNum(SlotGroup group) {
//		return groupNums.getOrDefault(group, 0);
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@Nullable
//	@Override
//	public Point trinkets$getGroupPos(SlotGroup group) {
//		return groupPos.get(group);
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@NotNull
//	@Override
//	public List<Point> trinkets$getSlotHeights(SlotGroup group) {
//		return slotHeights.getOrDefault(group, ImmutableList.of());
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@Nullable
//	@Override
//	public Point trinkets$getSlotHeight(SlotGroup group, int i) {
//		List<Point> points = this.trinkets$getSlotHeights(group);
//		return i < points.size() ? points.get(i) : null;
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@NotNull
//	@Override
//	public List<SlotType> trinkets$getSlotTypes(SlotGroup group) {
//		return slotTypes.getOrDefault(group, ImmutableList.of());
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@Override
//	public int trinkets$getSlotWidth(SlotGroup group) {
//		return slotWidths.getOrDefault(group, 0);
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@Override
//	public int trinkets$getGroupCount() {
//		return groupCount;
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@Override
//	public int trinkets$getTrinketSlotStart() {
//		return trinketSlotStart;
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@Override
//	public int trinkets$getTrinketSlotEnd() {
//		return trinketSlotEnd;
//	}

	//	/**
//	 * @author Emi
//	 */
	@Inject(at = @At("HEAD"), method = "onClosed")
	private void rpginventory$onClosed(PlayerEntity player, CallbackInfo info) {
//		if (player.getWorld().isClient) {
//			TrinketsClient.activeGroup = null;
//			TrinketsClient.activeType = null;
//			TrinketsClient.quickMoveGroup = null;
//		}
		// TODO trigger adventure hotbar items check
	}

	/**
	 * Modified and expanded code by @Emi
	 */
	@Inject(at = @At("HEAD"), method = "quickMove", cancellable = true)
	private void rpginventory$quickMove(PlayerEntity player, int slot, CallbackInfoReturnable<ItemStack> cir) {
//		Slot slot1 = slots.get(slot);
//		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;
//
////		// TODO adventure hotbar items
////		StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.statusEffects.civilisation_status_effect_identifier));
////		boolean hasCivilisationEffect = civilisation_status_effect != null && player.hasStatusEffect(civilisation_status_effect);
////
////		StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.statusEffects.wilderness_status_effect_identifier));
////		boolean hasWildernessEffect = wilderness_status_effect != null && player.hasStatusEffect(wilderness_status_effect);
////
////		boolean canChangeEquipment = true;
////
////		if (player.getServer() != null) {
////			canChangeEquipment = player.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
////		}
////		hasCivilisationEffect = hasCivilisationEffect || (canChangeEquipment && !hasWildernessEffect);
//
//		if (slot1.hasStack()) {
//			ItemStack stack = slot1.getStack();
//			if (slot >= trinketSlotStart && slot < trinketSlotEnd) {
//				if (!this.insertItem(stack, 9, 45, false)) {   // TODO adventure hotbar items
//					cir.setReturnValue(ItemStack.EMPTY);
//					cir.cancel();
//				} else {
//					cir.setReturnValue(stack);
//					cir.cancel();
//				}
//			} else if (slot >= 9 && slot < 45) {
//				TrinketsApi.getTrinketComponent(player).ifPresent(trinkets -> {
//							for (int i = trinketSlotStart; i < trinketSlotEnd; i++) {
//								Slot s = slots.get(i);
//								if (!(s instanceof SurvivalTrinketSlot) || !s.canInsert(stack)) {
//									continue;
//								}
//
//								SurvivalTrinketSlot ts = (SurvivalTrinketSlot) s;
//								SlotType type = ts.getType();
//								SlotReference ref = new SlotReference((TrinketInventory) ts.inventory, ts.getIndex());
//
//								boolean res = TrinketsApi.evaluatePredicateSet(type.getQuickMovePredicates(), stack, ref, player);
//
//								if (res) {
//									if (this.insertItem(stack, i, i + 1, false)) {
//										World world = player.getWorld();
//										if (world.isClient) {
//											TrinketsClient.quickMoveTimer = 20;
//											TrinketsClient.quickMoveGroup = TrinketsApi.getPlayerSlots(this.owner).get(type.getGroup());
//											if (ref.index() > 0) {
//												TrinketsClient.quickMoveType = type;
//											} else {
//												TrinketsClient.quickMoveType = null;
//											}
//										}
//									}
//								}
//							}
//						}
//				);
//
//				if (serverConfig.handSlotOverhaul.enable_hand_slot_overhaul.get()) {
//					EquipmentSlot equipmentSlot = this.owner.getPreferredEquipmentSlot(stack);
//
//					if (!stack.isEmpty() && (!serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || stack.isIn(Tags.HAND_ITEMS))) {
//						if (((DuckPlayerEntityMixin) this.owner).rpginventory$isHandStackSheathed() && !this.slots.get(47).hasStack()) {
//							if (!this.insertItem(stack, 47, 48, false)) {
//								cir.setReturnValue(ItemStack.EMPTY);
//								cir.cancel();
//							}
//						} else if (!((DuckPlayerEntityMixin) this.owner).rpginventory$isHandStackSheathed() && !this.slots.get(46).hasStack()) {
//							if (!this.insertItem(stack, 46, 47, false)) {
//								cir.setReturnValue(ItemStack.EMPTY);
//								cir.cancel();
//							}
//						}
//					}
//
//					if (!stack.isEmpty() && (equipmentSlot == EquipmentSlot.OFFHAND || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || stack.isIn(Tags.OFFHAND_ITEMS))) {
//						if (((DuckPlayerEntityMixin) this.owner).rpginventory$isOffhandStackSheathed() && !this.slots.get(48).hasStack()) {
//							if (!this.insertItem(stack, 48, 49, false)) {
//								cir.setReturnValue(ItemStack.EMPTY);
//								cir.cancel();
//							}
//						} else if (!((DuckPlayerEntityMixin) this.owner).rpginventory$isOffhandStackSheathed() && !this.slots.get(45).hasStack()) {
//							if (!this.insertItem(stack, 45, 46, false)) {
//								cir.setReturnValue(ItemStack.EMPTY);
//								cir.cancel();
//							}
//						}
//					}
//
//					if (!stack.isEmpty() && (!serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || stack.isIn(Tags.HAND_ITEMS))) {
//						if (!this.slots.get(49).hasStack()) {
//							if (!this.insertItem(stack, 49, 50, false)) {
//								cir.setReturnValue(ItemStack.EMPTY);
//								cir.cancel();
//							}
//						}
//					}
//
//					if (!stack.isEmpty() && (equipmentSlot == EquipmentSlot.OFFHAND || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || stack.isIn(Tags.OFFHAND_ITEMS))) {
//						if (!this.slots.get(50).hasStack()) {
//							if (!this.insertItem(stack, 50, 51, false)) {
//								cir.setReturnValue(ItemStack.EMPTY);
//								cir.cancel();
//							}
//						}
//					}
//				}
//			} else if (slot >= 45 && slot < 51 && RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get()) {
//				if (!this.insertItem(stack, 9, 45, false)) {   // TODO adventure hotbar items
//					cir.setReturnValue(ItemStack.EMPTY);
//					cir.cancel();
//				} else {
//					cir.setReturnValue(stack);
//					cir.cancel();
//				}
//			}
//		}
	}

	@Override
	public boolean rpginventory$isAttributeScreenVisible() {
		return this.isAttributeScreenVisible;
	}

	@Override
	public void rpginventory$setIsAttributeScreenVisible(boolean isAttributeScreenVisible) {
		this.isAttributeScreenVisible = isAttributeScreenVisible;
	}
}
