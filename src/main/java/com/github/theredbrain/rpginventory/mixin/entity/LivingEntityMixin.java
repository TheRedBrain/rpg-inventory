package com.github.theredbrain.rpginventory.mixin.entity;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.DataAttachmentHelper;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.RendersSheathedWeapons;
import com.github.theredbrain.rpginventory.entity.player.PlayerEntityHelper;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(value = LivingEntity.class, priority = 1050)
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin, RendersSheathedWeapons {

	@Shadow
	public abstract AttributeMap getAttributes();

	@Shadow
	public abstract boolean hasEffect(Holder<MobEffect> effect);

	@Shadow
	public abstract ItemStack getItemBySlot(EquipmentSlot slot);

	@Shadow
	public abstract HumanoidArm getMainArm();

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void rpginventory$createLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		cir.getReturnValue()
				.add(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT, 0.0F)
		;
	}

	@Inject(method = "onEffectsRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeMap;)V"))
	protected void rpginventory$onEffectsRemoved(Collection<MobEffectInstance> effects, CallbackInfo ci, @Local(name = "effect") MobEffectInstance effect) {
		if (effect.getEffect() == RPGInventory.PVP) {
			this.level().getScoreboard().removePlayerFromTeam(this.getScoreboardName());
		}
	}

	@WrapMethod(method = "getEquipmentSlot")
	private static EquipmentSlot rpginventory$wrap_getEquipmentSlot(int slot, Operation<EquipmentSlot> original) {
		if (slot == 1000 + ExtendedEquipmentSlot.BELT.getIndex()) {
			return ExtendedEquipmentSlot.BELT;
		} else if (slot == 1000 + ExtendedEquipmentSlot.GLOVES.getIndex()) {
			return ExtendedEquipmentSlot.GLOVES;
		} else if (slot == 1000 + ExtendedEquipmentSlot.NECKLACE.getIndex()) {
			return ExtendedEquipmentSlot.NECKLACE;
		} else if (slot == 1000 + ExtendedEquipmentSlot.RING_1.getIndex()) {
			return ExtendedEquipmentSlot.RING_1;
		} else if (slot == 1000 + ExtendedEquipmentSlot.RING_2.getIndex()) {
			return ExtendedEquipmentSlot.RING_2;
		} else if (slot == 1000 + ExtendedEquipmentSlot.SHOULDERS.getIndex()) {
			return ExtendedEquipmentSlot.SHOULDERS;
		} else if (slot == 1000 + ExtendedEquipmentSlot.SPELL_1.getIndex()) {
			return ExtendedEquipmentSlot.SPELL_1;
		} else if (slot == 1000 + ExtendedEquipmentSlot.SPELL_2.getIndex()) {
			return ExtendedEquipmentSlot.SPELL_2;
		} else if (slot == 1000 + ExtendedEquipmentSlot.SPELL_3.getIndex()) {
			return ExtendedEquipmentSlot.SPELL_3;
		} else if (slot == 1000 + ExtendedEquipmentSlot.SPELL_4.getIndex()) {
			return ExtendedEquipmentSlot.SPELL_4;
		} else if (slot == 1000 + ExtendedEquipmentSlot.SPELL_5.getIndex()) {
			return ExtendedEquipmentSlot.SPELL_5;
		} else if (slot == 1000 + ExtendedEquipmentSlot.SPELL_6.getIndex()) {
			return ExtendedEquipmentSlot.SPELL_6;
		} else if (slot == 1000 + ExtendedEquipmentSlot.SPELL_7.getIndex()) {
			return ExtendedEquipmentSlot.SPELL_7;
		} else if (slot == 1000 + ExtendedEquipmentSlot.SPELL_8.getIndex()) {
			return ExtendedEquipmentSlot.SPELL_8;
		} else if (slot == 1000 + ExtendedEquipmentSlot.RELIC.getIndex()) {
			return ExtendedEquipmentSlot.RELIC;
		} else if (slot == 1000 + ExtendedEquipmentSlot.CLASS_ITEM.getIndex()) {
			return ExtendedEquipmentSlot.CLASS_ITEM;
		} else {
			return original.call(slot);
		}
	}

	@WrapMethod(method = "canFreeze")
	private boolean rpginventory$canFreeze(Operation<Boolean> original) {
		boolean bl = !this.getItemBySlot(ExtendedEquipmentSlot.SHOULDERS).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getItemBySlot(ExtendedEquipmentSlot.GLOVES).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getItemBySlot(ExtendedEquipmentSlot.BELT).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getItemBySlot(ExtendedEquipmentSlot.RING_1).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getItemBySlot(ExtendedEquipmentSlot.RING_2).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getItemBySlot(ExtendedEquipmentSlot.NECKLACE).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getItemBySlot(ExtendedEquipmentSlot.RELIC).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getItemBySlot(ExtendedEquipmentSlot.CLASS_ITEM).is(ItemTags.FREEZE_IMMUNE_WEARABLES);
		return original.call() && bl;
	}

	@WrapMethod(method = "checkTotemDeathProtection")
	private boolean rpginventory$wrap_checkTotemDeathProtection(DamageSource killingDamage, Operation<Boolean> original) {

		LivingEntity thisLivingEntity = ((LivingEntity) (Object) this);
		if (thisLivingEntity instanceof Player playerEntity && this.hasEffect(RPGInventory.PVP)) {
			if (PlayerEntityHelper.rpginventory$onPVPDeath(killingDamage, playerEntity, RPGInventory.PVP)) {
				return true;
			}
		}
		return original.call(killingDamage);
	}

	@ModifyVariable(method = "doHurtEquipment(Lnet/minecraft/world/damagesource/DamageSource;F[Lnet/minecraft/world/entity/EquipmentSlot;)V", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(FF)F"), argsOnly = true, name = "damage")
	private float rpginventory$damageEquipment_divideAmount(float damage) {
		if (((LivingEntity) (Object) this) instanceof Player && RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
			return Math.max(1.0F, damage / 6.0F);
		} else {
			return Math.max(1.0F, damage / 4.0F);
		}
	}

	@Override
	public ItemStack rpginventory$getSheathedItemStackByArm(HumanoidArm arm) {

		boolean mainArmIsLeft = this.getMainArm() == HumanoidArm.LEFT;
		ItemStack itemStack;

		if (arm == HumanoidArm.LEFT) {
			if (mainArmIsLeft) {
				itemStack = rpginventory$isHandStackSheathed() ? this.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_HAND) : ItemStack.EMPTY;
			} else {
				itemStack = rpginventory$isOffhandStackSheathed() ? this.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND) : ItemStack.EMPTY;
			}
		} else {
			if (mainArmIsLeft) {
				itemStack = rpginventory$isOffhandStackSheathed() ? this.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND) : ItemStack.EMPTY;
			} else {
				itemStack = rpginventory$isHandStackSheathed() ? this.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_HAND) : ItemStack.EMPTY;
			}
		}
		return !itemStack.is(Tags.EMPTY_HAND_WEAPONS) && ItemUtils.isUsable(itemStack) && ItemUtils.isUsableByPlayer(itemStack, ((Player) (Object) this)) ? itemStack : ItemStack.EMPTY;
	}

	@Override
	public boolean rpginventory$isHandStackSheathed() {
		return DataAttachmentHelper.isHandStackSheathed((LivingEntity) (Object) this);
	}

	@Override
	public void rpginventory$setIsHandStackSheathed(boolean isHandStackSheathed) {
		DataAttachmentHelper.setIsHandStackSheathed((LivingEntity) (Object) this, isHandStackSheathed);
	}

	@Override
	public boolean rpginventory$isOffhandStackSheathed() {
		return DataAttachmentHelper.isOffhandStackSheathed((LivingEntity) (Object) this);
	}

	@Override
	public void rpginventory$setIsOffhandStackSheathed(boolean isOffhandStackSheathed) {
		DataAttachmentHelper.setIsOffhandStackSheathed((LivingEntity) (Object) this, isOffhandStackSheathed);
	}

}
