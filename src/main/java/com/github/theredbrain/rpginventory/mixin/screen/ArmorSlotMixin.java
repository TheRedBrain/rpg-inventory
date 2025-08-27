package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(targets = {"net/minecraft/screen/slot/ArmorSlot"})
public abstract class ArmorSlotMixin extends Slot {

	@Shadow
	@Final
	private LivingEntity entity;

	@Shadow
	@Final
	private EquipmentSlot equipmentSlot;

	public ArmorSlotMixin(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@WrapMethod(method = "canInsert")
	public boolean rpginventory$canInsert(ItemStack stack, Operation<Boolean> original) {
		Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
		boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && this.entity.hasStatusEffect(civilisation_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
		boolean hasWildernessEffect = wilderness_status_effect.isPresent() && this.entity.hasStatusEffect(wilderness_status_effect.get());

		boolean isOwned = true;
		boolean isCreative = false;
		if (entity instanceof PlayerEntity playerEntity) {
			isOwned = ItemUtils.isUsableByPlayer(stack, playerEntity);
			isCreative = playerEntity.isCreative();
		}

		return (original.call(stack) || rpginventory$isOfEquipmentTag(stack, this.equipmentSlot)) && isOwned && (hasCivilisationEffect || isCreative || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !hasWildernessEffect));
	}

	@WrapMethod(method = "canTakeItems")
	public boolean rpginventory$canTakeItems(PlayerEntity playerEntity, Operation<Boolean> original) {
		Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
		boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && this.entity.hasStatusEffect(civilisation_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
		boolean hasWildernessEffect = wilderness_status_effect.isPresent() && this.entity.hasStatusEffect(wilderness_status_effect.get());

		boolean isCreative = playerEntity.isCreative();

		return original.call(playerEntity) && !this.getStack().contains(RPGInventory.LOAD_OUT_ITEM) && (hasCivilisationEffect || isCreative || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !hasWildernessEffect));
	}

	@Unique
	private boolean rpginventory$isOfEquipmentTag(ItemStack itemStack, EquipmentSlot slot) {
		return switch (slot) {
			case FEET -> itemStack.isIn(Tags.BOOTS);
			case LEGS -> itemStack.isIn(Tags.LEGGINGS);
			case CHEST -> itemStack.isIn(Tags.CHEST_PLATES);
			case HEAD -> itemStack.isIn(Tags.HELMETS);
			default -> false;
		};
	}
}
