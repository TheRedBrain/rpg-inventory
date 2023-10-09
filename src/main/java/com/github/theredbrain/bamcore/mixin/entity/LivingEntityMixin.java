package com.github.theredbrain.bamcore.mixin.entity;

import com.github.theredbrain.bamcore.entity.DuckLivingEntityMixin;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.GameRulesRegistry;
import com.google.common.collect.*;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.TrinketsNetwork;
import dev.emi.trinkets.api.*;
import dev.emi.trinkets.api.event.TrinketDropCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(value = LivingEntity.class, priority = 950)
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin {
//    @Unique
//    private final Map<String, ItemStack> lastEquippedAdventureTrinkets = new HashMap<>();

//    @Unique
//    private static final TrackedData<Float> POISE = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT); // TODO poise

//    @Shadow
//    public DefaultedList<ItemStack> syncedHandStacks = DefaultedList.ofSize(4, ItemStack.EMPTY);
//
//    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /*@Inject(method = "initDataTracker", at = @At("RETURN"), cancellable = true) // TODO poise
    protected void bamcore$initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(POISE, 1.0F);

    }*/

    /**
     * @author TheRedBrain
     * @reason inject gamerule destroyDroppedItemsOnDeath into Trinkets drop logic
     */
    @Overwrite
    public void dropInventory() {
        LivingEntity entity = (LivingEntity) (Object) this;

        boolean keepInv = entity.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY);

        boolean destroyDroppedItems;
        if (entity.getServer() != null && entity instanceof PlayerEntity) {
            destroyDroppedItems = entity.getServer().getGameRules().getBoolean(GameRulesRegistry.DESTROY_DROPPED_ITEMS_ON_DEATH);
        } else {
            destroyDroppedItems = false;
        }
        TrinketsApi.getTrinketComponent(entity).ifPresent(trinkets -> trinkets.forEach((ref, stack) -> {
            if (stack.isEmpty()) {
                return;
            }

            TrinketEnums.DropRule dropRule = TrinketsApi.getTrinket(stack.getItem()).getDropRule(stack, ref, entity);

            dropRule = TrinketDropCallback.EVENT.invoker().drop(dropRule, stack, ref, entity);

            TrinketInventory inventory = ref.inventory();

            if (dropRule == TrinketEnums.DropRule.DEFAULT) {
                dropRule = inventory.getSlotType().getDropRule();
            }

            if (dropRule == TrinketEnums.DropRule.DEFAULT) {
                if (keepInv && entity.getType() == EntityType.PLAYER) {
                    dropRule = TrinketEnums.DropRule.KEEP;
                } else {
                    if (EnchantmentHelper.hasVanishingCurse(stack) || destroyDroppedItems) {
                        dropRule = TrinketEnums.DropRule.DESTROY;
                    } else {
                        dropRule = TrinketEnums.DropRule.DROP;
                    }
                }
            }

            switch (dropRule) {
                case DROP:
                    dropFromEntity(stack);
                    // Fallthrough
                case DESTROY:
                    inventory.setStack(ref.index(), ItemStack.EMPTY);
                    break;
                default:
                    break;
            }
        }));
    }

    private void dropFromEntity(ItemStack stack) {
        ItemEntity entity = dropStack(stack);
        // Mimic player drop behavior for only players
        if (entity != null && ((Entity) this) instanceof PlayerEntity) {
            entity.setPos(entity.getX(), this.getEyeY() - 0.3, entity.getZ());
            entity.setPickupDelay(40);
            float magnitude = this.random.nextFloat() * 0.5f;
            float angle = this.random.nextFloat() * ((float)Math.PI * 2);
            entity.setVelocity(-MathHelper.sin(angle) * magnitude, 0.2f, MathHelper.cos(angle) * magnitude);
        }
    }
//
//    @Inject(at = @At("TAIL"), method = "tick")
//    private void tick(CallbackInfo info) {
//        LivingEntity entity = (LivingEntity) (Object) this;
//        TrinketsApi.getTrinketComponent(entity).ifPresent(trinkets -> {
//            Map<String, ItemStack> newlyEquippedTrinkets = new HashMap<>();
//            Map<String, ItemStack> contentUpdates = new HashMap<>();
//            trinkets.forEach((ref, stack) -> {
//                TrinketInventory inventory = ref.inventory();
//                SlotType slotType = inventory.getSlotType();
//                int index = ref.index();
//                ItemStack oldStack = getOldAdventureStack(slotType, index);
//                ItemStack newStack = inventory.getStack(index);
//                ItemStack copy = newStack.copy();
//                String newRef = slotType.getGroup() + "/" + slotType.getName() + "/" + index;
//                newlyEquippedTrinkets.put(newRef, copy);
//
//                if (!ItemStack.areEqual(newStack, oldStack)) {
//
//                    if (!((LivingEntity) (Object) this).getWorld().isClient) {
//                        contentUpdates.put(newRef, copy);
//                        UUID uuid = SlotAttributes.getUuid(ref);
//
//                        if (!oldStack.isEmpty()) {
//                            Trinket trinket = TrinketsApi.getTrinket(oldStack.getItem());
//                            Multimap<EntityAttribute, EntityAttributeModifier> map = trinket.getModifiers(oldStack, ref, entity, uuid);
//                            Multimap<String, EntityAttributeModifier> slotMap = HashMultimap.create();
//                            Set<SlotAttributes.SlotEntityAttribute> toRemove = Sets.newHashSet();
//                            for (EntityAttribute attr : map.keySet()) {
//                                if (attr instanceof SlotAttributes.SlotEntityAttribute slotAttr) {
//                                    slotMap.putAll(slotAttr.slot, map.get(attr));
//                                    toRemove.add(slotAttr);
//                                }
//                            }
//                            for (SlotAttributes.SlotEntityAttribute attr : toRemove) {
//                                map.removeAll(attr);
//                            }
//                            ((LivingEntity) (Object) this).getAttributes().removeModifiers(map);
//                            trinkets.removeModifiers(slotMap);
//                        }
//
//                        if (!newStack.isEmpty()) {
//                            Trinket trinket = TrinketsApi.getTrinket(newStack.getItem());
//                            Multimap<EntityAttribute, EntityAttributeModifier> map = trinket.getModifiers(newStack, ref, entity, uuid);
//                            Multimap<String, EntityAttributeModifier> slotMap = HashMultimap.create();
//                            Set<SlotAttributes.SlotEntityAttribute> toRemove = Sets.newHashSet();
//                            for (EntityAttribute attr : map.keySet()) {
//                                if (attr instanceof SlotAttributes.SlotEntityAttribute slotAttr) {
//                                    slotMap.putAll(slotAttr.slot, map.get(attr));
//                                    toRemove.add(slotAttr);
//                                }
//                            }
//                            for (SlotAttributes.SlotEntityAttribute attr : toRemove) {
//                                map.removeAll(attr);
//                            }
//                            ((LivingEntity) (Object) this).getAttributes().addTemporaryModifiers(map);
//                            trinkets.addTemporaryModifiers(slotMap);
//                        }
//                    }
//
//                    if (!ItemStack.areItemsEqual(newStack, oldStack)) {
//                        TrinketsApi.getTrinket(oldStack.getItem()).onUnequip(oldStack, ref, entity);
//                        TrinketsApi.getTrinket(newStack.getItem()).onEquip(newStack, ref, entity);
//                    }
//                }
//            });
//
//            if (!((LivingEntity) (Object) this).getWorld().isClient) {
//                Set<TrinketInventory> inventoriesToSend = trinkets.getTrackingUpdates();
//
//                if (!contentUpdates.isEmpty() || !inventoriesToSend.isEmpty()) {
//                    PacketByteBuf buf = PacketByteBufs.create();
//                    buf.writeInt(entity.getId());
//                    NbtCompound tag = new NbtCompound();
//
//                    for (TrinketInventory trinketInventory : inventoriesToSend) {
//                        tag.put(trinketInventory.getSlotType().getGroup() + "/" + trinketInventory.getSlotType().getName(), trinketInventory.getSyncTag());
//                    }
//
//                    buf.writeNbt(tag);
//                    tag = new NbtCompound();
//
//                    for (Map.Entry<String, ItemStack> entry : contentUpdates.entrySet()) {
//                        tag.put(entry.getKey(), entry.getValue().writeNbt(new NbtCompound()));
//                    }
//
//                    buf.writeNbt(tag);
//
//                    for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
//                        ServerPlayNetworking.send(player, TrinketsNetwork.SYNC_INVENTORY, buf);
//                    }
//
//                    if (entity instanceof ServerPlayerEntity serverPlayer) {
//                        ServerPlayNetworking.send(serverPlayer, TrinketsNetwork.SYNC_INVENTORY, buf);
//
//                        if (!inventoriesToSend.isEmpty()) {
//                            ((TrinketPlayerScreenHandler) ((DuckPlayerEntityMixin)serverPlayer).bamcore$getAdventureInventoryScreenHandler()).trinkets$updateTrinketSlots(true);
//                        }
//                    }
//
//                    inventoriesToSend.clear();
//                }
//            }
//
//            lastEquippedAdventureTrinkets.clear();
//            lastEquippedAdventureTrinkets.putAll(newlyEquippedTrinkets);
//        });
//    }
//
//    @Unique
//    private ItemStack getOldAdventureStack(SlotType type, int index) {
//        return lastEquippedAdventureTrinkets.getOrDefault(type.getGroup() + "/" + type.getName() + "/" + index, ItemStack.EMPTY);
//    }

    /*@Override // TODO poise
    public float bamcore$getMaxPoise() {
        return (float) this.getAttributeValue(EntityAttributesRegistry.MAX_POISE);
    }

    @Override
    public void bamcore$addPoise(float amount) {
        float f = this.bamcore$getPoise();
        this.bamcore$setPoise(f + amount);
    }

    @Override
    public float bamcore$getPoise() {
        return this.dataTracker.get(POISE);
    }

    @Override
    public void bamcore$setPoise(float poise) {
        this.dataTracker.set(POISE, MathHelper.clamp(poise, 0, this.bamcore$getMaxPoise()));
    }*/
}
