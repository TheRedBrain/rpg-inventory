package com.github.theredbrain.rpginventory.mixin.trinkets;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.client.gui.screen.ingame.RPGInventoryScreen;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.TrinketInventory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = SurvivalTrinketSlot.class)
public abstract class SurvivalTrinketSlotMixin extends Slot {

	@Shadow
	@Final
	private TrinketInventory trinketInventory;

	@Shadow
	@Final
	private SlotGroup group;

	@Shadow
	public abstract boolean isTrinketFocused();

	@Shadow
	@Final
	private boolean alwaysVisible;

	public SurvivalTrinketSlotMixin(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Inject(method = "canInsert", at = @At("RETURN"), cancellable = true)
	public void rpginventory$canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {

		LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
		boolean bl = false;
		if (livingEntity instanceof PlayerEntity playerEntity && playerEntity.isCreative()) {
			bl = true;
		}
		boolean bl2 = true;
		if (livingEntity.getServer() != null) {
			bl2 = livingEntity.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
		}
		StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
		boolean hasCivilisationEffect = civilisation_status_effect != null && livingEntity.hasStatusEffect(civilisation_status_effect);

		StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
		boolean hasWildernessEffect = wilderness_status_effect != null && livingEntity.hasStatusEffect(wilderness_status_effect);

		cir.setReturnValue(cir.getReturnValue() && (hasCivilisationEffect || bl || (bl2 && !hasWildernessEffect)));
	}

	/**
	 * @author TheRedBrain
	 */
	@Inject(method = "canTakeItems", at = @At("RETURN"), cancellable = true)
	public void rpginventory$canTakeItems(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		boolean bl = true;
		if (player.getServer() != null) {
			bl = player.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
		}

		StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
		boolean hasCivilisationEffect = civilisation_status_effect != null && player.hasStatusEffect(civilisation_status_effect);

		StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
		boolean hasWildernessEffect = wilderness_status_effect != null && player.hasStatusEffect(wilderness_status_effect);

		cir.setReturnValue(cir.getReturnValue() && (hasCivilisationEffect || (bl && !hasWildernessEffect) || player.isCreative()));
	}

	/**
	 * @author TheRedBrain
	 * @reason convenience
	 */
	@Overwrite
	public boolean isEnabled() {
		boolean bl = isTrinketFocused();
		if (alwaysVisible) {
			bl = true;
			if (x < 0) {
				if (trinketInventory.getComponent().getEntity().getWorld().isClient) {
					MinecraftClient client = MinecraftClient.getInstance();
					Screen s = client.currentScreen;
					if (s instanceof RPGInventoryScreen screen) {
						if (screen.trinkets$isRecipeBookOpen()) {
							bl = false;
						}
					}
				}
			}
		}
		return super.isEnabled() && bl
				&& !((Objects.equals(this.group.getName(), "spell_slot_1") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 1)
				|| (Objects.equals(this.group.getName(), "spell_slot_2") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 2)
				|| (Objects.equals(this.group.getName(), "spell_slot_3") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 3)
				|| (Objects.equals(this.group.getName(), "spell_slot_4") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 4)
				|| (Objects.equals(this.group.getName(), "spell_slot_5") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 5)
				|| (Objects.equals(this.group.getName(), "spell_slot_6") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 6)
				|| (Objects.equals(this.group.getName(), "spell_slot_7") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 7)
				|| (Objects.equals(this.group.getName(), "spell_slot_8") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 8)
				|| (Objects.equals(this.group.getName(), "main_hand") && trinketInventory.getComponent().getEntity() instanceof PlayerEntity && ((DuckPlayerEntityMixin) trinketInventory.getComponent().getEntity()).rpginventory$isMainHandStackSheathed())
				|| (Objects.equals(this.group.getName(), "sheathed_main_hand") && trinketInventory.getComponent().getEntity() instanceof PlayerEntity && !((DuckPlayerEntityMixin) trinketInventory.getComponent().getEntity()).rpginventory$isMainHandStackSheathed())
				|| (Objects.equals(this.group.getName(), "sheathed_offhand") && trinketInventory.getComponent().getEntity() instanceof PlayerEntity && !((DuckPlayerEntityMixin) trinketInventory.getComponent().getEntity()).rpginventory$isOffHandStackSheathed())
		);
	}

	/**
	 * @author TheRedBrain
	 */
	@Inject(method = "isTrinketFocused", at = @At("HEAD"), cancellable = true, remap = false)
	public void rpginventory$isTrinketFocused(CallbackInfoReturnable<Boolean> cir) {
		if ((Objects.equals(this.group.getName(), "spell_slot_1") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 1)
				|| (Objects.equals(this.group.getName(), "spell_slot_2") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 2)
				|| (Objects.equals(this.group.getName(), "spell_slot_3") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 3)
				|| (Objects.equals(this.group.getName(), "spell_slot_4") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 4)
				|| (Objects.equals(this.group.getName(), "spell_slot_5") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 5)
				|| (Objects.equals(this.group.getName(), "spell_slot_6") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 6)
				|| (Objects.equals(this.group.getName(), "spell_slot_7") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 7)
				|| (Objects.equals(this.group.getName(), "spell_slot_8") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 8)
				|| (Objects.equals(this.group.getName(), "main_hand") && trinketInventory.getComponent().getEntity() instanceof PlayerEntity && ((DuckPlayerEntityMixin) trinketInventory.getComponent().getEntity()).rpginventory$isMainHandStackSheathed())
				|| (Objects.equals(this.group.getName(), "sheathed_main_hand") && trinketInventory.getComponent().getEntity() instanceof PlayerEntity && !((DuckPlayerEntityMixin) trinketInventory.getComponent().getEntity()).rpginventory$isMainHandStackSheathed())
				|| (Objects.equals(this.group.getName(), "sheathed_offhand") && trinketInventory.getComponent().getEntity() instanceof PlayerEntity && !((DuckPlayerEntityMixin) trinketInventory.getComponent().getEntity()).rpginventory$isOffHandStackSheathed())
		) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}
