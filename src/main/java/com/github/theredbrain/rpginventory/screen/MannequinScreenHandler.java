package com.github.theredbrain.rpginventory.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.block.entity.MannequinBlockEntity;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import com.github.theredbrain.rpginventory.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.screen.slot.AlternativeHandSlot;
import com.github.theredbrain.rpginventory.screen.slot.CustomArmorSlot;
import com.github.theredbrain.rpginventory.screen.slot.MannequinSlot;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MannequinScreenHandler extends ScreenHandler {
	private static final Identifier EMPTY_HAND_SLOT = RPGInventory.identifier("item/empty_slot_hand");
	private static final Identifier EMPTY_ALTERNATIVE_HAND_SLOT = RPGInventory.identifier("item/empty_slot_alternative_hand");
	private static final Identifier EMPTY_ALTERNATIVE_OFFHAND_SLOT = RPGInventory.identifier("item/empty_slot_alternative_offhand");
	private static final Identifier EMPTY_BELT_SLOT = RPGInventory.identifier("item/empty_slot_belt");
	private static final Identifier EMPTY_GLOVES_SLOT = RPGInventory.identifier("item/empty_slot_gloves");
	private static final Identifier EMPTY_NECKLACE_SLOT = RPGInventory.identifier("item/empty_slot_necklace");
	private static final Identifier EMPTY_RING_1_SLOT = RPGInventory.identifier("item/empty_slot_ring_1");
	private static final Identifier EMPTY_RING_2_SLOT = RPGInventory.identifier("item/empty_slot_ring_2");
	private static final Identifier EMPTY_SHOULDERS_SLOT = RPGInventory.identifier("item/empty_slot_shoulders");
	private static final Identifier EMPTY_SPELL_1_SLOT = RPGInventory.identifier("item/empty_slot_spell_1");
	private static final Identifier EMPTY_SPELL_2_SLOT = RPGInventory.identifier("item/empty_slot_spell_2");
	private static final Identifier EMPTY_SPELL_3_SLOT = RPGInventory.identifier("item/empty_slot_spell_3");
	private static final Identifier EMPTY_SPELL_4_SLOT = RPGInventory.identifier("item/empty_slot_spell_4");
	private static final Identifier EMPTY_SPELL_5_SLOT = RPGInventory.identifier("item/empty_slot_spell_5");
	private static final Identifier EMPTY_SPELL_6_SLOT = RPGInventory.identifier("item/empty_slot_spell_6");
	private static final Identifier EMPTY_SPELL_7_SLOT = RPGInventory.identifier("item/empty_slot_spell_7");
	private static final Identifier EMPTY_SPELL_8_SLOT = RPGInventory.identifier("item/empty_slot_spell_8");

	private static int EQUIPMENT_SLOTS_START;
	private static int MANNEQUIN_SLOTS_START;
	private final Inventory inventory;
	private final PlayerInventory playerInventory;
	private final World world;
	private static Map<EquipmentSlot, Identifier> EMPTY_ARMOR_SLOT_TEXTURES;
	private static EquipmentSlot[] EQUIPMENT_SLOT_ORDER;
	private static List<List<Text>> ARMOR_SLOT_TOOLTIPS;
	private final PlayerEntity owner;

	public MannequinScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(MannequinBlockEntity.INVENTORY_SIZE));
	}

	public MannequinScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		super(ScreenHandlerTypesRegistry.MANNEQUIN_SCREEN_HANDLER, syncId);
		checkSize(inventory, MannequinBlockEntity.INVENTORY_SIZE);
		this.inventory = inventory;
		this.playerInventory = playerInventory;
		this.owner = playerInventory.player;
		this.world = playerInventory.player.getWorld();
		inventory.onOpen(playerInventory.player);
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

		// 0 - 26
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 147 + i * 18));
			}
		}

		// 27 - 35
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 205));
		}

		// 36 - 39
		EQUIPMENT_SLOTS_START = 36;
		for (int i = 0; i < 4; i++) {
			EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[i];
			this.addSlot(new CustomArmorSlot(playerInventory, MannequinScreenHandler.this.owner, equipmentSlot, 39 - i, 8, 17 + i * 18, EMPTY_ARMOR_SLOT_TEXTURES.get(equipmentSlot), ARMOR_SLOT_TOOLTIPS.get(i), true));
		}

		// 40 offhand
		this.addSlot(new CustomArmorSlot(playerInventory, MannequinScreenHandler.this.owner, EquipmentSlot.OFFHAND, 40, 44, 53, PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT, List.of(Text.translatable("slot.tooltip.offhand")), true) {

			@Override
			public boolean isEnabled() {
				return !((DuckPlayerEntityMixin) MannequinScreenHandler.this.owner).rpginventory$isOffhandStackSheathed() || !RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get();
			}

			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (MannequinScreenHandler.this.owner.getServer() != null) {
					bl = MannequinScreenHandler.this.owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(wilderness_status_effect.get());

				return (EquipmentSlot.OFFHAND == MannequinScreenHandler.this.owner.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || !serverConfig.handSlotOverhaul.enable_hand_slot_overhaul.get()) && ItemUtils.isOwnedByPlayer(stack, MannequinScreenHandler.this.owner.getGameProfile()) && (hasCivilisationEffect || MannequinScreenHandler.this.owner.isCreative() || (bl && !hasWildernessEffect)) && !((DuckPlayerEntityMixin) MannequinScreenHandler.this.owner).rpginventory$isOffhandStackSheathed();
			}

		});

		// 41 main hand
		this.addSlot(new CustomArmorSlot(playerInventory, MannequinScreenHandler.this.owner, EquipmentSlot.MAINHAND, 41, 26, 53, EMPTY_HAND_SLOT, List.of(Text.translatable("slot.tooltip.hand")), true) {

			@Override
			public boolean isEnabled() {
				return !((DuckPlayerEntityMixin) MannequinScreenHandler.this.owner).rpginventory$isHandStackSheathed() && RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get();
			}

			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (MannequinScreenHandler.this.owner.getServer() != null) {
					bl = MannequinScreenHandler.this.owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(wilderness_status_effect.get());

				return (EquipmentSlot.MAINHAND == MannequinScreenHandler.this.owner.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isOwnedByPlayer(stack, MannequinScreenHandler.this.owner.getGameProfile()) && (hasCivilisationEffect || MannequinScreenHandler.this.owner.isCreative() || (bl && !hasWildernessEffect)) && !((DuckPlayerEntityMixin) MannequinScreenHandler.this.owner).rpginventory$isHandStackSheathed();
			}

		});

		// 42 sheathed main hand
		this.addSlot(new CustomArmorSlot(playerInventory, MannequinScreenHandler.this.owner, EquipmentSlot.MAINHAND, 42, 26, 53, EMPTY_HAND_SLOT, List.of(Text.translatable("slot.tooltip.hand")), true) {

			@Override
			public boolean isEnabled() {
				return ((DuckPlayerEntityMixin) MannequinScreenHandler.this.owner).rpginventory$isHandStackSheathed() && RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get();
			}

			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (MannequinScreenHandler.this.owner.getServer() != null) {
					bl = MannequinScreenHandler.this.owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(wilderness_status_effect.get());

				return (EquipmentSlot.MAINHAND == MannequinScreenHandler.this.owner.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isOwnedByPlayer(stack, MannequinScreenHandler.this.owner.getGameProfile()) && (hasCivilisationEffect || MannequinScreenHandler.this.owner.isCreative() || (bl && !hasWildernessEffect)) && ((DuckPlayerEntityMixin) MannequinScreenHandler.this.owner).rpginventory$isHandStackSheathed();
			}

		});

		// 43 sheathed offhand
		this.addSlot(new CustomArmorSlot(playerInventory, MannequinScreenHandler.this.owner, EquipmentSlot.OFFHAND, 43, 44, 53, PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT, List.of(Text.translatable("slot.tooltip.offhand")), true) {

			@Override
			public boolean isEnabled() {
				return ((DuckPlayerEntityMixin) MannequinScreenHandler.this.owner).rpginventory$isOffhandStackSheathed() && RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get();
			}

			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (MannequinScreenHandler.this.owner.getServer() != null) {
					bl = MannequinScreenHandler.this.owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(wilderness_status_effect.get());

				return (EquipmentSlot.OFFHAND == MannequinScreenHandler.this.owner.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isOwnedByPlayer(stack, MannequinScreenHandler.this.owner.getGameProfile()) && (hasCivilisationEffect || MannequinScreenHandler.this.owner.isCreative() || (bl && !hasWildernessEffect)) && ((DuckPlayerEntityMixin) MannequinScreenHandler.this.owner).rpginventory$isOffhandStackSheathed();
			}

		});

		// 44 alternative main hand slot
		this.addSlot(new AlternativeHandSlot(playerInventory, MannequinScreenHandler.this.owner, EquipmentSlot.MAINHAND, 46, 26, 71, EMPTY_ALTERNATIVE_HAND_SLOT, List.of(Text.translatable("slot.tooltip.alternative_hand")), true) {

			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (MannequinScreenHandler.this.owner.getServer() != null) {
					bl = MannequinScreenHandler.this.owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(wilderness_status_effect.get());

				return (stack.isIn(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isOwnedByPlayer(stack, MannequinScreenHandler.this.owner.getGameProfile()) && (hasCivilisationEffect || MannequinScreenHandler.this.owner.isCreative() || (bl && !hasWildernessEffect));
			}

			@Override
			public boolean isEnabled() {
				return RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get();
			}

		});

		// 45 alternative offhand slot
		this.addSlot(new AlternativeHandSlot(playerInventory, MannequinScreenHandler.this.owner, EquipmentSlot.OFFHAND, 47, 44, 71, EMPTY_ALTERNATIVE_OFFHAND_SLOT, List.of(Text.translatable("slot.tooltip.alternative_offhand")), true) {

			@Override
			public boolean canInsert(ItemStack stack) {
				boolean bl = true;
				if (MannequinScreenHandler.this.owner.getServer() != null) {
					bl = MannequinScreenHandler.this.owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
				}
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
				boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(civilisation_status_effect.get());

				Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
				boolean hasWildernessEffect = wilderness_status_effect.isPresent() && MannequinScreenHandler.this.owner.hasStatusEffect(wilderness_status_effect.get());

				return (stack.isIn(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isOwnedByPlayer(stack, MannequinScreenHandler.this.owner.getGameProfile()) && (hasCivilisationEffect || MannequinScreenHandler.this.owner.isCreative() || (bl && !hasWildernessEffect));
			}

			@Override
			public boolean isEnabled() {
				return RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get();
			}

		});

		// index 44 & 45 are the empty hand slots

		// 46 belt slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.BELT, 48, 62, 71, EMPTY_BELT_SLOT, List.of(Text.translatable("slot.tooltip.belt")), true));

		// 47 gloves slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.GLOVES, 49, 62, 53, EMPTY_GLOVES_SLOT, List.of(Text.translatable("slot.tooltip.gloves")), true));

		// 48 necklace slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.NECKLACE, 50, 44, 17, EMPTY_NECKLACE_SLOT, List.of(Text.translatable("slot.tooltip.necklace")), true));

		// 49 ring 1 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.RING_1, 51, 62, 17, EMPTY_RING_1_SLOT, List.of(Text.translatable("slot.tooltip.ring_1")), true));

		// 50 ring 2 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.RING_2, 52, 62, 35, EMPTY_RING_2_SLOT, List.of(Text.translatable("slot.tooltip.ring_2")), true));

		// 51 shoulders slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SHOULDERS, 53, 26, 17, EMPTY_SHOULDERS_SLOT, List.of(Text.translatable("slot.tooltip.shoulders")), true));

		// 52 spell 1 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_1, 54, 8, 89, EMPTY_SPELL_1_SLOT, List.of(Text.translatable("slot.tooltip.spell_1")), true) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 1;
			}

		});

		// 53 spell 2 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_2, 55, 26, 89, EMPTY_SPELL_2_SLOT, List.of(Text.translatable("slot.tooltip.spell_2")), true) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 2;
			}

		});

		// 54 spell 3 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_3, 56, 44, 89, EMPTY_SPELL_3_SLOT, List.of(Text.translatable("slot.tooltip.spell_3")), true) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 3;
			}

		});

		// 55 spell 4 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_4, 57, 62, 89, EMPTY_SPELL_4_SLOT, List.of(Text.translatable("slot.tooltip.spell_4")), true) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 4;
			}

		});

		// 56 spell 5 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_5, 58, 8, 107, EMPTY_SPELL_5_SLOT, List.of(Text.translatable("slot.tooltip.spell_5")), true) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 5;
			}

		});

		// 57 spell 6 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_6, 59, 26, 107, EMPTY_SPELL_6_SLOT, List.of(Text.translatable("slot.tooltip.spell_6")), true) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 6;
			}

		});

		// 58 spell 7 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_7, 60, 44, 107, EMPTY_SPELL_7_SLOT, List.of(Text.translatable("slot.tooltip.spell_7")), true) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 7;
			}

		});

		// 59 spell 8 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_8, 61, 62, 107, EMPTY_SPELL_8_SLOT, List.of(Text.translatable("slot.tooltip.spell_8")), true) {

			@Override
			public boolean isEnabled() {
				return super.isEnabled() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 8;
			}

		});


		MANNEQUIN_SLOTS_START = 60;
		// 60 - 63
		for (int i = 0; i < 4; i++) {
			EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[i];
			this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, equipmentSlot, i, 90 + 8, 17 + i * 18, EMPTY_ARMOR_SLOT_TEXTURES.get(equipmentSlot), ARMOR_SLOT_TOOLTIPS.get(i)));
//			this.addSlot(new Slot(playerInventory, 39 - i, 98, 17 + i * 18) {
//
//				@Override
//				public void setStack(ItemStack stack, ItemStack previousStack) {
//					owner.onEquipStack(equipmentSlot, previousStack, stack);
//					super.setStack(stack, previousStack);
//				}
//
//				@Override
//				public int getMaxItemCount() {
//					return 1;
//				}
//
//				@Override
//				public boolean canInsert(ItemStack stack) {
//					boolean bl = true;
//					if (owner.getServer() != null) {
//						bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
//					}
//
//					Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
//					boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());
//
//					Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
//					boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());
//
//					boolean isOwned = ItemUtils.isOwnedByPlayer(stack, owner.getGameProfile());
//					boolean isCreative = owner.isCreative();
//
//					return (equipmentSlot == owner.getPreferredEquipmentSlot(stack) || rpginventory$isOfEquipmentTag(stack, equipmentSlot)) && isOwned && (hasCivilisationEffect || isCreative || (bl && !hasWildernessEffect));
//				}
//
//				@Override
//				public boolean canTakeItems(PlayerEntity playerEntity) {
//					boolean bl = true;
//					if (owner.getServer() != null) {
//						bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
//					}
//
//					Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
//					boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());
//
//					Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
//					boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());
//
//					boolean isCreative = playerEntity.isCreative();
//
//					ItemStack itemStack = this.getStack();
//					return (
//							!itemStack.isEmpty()
//							&& !isCreative
//							&& EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE)
//							? false
//							: super.canTakeItems(playerEntity)
//					) && !this.getStack().contains(RPGInventory.LOAD_OUT_ITEM) && (hasCivilisationEffect || isCreative || (bl && !hasWildernessEffect));
//				}
//
//				public Pair<Identifier, Identifier> getBackgroundSprite() {
//					return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, identifier);
//				}
//			});
		}
		// 64 offhand
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, EquipmentSlot.OFFHAND, 4, 90 + 44, 53, PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT, List.of(Text.translatable("slot.tooltip.offhand"))));

		// 65 main hand
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, EquipmentSlot.MAINHAND, 5, 90 + 26, 53, EMPTY_HAND_SLOT, List.of(Text.translatable("slot.tooltip.hand"))));

		// 66 alternative main hand
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, EquipmentSlot.MAINHAND, 6, 90 + 26, 71, EMPTY_ALTERNATIVE_HAND_SLOT, List.of(Text.translatable("slot.tooltip.alternative_hand"))));

		// 67 alternative offhand
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, EquipmentSlot.OFFHAND, 7, 90 + 44, 71, EMPTY_ALTERNATIVE_OFFHAND_SLOT, List.of(Text.translatable("slot.tooltip.alternative_offhand"))));

		// 68 belt slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.BELT, 8, 90 + 62, 71, EMPTY_BELT_SLOT, List.of(Text.translatable("slot.tooltip.belt"))));

		// 69 gloves slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.GLOVES, 9, 90 + 62, 53, EMPTY_GLOVES_SLOT, List.of(Text.translatable("slot.tooltip.gloves"))));

		// 70 necklace slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.NECKLACE, 10, 90 + 44, 17, EMPTY_NECKLACE_SLOT, List.of(Text.translatable("slot.tooltip.necklace"))));

		// 71 ring 1 slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.RING_1, 11, 90 + 62, 17, EMPTY_RING_1_SLOT, List.of(Text.translatable("slot.tooltip.ring_1"))));

		// 72 ring 2 slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.RING_2, 12, 90 + 62, 35, EMPTY_RING_2_SLOT, List.of(Text.translatable("slot.tooltip.ring_2"))));

		// 73 shoulders slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SHOULDERS, 13, 90 + 26, 17, EMPTY_SHOULDERS_SLOT, List.of(Text.translatable("slot.tooltip.shoulders"))));

		// 74 spell 1 slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_1, 14, 90 + 8, 89, EMPTY_SPELL_1_SLOT, List.of(Text.translatable("slot.tooltip.spell_1"))));

		// 75 spell 2 slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_2, 15, 90 + 26, 89, EMPTY_SPELL_2_SLOT, List.of(Text.translatable("slot.tooltip.spell_2"))));

		// 54 spell 3 slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_3, 16, 90 + 44, 89, EMPTY_SPELL_3_SLOT, List.of(Text.translatable("slot.tooltip.spell_3"))));

		// 55 spell 4 slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_4, 17, 90 + 62, 89, EMPTY_SPELL_4_SLOT, List.of(Text.translatable("slot.tooltip.spell_4"))));

		// 56 spell 5 slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_5, 18, 90 + 8, 107, EMPTY_SPELL_5_SLOT, List.of(Text.translatable("slot.tooltip.spell_5"))));

		// 57 spell 6 slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_6, 19, 90 + 26, 107, EMPTY_SPELL_6_SLOT, List.of(Text.translatable("slot.tooltip.spell_6"))));

		// 58 spell 7 slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_7, 20, 90 + 44, 107, EMPTY_SPELL_7_SLOT, List.of(Text.translatable("slot.tooltip.spell_7"))));

		// 59 spell 8 slot
		this.addSlot(new MannequinSlot(inventory, MannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_8, 21, 90 + 62, 107, EMPTY_SPELL_8_SLOT, List.of(Text.translatable("slot.tooltip.spell_8"))));
//		this.addSlot(new Slot(inventory, 0, 8, 17) {
//
//			public boolean canTakeItems(PlayerEntity playerEntity) {
//				return true; // TODO player has status effect
//			}
//
//			public Pair<Identifier, Identifier> getBackgroundSprite() {
//				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_HELMET_SLOT_TEXTURE);
//			}
//		});
//
//		this.addSlot(new Slot(inventory, 1, 8, 35) {
//
//			public boolean canTakeItems(PlayerEntity playerEntity) {
//				return true; // TODO player has status effect
//			}
//
//			public Pair<Identifier, Identifier> getBackgroundSprite() {
//				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_CHESTPLATE_SLOT_TEXTURE);
//			}
//		});
//
//		this.addSlot(new Slot(inventory, 2, 8, 53) {
//
//			public boolean canTakeItems(PlayerEntity playerEntity) {
//				return true; // TODO player has status effect
//			}
//
//			public Pair<Identifier, Identifier> getBackgroundSprite() {
//				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_LEGGINGS_SLOT_TEXTURE);
//			}
//		});
//
//		this.addSlot(new Slot(inventory, 3, 8, 71) {
//
//			public boolean canTakeItems(PlayerEntity playerEntity) {
//				return true; // TODO player has status effect
//			}
//
//			public Pair<Identifier, Identifier> getBackgroundSprite() {
//				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_BOOTS_SLOT_TEXTURE);
//			}
//		});

	}

	public PlayerInventory getPlayerInventory() {
		return this.playerInventory;
	}

	private void equip(PlayerEntity player) {

//		RPGInventory.LOGGER.info("equip");

		// regular armor
		for (int i = 0; i < 4; i++) {
			equipSingleSlot(MANNEQUIN_SLOTS_START + i, EQUIPMENT_SLOTS_START + i);
		}

		if (((DuckPlayerEntityMixin) MannequinScreenHandler.this.owner).rpginventory$isOffhandStackSheathed()) {
			// sheathed offhand
			equipSingleSlot(MANNEQUIN_SLOTS_START + 4, EQUIPMENT_SLOTS_START + 7);
		} else {
			// offhand
			equipSingleSlot(MANNEQUIN_SLOTS_START + 4, EQUIPMENT_SLOTS_START + 4);
		}

		if (((DuckPlayerEntityMixin) MannequinScreenHandler.this.owner).rpginventory$isHandStackSheathed()) {
			// sheathed main hand
			equipSingleSlot(MANNEQUIN_SLOTS_START + 5, EQUIPMENT_SLOTS_START + 6);
		} else {
			// main hand
			equipSingleSlot(MANNEQUIN_SLOTS_START + 5, EQUIPMENT_SLOTS_START + 5);
		}

		// regular armor
		for (int i = 0; i < 8; i++) {
			equipSingleSlot(MANNEQUIN_SLOTS_START + 6 + i, EQUIPMENT_SLOTS_START + 8 + i);
		}

		// spell slots
		for (int i = 0; i < 8; i++) {
			if (((DuckPlayerEntityMixin) player).rpginventory$getActiveSpellSlotAmount() >= i + 1) {
				equipSingleSlot(MANNEQUIN_SLOTS_START + 14 + i, EQUIPMENT_SLOTS_START + 16 + i);
			}
		}

	}

	private void equipSingleSlot(int mannequin_index, int player_index) {
		ItemStack equipmentStack = this.slots.get(player_index).getStack();
		ItemStack mannequinStack = this.slots.get(mannequin_index).getStack();

		if (!mannequinStack.isEmpty() && (equipmentStack.isEmpty() || equipmentStack.contains(RPGInventory.LOAD_OUT_ITEM))) {
			ItemStack newStack = mannequinStack.copy();
			newStack.set(RPGInventory.LOAD_OUT_ITEM, Unit.INSTANCE);
			this.slots.get(player_index).setStack(newStack);
		}
	}

	private void unequip(PlayerEntity player) {

//		RPGInventory.LOGGER.info("unequip");

		for (int i = 0; i < MannequinBlockEntity.INVENTORY_SIZE + 2; i++) {
			unequipSingleSlot(EQUIPMENT_SLOTS_START + i);
		}
	}

	private void unequipSingleSlot(int player_index) {
		ItemStack itemStack = this.slots.get(player_index).getStack();
		if (!itemStack.isEmpty() && itemStack.contains(RPGInventory.LOAD_OUT_ITEM)) {
			this.slots.get(player_index).setStack(ItemStack.EMPTY);
		}
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
//		ItemStack itemStack = this.inventory.getStack(0);
//		ItemStack itemStack2 = this.inventory.getStack(1);
//		ServerConfig serverConfig = RPGEnchanting.SERVER_CONFIG;
//		if (itemStack.isEmpty()) {
//			return false;
//		}
		if (id == 0) {

			this.equip(player);

//			MutablePair<RegistryEntry.Reference<Enchantment>, Integer> newEnchantment = this.current_prefix_enchantments.get(id);
//
//			int experience_cost_amount = this.existing_enchantment_costs[0] + (int) Math.max(0, Math.floor(newEnchantment.getLeft().value().getMaxPower(newEnchantment.getRight()) * serverConfig.new_enchantment_exp_cost_multiplier.get()));
//			if (player.experienceLevel < experience_cost_amount && !player.isInCreativeMode()) {
//				return false;
//			}
//			int item_cost_amount = this.existing_enchantment_costs[1] + (int) Math.max(0, Math.floor(newEnchantment.getLeft().value().getAnvilCost() * newEnchantment.getRight() * serverConfig.new_enchantment_item_cost_multiplier.get()));
//			if ((!itemStack2.isOf(Registries.ITEM.get(serverConfig.prefix_item_cost.get())) || itemStack2.getCount() < item_cost_amount) && !player.isInCreativeMode()) {
//				return false;
//			}
//			player.applyEnchantmentCosts(itemStack, experience_cost_amount);
//			ItemEnchantmentsComponent.Builder itemEnchantmentsComponentBuilder = new ItemEnchantmentsComponent.Builder(itemStack.getEnchantments());
//			if (this.existing_prefix_enchantment != null) {
//				itemEnchantmentsComponentBuilder.set(this.existing_prefix_enchantment.getLeft(), 0);
//			}
//			itemEnchantmentsComponentBuilder.add(newEnchantment.getLeft(), newEnchantment.getRight());
//			itemStack.set(DataComponentTypes.ENCHANTMENTS, itemEnchantmentsComponentBuilder.build().withShowInTooltip(false));
//			itemStack.set(RPGEnchanting.SHOW_ENCHANTMENT_NAME_ADDITIONS, Unit.INSTANCE);
//			if (serverConfig.enable_enchanted_by_player_component_application.get()) {
//				itemStack.set(RPGEnchanting.PLAYER_ENCHANTED, new ProfileComponent(player.getGameProfile()));
//			}
//
//			itemStack2.decrementUnlessCreative(item_cost_amount, player);
//			if (itemStack2.isEmpty()) {
//				this.inventory.setStack(1, ItemStack.EMPTY);
//			}
//
//			player.incrementStat(Stats.ENCHANT_ITEM);
//			if (player instanceof ServerPlayerEntity) {
//				Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity) player, itemStack, experience_cost_amount);
//			}
//
//			this.inventory.markDirty();
//			this.onContentChanged(this.inventory);
//
//			world.playSound(null, this.blockPos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);

		} else if (id == 1) {

			this.unequip(player);

//			MutablePair<RegistryEntry.Reference<Enchantment>, Integer> newEnchantment = this.current_suffix_enchantments.get(id - first_threshold);
//
//			int experience_cost_amount = this.existing_enchantment_costs[2] + (int) Math.max(0, Math.floor(newEnchantment.getLeft().value().getMaxPower(newEnchantment.getRight()) * serverConfig.new_enchantment_exp_cost_multiplier.get()));
//			if (player.experienceLevel < experience_cost_amount && !player.isInCreativeMode()) {
//				return false;
//			}
//			int item_cost_amount = this.existing_enchantment_costs[3] + (int) Math.max(0, Math.floor(newEnchantment.getLeft().value().getAnvilCost() * newEnchantment.getRight() * serverConfig.new_enchantment_item_cost_multiplier.get()));
//			if ((!itemStack2.isOf(Registries.ITEM.get(serverConfig.suffix_item_cost.get())) || itemStack2.getCount() < item_cost_amount) && !player.isInCreativeMode()) {
//				return false;
//			}
//			player.applyEnchantmentCosts(itemStack, experience_cost_amount);
//
//			ItemEnchantmentsComponent.Builder itemEnchantmentsComponentBuilder = new ItemEnchantmentsComponent.Builder(itemStack.getEnchantments());
//			if (this.existing_suffix_enchantment != null) {
//				itemEnchantmentsComponentBuilder.set(this.existing_suffix_enchantment.getLeft(), 0);
//			}
//			itemEnchantmentsComponentBuilder.add(newEnchantment.getLeft(), newEnchantment.getRight());
//			itemStack.set(DataComponentTypes.ENCHANTMENTS, itemEnchantmentsComponentBuilder.build().withShowInTooltip(false));
//			itemStack.set(RPGEnchanting.SHOW_ENCHANTMENT_NAME_ADDITIONS, Unit.INSTANCE);
//			if (serverConfig.enable_enchanted_by_player_component_application.get()) {
//				itemStack.set(RPGEnchanting.PLAYER_ENCHANTED, new ProfileComponent(player.getGameProfile()));
//			}
//
//			itemStack2.decrementUnlessCreative(item_cost_amount, player);
//			if (itemStack2.isEmpty()) {
//				this.inventory.setStack(1, ItemStack.EMPTY);
//			}
//
//			player.incrementStat(Stats.ENCHANT_ITEM);
//			if (player instanceof ServerPlayerEntity) {
//				Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity) player, itemStack, experience_cost_amount);
//			}
//
//			this.inventory.markDirty();
//			this.onContentChanged(this.inventory);
//
//			world.playSound(null, this.blockPos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);

		} else {
			Util.error(player.getName() + " pressed invalid button id: " + id);
			return false;
		}
		return true;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		ItemStack itemStack = ItemStack.EMPTY;
//		Slot slot2 = this.slots.get(slot);
//		if (slot2 != null && slot2.hasStack()) {
//			ItemStack itemStack2 = slot2.getStack();
//			itemStack = itemStack2.copy();
//			if (slot < 9) {
//				if (!this.insertItem(itemStack2, 9, 45, true)) {
//					return ItemStack.EMPTY;
//				}
//			} else if (!this.insertItem(itemStack2, 0, 9, false)) {
//				return ItemStack.EMPTY;
//			}
//
//			if (itemStack2.isEmpty()) {
//				slot2.setStack(ItemStack.EMPTY);
//			} else {
//				slot2.markDirty();
//			}
//
//			if (itemStack2.getCount() == itemStack.getCount()) {
//				return ItemStack.EMPTY;
//			}
//
//			slot2.onTakeItem(player, itemStack2);
//		}

		return itemStack;
	}

	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		this.inventory.onClose(player);
	}

	static {
		EMPTY_ARMOR_SLOT_TEXTURES = Map.of(
				EquipmentSlot.HEAD,
				PlayerScreenHandler.EMPTY_HELMET_SLOT_TEXTURE,
				EquipmentSlot.CHEST,
				PlayerScreenHandler.EMPTY_CHESTPLATE_SLOT_TEXTURE,
				EquipmentSlot.LEGS,
				PlayerScreenHandler.EMPTY_LEGGINGS_SLOT_TEXTURE,
				EquipmentSlot.FEET,
				PlayerScreenHandler.EMPTY_BOOTS_SLOT_TEXTURE
		);
		EQUIPMENT_SLOT_ORDER = new EquipmentSlot[]{
				EquipmentSlot.HEAD,
				EquipmentSlot.CHEST,
				EquipmentSlot.LEGS,
				EquipmentSlot.FEET
		};
		ARMOR_SLOT_TOOLTIPS = List.of(
				List.of(Text.translatable("slot.tooltip.head")),
				List.of(Text.translatable("slot.tooltip.chest")),
				List.of(Text.translatable("slot.tooltip.legs")),
				List.of(Text.translatable("slot.tooltip.feet"))
		);
	}
}
