package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.compat.container.ContainerCompat;
import net.spell_engine.internals.SpellCooldownManager;
import net.spell_engine.internals.casting.SpellCasterEntity;
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
		ContainerCompat.addProvider((playerEntity) -> ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getSpellProvidingEquipmentItems());
		SpellContainerSource.addSource(RPG_EQUIPMENT);
	}

	private static void addSourceIfValid(ItemStack fromItemStack, List<SpellContainerSource.SourcedContainer> sources, String name) {
		SpellContainer container = SpellContainerHelper.containerFromItemStack(fromItemStack);
		if (container != null && container.isValid()) {
			sources.add(new SpellContainerSource.SourcedContainer(name, fromItemStack, container));
		}
	}

	public static void resetSpellCooldowns(PlayerEntity playerEntity) {
		if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
			SpellCooldownManager spellCooldownManager = ((SpellCasterEntity) serverPlayerEntity).getCooldownManager();
			for (RegistryEntry.Reference<Spell> spell : SpellRegistry.stream(serverPlayerEntity.getWorld()).toList()) {
				spellCooldownManager.remove(spell.registryKey().getValue());
			}
		}
	}

	public static boolean doesCurrentPlayerStatusPreventHandSlotAction(PlayerEntity playerEntity) {
		return ((SpellCasterEntity) playerEntity).isCastingSpell();
	}
}
