package com.github.theredbrain.rpginventory.screen;

import com.github.theredbrain.rpginventory.block.entity.MannequinBlockEntity;
import com.github.theredbrain.rpginventory.registry.ScreenHandlerTypesRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.util.math.BlockPos;

public class RPGMannequinScreenHandler extends AbstractMannequinScreenHandler {

	public RPGMannequinScreenHandler(int syncId, PlayerInventory playerInventory, MannequinBlockData data) {
		this(syncId, playerInventory, new SimpleInventory(MannequinBlockEntity.INVENTORY_SIZE), data.blockPos(), data.canChangeInventory(), data.canEquip());
	}

	public RPGMannequinScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, BlockPos blockPos, boolean canChangeInventory, boolean canEquip) {
		super(ScreenHandlerTypesRegistry.RPG_MANNEQUIN_SCREEN_HANDLER, syncId, playerInventory, inventory, blockPos, canChangeInventory, canEquip);
	}
}
