package com.github.theredbrain.rpginventory.compat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class TrinketsCompat {

	public static boolean isTrinketEquipped(LivingEntity livingEntity, Predicate<ItemStack> itemStackPredicate) {

//		boolean bl = false;
//
//		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(livingEntity);
//		if (trinkets.isPresent()) {
//			bl = trinkets.get().isEquipped(itemStackPredicate);
//		}
//		return bl;
		return false;
	}

	public static void breakKeepInventoryTrinkets(LivingEntity livingEntity) {
//		Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(livingEntity);
//		if (trinkets.isPresent()) {
//			List<Tuple<SlotReference, ItemStack>> trinketList = trinkets.get().getAllEquipped();
//			for (net.minecraft.util.Tuple<SlotReference, ItemStack> trinket : trinketList) {
//				if (trinket.getB().is(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
//					trinket.getA().inventory().clearContent();
//				}
//			}
//		}
	}

	//
//	static {
//		TrinketsApi.registerTrinketPredicate(RPGInventory.identifier("can_change_equipment"), (stack, ref, entity) -> {
//
//			if (entity.hasEffect(RPGInventory.CIVILISATION)
//					|| entity instanceof Player playerEntity && playerEntity.isCreative()
//					|| entity.getServer() == null
//					|| (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !entity.hasEffect(RPGInventory.WILDERNESS))) {
//				return TriState.TRUE;
//			}
//			return TriState.FALSE;
//		});
//	}
//
	public static void init() {
	}
}
