package com.github.theredbrain.rpginventory.mixin.entity;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlotType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
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
import java.util.function.Predicate;

@Mixin(value = LivingEntity.class, priority = 1050)
@SuppressWarnings("UnreachableCode")
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin {

	@Unique
	private final DefaultedList<ItemStack> syncedAdditionalEquipmentStacks = DefaultedList.ofSize(14, ItemStack.EMPTY);

//	@Unique
//	private final Map<String, ItemStack> lastEquippedTrinkets = new HashMap<>();

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

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void rpginventory$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue()
				.add(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT, 0.0F)
		;
	}

//	/**
//	 * @author Emi
//	 */ // TODO
//	@Inject(at = @At("HEAD"), method = "canFreeze", cancellable = true)
//	private void rpginventory$canFreeze(CallbackInfoReturnable<Boolean> cir) {
//		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent((LivingEntity) (Object) this);
//		if (component.isPresent()) {
//			for (Pair<SlotReference, ItemStack> equipped : component.get().getAllEquipped()) {
//				if (equipped.getRight().isIn(ItemTags.FREEZE_IMMUNE_WEARABLES)) {
//					cir.setReturnValue(false);
//					break;
//				}
//			}
//		}
//	}

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

	@Override
	public boolean rpginventory$hasEquipped(Predicate<ItemStack> predicate) {
		if (predicate.test(this.getEquippedStack(EquipmentSlot.MAINHAND))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.OFFHAND))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.FEET))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.LEGS))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.CHEST))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.HEAD))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SHOULDERS))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.GLOVES))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.RING_1))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.RING_2))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.NECKLACE))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.BELT))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_1))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_2))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_3))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_4))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_5))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_6))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_7))) {
			return true;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_8))) {
			return true;
		}
		return false;
	}

	@Override
	public int rpginventory$getAmountEquipped(Predicate<ItemStack> predicate) {
		int i = 0;
		if (predicate.test(this.getEquippedStack(EquipmentSlot.MAINHAND))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.OFFHAND))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.FEET))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.LEGS))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.CHEST))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(EquipmentSlot.HEAD))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SHOULDERS))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.GLOVES))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.RING_1))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.RING_2))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.NECKLACE))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.BELT))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_1))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_2))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_3))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_4))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_5))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_6))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_7))) {
			i += 1;
		}
		if (predicate.test(this.getEquippedStack(ExtendedEquipmentSlot.SPELL_8))) {
			i += 1;
		}
		return i;
	}

	@ModifyVariable(method = "damageEquipment(Lnet/minecraft/entity/damage/DamageSource;F[Lnet/minecraft/entity/EquipmentSlot;)V", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(FF)F"), argsOnly = true)
	private float rpginventory$damageEquipment_divideAmount(float oldValue, DamageSource source, float amount) {
		return Math.max(1.0F, amount / 6.0F);
	}

//	/**
//	 * @author TheRedBrain
//	 */ // TODO
//	@Inject(method = "damageEquipment(Lnet/minecraft/entity/damage/DamageSource;F[Lnet/minecraft/entity/EquipmentSlot;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER))
//	public void damageEquipment(DamageSource source, float amount, EquipmentSlot[] slots, CallbackInfo ci) {
//		ItemStack itemStack1 = this.getEquippedStack(equipmentSlot);
//		if (itemStack.getItem() instanceof ArmorItem && itemStack.takesDamageFrom(source)) {
//			itemStack.damage(i, this, equipmentSlot);
//		}
//		if (!(amount <= 0.0F)) {
//			int newAmount = (int) Math.max(1.0F, amount / 6.0F);
//			// check for slots length prevents damaging trinkets when only a specific armor item is damaged, e.g. boots from fall damage
//			if (slots.length > 1 && this.getWorld() instanceof ServerWorld serverWorld && ((LivingEntity) (Object) this) instanceof ServerPlayerEntity serverPlayerEntity) {
//				// armor trinkets
//				TrinketsApi.getTrinketComponent(serverPlayerEntity).ifPresent(trinkets ->
//						trinkets.forEach((slotReference, itemStack) -> {
//							if (itemStack.takesDamageFrom(source) && itemStack.isIn(Tags.ARMOR_TRINKETS) && ItemUtils.isUsable(itemStack)) {
//								itemStack.damage((int) newAmount, serverWorld, serverPlayerEntity, (item) -> TrinketsApi.onTrinketBroken(itemStack, slotReference, serverPlayerEntity));
//							}
//						}));
//			}
//		}
//	}

//	/**
//	 * Modified code by @Emi to inject gamerule destroyDroppedItemsOnDeath into Trinkets drop logic
//	 */ // TODO
//	@Inject(at = @At("TAIL"), method = "dropInventory")
//	private void rpginventory$dropInventory(CallbackInfo info) {
//		LivingEntity entity = (LivingEntity) (Object) this;
//
//		boolean keepInv = entity.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY);
//
//		boolean destroyDroppedItems;
//		if (entity.getServer() != null && entity instanceof PlayerEntity) {
//			destroyDroppedItems = entity.getServer().getGameRules().getBoolean(GameRulesRegistry.DESTROY_DROPPED_ITEMS_ON_DEATH);
//		} else {
//			destroyDroppedItems = false;
//		}
//
//		TrinketsApi.getTrinketComponent(entity).ifPresent(trinkets -> trinkets.forEach((ref, stack) -> {
//			if (stack.isEmpty()) {
//				return;
//			}
//
//			TrinketEnums.DropRule dropRule = TrinketsApi.getTrinket(stack.getItem()).getDropRule(stack, ref, entity);
//
//			dropRule = TrinketDropCallback.EVENT.invoker().drop(dropRule, stack, ref, entity);
//
//			TrinketInventory inventory = ref.inventory();
//
//			if (dropRule == TrinketEnums.DropRule.DEFAULT) {
//				dropRule = inventory.getSlotType().getDropRule();
//			}
//
//			if (dropRule == TrinketEnums.DropRule.DEFAULT) {
//				if (keepInv && entity.getType() == EntityType.PLAYER) {
//					dropRule = TrinketEnums.DropRule.KEEP;
//				} else {
//					if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP) || destroyDroppedItems) {
//						dropRule = TrinketEnums.DropRule.DESTROY;
//					} else {
//						dropRule = TrinketEnums.DropRule.DROP;
//					}
//				}
//			}
//
//			switch (dropRule) {
//				case DROP:
//					dropFromEntity(stack);
//					// Fallthrough
//				case DESTROY:
//					inventory.setStack(ref.index(), ItemStack.EMPTY);
//					break;
//				default:
//					break;
//			}
//		}));
//	}

//	/**
//	 * @author Emi
//	 */
//	@Unique
//	private void dropFromEntity(ItemStack stack) {
//		// Mimic player drop behavior for only players
//		if (((Entity) this) instanceof PlayerEntity player) {
//			ItemEntity entity = player.dropItem(stack, true, false);
//		} else {
//			ItemEntity entity = dropStack(stack);
//		}
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@Inject(at = @At("TAIL"), method = "tick")
//	private void rpginventory$tick(CallbackInfo info) {
//		LivingEntity entity = (LivingEntity) (Object) this;
//		if (entity.isRemoved()) {
//			return;
//		}
//		TrinketsApi.getTrinketComponent(entity).ifPresent(trinkets -> {
//			Map<String, ItemStack> newlyEquippedTrinkets = new HashMap<>();
//			Map<String, ItemStack> contentUpdates = new HashMap<>();
//			trinkets.forEach((ref, stack) -> {
//				TrinketInventory inventory = ref.inventory();
//				SlotType slotType = inventory.getSlotType();
//				int index = ref.index();
//				ItemStack oldStack = getOldStack(slotType, index);
//				ItemStack newStack = inventory.getStack(index);
//				ItemStack newStackCopy = newStack.copy();
//				String newRef = slotType.getGroup() + "/" + slotType.getName() + "/" + index;
//
//				if (!ItemStack.areEqual(newStack, oldStack)) {
//
//					TrinketsApi.getTrinket(oldStack.getItem()).onUnequip(oldStack, ref, entity);
//					TrinketUnequipCallback.EVENT.invoker().onUnequip(oldStack, ref, entity);
//					TrinketsApi.getTrinket(newStack.getItem()).onEquip(newStack, ref, entity);
//					TrinketEquipCallback.EVENT.invoker().onEquip(newStack, ref, entity);
//
//					World world = this.getWorld();
//					if (!world.isClient) {
//						contentUpdates.put(newRef, newStackCopy);
//
//						if (!oldStack.isEmpty()) {
//							Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> map = TrinketModifiers.get(oldStack, ref, entity);
//							Multimap<String, EntityAttributeModifier> slotMap = HashMultimap.create();
//							Set<RegistryEntry<EntityAttribute>> toRemove = Sets.newHashSet();
//							for (RegistryEntry<EntityAttribute> attr : map.keySet()) {
//								if (attr.hasKeyAndValue() && attr.value() instanceof SlotAttributes.SlotEntityAttribute slotAttr) {
//									slotMap.putAll(slotAttr.slot, map.get(attr));
//									toRemove.add(attr);
//								}
//							}
//							for (RegistryEntry<EntityAttribute> attr : toRemove) {
//								map.removeAll(attr);
//							}
//							//this.getAttributes().removeModifiers(map);
//							map.asMap().forEach((attribute, modifiers) -> {
//								EntityAttributeInstance entityAttributeInstance = this.getAttributes().getCustomInstance(attribute);
//								if (entityAttributeInstance != null) {
//									modifiers.forEach(modifier -> entityAttributeInstance.removeModifier(modifier.id()));
//								}
//							});
//
//							trinkets.removeModifiers(slotMap);
//						}
//
//						if (!newStack.isEmpty()) {
//							Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> map = TrinketModifiers.get(newStack, ref, entity);
//							Multimap<String, EntityAttributeModifier> slotMap = HashMultimap.create();
//							Set<RegistryEntry<EntityAttribute>> toRemove = Sets.newHashSet();
//							for (RegistryEntry<EntityAttribute> attr : map.keySet()) {
//								if (attr.hasKeyAndValue() && attr.value() instanceof SlotAttributes.SlotEntityAttribute slotAttr) {
//									slotMap.putAll(slotAttr.slot, map.get(attr));
//									toRemove.add(attr);
//								}
//							}
//							for (RegistryEntry<EntityAttribute> attr : toRemove) {
//								map.removeAll(attr);
//							}
//							//this.getAttributes().addTemporaryModifiers(map);
//							map.forEach((attribute, attributeModifier) -> {
//								EntityAttributeInstance entityAttributeInstance = this.getAttributes().getCustomInstance(attribute);
//								if (entityAttributeInstance != null) {
//									entityAttributeInstance.removeModifier(attributeModifier.id());
//									entityAttributeInstance.addTemporaryModifier(attributeModifier);
//								}
//
//							});
//							trinkets.addTemporaryModifiers(slotMap);
//						}
//					}
//				}
//				TrinketsApi.getTrinket(newStack.getItem()).tick(newStack, ref, entity);
//				ItemStack tickedStack = inventory.getStack(index);
//				// Avoid calling equip/unequip on stacks that mutate themselves
//				if (tickedStack.getItem() == newStackCopy.getItem()) {
//					newlyEquippedTrinkets.put(newRef, tickedStack.copy());
//				} else {
//					newlyEquippedTrinkets.put(newRef, newStackCopy);
//				}
//			});
//
//			World world = this.getWorld();
//			if (!world.isClient) {
//				Set<TrinketInventory> inventoriesToSend = trinkets.getTrackingUpdates();
//
//				if (!contentUpdates.isEmpty() || !inventoriesToSend.isEmpty()) {
//					Map<String, NbtCompound> map = new HashMap<>();
//
//					for (TrinketInventory trinketInventory : inventoriesToSend) {
//						map.put(trinketInventory.getSlotType().getId(), trinketInventory.getSyncTag());
//					}
//					SyncInventoryPayload packet = new SyncInventoryPayload(this.getId(), contentUpdates, map);
//
//					for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
//						ServerPlayNetworking.send(player, packet);
//					}
//
//					if (entity instanceof ServerPlayerEntity serverPlayer) {
//						ServerPlayNetworking.send(serverPlayer, packet);
//
//						if (!inventoriesToSend.isEmpty()) {
//							((TrinketPlayerScreenHandler) serverPlayer.playerScreenHandler).trinkets$updateTrinketSlots(false);
//						}
//					}
//
//					inventoriesToSend.clear();
//				}
//			}
//
//			lastEquippedTrinkets.clear();
//			lastEquippedTrinkets.putAll(newlyEquippedTrinkets);
//		});
//	}
//
//	/**
//	 * @author Emi
//	 */
//	@Unique
//	private ItemStack getOldStack(SlotType type, int index) {
//		return lastEquippedTrinkets.getOrDefault(type.getGroup() + "/" + type.getName() + "/" + index, ItemStack.EMPTY);
//	}
}
