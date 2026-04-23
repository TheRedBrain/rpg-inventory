package com.github.theredbrain.rpginventory.compat;

import net.minecraft.world.entity.player.Player;

public class SpellEngineCompat {

//	public static final SpellContainerSource.Entry RPG_EQUIPMENT = new SpellContainerSource.Entry("rpg_equipment", (player, sourceName) -> {
//		List<SpellContainerSource.SourcedContainer> sources = new ArrayList<SpellContainerSource.SourcedContainer>();
//		for (ItemStack stack : ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSpellProvidingEquipmentItems()) {
//			addSourceIfValid(stack, sources, sourceName);
//		}
//		return sources;
//	}, player -> ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSpellProvidingEquipmentItems());

	public static void init() {
//		ContainerCompat.addProvider((playerEntity) -> ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getSpellProvidingEquipmentItems());
//		SpellContainerSource.addSource(RPG_EQUIPMENT);
	}

//	private static void addSourceIfValid(ItemStack fromItemStack, List<SpellContainerSource.SourcedContainer> sources, String name) {
//		SpellContainer container = SpellContainerHelper.containerFromItemStack(fromItemStack);
//		if (container != null && container.isValid()) {
//			sources.add(new SpellContainerSource.SourcedContainer(name, fromItemStack, container));
//		}
//	}

	public static void resetSpellCooldowns(Player playerEntity) {
//		if (playerEntity instanceof ServerPlayer serverPlayerEntity) {
//			SpellCooldownManager spellCooldownManager = ((SpellCasterEntity) serverPlayerEntity).getCooldownManager();
//			for (Holder.Reference<Spell> spell : SpellRegistry.stream(serverPlayerEntity.level()).toList()) {
//				spellCooldownManager.remove(spell.key().location());
//			}
//		}
	}

	public static boolean doesCurrentPlayerStatusPreventHandSlotAction(Player playerEntity) {
//		return ((SpellCasterEntity) playerEntity).isCastingSpell();
		return false;
	}

	public static void configureEffects() {
//		ActionImpairing.configure(StatusEffectsRegistry.NEEDS_TWO_HANDING, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(true), ExtendedEntityActionsAllowedSemanticType.NEEDS_TWO_HANDING));
//		ActionImpairing.configure(StatusEffectsRegistry.NO_ATTACK_ITEM, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, true, true), new EntityActionsAllowed.MobsAllowed(true), ExtendedEntityActionsAllowedSemanticType.NO_ATTACK_ITEM));
	}
}
