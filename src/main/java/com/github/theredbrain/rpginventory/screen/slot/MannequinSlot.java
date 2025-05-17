package com.github.theredbrain.rpginventory.screen.slot;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.component.type.ExtendedAttributeModifierSlot;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.screen.DuckSlotMixin;
import com.mojang.datafixers.util.Pair;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

public class MannequinSlot extends Slot {
	private final LivingEntity entity;
	private final EquipmentSlot equipmentSlot;
	@Nullable
	private final Identifier backgroundSprite;

	public MannequinSlot(Inventory inventory, LivingEntity entity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, List<Text> tooltip) {
		super(inventory, index, x, y);
		this.entity = entity;
		this.equipmentSlot = equipmentSlot;
		this.backgroundSprite = backgroundSprite;
		((DuckSlotMixin)this).rpginventory$setSlotTooltipText(tooltip);
	}

	@Override
	public int getMaxItemCount() {
		return 1;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		boolean hasPreventMannequinSlotInteractionEffect = false;
		for (StatusEffectInstance instance : this.entity.getStatusEffects()) {
			if (instance.getEffectType().isIn(RPGInventory.PREVENTS_MANNEQUIN_SLOT_INTERACTION)) {
				hasPreventMannequinSlotInteractionEffect = true;
				break;
			}
		}
		return (equipmentSlot == this.entity.getPreferredEquipmentSlot(stack) || ExtendedEquipmentSlot.rpginventory$isOfEquipmentTag(stack, equipmentSlot)) && !hasPreventMannequinSlotInteractionEffect;
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {
		boolean hasPreventMannequinSlotInteractionEffect = false;
		for (StatusEffectInstance instance : this.entity.getStatusEffects()) {
			if (instance.getEffectType().isIn(RPGInventory.PREVENTS_MANNEQUIN_SLOT_INTERACTION)) {
				hasPreventMannequinSlotInteractionEffect = true;
				break;
			}
		}
		return super.canTakeItems(playerEntity) && !hasPreventMannequinSlotInteractionEffect;
	}

	@Override
	public Pair<Identifier, Identifier> getBackgroundSprite() {
		return this.backgroundSprite != null ? Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, this.backgroundSprite) : super.getBackgroundSprite();
	}
}
