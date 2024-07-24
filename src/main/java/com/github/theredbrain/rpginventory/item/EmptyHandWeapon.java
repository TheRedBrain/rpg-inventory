package com.github.theredbrain.rpginventory.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.item.Item;

public class EmptyHandWeapon extends Item {

	public EmptyHandWeapon(Settings settings) {
		super(settings.component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(false)));
	}
}
