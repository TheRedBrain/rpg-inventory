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
import net.minecraft.entity.LivingEntity;
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

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

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
}
