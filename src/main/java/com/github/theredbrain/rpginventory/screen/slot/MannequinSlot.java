package com.github.theredbrain.rpginventory.screen.slot;

import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import com.mojang.datafixers.util.Pair;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class MannequinSlot extends Slot {
	private final LivingEntity entity;
	private final EquipmentSlot equipmentSlot;
	@Nullable
	private final Identifier backgroundSprite;
	private final boolean canChangeInventory;

	public MannequinSlot(Container inventory, LivingEntity entity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, boolean canChangeInventory, List<Component> tooltip) {
		super(inventory, index, x, y);
		this.entity = entity;
		this.equipmentSlot = equipmentSlot;
		this.backgroundSprite = backgroundSprite;
		this.canChangeInventory = canChangeInventory;
		((SlotCustomization) this).slotcustomizationapi$setSlotTooltipText(tooltip);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		boolean hasPreventMannequinSlotInteractionEffect = false;
		for (MobEffectInstance instance : this.entity.getActiveEffects()) {
			if (instance.getEffect().is(Tags.PREVENTS_MANNEQUIN_SLOT_INTERACTION)) {
				hasPreventMannequinSlotInteractionEffect = true;
				break;
			}
		}
		return (equipmentSlot == this.entity.getEquipmentSlotForItem(stack) || ExtendedEquipmentSlot.rpginventory$isOfEquipmentTag(stack, equipmentSlot)) && !hasPreventMannequinSlotInteractionEffect && this.canChangeInventory;
	}

	@Override
	public boolean mayPickup(Player playerEntity) {
		boolean hasPreventMannequinSlotInteractionEffect = false;
		for (MobEffectInstance instance : this.entity.getActiveEffects()) {
			if (instance.getEffect().is(Tags.PREVENTS_MANNEQUIN_SLOT_INTERACTION)) {
				hasPreventMannequinSlotInteractionEffect = true;
				break;
			}
		}
		return super.mayPickup(playerEntity) && !hasPreventMannequinSlotInteractionEffect && this.canChangeInventory;
	}

	@Override
	@Nullable
	public Identifier getNoItemIcon() {
		return this.backgroundSprite;
	}
}
