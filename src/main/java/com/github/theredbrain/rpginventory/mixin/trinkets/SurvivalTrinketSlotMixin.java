package com.github.theredbrain.rpginventory.mixin.trinkets;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.screen.DuckPlayerScreenHandlerMixin;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(value = SurvivalTrinketSlot.class)
public abstract class SurvivalTrinketSlotMixin extends Slot {

	@Shadow(remap = false)
	@Final
	private TrinketInventory trinketInventory;

	@Shadow(remap = false)
	@Final
	private boolean alwaysVisible;

	public SurvivalTrinketSlotMixin(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Inject(method = "<init>", at = @At("TAIL"), remap = false)
	public void SurvivalTrinketSlot(TrinketInventory inventory, int index, int x, int y, SlotGroup group, SlotType type, int slotOffset, boolean alwaysVisible, CallbackInfo ci) {
		addSlotTooltip(this, group.getName(), type.getName());
	}

	@Unique
	private static void addSlotTooltip(Slot slot, String groupName, String slotName) {
		List<Text> list = new ArrayList<>();
		Text text = Text.translatable("slot.tooltip." + groupName + "." + slotName);
		if (!text.getString().isEmpty()) {
			list.add(text);
			((SlotCustomization) slot).slotcustomizationapi$setSlotTooltipText(list);
		}
	}

	@Inject(method = "canInsert", at = @At("RETURN"), cancellable = true)
	public void rpginventory$canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {

		LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
		boolean bl = false;
		boolean isOwned = true;
		if (livingEntity instanceof PlayerEntity playerEntity) {
			bl = playerEntity.isCreative();
			isOwned = ItemUtils.isUsableByPlayer(stack, playerEntity);
		}

		Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
		boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && livingEntity.hasStatusEffect(civilisation_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
		boolean hasWildernessEffect = wilderness_status_effect.isPresent() && livingEntity.hasStatusEffect(wilderness_status_effect.get());

		cir.setReturnValue(cir.getReturnValue() && isOwned && (hasCivilisationEffect || bl || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !hasWildernessEffect)));
	}

	/**
	 * @author TheRedBrain
	 */
	@Inject(method = "canTakeItems", at = @At("RETURN"), cancellable = true)
	public void rpginventory$canTakeItems(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		Optional<RegistryEntry.Reference<StatusEffect>> civilisation_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.civilisation_status_effect_identifier.get());
		boolean hasCivilisationEffect = civilisation_status_effect.isPresent() && player.hasStatusEffect(civilisation_status_effect.get());

		Optional<RegistryEntry.Reference<StatusEffect>> wilderness_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.wilderness_status_effect_identifier.get());
		boolean hasWildernessEffect = wilderness_status_effect.isPresent() && player.hasStatusEffect(wilderness_status_effect.get());

		cir.setReturnValue(cir.getReturnValue() && (hasCivilisationEffect || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !hasWildernessEffect) || player.isCreative()));
	}

	@Inject(method = "isEnabled", at = @At(value = "HEAD"), cancellable = true)
	public void rpginventory$isEnabled_checkForRecipeBook(CallbackInfoReturnable<Boolean> cir) {
		if (alwaysVisible && x < 0 && trinketInventory.getComponent().getEntity() instanceof PlayerEntity player) {
			if (player.currentScreenHandler instanceof DuckPlayerScreenHandlerMixin playerScreenHandler && playerScreenHandler.rpginventory$isAttributeScreenVisible()) {
				cir.setReturnValue(false);
				cir.cancel();
			}
		}
	}
}
