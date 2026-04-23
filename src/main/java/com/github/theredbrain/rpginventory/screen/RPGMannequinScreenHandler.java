package com.github.theredbrain.rpginventory.screen;

import com.github.theredbrain.rpginventory.block.entity.MannequinBlockEntity;
import com.github.theredbrain.rpginventory.registry.ScreenHandlerTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

public class RPGMannequinScreenHandler extends AbstractMannequinScreenHandler {

	public RPGMannequinScreenHandler(int syncId, Inventory playerInventory, MannequinBlockData data) {
		this(syncId, playerInventory, new SimpleContainer(MannequinBlockEntity.INVENTORY_SIZE), data.blockPos(), data.canChangeInventory(), data.canEquip());
	}

	public RPGMannequinScreenHandler(int syncId, Inventory playerInventory, Container inventory, BlockPos blockPos, boolean canChangeInventory, boolean canEquip) {
		super(ScreenHandlerTypesRegistry.RPG_MANNEQUIN_SCREEN_HANDLER, syncId, playerInventory, inventory, blockPos, canChangeInventory, canEquip);
	}
}
