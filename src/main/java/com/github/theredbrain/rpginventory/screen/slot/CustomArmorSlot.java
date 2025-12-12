package com.github.theredbrain.rpginventory.screen.slot;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import com.mojang.datafixers.util.Pair;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CustomArmorSlot extends Slot {
	private final PlayerEntity owner;
	private final EquipmentSlot equipmentSlot;
	@Nullable
	private final Identifier backgroundSprite;
	private final boolean allowsLoadoutItemRemoval;

	public CustomArmorSlot(Inventory inventory, PlayerEntity playerEntity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, List<Text> tooltip) {
		this(inventory, playerEntity, equipmentSlot, index, x, y, backgroundSprite, tooltip, false);
	}

	public CustomArmorSlot(Inventory inventory, PlayerEntity playerEntity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, List<Text> tooltip, boolean allowsLoadoutItemRemoval) {
		super(inventory, index, x, y);
		this.owner = playerEntity;
		this.equipmentSlot = equipmentSlot;
		this.backgroundSprite = backgroundSprite;
		this.allowsLoadoutItemRemoval = allowsLoadoutItemRemoval;
		((SlotCustomization) this).slotcustomizationapi$setSlotTooltipText(tooltip);
	}

	@Override
	public void onTakeItem(PlayerEntity player, ItemStack stack) {
		if (stack.contains(RPGInventory.LOAD_OUT_ITEM) && this.allowsLoadoutItemRemoval) {
			stack.setCount(0);
		}
		super.onTakeItem(player, stack);
	}

	@Override
	public void setStack(ItemStack stack, ItemStack previousStack) {
		this.owner.onEquipStack(this.equipmentSlot, previousStack, stack);
		super.setStack(stack, previousStack);
	}

	@Override
	public int getMaxItemCount() {
		return 1;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
		boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && this.owner.hasStatusEffect(civilisation_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
		boolean hasWildernessEffect = wilderness_status_effect.isPresent() && this.owner.hasStatusEffect(wilderness_status_effect.get());

		boolean isOwned = ItemUtils.isUsableByPlayer(stack, this.owner);
		boolean isCreative = this.owner.isCreative();

		return (equipmentSlot == this.owner.getPreferredEquipmentSlot(stack) || ExtendedEquipmentSlot.rpginventory$isOfEquipmentTag(stack, equipmentSlot)) && isOwned && (stack.contains(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || hasCivilisationEffect || isCreative || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !hasWildernessEffect));
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {
		Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
		boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && owner.hasStatusEffect(civilisation_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
		boolean hasWildernessEffect = wilderness_status_effect.isPresent() && owner.hasStatusEffect(wilderness_status_effect.get());

		boolean isCreative = playerEntity.isCreative();

		ItemStack itemStack = this.getStack();
		return (
				!itemStack.isEmpty()
						&& !isCreative
						&& EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE)
						? false
						: super.canTakeItems(playerEntity)
		) && (!this.getStack().contains(RPGInventory.LOAD_OUT_ITEM) || this.allowsLoadoutItemRemoval) && (itemStack.contains(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || hasCivilisationEffect || isCreative || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !hasWildernessEffect));
	}

	@Override
	public Pair<Identifier, Identifier> getBackgroundSprite() {
		return this.backgroundSprite != null ? Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, this.backgroundSprite) : super.getBackgroundSprite();
	}
}
