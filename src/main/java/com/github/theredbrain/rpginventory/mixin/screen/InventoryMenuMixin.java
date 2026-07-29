package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.PlayerEntityHelper;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.screen.DuckPlayerScreenHandlerMixin;
import com.github.theredbrain.rpginventory.screen.slot.AlternativeHandSlot;
import com.github.theredbrain.rpginventory.screen.slot.CustomArmorSlot;
import com.github.theredbrain.rpginventory.screen.slot.CustomHandSlot;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends AbstractContainerMenu implements DuckPlayerScreenHandlerMixin {
	@Unique
	private static final Identifier EMPTY_HAND_SLOT = RPGInventory.identifier("container/slot/empty_slot_hand");
	@Unique
	private static final Identifier EMPTY_ALTERNATIVE_HAND_SLOT = RPGInventory.identifier("container/slot/empty_slot_alternative_hand");
	@Unique
	private static final Identifier EMPTY_ALTERNATIVE_OFFHAND_SLOT = RPGInventory.identifier("container/slot/empty_slot_alternative_offhand");
	@Unique
	private static final Identifier EMPTY_BELT_SLOT = RPGInventory.identifier("container/slot/empty_slot_belt");
	@Unique
	private static final Identifier EMPTY_GLOVES_SLOT = RPGInventory.identifier("container/slot/empty_slot_gloves");
	@Unique
	private static final Identifier EMPTY_NECKLACE_SLOT = RPGInventory.identifier("container/slot/empty_slot_necklace");
	@Unique
	private static final Identifier EMPTY_RING_1_SLOT = RPGInventory.identifier("container/slot/empty_slot_ring_1");
	@Unique
	private static final Identifier EMPTY_RING_2_SLOT = RPGInventory.identifier("container/slot/empty_slot_ring_2");
	@Unique
	private static final Identifier EMPTY_SHOULDERS_SLOT = RPGInventory.identifier("container/slot/empty_slot_shoulders");
	@Unique
	private static final Identifier EMPTY_SPELL_1_SLOT = RPGInventory.identifier("container/slot/empty_slot_spell_1");
	@Unique
	private static final Identifier EMPTY_SPELL_2_SLOT = RPGInventory.identifier("container/slot/empty_slot_spell_2");
	@Unique
	private static final Identifier EMPTY_SPELL_3_SLOT = RPGInventory.identifier("container/slot/empty_slot_spell_3");
	@Unique
	private static final Identifier EMPTY_SPELL_4_SLOT = RPGInventory.identifier("container/slot/empty_slot_spell_4");
	@Unique
	private static final Identifier EMPTY_SPELL_5_SLOT = RPGInventory.identifier("container/slot/empty_slot_spell_5");
	@Unique
	private static final Identifier EMPTY_SPELL_6_SLOT = RPGInventory.identifier("container/slot/empty_slot_spell_6");
	@Unique
	private static final Identifier EMPTY_SPELL_7_SLOT = RPGInventory.identifier("container/slot/empty_slot_spell_7");
	@Unique
	private static final Identifier EMPTY_SPELL_8_SLOT = RPGInventory.identifier("container/slot/empty_slot_spell_8");
	@Unique
	private static final Identifier EMPTY_RELIC_SLOT = RPGInventory.identifier("container/slot/empty_slot_relic");

	@Unique
	private boolean isAttributeScreenVisible = false;

	public InventoryMenuMixin() {
		super(null, 0);
	}

	/**
	 * @author TheRedBrain
	 */
	@Inject(method = "<init>", at = @At("TAIL"))
	public void PlayerScreenHandler(Inventory inventory, boolean active, Player owner, CallbackInfo ci) {

		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;
		boolean isRPGInventoryScreenActivated = serverConfig.activate_rpg_inventory_screen.get();

		if (isRPGInventoryScreenActivated) {

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
		}

		// main hand slot 46
		this.addSlot(new CustomHandSlot(inventory, owner, EquipmentSlot.MAINHAND, 43, serverConfig.inventorySlots.hand_slot_x_offset.get(), serverConfig.inventorySlots.hand_slot_y_offset.get(), EMPTY_HAND_SLOT, List.of(Component.translatable("slot.tooltip.hand")), false) {

			@Override
			public boolean isActive() {
				return isRPGInventoryScreenActivated && !((DuckLivingEntityMixin) owner).rpginventory$isHandStackSheathed() && ((DuckPlayerEntityMixin) owner).rpginventory$isHandSlotOverhaulActive();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				return (stack.is(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, owner) && (stack.has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || owner.hasEffect(RPGInventory.CIVILISATION) || owner.isCreative() || (serverConfig.allow_equipment_changes.get() && !owner.hasEffect(RPGInventory.WILDERNESS))) && !((DuckLivingEntityMixin) owner).rpginventory$isHandStackSheathed();
			}

		});

		// sheathed main hand slot 47
		this.addSlot(new CustomHandSlot(inventory, owner, ExtendedEquipmentSlot.SHEATHED_HAND, ExtendedEquipmentSlot.SHEATHED_HAND.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.hand_slot_x_offset.get(), serverConfig.inventorySlots.hand_slot_y_offset.get(), EMPTY_HAND_SLOT, List.of(Component.translatable("slot.tooltip.hand")), false) {

			@Override
			public boolean isActive() {
				return isRPGInventoryScreenActivated && ((DuckLivingEntityMixin) owner).rpginventory$isHandStackSheathed() && ((DuckPlayerEntityMixin) owner).rpginventory$isHandSlotOverhaulActive();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				return (stack.is(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, owner) && (stack.has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || owner.hasEffect(RPGInventory.CIVILISATION) || owner.isCreative() || (serverConfig.allow_equipment_changes.get() && !owner.hasEffect(RPGInventory.WILDERNESS))) && ((DuckLivingEntityMixin) owner).rpginventory$isHandStackSheathed();
			}

		});

		// sheathed offhand slot 48
		this.addSlot(new CustomHandSlot(inventory, owner, ExtendedEquipmentSlot.SHEATHED_OFF_HAND, ExtendedEquipmentSlot.SHEATHED_OFF_HAND.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.offhand_slot_x_offset.get(), serverConfig.inventorySlots.offhand_slot_y_offset.get(), InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD, List.of(Component.translatable("slot.tooltip.offhand")), false) {

			@Override
			public boolean isActive() {
				return isRPGInventoryScreenActivated && ((DuckLivingEntityMixin) owner).rpginventory$isOffhandStackSheathed() && ((DuckPlayerEntityMixin) owner).rpginventory$isHandSlotOverhaulActive();
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				return (EquipmentSlot.OFFHAND == owner.getEquipmentSlotForItem(stack) || stack.is(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, owner) && (stack.has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || owner.hasEffect(RPGInventory.CIVILISATION) || owner.isCreative() || (serverConfig.allow_equipment_changes.get() && !owner.hasEffect(RPGInventory.WILDERNESS))) && ((DuckLivingEntityMixin) owner).rpginventory$isOffhandStackSheathed();
			}

		});

		// alternative main hand slot 49
		this.addSlot(new AlternativeHandSlot(inventory, owner, ExtendedEquipmentSlot.ALTERNATIVE_HAND, ExtendedEquipmentSlot.ALTERNATIVE_HAND.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.alternative_hand_slot_x_offset.get(), serverConfig.inventorySlots.alternative_hand_slot_y_offset.get(), EMPTY_ALTERNATIVE_HAND_SLOT, List.of(Component.translatable("slot.tooltip.alternative_hand")), false) {

			@Override
			public boolean mayPlace(ItemStack stack) {
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				return (stack.is(Tags.HAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, owner) && (stack.has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || owner.hasEffect(RPGInventory.CIVILISATION) || owner.isCreative() || (serverConfig.allow_equipment_changes.get() && !owner.hasEffect(RPGInventory.WILDERNESS)));
			}

			@Override
			public boolean isActive() {
				return isRPGInventoryScreenActivated && ((DuckPlayerEntityMixin) owner).rpginventory$isHandSlotOverhaulActive() && serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get();
			}

		});

		// alternative offhand slot 50
		this.addSlot(new AlternativeHandSlot(inventory, owner, ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND, ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.alternative_offhand_slot_x_offset.get(), serverConfig.inventorySlots.alternative_offhand_slot_y_offset.get(), EMPTY_ALTERNATIVE_OFFHAND_SLOT, List.of(Component.translatable("slot.tooltip.alternative_offhand")), false) {

			@Override
			public boolean mayPlace(ItemStack stack) {
				ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

				return (stack.is(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, owner) && (stack.has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || owner.hasEffect(RPGInventory.CIVILISATION) || owner.isCreative() || (serverConfig.allow_equipment_changes.get() && !owner.hasEffect(RPGInventory.WILDERNESS)));
			}

			@Override
			public boolean isActive() {
				return isRPGInventoryScreenActivated && ((DuckPlayerEntityMixin) owner).rpginventory$isHandSlotOverhaulActive() && serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get();
			}

		});

		// belt slot 51
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.BELT, ExtendedEquipmentSlot.BELT.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.belt_slot_x_offset.get(), serverConfig.inventorySlots.belt_slot_y_offset.get(), EMPTY_BELT_SLOT, List.of(Component.translatable("slot.tooltip.belt"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && serverConfig.inventorySlots.is_belt_slot_enabled.get();
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

		// gloves slot 52
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.GLOVES, ExtendedEquipmentSlot.GLOVES.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.gloves_slot_x_offset.get(), serverConfig.inventorySlots.gloves_slot_y_offset.get(), EMPTY_GLOVES_SLOT, List.of(Component.translatable("slot.tooltip.gloves"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && serverConfig.inventorySlots.is_gloves_slot_enabled.get();
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

		// necklace slot 53
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.NECKLACE, ExtendedEquipmentSlot.NECKLACE.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.necklace_slot_x_offset.get(), serverConfig.inventorySlots.necklace_slot_y_offset.get(), EMPTY_NECKLACE_SLOT, List.of(Component.translatable("slot.tooltip.necklace"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && serverConfig.inventorySlots.is_necklace_slot_enabled.get();
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

		// ring 1 slot 54
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.RING_1, ExtendedEquipmentSlot.RING_1.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.ring_1_slot_x_offset.get(), serverConfig.inventorySlots.ring_1_slot_y_offset.get(), EMPTY_RING_1_SLOT, List.of(Component.translatable("slot.tooltip.ring_1"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && serverConfig.inventorySlots.is_ring_1_slot_enabled.get();
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

		// ring 2 slot 55
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.RING_2, ExtendedEquipmentSlot.RING_2.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.ring_2_slot_x_offset.get(), serverConfig.inventorySlots.ring_2_slot_y_offset.get(), EMPTY_RING_2_SLOT, List.of(Component.translatable("slot.tooltip.ring_2"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && serverConfig.inventorySlots.is_ring_2_slot_enabled.get();
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

		// shoulders slot 56
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SHOULDERS, ExtendedEquipmentSlot.SHOULDERS.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.shoulders_slot_x_offset.get(), serverConfig.inventorySlots.shoulders_slot_y_offset.get(), EMPTY_SHOULDERS_SLOT, List.of(Component.translatable("slot.tooltip.shoulders"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && serverConfig.inventorySlots.is_shoulders_slot_enabled.get();
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

		// spell 1 slot 57
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_1, ExtendedEquipmentSlot.SPELL_1.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.spell_1_slot_x_offset.get(), serverConfig.inventorySlots.spell_1_slot_y_offset.get(), EMPTY_SPELL_1_SLOT, List.of(Component.translatable("slot.tooltip.spell_1"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 1;
			}

		});

		// spell 2 slot 58
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_2, ExtendedEquipmentSlot.SPELL_2.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.spell_2_slot_x_offset.get(), serverConfig.inventorySlots.spell_2_slot_y_offset.get(), EMPTY_SPELL_2_SLOT, List.of(Component.translatable("slot.tooltip.spell_2"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 2;
			}

		});

		// spell 3 slot 59
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_3, ExtendedEquipmentSlot.SPELL_3.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.spell_3_slot_x_offset.get(), serverConfig.inventorySlots.spell_3_slot_y_offset.get(), EMPTY_SPELL_3_SLOT, List.of(Component.translatable("slot.tooltip.spell_3"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 3;
			}

		});

		// spell 4 slot 60
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_4, ExtendedEquipmentSlot.SPELL_4.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.spell_4_slot_x_offset.get(), serverConfig.inventorySlots.spell_4_slot_y_offset.get(), EMPTY_SPELL_4_SLOT, List.of(Component.translatable("slot.tooltip.spell_4"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 4;
			}

		});

		// spell 5 slot 61
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_5, ExtendedEquipmentSlot.SPELL_5.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.spell_5_slot_x_offset.get(), serverConfig.inventorySlots.spell_5_slot_y_offset.get(), EMPTY_SPELL_5_SLOT, List.of(Component.translatable("slot.tooltip.spell_5"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 5;
			}

		});

		// spell 6 slot 62
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_6, ExtendedEquipmentSlot.SHEATHED_OFF_HAND.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.spell_6_slot_x_offset.get(), serverConfig.inventorySlots.spell_6_slot_y_offset.get(), EMPTY_SPELL_6_SLOT, List.of(Component.translatable("slot.tooltip.spell_6"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 6;
			}

		});

		// spell 7 slot 63
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_7, ExtendedEquipmentSlot.SPELL_7.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.spell_7_slot_x_offset.get(), serverConfig.inventorySlots.spell_7_slot_y_offset.get(), EMPTY_SPELL_7_SLOT, List.of(Component.translatable("slot.tooltip.spell_7"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 7;
			}

		});

		// spell 8 slot 64
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.SPELL_8, ExtendedEquipmentSlot.SPELL_8.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.spell_8_slot_x_offset.get(), serverConfig.inventorySlots.spell_8_slot_y_offset.get(), EMPTY_SPELL_8_SLOT, List.of(Component.translatable("slot.tooltip.spell_8"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && (int) ((DuckPlayerEntityMixin) owner).rpginventory$getActiveSpellSlotAmount() >= 8;
			}

		});

		// relic slot 65
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.RELIC, ExtendedEquipmentSlot.RELIC.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), serverConfig.inventorySlots.relic_slot_x_offset.get(), serverConfig.inventorySlots.relic_slot_y_offset.get(), EMPTY_RELIC_SLOT, List.of(Component.translatable("slot.tooltip.relic"))) {

			@Override
			public boolean isActive() {
				return super.isActive() && isRPGInventoryScreenActivated && serverConfig.inventorySlots.is_relic_slot_enabled.get();
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

		// class item slot 66
		this.addSlot(new CustomArmorSlot(inventory, owner, ExtendedEquipmentSlot.CLASS_ITEM, ExtendedEquipmentSlot.CLASS_ITEM.getIndex(PlayerEntityHelper.EXTENDED_EQUIPMENT_SLOT_INDEX_OFFSET), 0, 0, EMPTY_RELIC_SLOT, List.of(Component.empty())) {

			@Override
			public boolean isActive() {
				return false;
			}

			@Override
			public boolean mayPlace(ItemStack stack) {
				return true;
			}

			@Override
			public boolean mayPickup(Player playerEntity) {
				return true;
			}

		});

		// adding slot tooltips
		List<Component> list5 = new ArrayList<>();
		list5.add(Component.translatable("slot.tooltip.head"));
		((SlotCustomization) this.slots.get(5)).slotcustomizationapi$setSlotTooltipText(list5);

		List<Component> list6 = new ArrayList<>();
		list6.add(Component.translatable("slot.tooltip.chest"));
		((SlotCustomization) this.slots.get(6)).slotcustomizationapi$setSlotTooltipText(list6);

		List<Component> list7 = new ArrayList<>();
		list7.add(Component.translatable("slot.tooltip.legs"));
		((SlotCustomization) this.slots.get(7)).slotcustomizationapi$setSlotTooltipText(list7);

		List<Component> list8 = new ArrayList<>();
		list8.add(Component.translatable("slot.tooltip.feet"));
		((SlotCustomization) this.slots.get(8)).slotcustomizationapi$setSlotTooltipText(list8);

		List<Component> list45 = new ArrayList<>();
		list45.add(Component.translatable("slot.tooltip.offhand"));
		((SlotCustomization) this.slots.get(45)).slotcustomizationapi$setSlotTooltipText(list45);

	}

//	@Inject(at = @At("HEAD"), method = "removed")
//	private void rpginventory$removed(Player player, CallbackInfo info) {
//		// TODO trigger adventure hotbar items check
//	}

	@WrapMethod(method = "quickMoveStack")
	private ItemStack rpginventory$wrap_quickMoveStack(Player player, int slotIndex, Operation<ItemStack> original) {

		if (RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {

			Slot rpginventory$slot = this.slots.get(slotIndex);
			ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;
			ItemStack rpginventory$stack = ItemStack.EMPTY;

//		// TODO adventure hotbar items
//		StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.statusEffects.civilisation_status_effect_identifier));
//		boolean owner.hasStatusEffect(RPGInventory.CIVILISATION) = civilisation_status_effect != null && player.hasStatusEffect(civilisation_status_effect);
//
//		StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.statusEffects.wilderness_status_effect_identifier));
//		boolean owner.hasStatusEffect(RPGInventory.WILDERNESS) = wilderness_status_effect != null && player.hasStatusEffect(wilderness_status_effect);
//
//		boolean canChangeEquipment = true;
//
//		if (player.getServer() != null) {
//			canChangeEquipment = player.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
//		}
//		owner.hasStatusEffect(RPGInventory.CIVILISATION) = owner.hasStatusEffect(RPGInventory.CIVILISATION) || (canChangeEquipment && !owner.hasStatusEffect(RPGInventory.WILDERNESS));

			if (rpginventory$slot.hasItem()) {
				ItemStack rpginventory$stack2 = rpginventory$slot.getItem();
				rpginventory$stack = rpginventory$stack2.copy();
				EquipmentSlot rpginventory$equipmentSlot = player.getEquipmentSlotForItem(rpginventory$stack2);
				if (slotIndex == 0) {
					if (!this.moveItemStackTo(rpginventory$stack2, 9, 45, true)) {
						return ItemStack.EMPTY;
					}

					rpginventory$slot.onQuickCraft(rpginventory$stack2, rpginventory$stack);
				} else if (slotIndex >= 1 && slotIndex < 5) {
					if (!this.moveItemStackTo(rpginventory$stack2, 9, 45, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slotIndex >= 5 && slotIndex < 9) {
					if (!this.moveItemStackTo(rpginventory$stack2, 9, 45, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slotIndex >= 44 && slotIndex < 66) {
					if (!this.moveItemStackTo(rpginventory$stack2, 9, 45, false)) {   // TODO adventure hotbar items
						return ItemStack.EMPTY;
					}
				} else if (slotIndex >= 9 && slotIndex < 45) {

					// helmet slot 5
					if ((rpginventory$equipmentSlot == EquipmentSlot.HEAD || rpginventory$stack2.is(Tags.HELMETS)) && !this.slots.get(5).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 5, 6, false)) {
							return ItemStack.EMPTY;
						}
					}

					// chestplate slot 5
					if ((rpginventory$equipmentSlot == EquipmentSlot.CHEST || rpginventory$stack2.is(Tags.CHEST_PLATES)) && !this.slots.get(6).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 6, 7, false)) {
							return ItemStack.EMPTY;
						}
					}

					// leggings slot 5
					if ((rpginventory$equipmentSlot == EquipmentSlot.LEGS || rpginventory$stack2.is(Tags.LEGGINGS)) && !this.slots.get(7).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 7, 8, false)) {
							return ItemStack.EMPTY;
						}
					}

					// boots slot 5
					if ((rpginventory$equipmentSlot == EquipmentSlot.FEET || rpginventory$stack2.is(Tags.BOOTS)) && !this.slots.get(8).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 8, 9, false)) {
							return ItemStack.EMPTY;
						}
					}

					// belt slot 51
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.BELT || rpginventory$stack2.is(Tags.BELTS)) && !this.slots.get(51).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 51, 52, false)) {
							return ItemStack.EMPTY;
						}
					}

					// gloves slot 52
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.GLOVES || rpginventory$stack2.is(Tags.GLOVES)) && !this.slots.get(52).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 52, 53, false)) {
							return ItemStack.EMPTY;
						}
					}

					// necklace slot 53
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.NECKLACE || rpginventory$stack2.is(Tags.NECKLACES)) && !this.slots.get(53).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 53, 54, false)) {
							return ItemStack.EMPTY;
						}
					}

					// ring 1 slot 54
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.RING_1 || rpginventory$stack2.is(Tags.RINGS_1)) && !this.slots.get(54).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 54, 55, false)) {
							return ItemStack.EMPTY;
						}
					}

					// ring 2 slot 55
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.RING_2 || rpginventory$stack2.is(Tags.RINGS_2)) && !this.slots.get(55).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 55, 56, false)) {
							return ItemStack.EMPTY;
						}
					}

					// shoulders slot 56
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.SHOULDERS || rpginventory$stack2.is(Tags.SHOULDERS)) && !this.slots.get(56).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 56, 57, false)) {
							return ItemStack.EMPTY;
						}
					}

					// spell 1 slot 57
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_1 || rpginventory$stack2.is(Tags.SPELLS_1)) && !this.slots.get(57).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 57, 58, false)) {
							return ItemStack.EMPTY;
						}
					}

					// spell 2 slot 58
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_2 || rpginventory$stack2.is(Tags.SPELLS_2)) && !this.slots.get(58).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 58, 59, false)) {
							return ItemStack.EMPTY;
						}
					}

					// spell 3 slot 59
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_3 || rpginventory$stack2.is(Tags.SPELLS_3)) && !this.slots.get(59).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 59, 60, false)) {
							return ItemStack.EMPTY;
						}
					}

					// spell 4 slot 60
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_4 || rpginventory$stack2.is(Tags.SPELLS_4)) && !this.slots.get(60).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 60, 61, false)) {
							return ItemStack.EMPTY;
						}
					}

					// spell 5 slot 61
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_5 || rpginventory$stack2.is(Tags.SPELLS_5)) && !this.slots.get(61).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 61, 62, false)) {
							return ItemStack.EMPTY;
						}
					}

					// spell 6 slot 62
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_6 || rpginventory$stack2.is(Tags.SPELLS_6)) && !this.slots.get(62).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 62, 63, false)) {
							return ItemStack.EMPTY;
						}
					}

					// spell 7 slot 63
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_7 || rpginventory$stack2.is(Tags.SPELLS_7)) && !this.slots.get(63).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 63, 64, false)) {
							return ItemStack.EMPTY;
						}
					}

					// spell 8 slot 64
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.SPELL_8 || rpginventory$stack2.is(Tags.SPELLS_8)) && !this.slots.get(64).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 64, 65, false)) {
							return ItemStack.EMPTY;
						}
					}

					// relic slot 65
					if ((rpginventory$equipmentSlot == ExtendedEquipmentSlot.RELIC || rpginventory$stack2.is(Tags.RELICS)) && !this.slots.get(65).hasItem()) {
						if (!this.moveItemStackTo(rpginventory$stack2, 65, 66, false)) {
							return ItemStack.EMPTY;
						}
					}

					if (((DuckPlayerEntityMixin) player).rpginventory$isHandSlotOverhaulActive()) {

						if (!serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || rpginventory$stack2.is(Tags.HAND_ITEMS)) {
							if (((DuckLivingEntityMixin) player).rpginventory$isHandStackSheathed() && !this.slots.get(47).hasItem()) {
								if (!this.moveItemStackTo(rpginventory$stack2, 47, 48, false)) {
									return ItemStack.EMPTY;
								}
							} else if (!((DuckLivingEntityMixin) player).rpginventory$isHandStackSheathed() && !this.slots.get(46).hasItem()) {
								if (!this.moveItemStackTo(rpginventory$stack2, 46, 47, false)) {
									return ItemStack.EMPTY;
								}
							}
						}

						if (rpginventory$equipmentSlot == EquipmentSlot.OFFHAND || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || rpginventory$stack2.is(Tags.OFFHAND_ITEMS)) {
							if (((DuckLivingEntityMixin) player).rpginventory$isOffhandStackSheathed() && !this.slots.get(48).hasItem()) {
								if (!this.moveItemStackTo(rpginventory$stack2, 48, 49, false)) {
									return ItemStack.EMPTY;
								}
							} else if (!((DuckLivingEntityMixin) player).rpginventory$isOffhandStackSheathed() && !this.slots.get(45).hasItem()) {
								if (!this.moveItemStackTo(rpginventory$stack2, 45, 46, false)) {
									return ItemStack.EMPTY;
								}
							}
						}

						if (serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get()) {
							if ((!serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || rpginventory$stack2.is(Tags.HAND_ITEMS)) && !this.slots.get(49).hasItem()) {
								if (!this.moveItemStackTo(rpginventory$stack2, 49, 50, false)) {
									return ItemStack.EMPTY;
								}
							}

							if ((rpginventory$equipmentSlot == EquipmentSlot.OFFHAND || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || rpginventory$stack2.is(Tags.OFFHAND_ITEMS)) && !this.slots.get(50).hasItem()) {
								if (!this.moveItemStackTo(rpginventory$stack2, 50, 51, false)) {
									return ItemStack.EMPTY;
								}
							}
						}
					} else {
						if ((rpginventory$equipmentSlot == EquipmentSlot.OFFHAND || rpginventory$stack2.is(Tags.OFFHAND_ITEMS)) && !this.slots.get(45).hasItem()) {
							if (!this.moveItemStackTo(rpginventory$stack2, 45, 46, false)) {
								return ItemStack.EMPTY;
							}
						}
					}
				} else if (!this.moveItemStackTo(rpginventory$stack2, 9, 45, false)) {
					return ItemStack.EMPTY;
				}

				if (rpginventory$stack2.isEmpty()) {
					rpginventory$slot.setByPlayer(ItemStack.EMPTY, rpginventory$stack);
				} else {
					rpginventory$slot.setChanged();
				}

				if (rpginventory$stack2.getCount() == rpginventory$stack.getCount()) {
					return ItemStack.EMPTY;
				}

				rpginventory$slot.onTake(player, rpginventory$stack2);
				if (slotIndex == 0) {
					player.drop(rpginventory$stack2, false);
				}
			}
			return rpginventory$stack;
		} else {
			return original.call(player, slotIndex);
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
