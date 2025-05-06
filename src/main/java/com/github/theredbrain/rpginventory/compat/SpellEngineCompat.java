package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import net.minecraft.item.ItemStack;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.spell_engine.compat.container.ContainerCompat;
import net.spell_engine.internals.container.SpellContainerSource;

import java.util.ArrayList;
import java.util.List;

public class SpellEngineCompat {

	public static final SpellContainerSource.Entry RPG_EQUIPMENT = new SpellContainerSource.Entry("rpg_equipment", (player, sourceName) -> {
		List<SpellContainerSource.SourcedContainer> sources = new ArrayList<SpellContainerSource.SourcedContainer>();
		for (ItemStack stack : ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSpellProvidingEquipmentItems()) {
			addSourceIfValid(stack, sources, sourceName);
		}
		return sources;
	}, player -> ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSpellProvidingEquipmentItems());

	public static void init() {
		if (RPGInventory.isSpellEngineLoaded) {
			ContainerCompat.addProvider((playerEntity) -> ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getSpellProvidingEquipmentItems());
			SpellContainerSource.addSource(RPG_EQUIPMENT);
		}
	}

	private static void addSourceIfValid(ItemStack fromItemStack, List<SpellContainerSource.SourcedContainer> sources, String name) {
		SpellContainer container = SpellContainerHelper.containerFromItemStack(fromItemStack);
		if (container != null && container.isValid()) {
			sources.add(new SpellContainerSource.SourcedContainer(name, fromItemStack, container));
		}
	}
}
