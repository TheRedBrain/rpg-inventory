package com.github.theredbrain.bamcore.mixin.entity;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.google.common.collect.*;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.TrinketsNetwork;
import dev.emi.trinkets.api.*;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique
    private final Map<String, ItemStack> lastEquippedAdventureTrinkets = new HashMap<>();

    @Shadow
    public DefaultedList<ItemStack> syncedHandStacks = DefaultedList.ofSize(4, ItemStack.EMPTY);

//    @Shadow
//    public DefaultedList<ItemStack> syncedArmorStacks = DefaultedList.ofSize(6, ItemStack.EMPTY);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Shadow protected abstract void setSyncedArmorStack(EquipmentSlot slot, ItemStack armor);

    @Shadow protected abstract void setSyncedHandStack(EquipmentSlot slot, ItemStack stack);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow public abstract boolean areItemsDifferent(ItemStack stack, ItemStack stack2);

    @Shadow public abstract AttributeContainer getAttributes();

    @Shadow protected abstract ItemStack getSyncedHandStack(EquipmentSlot slot);

    @Shadow protected abstract ItemStack getSyncedArmorStack(EquipmentSlot slot);

//    private DefaultedList<ItemStack> syncedPermanentStacks;
//    private DefaultedList<ItemStack> syncedPetStacks;


//    /**
//     * @author TheRedBrain
//     */
//    @Inject(method = "<init>", at = @At("TAIL"))
//    public void LivingEntity(EntityType entityType, World world, CallbackInfo ci) {
//        this.syncedHandStacks = DefaultedList.ofSize(4, ItemStack.EMPTY);
//        this.syncedArmorStacks = DefaultedList.ofSize(6, ItemStack.EMPTY);
////        this.syncedPermanentStacks = DefaultedList.ofSize(2, ItemStack.EMPTY);
////        this.syncedPetStacks = DefaultedList.ofSize(2, ItemStack.EMPTY);
//    }


    // TODO move to Trinket Origin Compat
    //  maybe inject into Apoli mixin
//    @Inject(method = "getEquipmentChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/AttributeContainer;removeModifiers(Lcom/google/common/collect/Multimap;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void removeTrinketPowers(CallbackInfoReturnable<Map> cir, Map map, EquipmentSlot var2[], int var3, int var4, EquipmentSlot equipmentSlot, ItemStack itemStack3, ItemStack itemStack4) {
//        List<TrinketStackPowerUtil.TrinketStackPower> trinketPowers = TrinketStackPowerUtil.getPowers(itemStack3);
//        if(trinketPowers.size() > 0) {
//            Identifier source = RPGMod.identifier("trinkets_power_holder");
//            PowerHolderComponent powerHolder = PowerHolderComponent.KEY.get(this);
//            trinketPowers.forEach(sp -> {
//                if(PowerTypeRegistry.contains(sp.powerId)) {
//                    powerHolder.removePower(PowerTypeRegistry.get(sp.powerId), source);
//                }
//            });
//            powerHolder.sync();
//        }
//    }
//
//    @Inject(method = "getEquipmentChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/AttributeContainer;addTemporaryModifiers(Lcom/google/common/collect/Multimap;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void addTrinketPowers(CallbackInfoReturnable<Map> cir, Map map, EquipmentSlot var2[], int var3, int var4, EquipmentSlot equipmentSlot, ItemStack itemStack3, ItemStack itemStack4) {
//        List<TrinketStackPowerUtil.TrinketStackPower> trinketPowers = TrinketStackPowerUtil.getPowers(itemStack3);
//        if(trinketPowers.size() > 0) {
//            Identifier source = RPGMod.identifier("trinkets_power_holder");
//            PowerHolderComponent powerHolder = PowerHolderComponent.KEY.get(this);
//            trinketPowers.forEach(sp -> {
//                if(PowerTypeRegistry.contains(sp.powerId)) {
//                    powerHolder.addPower(PowerTypeRegistry.get(sp.powerId), source);
//                }
//            });
//            powerHolder.sync();
//        } else if(TrinketStackPowerUtil.getPowers(itemStack3).size() > 0) {
//            PowerHolderComponent.KEY.get(this).sync();
//        }
//
//    }

    @Shadow public abstract int getItemUseTimeLeft();

    @Shadow protected abstract void spawnConsumptionEffects(ItemStack stack, int particleCount);

    @Shadow protected int itemUseTimeLeft;

    @Shadow protected abstract void consumeItem();

    @Shadow protected abstract boolean shouldSpawnConsumptionEffects();

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo info) {
        LivingEntity entity = (LivingEntity) (Object) this;
        TrinketsApi.getTrinketComponent(entity).ifPresent(trinkets -> {
            Map<String, ItemStack> newlyEquippedTrinkets = new HashMap<>();
            Map<String, ItemStack> contentUpdates = new HashMap<>();
            trinkets.forEach((ref, stack) -> {
                TrinketInventory inventory = ref.inventory();
                SlotType slotType = inventory.getSlotType();
                int index = ref.index();
                ItemStack oldStack = getOldAdventureStack(slotType, index);
                ItemStack newStack = inventory.getStack(index);
                ItemStack copy = newStack.copy();
                String newRef = slotType.getGroup() + "/" + slotType.getName() + "/" + index;
                newlyEquippedTrinkets.put(newRef, copy);

                if (!ItemStack.areEqual(newStack, oldStack)) {

                    if (!((LivingEntity) (Object) this).getWorld().isClient) {
                        contentUpdates.put(newRef, copy);
                        UUID uuid = SlotAttributes.getUuid(ref);

                        if (!oldStack.isEmpty()) {
                            Trinket trinket = TrinketsApi.getTrinket(oldStack.getItem());
                            Multimap<EntityAttribute, EntityAttributeModifier> map = trinket.getModifiers(oldStack, ref, entity, uuid);
                            Multimap<String, EntityAttributeModifier> slotMap = HashMultimap.create();
                            Set<SlotAttributes.SlotEntityAttribute> toRemove = Sets.newHashSet();
                            for (EntityAttribute attr : map.keySet()) {
                                if (attr instanceof SlotAttributes.SlotEntityAttribute slotAttr) {
                                    slotMap.putAll(slotAttr.slot, map.get(attr));
                                    toRemove.add(slotAttr);
                                }
                            }
                            for (SlotAttributes.SlotEntityAttribute attr : toRemove) {
                                map.removeAll(attr);
                            }
                            ((LivingEntity) (Object) this).getAttributes().removeModifiers(map);
                            trinkets.removeModifiers(slotMap);
                        }

                        if (!newStack.isEmpty()) {
                            Trinket trinket = TrinketsApi.getTrinket(newStack.getItem());
                            Multimap<EntityAttribute, EntityAttributeModifier> map = trinket.getModifiers(newStack, ref, entity, uuid);
                            Multimap<String, EntityAttributeModifier> slotMap = HashMultimap.create();
                            Set<SlotAttributes.SlotEntityAttribute> toRemove = Sets.newHashSet();
                            for (EntityAttribute attr : map.keySet()) {
                                if (attr instanceof SlotAttributes.SlotEntityAttribute slotAttr) {
                                    slotMap.putAll(slotAttr.slot, map.get(attr));
                                    toRemove.add(slotAttr);
                                }
                            }
                            for (SlotAttributes.SlotEntityAttribute attr : toRemove) {
                                map.removeAll(attr);
                            }
                            ((LivingEntity) (Object) this).getAttributes().addTemporaryModifiers(map);
                            trinkets.addTemporaryModifiers(slotMap);
                        }
                    }

                    if (!ItemStack.areItemsEqual(newStack, oldStack)) {
                        TrinketsApi.getTrinket(oldStack.getItem()).onUnequip(oldStack, ref, entity);
                        TrinketsApi.getTrinket(newStack.getItem()).onEquip(newStack, ref, entity);
                    }
                }
            });

            if (!((LivingEntity) (Object) this).getWorld().isClient) {
                Set<TrinketInventory> inventoriesToSend = trinkets.getTrackingUpdates();

                if (!contentUpdates.isEmpty() || !inventoriesToSend.isEmpty()) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(entity.getId());
                    NbtCompound tag = new NbtCompound();

                    for (TrinketInventory trinketInventory : inventoriesToSend) {
                        tag.put(trinketInventory.getSlotType().getGroup() + "/" + trinketInventory.getSlotType().getName(), trinketInventory.getSyncTag());
                    }

                    buf.writeNbt(tag);
                    tag = new NbtCompound();

                    for (Map.Entry<String, ItemStack> entry : contentUpdates.entrySet()) {
                        tag.put(entry.getKey(), entry.getValue().writeNbt(new NbtCompound()));
                    }

                    buf.writeNbt(tag);

                    for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
                        ServerPlayNetworking.send(player, TrinketsNetwork.SYNC_INVENTORY, buf);
                    }

                    if (entity instanceof ServerPlayerEntity serverPlayer) {
                        ServerPlayNetworking.send(serverPlayer, TrinketsNetwork.SYNC_INVENTORY, buf);

                        if (!inventoriesToSend.isEmpty()) {
                            ((TrinketPlayerScreenHandler) ((DuckPlayerEntityMixin)serverPlayer).bamcore$getInventoryScreenHandler()).trinkets$updateTrinketSlots(true);
                        }
                    }

                    inventoriesToSend.clear();
                }
            }

            lastEquippedAdventureTrinkets.clear();
            lastEquippedAdventureTrinkets.putAll(newlyEquippedTrinkets);
        });
    }

    @Unique
    private ItemStack getOldAdventureStack(SlotType type, int index) {
        return lastEquippedAdventureTrinkets.getOrDefault(type.getGroup() + "/" + type.getName() + "/" + index, ItemStack.EMPTY);
    }

//    /**
//     * @author TheRedBrain
//     * @reason
//     */
//    @Overwrite
//    protected void tickItemStackUsage(ItemStack stack) {
//        stack.usageTick(this.getWorld(), ((LivingEntity) (Object) this), this.getItemUseTimeLeft());
//        if (this.shouldSpawnConsumptionEffects()) {
//            this.spawnConsumptionEffects(stack, 5);
//        }
//        if (--this.itemUseTimeLeft == 0 && !this.getWorld().isClient && !stack.isUsedOnRelease()) {
//            this.consumeItem();
//        }
//    }

//    /**
//     * @author TheRedBrain
//     * @reason account for additional synced stacks
//     */
//    @Overwrite
//    @Nullable
//    private Map<EquipmentSlot, ItemStack> getEquipmentChanges() {
//        EnumMap<EquipmentSlot, ItemStack> map = null;
//        block4: for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
//            ItemStack itemStack = ItemStack.EMPTY;
//            EquipmentSlot.Type type = equipmentSlot.getType();
//            if (type == EquipmentSlot.Type.HAND) {
//                itemStack = this.getSyncedHandStack(equipmentSlot);
//            } else if (type == EquipmentSlot.Type.ARMOR) {
//                itemStack = this.getSyncedArmorStack(equipmentSlot);
//            } else if (type == ExtendedEquipmentSlotType.PERMANENT) {
//                itemStack = this.getSyncedPermanentStack(equipmentSlot);
//            } else if (type == ExtendedEquipmentSlotType.PET) {
//                itemStack = this.getSyncedPetStack(equipmentSlot);
//            }
////            switch (equipmentSlot.getType()) {
////                case HAND: {
////                    itemStack = this.getSyncedHandStack(equipmentSlot);
////                    break;
////                }
////                case ARMOR: {
////                    itemStack = this.getSyncedArmorStack(equipmentSlot);
////                    break;
////                }
////                default: {
////                    continue block4;
////                }
////            }
//            ItemStack itemStack2 = this.getEquippedStack(equipmentSlot);
//            if (!this.areItemsDifferent(itemStack, itemStack2)) continue;
//            if (map == null) {
//                map = Maps.newEnumMap(EquipmentSlot.class);
//            }
//            map.put(equipmentSlot, itemStack2);
//            if (!itemStack.isEmpty()) {
//                this.getAttributes().removeModifiers(itemStack.getAttributeModifiers(equipmentSlot));
//            }
//            if (itemStack2.isEmpty()) continue;
//            this.getAttributes().addTemporaryModifiers(itemStack2.getAttributeModifiers(equipmentSlot));
//        }
//        return map;
//    }

//    /**
//     * @author TheRedBrain
//     * @reason account for additional synced stacks
//     */
//    @Overwrite
//    private void sendEquipmentChanges(Map<EquipmentSlot, ItemStack> equipmentChanges) {
//        ArrayList<Pair<EquipmentSlot, ItemStack>> list = Lists.newArrayListWithCapacity(equipmentChanges.size());
//        equipmentChanges.forEach((slot, stack) -> {
//            ItemStack itemStack = stack.copy();
//            list.add(Pair.of(slot, itemStack));
//            EquipmentSlot.Type type = slot.getType();
//            if (type == EquipmentSlot.Type.HAND) {
//                this.setSyncedHandStack((EquipmentSlot)((Object)slot), itemStack);
//            } else if (type == EquipmentSlot.Type.ARMOR) {
//                this.setSyncedArmorStack((EquipmentSlot)((Object)slot), itemStack);
//            } else if (type == ExtendedEquipmentSlotType.PERMANENT) {
//                this.setSyncedPermanentStack((EquipmentSlot)((Object)slot), itemStack);
//            } else if (type == ExtendedEquipmentSlotType.PET) {
//                this.setSyncedPetStack((EquipmentSlot)((Object)slot), itemStack);
//            }
//        });
//        ((ServerWorld)this.world).getChunkManager().sendToOtherNearbyPlayers(this, new EntityEquipmentUpdateS2CPacket(this.getId(), list));
//    }
//
//    public ItemStack getSyncedPermanentStack(EquipmentSlot slot) {
//        return this.syncedPermanentStacks.get(slot.getEntitySlotId());
//    }
//
//    public void setSyncedPermanentStack(EquipmentSlot slot, ItemStack armor) {
//        this.syncedPermanentStacks.set(slot.getEntitySlotId(), armor);
//    }
//
//    public ItemStack getSyncedPetStack(EquipmentSlot slot) {
//        return this.syncedPetStacks.get(slot.getEntitySlotId());
//    }
//
//    public void setSyncedPetStack(EquipmentSlot slot, ItemStack stack) {
//        this.syncedPetStacks.set(slot.getEntitySlotId(), stack);
//    }

//    /**
//     * @author TheRedBrain
//     */
//    @Inject(method = "getPreferredEquipmentSlot", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
//    private static void bam$getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir, Item item) {
//        if (item instanceof AccessoryBeltItem) {
//            cir.setReturnValue(ExtendedEquipmentSlot.BELT);
//            cir.cancel();
//        }
//        if (item instanceof AccessoryNecklaceItem) {
//            cir.setReturnValue(ExtendedEquipmentSlot.NECKLACE);
//            cir.cancel();
//        }
//    }

//    @Inject(method = "getPreferredEquipmentSlot", at = @At("TAIL"), cancellable = true)
//    private static void bam$getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
//        Item item = stack.getItem();
//        if (item instanceof CustomArmorItem) {
//            cir.setReturnValue(((CustomArmorItem)item).getSlotType());
//            cir.cancel();
//        }
//    }
}
