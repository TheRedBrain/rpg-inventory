package com.github.theredbrain.rpginventory.screen;

import com.github.theredbrain.rpginventory.block.entity.MannequinBlockEntity;
import com.github.theredbrain.rpginventory.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.util.math.BlockPos;

public class VanillaMannequinScreenHandler extends AbstractMannequinScreenHandler {

	public VanillaMannequinScreenHandler(int syncId, PlayerInventory playerInventory, MannequinBlockData data) {
		this(syncId, playerInventory, new SimpleInventory(MannequinBlockEntity.INVENTORY_SIZE), data.blockPos(), data.canChangeInventory(), data.canEquip());
	}

	public VanillaMannequinScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, BlockPos blockPos, boolean canChangeInventory, boolean canEquip) {
		super(ScreenHandlerTypesRegistry.VANILLA_MANNEQUIN_SCREEN_HANDLER, syncId, playerInventory, inventory, blockPos, canChangeInventory, canEquip);

		// 0 - 26
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				((SlotCustomization) this.slots.get(j + i * 9)).slotcustomizationapi$setY(84 + i * 18);
			}
		}

		// 27 - 35
		for (int i = 0; i < 9; i++) {
			((SlotCustomization) this.slots.get(27 + i)).slotcustomizationapi$setY(142);
		}

		((SlotCustomization) this.slots.get(36)).slotcustomizationapi$setX(20);
		((SlotCustomization) this.slots.get(36)).slotcustomizationapi$setY(18);

		((SlotCustomization) this.slots.get(37)).slotcustomizationapi$setX(38);
		((SlotCustomization) this.slots.get(37)).slotcustomizationapi$setY(18);

		((SlotCustomization) this.slots.get(38)).slotcustomizationapi$setX(20);
		((SlotCustomization) this.slots.get(38)).slotcustomizationapi$setY(36);

		((SlotCustomization) this.slots.get(39)).slotcustomizationapi$setX(38);
		((SlotCustomization) this.slots.get(39)).slotcustomizationapi$setY(36);

		((SlotCustomization) this.slots.get(40)).slotcustomizationapi$setX(56);
		((SlotCustomization) this.slots.get(40)).slotcustomizationapi$setY(27);

		for (int i = 41; i < 61; i++) {
			((SlotCustomization) this.slots.get(i)).slotcustomizationapi$setDisabledOverride(true);
		}

		((SlotCustomization) this.slots.get(61)).slotcustomizationapi$setX(104);
		((SlotCustomization) this.slots.get(61)).slotcustomizationapi$setY(18);

		((SlotCustomization) this.slots.get(62)).slotcustomizationapi$setX(122);
		((SlotCustomization) this.slots.get(62)).slotcustomizationapi$setY(18);

		((SlotCustomization) this.slots.get(63)).slotcustomizationapi$setX(104);
		((SlotCustomization) this.slots.get(63)).slotcustomizationapi$setY(36);

		((SlotCustomization) this.slots.get(64)).slotcustomizationapi$setX(122);
		((SlotCustomization) this.slots.get(64)).slotcustomizationapi$setY(36);

		((SlotCustomization) this.slots.get(65)).slotcustomizationapi$setX(140);
		((SlotCustomization) this.slots.get(65)).slotcustomizationapi$setY(27);

		for (int i = 66; i < 84; i++) {
			((SlotCustomization) this.slots.get(i)).slotcustomizationapi$setDisabledOverride(true);
		}
	}
}
