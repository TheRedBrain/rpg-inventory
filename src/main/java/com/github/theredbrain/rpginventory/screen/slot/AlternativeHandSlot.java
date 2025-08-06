package com.github.theredbrain.rpginventory.screen.slot;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
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

import java.util.List;
import java.util.Optional;

public class AlternativeHandSlot extends Slot {
	private final PlayerEntity owner;
	private final EquipmentSlot equipmentSlot;
	@Nullable
	private final Identifier backgroundSprite;
	private final boolean allowsLoadoutItemRemoval;

	public AlternativeHandSlot(Inventory inventory, PlayerEntity playerEntity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, List<Text> tooltip) {
		this(inventory, playerEntity, equipmentSlot, index, x, y, backgroundSprite, tooltip, false);
	}

	public AlternativeHandSlot(Inventory inventory, PlayerEntity playerEntity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, List<Text> tooltip, boolean allowsLoadoutItemRemoval) {
		super(inventory, index, x, y);
		this.owner = playerEntity;
		this.equipmentSlot = equipmentSlot;
		this.backgroundSprite = backgroundSprite;
		this.allowsLoadoutItemRemoval = allowsLoadoutItemRemoval;
		((DuckSlotMixin) this).rpginventory$setSlotTooltipText(tooltip);
	}

	@Override
	public void onTakeItem(PlayerEntity player, ItemStack stack) {
		if (stack.contains(RPGInventory.LOAD_OUT_ITEM) && this.allowsLoadoutItemRemoval) {
			stack.setCount(0);
		}
		super.onTakeItem(player, stack);
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

		boolean isOwned = ItemUtils.isUsableByPlayer(stack, this.owner);
		boolean isCreative = this.owner.isCreative();

		return (equipmentSlot == this.owner.getPreferredEquipmentSlot(stack) || ExtendedEquipmentSlot.rpginventory$isOfEquipmentTag(stack, equipmentSlot)) && isOwned && (hasCivilisationEffect || isCreative || (bl && !hasWildernessEffect));
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
		) && (!this.getStack().contains(RPGInventory.LOAD_OUT_ITEM) || this.allowsLoadoutItemRemoval) && (hasCivilisationEffect || isCreative || (bl && !hasWildernessEffect));
	}

	@Override
	public Pair<Identifier, Identifier> getBackgroundSprite() {
		return this.backgroundSprite != null ? Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, this.backgroundSprite) : super.getBackgroundSprite();
	}
}