package com.github.theredbrain.rpginventory.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;

public class EmptyHandWeapon extends Item {

	public EmptyHandWeapon(Properties settings) {
		super(settings.component(DataComponents.UNBREAKABLE, Unit.INSTANCE));
	}
}
