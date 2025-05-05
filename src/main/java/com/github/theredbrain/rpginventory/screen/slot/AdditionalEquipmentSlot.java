package com.github.theredbrain.rpginventory.screen.slot;

import com.github.theredbrain.rpginventory.RPGInventory;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Optional;

public class AdditionalEquipmentSlot extends Slot {
	private final PlayerEntity owner;
	private final EquipmentSlot equipmentSlot;
	private final TagKey<Item> equipmentTag;
	@Nullable
	private final Identifier backgroundSprite;

	public AdditionalEquipmentSlot(Inventory inventory, int index, PlayerEntity playerEntity, EquipmentSlot equipmentSlot, TagKey<Item> equipmentTag, int x, int y, @Nullable Identifier backgroundSprite, List<Text> tooltip) {
		super(inventory, index, x, y);
		this.owner = playerEntity;
		this.equipmentSlot = equipmentSlot;
		this.equipmentTag = equipmentTag;
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

		return stack.isIn(this.equipmentTag) && isOwned && (hasCivilisationEffect || isCreative || (bl && !hasWildernessEffect));
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
}
