package com.github.theredbrain.rpginventory.screen.slot;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.screen.DuckSlotMixin;
import com.github.theredbrain.rpginventory.util.ItemUtils;
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
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Optional;

public class CustomArmorSlot extends Slot {
	private final PlayerEntity owner;
	private final EquipmentSlot equipmentSlot;
	@Nullable
	private final Identifier backgroundSprite;

	public CustomArmorSlot(Inventory inventory, PlayerEntity playerEntity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, List<Text> tooltip) {
		super(inventory, index, x, y);
		this.owner = playerEntity;
		this.equipmentSlot = equipmentSlot;
		this.backgroundSprite = backgroundSprite;
		((DuckSlotMixin)this).rpginventory$setSlotTooltipText(tooltip);
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
		boolean bl = true;
		if (this.owner.getServer() != null) {
			bl = this.owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
		}

		Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
		boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && this.owner.hasStatusEffect(civilisation_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
		boolean hasWildernessEffect = wilderness_status_effect.isPresent() && this.owner.hasStatusEffect(wilderness_status_effect.get());

		boolean isOwned = ItemUtils.isOwnedByPlayer(stack, this.owner.getGameProfile());
		boolean isCreative = this.owner.isCreative();

		return (equipmentSlot == this.owner.getPreferredEquipmentSlot(stack) || rpginventory$isOfEquipmentTag(stack, equipmentSlot)) && isOwned && (hasCivilisationEffect || isCreative || (bl && !hasWildernessEffect));
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {
		boolean bl = true;
		if (owner.getServer() != null) {
			bl = owner.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
		}

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
		) && !this.getStack().contains(RPGInventory.LOAD_OUT_ITEM) && (hasCivilisationEffect || isCreative || (bl && !hasWildernessEffect));
	}

	@Override
	public Pair<Identifier, Identifier> getBackgroundSprite() {
		return this.backgroundSprite != null ? Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, this.backgroundSprite) : super.getBackgroundSprite();
	}

	@Unique
	private boolean rpginventory$isOfEquipmentTag(ItemStack itemStack, EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND) {
			return itemStack.isIn(Tags.HAND_ITEMS);
		} else if (slot == EquipmentSlot.OFFHAND) {
			return itemStack.isIn(Tags.OFFHAND_ITEMS);
		} else if (slot == EquipmentSlot.FEET) {
			return itemStack.isIn(Tags.BOOTS);
		} else if (slot == EquipmentSlot.LEGS) {
			return itemStack.isIn(Tags.LEGGINGS);
		} else if (slot == EquipmentSlot.CHEST) {
			return itemStack.isIn(Tags.CHEST_PLATES);
		} else if (slot == EquipmentSlot.HEAD) {
			return itemStack.isIn(Tags.HELMETS);
		} else if (slot == ExtendedEquipmentSlot.BELT) {
			return itemStack.isIn(Tags.BELTS);
		} else if (slot == ExtendedEquipmentSlot.GLOVES) {
			return itemStack.isIn(Tags.GLOVES);
		} else if (slot == ExtendedEquipmentSlot.NECKLACE) {
			boolean bl = itemStack.isIn(Tags.NECKLACES);
			RPGInventory.info("itemStack.isIn(Tags.NECKLACES): " + bl);
			return bl;
		} else if (slot == ExtendedEquipmentSlot.RING_1) {
			return itemStack.isIn(Tags.RINGS);
		} else if (slot == ExtendedEquipmentSlot.RING_2) {
			return itemStack.isIn(Tags.RINGS);
		} else if (slot == ExtendedEquipmentSlot.SHOULDERS) {
			return itemStack.isIn(Tags.SHOULDERS);
		} else if (slot == ExtendedEquipmentSlot.SPELL_1) {
			return itemStack.isIn(Tags.SPELLS);
		} else if (slot == ExtendedEquipmentSlot.SPELL_2) {
			return itemStack.isIn(Tags.SPELLS);
		} else if (slot == ExtendedEquipmentSlot.SPELL_3) {
			return itemStack.isIn(Tags.SPELLS);
		} else if (slot == ExtendedEquipmentSlot.SPELL_4) {
			return itemStack.isIn(Tags.SPELLS);
		} else if (slot == ExtendedEquipmentSlot.SPELL_5) {
			return itemStack.isIn(Tags.SPELLS);
		} else if (slot == ExtendedEquipmentSlot.SPELL_6) {
			return itemStack.isIn(Tags.SPELLS);
		} else if (slot == ExtendedEquipmentSlot.SPELL_7) {
			return itemStack.isIn(Tags.SPELLS);
		} else if (slot == ExtendedEquipmentSlot.SPELL_8) {
			return itemStack.isIn(Tags.SPELLS);
		} else {
			return false;
		}
	}
}
