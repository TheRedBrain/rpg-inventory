package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import net.minecraft.entity.EquipmentSlot;
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

import java.util.Optional;

@Mixin(targets = {"net/minecraft/screen/PlayerScreenHandler$1"})
public abstract class PlayerScreenHandlerOffHandSlotMixin extends Slot {

	@Shadow
	@Final
	PlayerEntity field_39410;

	public PlayerScreenHandlerOffHandSlotMixin(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean isEnabled() {
		return !RPGInventory.isHandSlotOverhaulActive() || !((DuckPlayerEntityMixin) this.field_39410).rpginventory$isOffhandStackSheathed();
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

		Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.civilisation_status_effect_identifier.get());
		boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && this.field_39410.hasStatusEffect(civilisation_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(serverConfig.statusEffects.wilderness_status_effect_identifier.get());
		boolean hasWildernessEffect = wilderness_status_effect.isPresent() && this.field_39410.hasStatusEffect(wilderness_status_effect.get());

		boolean handSlotOverhaulIsInactive = !RPGInventory.isHandSlotOverhaulActive();

		return (EquipmentSlot.OFFHAND == this.field_39410.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || handSlotOverhaulIsInactive) && (handSlotOverhaulIsInactive || !((DuckPlayerEntityMixin) this.field_39410).rpginventory$isOffhandStackSheathed()) && ItemUtils.isUsableByPlayer(stack, this.field_39410) && (hasCivilisationEffect || this.field_39410.isCreative() || (serverConfig.allow_equipment_changes.get() && !hasWildernessEffect));
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {
		Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
		boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && this.field_39410.hasStatusEffect(civilisation_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
		boolean hasWildernessEffect = wilderness_status_effect.isPresent() && this.field_39410.hasStatusEffect(wilderness_status_effect.get());

		boolean isCreative = playerEntity.isCreative();

		return !this.getStack().contains(RPGInventory.LOAD_OUT_ITEM) && (hasCivilisationEffect || isCreative || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !hasWildernessEffect));
	}
}
