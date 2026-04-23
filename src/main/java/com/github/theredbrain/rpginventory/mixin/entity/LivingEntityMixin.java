package com.github.theredbrain.rpginventory.mixin.entity;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.PlayerEntityHelper;
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
public abstract class LivingEntityMixin extends Entity {

	@Shadow
	public abstract AttributeMap getAttributes();

	@Shadow
	public abstract boolean hasEffect(Holder<MobEffect> effect);

	@Shadow
	public abstract ItemStack getItemBySlot(EquipmentSlot slot);

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
		return Math.max(1.0F, damage / 6.0F);
	}
}
