package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.screen.DuckPlayerScreenHandlerMixin;
import com.github.theredbrain.rpginventory.screen.DuckSlotMixin;
import com.github.theredbrain.rpginventory.screen.slot.AlternativeHandSlot;
import com.github.theredbrain.rpginventory.screen.slot.CustomArmorSlot;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import net.minecraft.entity.EquipmentSlot;
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
	private static final Identifier EMPTY_HAND_SLOT = RPGInventory.identifier("item/empty_slot_hand");
	@Unique
	private static final Identifier EMPTY_ALTERNATIVE_HAND_SLOT = RPGInventory.identifier("item/empty_slot_alternative_hand");
	@Unique
	private static final Identifier EMPTY_ALTERNATIVE_OFFHAND_SLOT = RPGInventory.identifier("item/empty_slot_alternative_offhand");
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
	@Unique
	private static final Identifier EMPTY_RELIC_SLOT = RPGInventory.identifier("item/empty_slot_relic");

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

		// main hand slot 46
		this.addSlot(new CustomArmorSlot(inventory, owner, EquipmentSlot.MAINHAND, 41,  serverConfig.inventorySlots.hand_slot_x_offset.get(), serverConfig.inventorySlots.hand_slot_y_offset.get(), EMPTY_HAND_SLOT, List.of(Text.translatable("slot.tooltip.hand")), false) {

			@Override
			public boolean isEnabled() {
				return !((DuckPlayerEntityMixin) owner).rpginventory$isHandStackSheathed() && RPGInventory.isHandSlotOverhaulActive();
			}

			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (owner.getServer() != null) {
					bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());

				return (stack.isIn(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, owner) && (hasCivilisationEffect || owner.isCreative() || (bl && !hasWildernessEffect)) && !((DuckPlayerEntityMixin) owner).rpginventory$isHandStackSheathed();
			}

		});

		// sheathed main hand slot 47
		this.addSlot(new CustomArmorSlot(inventory, owner, EquipmentSlot.MAINHAND, 42,  serverConfig.inventorySlots.hand_slot_x_offset.get(), serverConfig.inventorySlots.hand_slot_y_offset.get(), EMPTY_HAND_SLOT, List.of(Text.translatable("slot.tooltip.hand")), false) {

			@Override
			public boolean isEnabled() {
				return ((DuckPlayerEntityMixin) owner).rpginventory$isHandStackSheathed() && RPGInventory.isHandSlotOverhaulActive();
			}

			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (owner.getServer() != null) {
					bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());

				return (stack.isIn(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, owner) && (hasCivilisationEffect || owner.isCreative() || (bl && !hasWildernessEffect)) && ((DuckPlayerEntityMixin) owner).rpginventory$isHandStackSheathed();
			}

		});

		// sheathed offhand slot 48
		this.addSlot(new CustomArmorSlot(inventory, owner, EquipmentSlot.OFFHAND, 43, serverConfig.inventorySlots.offhand_slot_x_offset.get(), serverConfig.inventorySlots.offhand_slot_y_offset.get(), PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT, List.of(Text.translatable("slot.tooltip.offhand")), false) {

			@Override
			public boolean isEnabled() {
				return ((DuckPlayerEntityMixin) owner).rpginventory$isOffhandStackSheathed() && RPGInventory.isHandSlotOverhaulActive();
			}

			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (owner.getServer() != null) {
					bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());

				return (EquipmentSlot.OFFHAND == owner.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, owner) && (hasCivilisationEffect || owner.isCreative() || (bl && !hasWildernessEffect)) && ((DuckPlayerEntityMixin) owner).rpginventory$isOffhandStackSheathed();
			}

		});

		// alternative main hand slot 49
		this.addSlot(new AlternativeHandSlot(inventory, owner, EquipmentSlot.MAINHAND, 46, serverConfig.inventorySlots.alternative_hand_slot_x_offset.get(), serverConfig.inventorySlots.alternative_hand_slot_y_offset.get(), EMPTY_ALTERNATIVE_HAND_SLOT, List.of(Text.translatable("slot.tooltip.alternative_hand")), false) {

			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (owner.getServer() != null) {
					bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());

				return (stack.isIn(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, owner) && (hasCivilisationEffect || owner.isCreative() || (bl && !hasWildernessEffect));
			}

			@Override
			public boolean isEnabled() {
				return RPGInventory.isHandSlotOverhaulActive() && serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get();
			}

		});

		// alternative offhand slot 50
		this.addSlot(new AlternativeHandSlot(inventory, owner, EquipmentSlot.OFFHAND, 47, serverConfig.inventorySlots.alternative_offhand_slot_x_offset.get(), serverConfig.inventorySlots.alternative_offhand_slot_y_offset.get(), EMPTY_ALTERNATIVE_OFFHAND_SLOT, List.of(Text.translatable("slot.tooltip.alternative_offhand")), false) {

			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (owner.getServer() != null) {
					bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());

				return (stack.isIn(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, owner) && (hasCivilisationEffect || owner.isCreative() || (bl && !hasWildernessEffect));
			}

			@Override
			public boolean isEnabled() {
				return RPGInventory.isHandSlotOverhaulActive() && serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get();
			}

		});

		// belt slot 51
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.BELT, 48, serverConfig.inventorySlots.belt_slot_x_offset.get(), serverConfig.inventorySlots.belt_slot_y_offset.get(), EMPTY_BELT_SLOT, List.of(Text.translatable("slot.tooltip.belt"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && serverConfig.inventorySlots.is_belt_slot_enabled.get();
			}

		});

		// gloves slot 52
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.GLOVES, 49, serverConfig.inventorySlots.gloves_slot_x_offset.get(), serverConfig.inventorySlots.gloves_slot_y_offset.get(), EMPTY_GLOVES_SLOT, List.of(Text.translatable("slot.tooltip.gloves"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && serverConfig.inventorySlots.is_gloves_slot_enabled.get();
			}

		});

		// necklace slot 53
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.NECKLACE, 50, serverConfig.inventorySlots.necklace_slot_x_offset.get(), serverConfig.inventorySlots.necklace_slot_y_offset.get(), EMPTY_NECKLACE_SLOT, List.of(Text.translatable("slot.tooltip.necklace"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && serverConfig.inventorySlots.is_necklace_slot_enabled.get();
			}

		});

		// ring 1 slot 54
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.RING_1, 51, serverConfig.inventorySlots.ring_1_slot_x_offset.get(), serverConfig.inventorySlots.ring_1_slot_y_offset.get(), EMPTY_RING_1_SLOT, List.of(Text.translatable("slot.tooltip.ring_1"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && serverConfig.inventorySlots.is_ring_1_slot_enabled.get();
			}

		});

		// ring 2 slot 55
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.RING_2, 52, serverConfig.inventorySlots.ring_2_slot_x_offset.get(), serverConfig.inventorySlots.ring_2_slot_y_offset.get(), EMPTY_RING_2_SLOT, List.of(Text.translatable("slot.tooltip.ring_2"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && serverConfig.inventorySlots.is_ring_2_slot_enabled.get();
			}

		});

		// shoulders slot 56
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SHOULDERS, 53, serverConfig.inventorySlots.shoulders_slot_x_offset.get(), serverConfig.inventorySlots.shoulders_slot_y_offset.get(), EMPTY_SHOULDERS_SLOT, List.of(Text.translatable("slot.tooltip.shoulders"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && serverConfig.inventorySlots.is_shoulders_slot_enabled.get();
			}

		});

		// spell 1 slot 57
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_1, 54, serverConfig.inventorySlots.spell_1_slot_x_offset.get(), serverConfig.inventorySlots.spell_1_slot_y_offset.get(), EMPTY_SPELL_1_SLOT, List.of(Text.translatable("slot.tooltip.spell_1"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 1;
			}

		});

		// spell 2 slot 58
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_2, 55, serverConfig.inventorySlots.spell_2_slot_x_offset.get(), serverConfig.inventorySlots.spell_2_slot_y_offset.get(), EMPTY_SPELL_2_SLOT, List.of(Text.translatable("slot.tooltip.spell_2"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 2;
			}

		});

		// spell 3 slot 59
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_3, 56, serverConfig.inventorySlots.spell_3_slot_x_offset.get(), serverConfig.inventorySlots.spell_3_slot_y_offset.get(), EMPTY_SPELL_3_SLOT, List.of(Text.translatable("slot.tooltip.spell_3"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 3;
			}

		});

		// spell 4 slot 60
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_4, 57, serverConfig.inventorySlots.spell_4_slot_x_offset.get(), serverConfig.inventorySlots.spell_4_slot_y_offset.get(), EMPTY_SPELL_4_SLOT, List.of(Text.translatable("slot.tooltip.spell_4"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 4;
			}

		});

		// spell 5 slot 61
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_5, 58, serverConfig.inventorySlots.spell_5_slot_x_offset.get(), serverConfig.inventorySlots.spell_5_slot_y_offset.get(), EMPTY_SPELL_5_SLOT, List.of(Text.translatable("slot.tooltip.spell_5"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 5;
			}

		});

		// spell 6 slot 62
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_6, 59, serverConfig.inventorySlots.spell_6_slot_x_offset.get(), serverConfig.inventorySlots.spell_6_slot_y_offset.get(), EMPTY_SPELL_6_SLOT, List.of(Text.translatable("slot.tooltip.spell_6"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 6;
			}

		});

		// spell 7 slot 63
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_7, 60, serverConfig.inventorySlots.spell_7_slot_x_offset.get(), serverConfig.inventorySlots.spell_7_slot_y_offset.get(), EMPTY_SPELL_7_SLOT, List.of(Text.translatable("slot.tooltip.spell_7"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 7;
			}

		});

		// spell 8 slot 64
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_8, 61, serverConfig.inventorySlots.spell_8_slot_x_offset.get(), serverConfig.inventorySlots.spell_8_slot_y_offset.get(), EMPTY_SPELL_8_SLOT, List.of(Text.translatable("slot.tooltip.spell_8"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 8;
			}

		});

		// relic slot 65
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.RELIC, 62, serverConfig.inventorySlots.relic_slot_x_offset.get(), serverConfig.inventorySlots.relic_slot_y_offset.get(), EMPTY_RELIC_SLOT, List.of(Text.translatable("slot.tooltip.relic"))) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && serverConfig.inventorySlots.is_relic_slot_enabled.get();
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

	}

	@Inject(at = @At("HEAD"), method = "onClosed")
	private void rpginventory$onClosed(PlayerEntity player, CallbackInfo info) {
		// TODO trigger adventure hotbar items check
	}

	/**
	 * Modified and expanded code by @Emi
	 */
	@Inject(at = @At("HEAD"), method = "quickMove", cancellable = true)
	private void rpginventory$quickMove(PlayerEntity player, int slot, CallbackInfoReturnable<ItemStack> cir) {
		Slot rpginventory$slot = slots.get(slot);
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

//		// TODO adventure hotbar items
//		StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.statusEffects.civilisation_status_effect_identifier));
//		boolean hasCivilisationEffect = civilisation_status_effect != null && player.hasStatusEffect(civilisation_status_effect);
//
//		StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.statusEffects.wilderness_status_effect_identifier));
//		boolean hasWildernessEffect = wilderness_status_effect != null && player.hasStatusEffect(wilderness_status_effect);
//
//		boolean canChangeEquipment = true;
//
//		if (player.getServer() != null) {
//			canChangeEquipment = player.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
//		}
//		hasCivilisationEffect = hasCivilisationEffect || (canChangeEquipment && !hasWildernessEffect);

		if (rpginventory$slot.hasStack()) {
			ItemStack rpginventory$stack = rpginventory$slot.getStack();
			EquipmentSlot rpginventory$equipmentSlot = player.getPreferredEquipmentSlot(rpginventory$stack);
			if (slot >= 44 && slot < 66) {
				if (!this.insertItem(rpginventory$stack, 9, 45, false)) {   // TODO adventure hotbar items
					cir.setReturnValue(ItemStack.EMPTY);
					cir.cancel();
				} else {
					cir.setReturnValue(rpginventory$stack);
					cir.cancel();
				}
			} else if (slot >= 9 && slot < 45) {

				if (RPGInventory.isHandSlotOverhaulActive()) {

					if (!rpginventory$stack.isEmpty() && (!serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || rpginventory$stack.isIn(Tags.HAND_ITEMS))) {
						if (((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed() && !this.slots.get(47).hasStack()) {
							if (!this.insertItem(rpginventory$stack, 47, 48, false)) {
								cir.setReturnValue(ItemStack.EMPTY);
								cir.cancel();
							}
						} else if (!((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed() && !this.slots.get(46).hasStack()) {
							if (!this.insertItem(rpginventory$stack, 46, 47, false)) {
								cir.setReturnValue(ItemStack.EMPTY);
								cir.cancel();
							}
						}
					}

					if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == EquipmentSlot.OFFHAND || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || rpginventory$stack.isIn(Tags.OFFHAND_ITEMS))) {
						if (((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed() && !this.slots.get(48).hasStack()) {
							if (!this.insertItem(rpginventory$stack, 48, 49, false)) {
								cir.setReturnValue(ItemStack.EMPTY);
								cir.cancel();
							}
						} else if (!((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed() && !this.slots.get(45).hasStack()) {
							if (!this.insertItem(rpginventory$stack, 45, 46, false)) {
								cir.setReturnValue(ItemStack.EMPTY);
								cir.cancel();
							}
						}
					}

					if (serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get()) {
						if (!rpginventory$stack.isEmpty() && (!serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || rpginventory$stack.isIn(Tags.HAND_ITEMS))) {
							if (!this.slots.get(49).hasStack()) {
								if (!this.insertItem(rpginventory$stack, 49, 50, false)) {
									cir.setReturnValue(ItemStack.EMPTY);
									cir.cancel();
								}
							}
						}

						if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == EquipmentSlot.OFFHAND || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || rpginventory$stack.isIn(Tags.OFFHAND_ITEMS))) {
							if (!this.slots.get(50).hasStack()) {
								if (!this.insertItem(rpginventory$stack, 50, 51, false)) {
									cir.setReturnValue(ItemStack.EMPTY);
									cir.cancel();
								}
							}
						}
					}
				}

				// belt slot 51
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.BELT || rpginventory$stack.isIn(Tags.BELTS)) && !this.slots.get(51).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 51, 52, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// gloves slot 52
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.GLOVES || rpginventory$stack.isIn(Tags.GLOVES)) && !this.slots.get(52).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 52, 53, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// necklace slot 53
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.NECKLACE || rpginventory$stack.isIn(Tags.NECKLACES)) && !this.slots.get(53).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 53, 54, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// ring 1 slot 54
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.RING_1 || rpginventory$stack.isIn(Tags.RINGS_1)) && !this.slots.get(54).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 54, 55, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// ring 2 slot 55
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.RING_2 || rpginventory$stack.isIn(Tags.RINGS_2)) && !this.slots.get(55).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 55, 56, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// shoulders slot 56
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.SHOULDERS || rpginventory$stack.isIn(Tags.SHOULDERS)) && !this.slots.get(56).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 56, 57, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// spell 1 slot 57
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_1 || rpginventory$stack.isIn(Tags.SPELLS_1)) && !this.slots.get(57).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 57, 58, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// spell 2 slot 58
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_2 || rpginventory$stack.isIn(Tags.SPELLS_2)) && !this.slots.get(58).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 58, 59, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// spell 3 slot 59
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_3 || rpginventory$stack.isIn(Tags.SPELLS_3)) && !this.slots.get(59).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 59, 60, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// spell 4 slot 60
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_4 || rpginventory$stack.isIn(Tags.SPELLS_4)) && !this.slots.get(60).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 60, 61, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// spell 5 slot 61
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_5 || rpginventory$stack.isIn(Tags.SPELLS_5)) && !this.slots.get(61).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 61, 62, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// spell 6 slot 62
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_6 || rpginventory$stack.isIn(Tags.SPELLS_6)) && !this.slots.get(62).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 62, 63, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// spell 7 slot 63
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_7 || rpginventory$stack.isIn(Tags.SPELLS_7)) && !this.slots.get(63).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 63, 64, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// spell 8 slot 64
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_8 || rpginventory$stack.isIn(Tags.SPELLS_8)) && !this.slots.get(64).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 64, 65, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}

				// relic slot 65
				if (!rpginventory$stack.isEmpty() && (rpginventory$equipmentSlot == ExtendedEquipmentSlot.RELIC || rpginventory$stack.isIn(Tags.RELICS)) && !this.slots.get(65).hasStack()) {
					if (!this.insertItem(rpginventory$stack, 65, 66, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						cir.cancel();
					}
				}
//			} else if (slot >= 45 && slot < 51 && RPGInventory.isHandSlotOverhaulActive()) {
//				if (!this.insertItem(rpginventory$stack, 9, 45, false)) {   // TODO adventure hotbar items
//					cir.setReturnValue(ItemStack.EMPTY);
//					cir.cancel();
//				} else {
//					cir.setReturnValue(rpginventory$stack);
//					cir.cancel();
//				}
			}
		}
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
