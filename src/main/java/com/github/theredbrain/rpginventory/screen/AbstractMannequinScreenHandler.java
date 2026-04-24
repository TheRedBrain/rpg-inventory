package com.github.theredbrain.rpginventory.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.block.entity.MannequinBlockEntity;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.screen.slot.AlternativeHandSlot;
import com.github.theredbrain.rpginventory.screen.slot.CustomArmorSlot;
import com.github.theredbrain.rpginventory.screen.slot.MannequinSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public abstract class AbstractMannequinScreenHandler extends AbstractContainerMenu {
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
	private static final Identifier EMPTY_RELIC_SLOT = RPGInventory.identifier("item/empty_slot_relic");

	private static int EQUIPMENT_SLOTS_START;
	private static int MANNEQUIN_SLOTS_START;
	private final Container inventory;
	private final Inventory playerInventory;
	private final static Map<EquipmentSlot, Identifier> EMPTY_ARMOR_SLOT_TEXTURES;
	private final static EquipmentSlot[] EQUIPMENT_SLOT_ORDER;
	private final static List<List<Component>> ARMOR_SLOT_TOOLTIPS;
	private final Player owner;
	private final boolean canEquip;

	public AbstractMannequinScreenHandler(@Nullable MenuType<?> type, int syncId, Inventory playerInventory, Container inventory, BlockPos blockPos, boolean canChangeInventory, boolean canEquip) {
		super(type, syncId);
		checkContainerSize(inventory, MannequinBlockEntity.INVENTORY_SIZE);
		this.inventory = inventory;
		this.playerInventory = playerInventory;
		this.owner = playerInventory.player;
		this.canEquip = canEquip;
		inventory.startOpen(playerInventory.player);
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
			this.addSlot(new CustomArmorSlot(playerInventory, AbstractMannequinScreenHandler.this.owner, equipmentSlot, 39 - i, 8, 17 + i * 18, EMPTY_ARMOR_SLOT_TEXTURES.get(equipmentSlot), ARMOR_SLOT_TOOLTIPS.get(i), true));
		}

		// 40 offhand
		this.addSlot(new CustomArmorSlot(playerInventory, AbstractMannequinScreenHandler.this.owner, EquipmentSlot.OFFHAND, 40, 44, 53, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD, List.of(Component.translatable("slot.tooltip.offhand")), true) {

			@Override
			public boolean isActive() {
				return !((DuckLivingEntityMixin) AbstractMannequinScreenHandler.this.owner).rpginventory$isOffhandStackSheathed() || !RPGInventory.isHandSlotOverhaulActive();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && !((DuckLivingEntityMixin) AbstractMannequinScreenHandler.this.owner).rpginventory$isOffhandStackSheathed();
			}

		});

		// 41 main hand
		this.addSlot(new CustomArmorSlot(playerInventory, AbstractMannequinScreenHandler.this.owner, EquipmentSlot.MAINHAND, 41, 26, 53, EMPTY_HAND_SLOT, List.of(Component.translatable("slot.tooltip.hand")), true) {

			@Override
			public boolean isActive() {
				return !((DuckLivingEntityMixin) AbstractMannequinScreenHandler.this.owner).rpginventory$isHandStackSheathed() && RPGInventory.isHandSlotOverhaulActive();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && !((DuckLivingEntityMixin) AbstractMannequinScreenHandler.this.owner).rpginventory$isHandStackSheathed();
			}

		});

		// 42 sheathed main hand
		this.addSlot(new CustomArmorSlot(playerInventory, AbstractMannequinScreenHandler.this.owner, EquipmentSlot.MAINHAND, 42, 26, 53, EMPTY_HAND_SLOT, List.of(Component.translatable("slot.tooltip.hand")), true) {

			@Override
			public boolean isActive() {
				return ((DuckLivingEntityMixin) AbstractMannequinScreenHandler.this.owner).rpginventory$isHandStackSheathed() && RPGInventory.isHandSlotOverhaulActive();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && ((DuckLivingEntityMixin) AbstractMannequinScreenHandler.this.owner).rpginventory$isHandStackSheathed();
			}

		});

		// 43 sheathed offhand
		this.addSlot(new CustomArmorSlot(playerInventory, AbstractMannequinScreenHandler.this.owner, EquipmentSlot.OFFHAND, 43, 44, 53, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD, List.of(Component.translatable("slot.tooltip.offhand")), true) {

			@Override
			public boolean isActive() {
				return ((DuckLivingEntityMixin) AbstractMannequinScreenHandler.this.owner).rpginventory$isOffhandStackSheathed() && RPGInventory.isHandSlotOverhaulActive();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && ((DuckLivingEntityMixin) AbstractMannequinScreenHandler.this.owner).rpginventory$isOffhandStackSheathed();
			}

		});

		// 44 alternative main hand slot
		this.addSlot(new AlternativeHandSlot(playerInventory, AbstractMannequinScreenHandler.this.owner, EquipmentSlot.MAINHAND, 46, 26, 71, EMPTY_ALTERNATIVE_HAND_SLOT, List.of(Component.translatable("slot.tooltip.alternative_hand")), true) {

			@Override
			public boolean isActive() {
				return RPGInventory.isHandSlotOverhaulActive();
			}

		});

		// 45 alternative offhand slot
		this.addSlot(new AlternativeHandSlot(playerInventory, AbstractMannequinScreenHandler.this.owner, EquipmentSlot.OFFHAND, 47, 44, 71, EMPTY_ALTERNATIVE_OFFHAND_SLOT, List.of(Component.translatable("slot.tooltip.alternative_offhand")), true) {

			@Override
			public boolean isActive() {
				return RPGInventory.isHandSlotOverhaulActive();
			}

		});

		// index 44 & 45 are the empty hand slots

		// 46 belt slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.BELT, 48, 62, 71, EMPTY_BELT_SLOT, List.of(Component.translatable("slot.tooltip.belt")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_belt_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_belt_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_belt_item_taking.get();
			}

		});

		// 47 gloves slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.GLOVES, 49, 62, 53, EMPTY_GLOVES_SLOT, List.of(Component.translatable("slot.tooltip.gloves")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_gloves_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_gloves_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_gloves_item_taking.get();
			}

		});

		// 48 necklace slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.NECKLACE, 50, 44, 17, EMPTY_NECKLACE_SLOT, List.of(Component.translatable("slot.tooltip.necklace")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_necklace_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_necklace_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_necklace_item_taking.get();
			}

		});

		// 49 ring 1 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.RING_1, 51, 62, 35, EMPTY_RING_1_SLOT, List.of(Component.translatable("slot.tooltip.ring_1")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_ring_1_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_ring_1_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_ring_1_item_taking.get();
			}

		});

		// 50 ring 2 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.RING_2, 52, 44, 35, EMPTY_RING_2_SLOT, List.of(Component.translatable("slot.tooltip.ring_2")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_ring_2_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_ring_2_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_ring_2_item_taking.get();
			}

		});

		// 51 shoulders slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SHOULDERS, 53, 26, 17, EMPTY_SHOULDERS_SLOT, List.of(Component.translatable("slot.tooltip.shoulders")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_shoulders_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_shoulders_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_shoulders_item_taking.get();
			}

		});

		// 52 spell 1 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_1, 54, 8, 89, EMPTY_SPELL_1_SLOT, List.of(Component.translatable("slot.tooltip.spell_1")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 1;
			}

		});

		// 53 spell 2 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_2, 55, 26, 89, EMPTY_SPELL_2_SLOT, List.of(Component.translatable("slot.tooltip.spell_2")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 2;
			}

		});

		// 54 spell 3 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_3, 56, 44, 89, EMPTY_SPELL_3_SLOT, List.of(Component.translatable("slot.tooltip.spell_3")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 3;
			}

		});

		// 55 spell 4 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_4, 57, 62, 89, EMPTY_SPELL_4_SLOT, List.of(Component.translatable("slot.tooltip.spell_4")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 4;
			}

		});

		// 56 spell 5 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_5, 58, 8, 107, EMPTY_SPELL_5_SLOT, List.of(Component.translatable("slot.tooltip.spell_5")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 5;
			}

		});

		// 57 spell 6 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_6, 59, 26, 107, EMPTY_SPELL_6_SLOT, List.of(Component.translatable("slot.tooltip.spell_6")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 6;
			}

		});

		// 58 spell 7 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_7, 60, 44, 107, EMPTY_SPELL_7_SLOT, List.of(Component.translatable("slot.tooltip.spell_7")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 7;
			}

		});

		// 59 spell 8 slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.SPELL_8, 61, 62, 107, EMPTY_SPELL_8_SLOT, List.of(Component.translatable("slot.tooltip.spell_8")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 8;
			}

		});

		// 60 relic slot
		this.addSlot(new CustomArmorSlot(playerInventory, owner, ExtendedEquipmentSlot.RELIC, 62, 62, 17, EMPTY_RELIC_SLOT, List.of(Component.translatable("slot.tooltip.relic")), true) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_relic_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_relic_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_relic_item_taking.get();
			}

		});


		MANNEQUIN_SLOTS_START = 61;
		// 61 - 64
		for (int i = 0; i < 4; i++) {
			EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[i];
			this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, equipmentSlot, i, 90 + 8, 17 + i * 18, EMPTY_ARMOR_SLOT_TEXTURES.get(equipmentSlot), canChangeInventory, ARMOR_SLOT_TOOLTIPS.get(i)));
		}
		// 65 offhand
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, EquipmentSlot.OFFHAND, 4, 90 + 44, 53, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD, canChangeInventory, List.of(Component.translatable("slot.tooltip.offhand"))));

		// 66 main hand
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, EquipmentSlot.MAINHAND, 5, 90 + 26, 53, EMPTY_HAND_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.hand"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && RPGInventory.isHandSlotOverhaulActive();
			}

		});

		// 67 alternative main hand
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, EquipmentSlot.MAINHAND, 6, 90 + 26, 71, EMPTY_ALTERNATIVE_HAND_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.alternative_hand"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && RPGInventory.isHandSlotOverhaulActive() && serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get();
			}

		});

		// 68 alternative offhand
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, EquipmentSlot.OFFHAND, 7, 90 + 44, 71, EMPTY_ALTERNATIVE_OFFHAND_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.alternative_offhand"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && RPGInventory.isHandSlotOverhaulActive() && serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get();
			}

		});

		// 69 belt slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.BELT, 8, 90 + 62, 71, EMPTY_BELT_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.belt"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_belt_slot_enabled.get() && serverConfig.inventorySlots.is_belt_mannequin_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_belt_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_belt_item_taking.get();
			}

		});

		// 70 gloves slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.GLOVES, 9, 90 + 62, 53, EMPTY_GLOVES_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.gloves"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_gloves_slot_enabled.get() && serverConfig.inventorySlots.is_gloves_mannequin_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_gloves_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_gloves_item_taking.get();
			}

		});

		// 71 necklace slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.NECKLACE, 10, 90 + 44, 17, EMPTY_NECKLACE_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.necklace"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_necklace_slot_enabled.get() && serverConfig.inventorySlots.is_necklace_mannequin_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_necklace_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_necklace_item_taking.get();
			}

		});

		// 72 ring 1 slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.RING_1, 11, 90 + 62, 35, EMPTY_RING_1_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.ring_1"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_ring_1_slot_enabled.get() && serverConfig.inventorySlots.is_ring_1_mannequin_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_ring_1_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_ring_1_item_taking.get();
			}

		});

		// 73 ring 2 slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.RING_2, 12, 90 + 44, 35, EMPTY_RING_2_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.ring_2"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_ring_2_slot_enabled.get() && serverConfig.inventorySlots.is_ring_2_mannequin_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_ring_2_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_ring_2_item_taking.get();
			}

		});

		// 74 shoulders slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SHOULDERS, 13, 90 + 26, 17, EMPTY_SHOULDERS_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.shoulders"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_shoulders_slot_enabled.get() && serverConfig.inventorySlots.is_shoulders_mannequin_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_shoulders_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_shoulders_item_taking.get();
			}

		});

		// 75 spell 1 slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_1, 14, 90 + 8, 89, EMPTY_SPELL_1_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.spell_1"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_spell_1_mannequin_slot_enabled.get();
			}

		});

		// 76 spell 2 slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_2, 15, 90 + 26, 89, EMPTY_SPELL_2_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.spell_2"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_spell_2_mannequin_slot_enabled.get();
			}

		});

		// 77 spell 3 slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_3, 16, 90 + 44, 89, EMPTY_SPELL_3_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.spell_3"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_spell_3_mannequin_slot_enabled.get();
			}

		});

		// 78 spell 4 slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_4, 17, 90 + 62, 89, EMPTY_SPELL_4_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.spell_4"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_spell_4_mannequin_slot_enabled.get();
			}

		});

		// 79 spell 5 slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_5, 18, 90 + 8, 107, EMPTY_SPELL_5_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.spell_5"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_spell_5_mannequin_slot_enabled.get();
			}

		});

		// 80 spell 6 slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_6, 19, 90 + 26, 107, EMPTY_SPELL_6_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.spell_6"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_spell_6_mannequin_slot_enabled.get();
			}

		});

		// 81 spell 7 slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_7, 20, 90 + 44, 107, EMPTY_SPELL_7_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.spell_7"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_spell_7_mannequin_slot_enabled.get();
			}

		});

		// 82 spell 8 slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.SPELL_8, 21, 90 + 62, 107, EMPTY_SPELL_8_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.spell_8"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_spell_8_mannequin_slot_enabled.get();
			}

		});

		// 83 relic slot
		this.addSlot(new MannequinSlot(inventory, AbstractMannequinScreenHandler.this.owner, ExtendedEquipmentSlot.RELIC, 22, 90 + 62, 17, EMPTY_RELIC_SLOT, canChangeInventory, List.of(Component.translatable("slot.tooltip.relic"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && serverConfig.activate_rpg_inventory_screen.get() && serverConfig.inventorySlots.is_relic_slot_enabled.get() && serverConfig.inventorySlots.is_relic_mannequin_slot_enabled.get();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return super.mayPlace(stack) && serverConfig.inventorySlots.allow_relic_item_insertion.get();
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return super.mayPickup(playerEntity) && serverConfig.inventorySlots.allow_relic_item_taking.get();
			}

		});

	}

	public Inventory getPlayerInventory() {
		return this.playerInventory;
	}

	public boolean canEquip() {
		return this.canEquip;
	}

	private void equip(Player player) {

		// regular armor
		for (int i = 0; i < 4; i++) {
			equipSingleSlot(MANNEQUIN_SLOTS_START + i, EQUIPMENT_SLOTS_START + i);
		}

		if (((DuckLivingEntityMixin) AbstractMannequinScreenHandler.this.owner).rpginventory$isOffhandStackSheathed()) {
			// sheathed offhand
			equipSingleSlot(MANNEQUIN_SLOTS_START + 4, EQUIPMENT_SLOTS_START + 7);
		} else {
			// offhand
			equipSingleSlot(MANNEQUIN_SLOTS_START + 4, EQUIPMENT_SLOTS_START + 4);
		}

		if (((DuckLivingEntityMixin) AbstractMannequinScreenHandler.this.owner).rpginventory$isHandStackSheathed()) {
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
		ItemStack equipmentStack = this.slots.get(player_index).getItem();
		ItemStack mannequinStack = this.slots.get(mannequin_index).getItem();

		if (!mannequinStack.isEmpty() && (equipmentStack.isEmpty() || equipmentStack.has(RPGInventory.LOAD_OUT_ITEM))) {
			ItemStack newStack = mannequinStack.copy();
			newStack.set(RPGInventory.LOAD_OUT_ITEM, Unit.INSTANCE);
			if (RPGInventory.SERVER_CONFIG.should_keep_loadout_items_on_death.get()) {
				newStack.set(RPGInventory.IS_KEPT_ON_DEATH, Unit.INSTANCE);
			} else {
				newStack.set(RPGInventory.IS_DESTROYED_ON_DEATH, Unit.INSTANCE);
			}
			this.slots.get(player_index).setByPlayer(newStack);
		}
	}

	private void unequip(Player player) {

		for (int i = 0; i < MannequinBlockEntity.INVENTORY_SIZE + 2; i++) {
			unequipSingleSlot(EQUIPMENT_SLOTS_START + i);
		}
	}

	private void unequipSingleSlot(int player_index) {
		ItemStack itemStack = this.slots.get(player_index).getItem();
		if (!itemStack.isEmpty() && itemStack.has(RPGInventory.LOAD_OUT_ITEM)) {
			this.slots.get(player_index).setByPlayer(ItemStack.EMPTY);
		}
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (id == 0) {
			this.equip(player);

		} else if (id == 1) {

			this.unequip(player);

		} else {
			Util.logAndPauseIfInIde(player.getName() + " pressed invalid button id: " + id);
			return false;
		}
		return true;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.inventory.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = slots.get(slotIndex);
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

		if (slot.hasItem()) {
			ItemStack itemStack1 = slot.getItem();
			itemStack = itemStack1.copy();
			EquipmentSlot equipmentSlot = player.getEquipmentSlotForItem(itemStack1);
			if (slotIndex >= 36 && slotIndex < 82) {
				if (itemStack.has(RPGInventory.LOAD_OUT_ITEM)) {
					slot.setByPlayer(ItemStack.EMPTY);
					slot.setChanged();
					return ItemStack.EMPTY;
				} else if (!this.moveItemStackTo(itemStack1, 0, 36, false)) {   // TODO adventure hotbar items
					return ItemStack.EMPTY;
				} else {
					return ItemStack.EMPTY;
				}
			} else if (slotIndex >= 0 && slotIndex < 36) {
				if (equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && !this.slots.get(63 - equipmentSlot.getIndex()).hasItem()) {
					int i = 63 - equipmentSlot.getIndex();
					if (!this.moveItemStackTo(itemStack1, i, i + 1, false)) {
						return ItemStack.EMPTY;
					}
				}

				if (RPGInventory.isHandSlotOverhaulActive()) {

					if (!itemStack1.isEmpty() && (!serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || itemStack1.is(Tags.HAND_ITEMS)) && !this.slots.get(65).hasItem()) {
						if (!this.moveItemStackTo(itemStack1, 65, 66, false)) {
							return ItemStack.EMPTY;
						}
					}

					if (!itemStack1.isEmpty() && (equipmentSlot == EquipmentSlot.OFFHAND || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || itemStack1.is(Tags.OFFHAND_ITEMS)) && !this.slots.get(64).hasItem()) {
						if (!this.moveItemStackTo(itemStack1, 64, 65, false)) {
							return ItemStack.EMPTY;
						}
					}

					if (serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get()) {
						if (!itemStack1.isEmpty() && (!serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || itemStack1.is(Tags.HAND_ITEMS))) {
							if (!this.slots.get(66).hasItem()) {
								if (!this.moveItemStackTo(itemStack1, 66, 67, false)) {
									return ItemStack.EMPTY;
								}
							}
						}

						if (!itemStack1.isEmpty() && (equipmentSlot == EquipmentSlot.OFFHAND || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || itemStack1.is(Tags.OFFHAND_ITEMS))) {
							if (!this.slots.get(67).hasItem()) {
								if (!this.moveItemStackTo(itemStack1, 67, 68, false)) {
									return ItemStack.EMPTY;
								}
							}
						}
					}
				} else if (!itemStack1.isEmpty() && (equipmentSlot == EquipmentSlot.OFFHAND || itemStack1.is(Tags.OFFHAND_ITEMS)) && !this.slots.get(64).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 64, 65, false)) {
						return ItemStack.EMPTY;
					}
				}

				// belt slot 51
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.BELT || itemStack1.is(Tags.BELTS)) && !this.slots.get(68).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 68, 69, false)) {
						return ItemStack.EMPTY;
					}
				}

				// gloves slot 52
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.GLOVES || itemStack1.is(Tags.GLOVES)) && !this.slots.get(69).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 69, 70, false)) {
						return ItemStack.EMPTY;
					}
				}

				// necklace slot 53
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.NECKLACE || itemStack1.is(Tags.NECKLACES)) && !this.slots.get(70).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 70, 71, false)) {
						return ItemStack.EMPTY;
					}
				}

				// ring 1 slot 54
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.RING_1 || itemStack1.is(Tags.RINGS_1)) && !this.slots.get(71).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 71, 72, false)) {
						return ItemStack.EMPTY;
					}
				}

				// ring 2 slot 55
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.RING_2 || itemStack1.is(Tags.RINGS_2)) && !this.slots.get(72).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 72, 73, false)) {
						return ItemStack.EMPTY;
					}
				}

				// shoulders slot 56
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.SHOULDERS || itemStack1.is(Tags.SHOULDERS)) && !this.slots.get(73).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 73, 74, false)) {
						return ItemStack.EMPTY;
					}
				}

				// spell 1 slot 57
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.SPELL_1 || itemStack1.is(Tags.SPELLS_1)) && !this.slots.get(74).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 74, 75, false)) {
						return ItemStack.EMPTY;
					}
				}

				// spell 2 slot 58
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.SPELL_2 || itemStack1.is(Tags.SPELLS_2)) && !this.slots.get(75).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 75, 76, false)) {
						return ItemStack.EMPTY;
					}
				}

				// spell 3 slot 59
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.SPELL_3 || itemStack1.is(Tags.SPELLS_3)) && !this.slots.get(76).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 76, 77, false)) {
						return ItemStack.EMPTY;
					}
				}

				// spell 4 slot 60
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.SPELL_4 || itemStack1.is(Tags.SPELLS_4)) && !this.slots.get(77).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 77, 78, false)) {
						return ItemStack.EMPTY;
					}
				}

				// spell 5 slot 61
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.SPELL_5 || itemStack1.is(Tags.SPELLS_5)) && !this.slots.get(78).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 78, 79, false)) {
						return ItemStack.EMPTY;
					}
				}

				// spell 6 slot 62
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.SPELL_6 || itemStack1.is(Tags.SPELLS_6)) && !this.slots.get(79).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 79, 80, false)) {
						return ItemStack.EMPTY;
					}
				}

				// spell 7 slot 63
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.SPELL_7 || itemStack1.is(Tags.SPELLS_7)) && !this.slots.get(80).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 80, 81, false)) {
						return ItemStack.EMPTY;
					}
				}

				// spell 8 slot 64
				if (!itemStack1.isEmpty() && (equipmentSlot == ExtendedEquipmentSlot.SPELL_8 || itemStack1.is(Tags.SPELLS_8)) && !this.slots.get(81).hasItem()) {
					if (!this.moveItemStackTo(itemStack1, 81, 82, false)) {
						return ItemStack.EMPTY;
					}
				}
//			} else if (slot >= 45 && slot < 51 && RPGInventory.isHandSlotOverhaulActive()) {
//				if (!this.insertItem(itemStack1, 9, 45, false)) {   // TODO adventure hotbar items
//					cir.setReturnValue(ItemStack.EMPTY);
//					cir.cancel();
//				} else {
//					cir.setReturnValue(itemStack1);
//					cir.cancel();
//				}
			}

			if (itemStack1.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY, itemStack);
			} else {
				slot.setChanged();
			}

			if (itemStack1.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}
		}
		return itemStack;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.inventory.stopOpen(player);
	}

	public record MannequinBlockData(
			BlockPos blockPos,
			boolean canChangeInventory,
			boolean canEquip
	) {

		public static final StreamCodec<RegistryFriendlyByteBuf, MannequinBlockData> PACKET_CODEC = StreamCodec.ofMember(MannequinBlockData::write, MannequinBlockData::new);

		public MannequinBlockData(RegistryFriendlyByteBuf registryByteBuf) {
			this(
					registryByteBuf.readBlockPos(),
					registryByteBuf.readBoolean(),
					registryByteBuf.readBoolean()
			);
		}

		private void write(RegistryFriendlyByteBuf registryByteBuf) {
			registryByteBuf.writeBlockPos(blockPos);
			registryByteBuf.writeBoolean(canChangeInventory);
			registryByteBuf.writeBoolean(canEquip);
		}
	}

	static {
		EMPTY_ARMOR_SLOT_TEXTURES = Map.of(
				EquipmentSlot.HEAD,
				InventoryMenu.EMPTY_ARMOR_SLOT_HELMET,
				EquipmentSlot.CHEST,
				InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
				EquipmentSlot.LEGS,
				InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
				EquipmentSlot.FEET,
				InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS
		);
		EQUIPMENT_SLOT_ORDER = new EquipmentSlot[]{
				EquipmentSlot.HEAD,
				EquipmentSlot.CHEST,
				EquipmentSlot.LEGS,
				EquipmentSlot.FEET
		};
		ARMOR_SLOT_TOOLTIPS = List.of(
				List.of(Component.translatable("slot.tooltip.head")),
				List.of(Component.translatable("slot.tooltip.chest")),
				List.of(Component.translatable("slot.tooltip.legs")),
				List.of(Component.translatable("slot.tooltip.feet"))
		);
	}
}
