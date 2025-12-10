package com.github.theredbrain.rpginventory.mixin.entity;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlotType;
import com.github.theredbrain.rpginventory.entity.player.PlayerEntityHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(value = LivingEntity.class, priority = 1050)
@SuppressWarnings("UnreachableCode")
public abstract class LivingEntityMixin extends Entity {

	@Unique
	private final DefaultedList<ItemStack> syncedAdditionalEquipmentStacks = DefaultedList.ofSize(15, ItemStack.EMPTY);

	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	@Shadow
	public abstract AttributeContainer getAttributes();

	@Shadow
	public abstract boolean areItemsDifferent(ItemStack stack, ItemStack stack2);

	@Shadow
	@Final
	private AttributeContainer attributes;

	@Shadow
	protected abstract ItemStack getSyncedHandStack(EquipmentSlot slot);

	@Shadow
	protected abstract ItemStack getSyncedArmorStack(EquipmentSlot slot);

	@Shadow
	private ItemStack syncedBodyArmorStack;

	@Shadow
	protected abstract void setSyncedHandStack(EquipmentSlot slot, ItemStack stack);

	@Shadow
	protected abstract void setSyncedArmorStack(EquipmentSlot slot, ItemStack armor);

	@Shadow
	public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void rpginventory$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue()
				.add(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT, 0.0F)
		;
	}

	@WrapMethod(method = "canFreeze")
	private boolean rpginventory$canFreeze(Operation<Boolean> original) {
		boolean bl = !this.getEquippedStack(ExtendedEquipmentSlot.SHOULDERS).isIn(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getEquippedStack(ExtendedEquipmentSlot.GLOVES).isIn(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getEquippedStack(ExtendedEquipmentSlot.BELT).isIn(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getEquippedStack(ExtendedEquipmentSlot.RING_1).isIn(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getEquippedStack(ExtendedEquipmentSlot.RING_2).isIn(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getEquippedStack(ExtendedEquipmentSlot.NECKLACE).isIn(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getEquippedStack(ExtendedEquipmentSlot.RELIC).isIn(ItemTags.FREEZE_IMMUNE_WEARABLES)
				&& !this.getEquippedStack(ExtendedEquipmentSlot.CLASS_ITEM).isIn(ItemTags.FREEZE_IMMUNE_WEARABLES);
		return original.call() && bl;
	}

	@WrapMethod(method = "tryUseTotem")
	private boolean rpginventory$wrap_tryUseTotem(DamageSource source, Operation<Boolean> original) {

		LivingEntity thisLivingEntity = ((LivingEntity) (Object) this);
		Optional<RegistryEntry.Reference<StatusEffect>> pvp_status_effect = Registries.STATUS_EFFECT.getEntry(RPGInventory.SERVER_CONFIG.statusEffects.pvp_status_effect_identifier.get());
		if (pvp_status_effect.isPresent() && thisLivingEntity instanceof ServerPlayerEntity serverPlayerEntity && this.hasStatusEffect(pvp_status_effect.get())) {
			if (PlayerEntityHelper.rpginventory$onPVPDeath(source, serverPlayerEntity, pvp_status_effect.get())) {
				return true;
			}
		}
		return original.call(source);
	}

	// TODO find better mixin
	@Inject(method = "getEquipmentChanges", at = @At(value = "HEAD"), cancellable = true)
	private void rpginventory$getEquipmentChanges(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
		Map<EquipmentSlot, ItemStack> map = null;

		for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {

			ItemStack itemStack;
			if (equipmentSlot.getType() == EquipmentSlot.Type.HAND) {
				itemStack = this.getSyncedHandStack(equipmentSlot);
			} else if (equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
				itemStack = this.getSyncedArmorStack(equipmentSlot);
			} else if (equipmentSlot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
				itemStack = this.syncedBodyArmorStack;
			} else if (equipmentSlot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
				itemStack = this.getSyncedAdditionalEquipmentStack(equipmentSlot);
			} else {
				itemStack = ItemStack.EMPTY;
			}
			ItemStack itemStack2 = this.getEquippedStack(equipmentSlot);
			if (this.areItemsDifferent(itemStack, itemStack2)) {
				if (map == null) {
					map = Maps.newEnumMap(EquipmentSlot.class);
				}

				map.put(equipmentSlot, itemStack2);
				AttributeContainer attributeContainer = this.getAttributes();
				if (!itemStack.isEmpty()) {
					itemStack.applyAttributeModifiers(equipmentSlot, (attribute, modifier) -> {
						EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(attribute);
						if (entityAttributeInstance != null) {
							entityAttributeInstance.removeModifier(modifier);
						}

						EnchantmentHelper.removeLocationBasedEffects(itemStack, ((LivingEntity) (Object) this), equipmentSlot);
					});
				}
			}
		}

		if (map != null) {
			for (Map.Entry<EquipmentSlot, ItemStack> entry : map.entrySet()) {
				EquipmentSlot equipmentSlot2 = (EquipmentSlot) entry.getKey();
				ItemStack itemStack3 = (ItemStack) entry.getValue();
				if (!itemStack3.isEmpty()) {
					itemStack3.applyAttributeModifiers(equipmentSlot2, (registryEntry, entityAttributeModifier) -> {
						EntityAttributeInstance entityAttributeInstance = this.attributes.getCustomInstance(registryEntry);
						if (entityAttributeInstance != null) {
							entityAttributeInstance.removeModifier(entityAttributeModifier.id());
							entityAttributeInstance.addTemporaryModifier(entityAttributeModifier);
						}

						if (this.getWorld() instanceof ServerWorld serverWorld) {
							EnchantmentHelper.applyLocationBasedEffects(serverWorld, itemStack3, ((LivingEntity) (Object) this), equipmentSlot2);
						}
					});
				}
			}
		}

		cir.setReturnValue(map);
		cir.cancel();
	}

	// TODO find better mixin

	/**
	 * Sends equipment changes to nearby players.
	 *
	 * @author TheRedBrain
	 * @reason WIP
	 */
	@Overwrite
	private void sendEquipmentChanges(Map<EquipmentSlot, ItemStack> equipmentChanges) {
		List<com.mojang.datafixers.util.Pair<EquipmentSlot, ItemStack>> list = Lists.<com.mojang.datafixers.util.Pair<EquipmentSlot, ItemStack>>newArrayListWithCapacity(equipmentChanges.size());
		equipmentChanges.forEach((slot, stack) -> {
			ItemStack itemStack = stack.copy();
			list.add(Pair.of(slot, itemStack));
			if (slot.getType() == EquipmentSlot.Type.HAND) {
				this.setSyncedHandStack(slot, itemStack);
			} else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
				this.setSyncedArmorStack(slot, itemStack);
			} else if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
				this.syncedBodyArmorStack = itemStack;
			} else if (slot.getType() == ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE) {
				this.setSyncedAdditionalEquipmentStack(slot, itemStack);
			}
		});
		((ServerWorld) this.getWorld()).getChunkManager().sendToOtherNearbyPlayers(this, new EntityEquipmentUpdateS2CPacket(this.getId(), list));
	}

	@Unique
	private ItemStack getSyncedAdditionalEquipmentStack(EquipmentSlot slot) {
		return this.syncedAdditionalEquipmentStacks.get(slot.getEntitySlotId());
	}

	@Unique
	private void setSyncedAdditionalEquipmentStack(EquipmentSlot slot, ItemStack equipment) {
		this.syncedAdditionalEquipmentStacks.set(slot.getEntitySlotId(), equipment);
	}

	@ModifyVariable(method = "damageEquipment(Lnet/minecraft/entity/damage/DamageSource;F[Lnet/minecraft/entity/EquipmentSlot;)V", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(FF)F"), argsOnly = true)
	private float rpginventory$damageEquipment_divideAmount(float oldValue, DamageSource source, float amount) {
		return Math.max(1.0F, amount / 6.0F);
	}
}
