package com.github.theredbrain.bamcore.screen;

import com.github.theredbrain.bamcore.BetterAdventureModCore;
import com.github.theredbrain.bamcore.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.EntityAttributesRegistry;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import com.github.theredbrain.bamcore.registry.Tags;
import com.github.theredbrain.bamcore.screen.slot.AdventureTrinketSlot;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.emi.trinkets.Point;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.TrinketsClient;
import dev.emi.trinkets.api.*;
import dev.emi.trinkets.mixin.accessor.ScreenHandlerAccessor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AdventureInventoryScreenHandler extends ScreenHandler implements TrinketPlayerScreenHandler {
    public static final Identifier BLOCK_ATLAS_TEXTURE = new Identifier("textures/atlas/blocks.png");
//    public static final Identifier EMPTY_BELT_SLOT_TEXTURE = RPGMod.identifier("item/empty_armor_slot_belt");
    public static final Identifier EMPTY_BOOTS_SLOT_TEXTURE = BetterAdventureModCore.identifier("item/empty_armor_slot_boots");
    public static final Identifier EMPTY_CHESTPLATE_SLOT_TEXTURE = BetterAdventureModCore.identifier("item/empty_armor_slot_chestplate");
//    public static final Identifier EMPTY_GLOVES_SLOT_TEXTURE = RPGMod.identifier("item/empty_armor_slot_gloves");
    public static final Identifier EMPTY_HELMET_SLOT_TEXTURE = BetterAdventureModCore.identifier("item/empty_armor_slot_helmet");
    public static final Identifier EMPTY_LEGGINGS_SLOT_TEXTURE = BetterAdventureModCore.identifier("item/empty_armor_slot_leggings");
//    public static final Identifier EMPTY_NECKLACE_SLOT_TEXTURE = RPGMod.identifier("item/empty_armor_slot_necklace");
//    public static final Identifier EMPTY_RING_SLOT_TEXTURE = RPGMod.identifier("item/empty_armor_slot_ring_2");
    public static final Identifier EMPTY_OFFHAND_ARMOR_SLOT = BetterAdventureModCore.identifier("item/empty_armor_slot_shield");
//    public static final Identifier EMPTY_SHOULDERS_SLOT_TEXTURE = RPGMod.identifier("item/empty_armor_slot_shoulders");
    public static final Identifier EMPTY_MAINHAND_ARMOR_SLOT = BetterAdventureModCore.identifier("item/empty_armor_slot_sword");
//    public static final Identifier EMPTY_MOUNT_SLOT_TEXTURE = RPGMod.identifier("item/empty_armor_slot_mount");
    public final boolean onServer;
    private final PlayerEntity owner;
    private int spellSlotsX = 98;
    private int spellSlotsY = 62;
    private final int[] spellSlotIds = {48, 49, 50, 51, 52, 53, 54, 55};
    private final int trinketSlotAmount = 14;

    private final Map<SlotGroup, Integer> groupNums = new HashMap<>();
    private final Map<SlotGroup, Point> groupPos = new HashMap<>();
    private final Map<SlotGroup, List<Point>> slotHeights = new HashMap<>();
    private final Map<SlotGroup, List<SlotType>> slotTypes = new HashMap<>();
    private final Map<SlotGroup, Integer> slotWidths = new HashMap<>();
    private int trinketSlotStart = 0;
    private int trinketSlotEnd = 0;

    private int groupCount = 0;

    private PlayerInventory inventory;

    public AdventureInventoryScreenHandler(PlayerInventory inventory, boolean onServer, final PlayerEntity owner) {
        super(null, 0);
        int i;
        this.onServer = onServer;
        this.owner = owner;
        this.inventory = inventory;
        // hotbar
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 178) {

                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.isIn(Tags.ADVENTURE_HOTBAR_ITEMS) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()) || owner.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT);
                }
            });
        }
        // main inventory
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + (i + 1) * 9, 8 + j * 18, 120 + i * 18));
            }
        }
        // boots slot
        this.addSlot(new Slot(inventory, 36, 77, 80) {

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.FEET, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return (EquipmentSlot.FEET == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.EXTRA_BOOTS_ITEMS))
                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_BOOTS_SLOT_TEXTURE);
            }
        });
        // leggings slot
        this.addSlot(new Slot(inventory, 37, 8, 80) {

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.LEGS, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return (EquipmentSlot.LEGS == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.EXTRA_LEGGINGS_ITEMS))
                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_LEGGINGS_SLOT_TEXTURE);
            }
        });
        // chestplate slot
        this.addSlot(new Slot(inventory, 38, 8, 44) {

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.CHEST, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return (EquipmentSlot.CHEST == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.EXTRA_CHESTPLATE_ITEMS))
                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_CHESTPLATE_SLOT_TEXTURE);
            }
        });
        // helmet slot
        this.addSlot(new Slot(inventory, 39, 33, 8) {

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.HEAD, itemStack, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return (EquipmentSlot.HEAD == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.EXTRA_HELMET_ITEMS))
                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_HELMET_SLOT_TEXTURE);
            }
        });
//        // gloves slot
//        this.addSlot(new Slot(inventory, 40, 77, 62) {
//
//            @Override
//            public void setStack(ItemStack stack) {
//                ItemStack itemStack = this.getStack();
//                super.setStack(stack);
//                owner.onEquipStack(ExtendedEquipmentSlot.GLOVES, itemStack, stack);
//            }
//
//            @Override
//            public int getMaxItemCount() {
//                return 1;
//            }
//
//            @Override
//            public boolean canInsert(ItemStack stack) {
//                return (ExtendedEquipmentSlot.GLOVES == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.EXTRA_GLOVES_ITEMS))
//                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
//            }
//
//            @Override
//            public boolean canTakeItems(PlayerEntity playerEntity) {
//                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
//            }
//
//            @Override
//            public Pair<Identifier, Identifier> getBackgroundSprite() {
//                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_GLOVES_SLOT_TEXTURE);
//            }
//        });
//        // shoulders slot
//        this.addSlot(new Slot(inventory, 41, 8, 26) {
//
//            @Override
//            public void setStack(ItemStack stack) {
//                ItemStack itemStack = this.getStack();
//                super.setStack(stack);
//                owner.onEquipStack(ExtendedEquipmentSlot.SHOULDERS, itemStack, stack);
//            }
//
//            @Override
//            public int getMaxItemCount() {
//                return 1;
//            }
//
//            @Override
//            public boolean canInsert(ItemStack stack) {
//                return (ExtendedEquipmentSlot.SHOULDERS == MobEntity.getPreferredEquipmentSlot(stack) || stack.isIn(Tags.EXTRA_SHOULDERS_ITEMS))
//                        && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure()));
//            }
//
//            @Override
//            public boolean canTakeItems(PlayerEntity playerEntity) {
//                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).isAdventure());
//            }
//
//            @Override
//            public Pair<Identifier, Identifier> getBackgroundSprite() {
//                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_SHOULDERS_SLOT_TEXTURE);
//            }
//        });
        // mainhand slot
        this.addSlot(new Slot(inventory, 40, 8, 98) {

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.MAINHAND, itemStack, stack);
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.MAIN_HAND_ITEMS) && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_MAINHAND_ARMOR_SLOT);
            }
        });
        // offhand slot
        this.addSlot(new Slot(inventory, 41, 26, 98) {

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(EquipmentSlot.OFFHAND, itemStack, stack);
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.OFF_HAND_ITEMS) && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_OFFHAND_ARMOR_SLOT);
            }
        });
        // alternative mainhand slot
        this.addSlot(new Slot(inventory, 42, 59, 98) {

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(ExtendedEquipmentSlot.ALT_MAINHAND, itemStack, stack);
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.MAIN_HAND_ITEMS) && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_MAINHAND_ARMOR_SLOT);
            }
        });
        // alternative offhand slot
        this.addSlot(new Slot(inventory, 43, 77, 98) {

            @Override
            public void setStack(ItemStack stack) {
                ItemStack itemStack = this.getStack();
                super.setStack(stack);
                owner.onEquipStack(ExtendedEquipmentSlot.ALT_OFFHAND, itemStack, stack);
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(Tags.OFF_HAND_ITEMS) && (owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return owner.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure());
            }

            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_OFFHAND_ARMOR_SLOT);
            }
        });
        trinkets$updateTrinketSlots(true);
    }

    public static boolean isInHotbar(int slot) {
        return slot >= 0 && slot < 9;// || slot == 45;
    }

//    @Override
//    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
//        boolean bl = super.insertItem(stack, startIndex, endIndex, fromLast);
////        RPGMod.LOGGER.info("insertItem: " + bl);
//        return bl;
//    }

    @Override
    public void onClosed(PlayerEntity player) {
        if (player.getWorld().isClient) {
            TrinketsClient.activeGroup = null;
            TrinketsClient.activeType = null;
            TrinketsClient.quickMoveGroup = null;
        }
        super.onClosed(player);
//        if (owner instanceof ServerPlayerEntity) {
//            RPGMod.LOGGER.info("-----onClosed-----");
//            int activeSpellSlotAmount = (int) owner.getAttributeInstance(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT).getValue();
//
////            PlayerInventory playerInventory = owner.getInventory();
////            this.inventory
//            for (int i = 0; i < 8; i++) {
//                if (activeSpellSlotAmount <= i) {
//                    RPGMod.LOGGER.info("spell slot " + i + " is inactive");
//
//                    RPGMod.LOGGER.info("before drop attempt: spell slot " + i + " is empty: " + this.slots.get(spellSlotIds[i]).inventory.isEmpty());
////                    owner.dropItem(this.slots.get(spellSlotIds[i]).inventory.removeStack(0), true);
//                    this.inventory.offerOrDrop(this.slots.get(spellSlotIds[i]).inventory.removeStack(0));
//                    RPGMod.LOGGER.info("after drop attempt: spell slot " + i + " is empty: " + this.slots.get(spellSlotIds[i]).inventory.isEmpty());
//                } else {
//                    RPGMod.LOGGER.info("spell slot " + i + " is active");
//                }
////                RPGMod.LOGGER.info("spell slot " + i + "'s size: " + this.slots.get(spellSlotIds[i]).inventory.size());
//                for (int j = 0; j < this.slots.get(spellSlotIds[i]).inventory.size(); j++) {
//                    RPGMod.LOGGER.info("spell slot " + i + "'s itemStack in slot " + j + ": " + this.slots.get(spellSlotIds[i]).inventory.getStack(j).toString());
//                }
//                RPGMod.LOGGER.info("spell slot id for slot " + i + ": " + spellSlotIds[i]);
//            }
////            String slotGroup = slot.inventory().getSlotType().getGroup();
////            if ((Objects.equals(slotGroup, "spell_slot_1") && activeSpellSlotAmount < 1)
////                    || (Objects.equals(slotGroup, "spell_slot_2") && activeSpellSlotAmount < 2)
////                    || (Objects.equals(slotGroup, "spell_slot_3") && activeSpellSlotAmount < 3)
////                    || (Objects.equals(slotGroup, "spell_slot_4") && activeSpellSlotAmount < 4)
////                    || (Objects.equals(slotGroup, "spell_slot_5") && activeSpellSlotAmount < 5)
////                    || (Objects.equals(slotGroup, "spell_slot_6") && activeSpellSlotAmount < 6)
////                    || (Objects.equals(slotGroup, "spell_slot_7") && activeSpellSlotAmount < 7)
////                    || (Objects.equals(slotGroup, "spell_slot_8") && activeSpellSlotAmount < 8)) {
////                Slot currentSlot = slot.inventory().sta.getStack(slot.index());
////                playerEntity.getInventory().offerOrDrop(slot.inventory().getStack(slot.index()));
////            }
//        }
    }
    /**
     * Called when a slot's content has changed.
     *
     * <p>This is not called by default; subclasses that override this method
     * should also use a custom {@link Inventory} whose {@link Inventory#markDirty markDirty} method is
     * overridden to call this method as a backing inventory of the slot.
     *
     * <p>This can be used to update the output slot when input changes.
     */
    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
//        RPGMod.LOGGER.info("-----onContentChanged-----");
////        if (owner instanceof ServerPlayerEntity) {
//            int activeSpellSlotAmount = (int) owner.getAttributeInstance(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT).getValue();
//
////            PlayerInventory playerInventory = owner.getInventory();
////            this.inventory
//            for (int i = 0; i < 8; i++) {
//                if (activeSpellSlotAmount <= i) {
//                    RPGMod.LOGGER.info("spell slot " + i + " is inactive");
//                    RPGMod.LOGGER.info("spell slot " + i + " is empty. " + this.slots.get(spellSlotIds[i]).inventory.isEmpty());
//
//                    this.inventory.offerOrDrop(this.slots.get(spellSlotIds[i]).inventory.removeStack(0));
//                }
//            }
//            String slotGroup = slot.inventory().getSlotType().getGroup();
//            if ((Objects.equals(slotGroup, "spell_slot_1") && activeSpellSlotAmount < 1)
//                    || (Objects.equals(slotGroup, "spell_slot_2") && activeSpellSlotAmount < 2)
//                    || (Objects.equals(slotGroup, "spell_slot_3") && activeSpellSlotAmount < 3)
//                    || (Objects.equals(slotGroup, "spell_slot_4") && activeSpellSlotAmount < 4)
//                    || (Objects.equals(slotGroup, "spell_slot_5") && activeSpellSlotAmount < 5)
//                    || (Objects.equals(slotGroup, "spell_slot_6") && activeSpellSlotAmount < 6)
//                    || (Objects.equals(slotGroup, "spell_slot_7") && activeSpellSlotAmount < 7)
//                    || (Objects.equals(slotGroup, "spell_slot_8") && activeSpellSlotAmount < 8)) {
//                Slot currentSlot = slot.inventory().sta.getStack(slot.index());
//                playerEntity.getInventory().offerOrDrop(slot.inventory().getStack(slot.index()));
//            }
//        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (/*slot2 != null && */slot.hasStack()) {
            int i;
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
            if (index >= trinketSlotStart && index < trinketSlotEnd) {
                if (!this.insertItem(itemStack2, 0, 36, false)) {
                    return ItemStack.EMPTY;
                } else {
                    return itemStack2;
                }
            } else if (index >= 0 && index < 36) {
                TrinketsApi.getTrinketComponent(player).ifPresent(trinkets -> {
                    for (int j = trinketSlotStart; j < trinketSlotEnd; j++) {
                        Slot s = slots.get(j);
                        if (!(s instanceof AdventureTrinketSlot) || !s.canInsert(itemStack2)) {
                            continue;
                        }

                        AdventureTrinketSlot ts = (AdventureTrinketSlot) s;
                        SlotType type = ts.getType();
                        SlotReference ref = new SlotReference((TrinketInventory) ts.inventory, ts.getIndex());

                        boolean res = TrinketsApi.evaluatePredicateSet(type.getQuickMovePredicates(), itemStack2, ref, player);

                        if (res) {
                            if (this.insertItem(itemStack2, j, j + 1, false)) {
                                if (player.getWorld().isClient) {
                                    TrinketsClient.quickMoveTimer = 20;
                                    TrinketsClient.quickMoveGroup = TrinketsApi.getPlayerSlots(this.owner).get(type.getGroup());
                                    if (ref.index() > 0) {
                                        TrinketsClient.quickMoveType = type;
                                    } else {
                                        TrinketsClient.quickMoveType = null;
                                    }
                                }
                            }
                        }
                    }
                });
            }
//            if (itemStack.getItem() instanceof AccessoryRingItem) {
//
//            }
//            if (slot == 0) {
//                if (!this.insertItem(itemStack2, 9, 45, true)) {
//                    return ItemStack.EMPTY;
//                }
//                slot2.onQuickTransfer(itemStack2, itemStack);
//            } else
            if ((index >= 36 && index < 44 ? !this.insertItem(itemStack2, 0, 36, false)
                    : (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR && !((Slot)this.slots.get(36 + equipmentSlot.getEntitySlotId())).hasStack() ? !this.insertItem(itemStack2, i = 36 + equipmentSlot.getEntitySlotId(), i + 1, false)
//                    : (equipmentSlot.getType() == ExtendedEquipmentSlotType.ACCESSORY && !((Slot)this.slots.get(42 + equipmentSlot.getEntitySlotId())).hasStack() ? !this.insertItem(itemStack2, i = 42 + equipmentSlot.getEntitySlotId(), i + 1, false)
//                    : (itemStack.getItem() instanceof AccessoryRingItem && !((Slot)this.slots.get(42)).hasStack() ? !this.insertItem(itemStack2, 42, 43, false)
                    : (itemStack.isIn(Tags.MAIN_HAND_ITEMS) && !((Slot)this.slots.get(40)).hasStack() ? !this.insertItem(itemStack2, 40, 41, false)
                    : (itemStack.isIn(Tags.OFF_HAND_ITEMS) && !((Slot)this.slots.get(41)).hasStack() ? !this.insertItem(itemStack2, 41, 42, false)
                    : (itemStack.isIn(Tags.MAIN_HAND_ITEMS) && !((Slot)this.slots.get(42)).hasStack() ? !this.insertItem(itemStack2, 42, 43, false)
                    : (itemStack.isIn(Tags.OFF_HAND_ITEMS) && !((Slot)this.slots.get(43)).hasStack() ? !this.insertItem(itemStack2, 43, 44, false)
//                    : (equipmentSlot.getType() == EquipmentSlot.Type.HAND && !((Slot)this.slots.get(42 + equipmentSlot.getEntitySlotId())).hasStack() ? !this.insertItem(itemStack2, i = 42 + equipmentSlot.getEntitySlotId(), i + 1, false)
//                    : (equipmentSlot.getType() == ExtendedEquipmentSlotType.PET && !((Slot)this.slots.get(46 + equipmentSlot.getEntitySlotId())).hasStack() ? !this.insertItem(itemStack2, i = 46 + equipmentSlot.getEntitySlotId(), i + 1, false)
                    : (index >= 9 && index < 36 ? !this.insertItem(itemStack2, 0, 9, false)
                    : (index >= 0 && index < 9 ? !this.insertItem(itemStack2, 9, 36, false)
                    : !this.insertItem(itemStack2, 0, 36, false)))))))))) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
//            if (slot == 0) {
//                player.dropItem(itemStack2, false);
//            }
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    // test
/*
    @Override
    public void trinkets$updateTrinketSlots(boolean slotsChanged) {
        TrinketsApi.getTrinketComponent(owner).ifPresent(trinkets -> {
            if (slotsChanged) trinkets.update();
            Map<String, SlotGroup> groups = trinkets.getGroups();
            groupPos.clear();
            while (trinketSlotStart < trinketSlotEnd) {
                slots.remove(trinketSlotStart);
                ((ScreenHandlerAccessor) (this)).getTrackedStacks().remove(trinketSlotStart);
                ((ScreenHandlerAccessor) (this)).getPreviousTrackedStacks().remove(trinketSlotStart);
                trinketSlotEnd--;
            }

            int groupNum = 1; // Start at 1 because offhand exists

            for (SlotGroup group : groups.values().stream().sorted(Comparator.comparing(SlotGroup::getOrder)).toList()) {
                if (!hasSlots(trinkets, group)) {
                    continue;
                }
                int order = group.getOrder();
                int id = group.getSlotId();
                if (id != -1) {
//                    if (this.slots.size() > id) {
//                        Slot slot = this.slots.get(id);
//                        if (!(slot instanceof SurvivalTrinketSlot)) {
//                            groupPos.put(group, new Point(slot.x, slot.y));
//                            groupNums.put(group, -id);
//                        }
//                    }
                    return;
                } else {
//                    int x = 77;
//                    int y;
//                    if (groupNum >= 4) {
//                        x = 4 - (groupNum / 4) * 18;
//                        y = 8 + (groupNum % 4) * 18;
//                    } else {
//                        y = 62 - groupNum * 18;
//                    }
                    int x;
                    int y;
                    if (order == 0) {
                        // necklaces
                        x = 52;
                        y = -10;//8; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 1) {
                        // belts
                        x = 8;
                        y = 44;//62; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 2) {
                        // rings 1
                        x = 77;
                        y = 8;//26; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 3) {
                        // rings 2
                        x = 77;
                        y = 26;//44; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 4) {
                        // spell slot 1
                        x = this.spellSlotsX;
                        y = this.spellSlotsY;

                    } else if (order == 5) {
                        // spell slot 2
                        x = this.spellSlotsX + 18;
                        y = this.spellSlotsY;

                    } else if (order == 6) {
                        // spell slot 3
                        x = this.spellSlotsX + 36;
                        y = this.spellSlotsY;

                    } else if (order == 7) {
                        // spell slot 4
                        x = this.spellSlotsX + 54;
                        y = this.spellSlotsY;

                    } else if (order == 8) {
                        // spell slot 5
                        x = this.spellSlotsX;
                        y = this.spellSlotsY + 18;

                    } else if (order == 9) {
                        // spell slot 6
                        x = this.spellSlotsX + 18;
                        y = this.spellSlotsY + 18;

                    } else if (order == 10) {
                        // spell slot 7
                        x = this.spellSlotsX + 36;
                        y = this.spellSlotsY + 18;

                    } else if (order == 11) {
                        // spell slot 8
                        x = this.spellSlotsX + 54;
                        y = this.spellSlotsY + 18;

                    } else if (order == 12) {
                        // gloves
                        x = 77;
                        y = 62;

                    } else if (order == 13) {
                        // shoulders
                        x = 8;
                        y = 26;

                    } else {
                        return;
                    }
                    groupPos.put(group, new Point(x, y));
                    groupNums.put(group, groupNum);
                    groupNum++;
                }
            }
            groupCount = Math.max(0, groupNum - 4);
            trinketSlotStart = slots.size();
            slotWidths.clear();
            slotHeights.clear();
            slotTypes.clear();

            int activeSpellSlotAmount = (int) owner.getAttributeInstance(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT).getValue();
            AdventureTrinketSlot[] newSlots = new AdventureTrinketSlot[this.trinketSlotAmount];

            for (Map.Entry<String, Map<String, TrinketInventory>> entry : trinkets.getInventory().entrySet()) {
                String groupId = entry.getKey();
                SlotGroup group = groups.get(groupId);
                int groupOffset = 1;

                if (group.getSlotId() != -1) {
                    groupOffset++;
                }
                int width = 0;
                Point pos = trinkets$getGroupPos(group);
                if (pos == null) {
                    continue;
                }
                for (Map.Entry<String, TrinketInventory> slot : entry.getValue().entrySet().stream().sorted((a, b) ->
                        Integer.compare(a.getValue().getSlotType().getOrder(), b.getValue().getSlotType().getOrder())).toList()) {
                    TrinketInventory stacks = slot.getValue();
                    if (stacks.size() == 0) {
                        continue;
                    }
                    int slotOffset = 1;
//                    int x = (int) ((groupOffset / 2) * 18 * Math.pow(-1, groupOffset));
                    int x = groupPos.get(group).x();
                    slotHeights.computeIfAbsent(group, (k) -> new ArrayList<>()).add(new Point(x, stacks.size()));
                    slotTypes.computeIfAbsent(group, (k) -> new ArrayList<>()).add(stacks.getSlotType());
                    for (int i = 0; i < stacks.size(); i++) {

                        int order = group.getOrder();
//                        int y = (int) (pos.y() + (slotOffset / 2) * 18 * Math.pow(-1, slotOffset));
                        this.addSlot(new AdventureTrinketSlot(stacks, i, groupPos.get(group).x(), groupPos.get(group).y(), group, stacks.getSlotType(), 0, owner) {
                            @Override
                            public boolean isEnabled() {
                                return !((Objects.equals(groupId, "spell_slot_1") && activeSpellSlotAmount < 1)
                                        || (Objects.equals(groupId, "spell_slot_2") && activeSpellSlotAmount < 2)
                                        || (Objects.equals(groupId, "spell_slot_3") && activeSpellSlotAmount < 3)
                                        || (Objects.equals(groupId, "spell_slot_4") && activeSpellSlotAmount < 4)
                                        || (Objects.equals(groupId, "spell_slot_5") && activeSpellSlotAmount < 5)
                                        || (Objects.equals(groupId, "spell_slot_6") && activeSpellSlotAmount < 6)
                                        || (Objects.equals(groupId, "spell_slot_7") && activeSpellSlotAmount < 7)
                                        || (Objects.equals(groupId, "spell_slot_8") && activeSpellSlotAmount < 8));
                            }
                        });
                        slotOffset++;
//                        newSlots[order] = new AdventureTrinketSlot(stacks, i, groupPos.get(group).x(), groupPos.get(group).y(), group, stacks.getSlotType(), 0, owner) {
//                            @Override
//                            public boolean isEnabled() {
//                                return !((Objects.equals(groupId, "spell_slot_1") && activeSpellSlotAmount < 1)
//                                        || (Objects.equals(groupId, "spell_slot_2") && activeSpellSlotAmount < 2)
//                                        || (Objects.equals(groupId, "spell_slot_3") && activeSpellSlotAmount < 3)
//                                        || (Objects.equals(groupId, "spell_slot_4") && activeSpellSlotAmount < 4)
//                                        || (Objects.equals(groupId, "spell_slot_5") && activeSpellSlotAmount < 5)
//                                        || (Objects.equals(groupId, "spell_slot_6") && activeSpellSlotAmount < 6)
//                                        || (Objects.equals(groupId, "spell_slot_7") && activeSpellSlotAmount < 7)
//                                        || (Objects.equals(groupId, "spell_slot_8") && activeSpellSlotAmount < 8));
//                            }
//                        };
                    }
                    groupOffset++;
                    width++;
                }
                slotWidths.put(group, width);
            }

//            for (int i = 0; i < this.trinketSlotAmount; i++) {
//                if (newSlots[i] != null) {
//                    RPGMod.LOGGER.info("newSlots[" + i + "] != null");
//                    this.addSlot(newSlots[i]);
//                } else {
//                    RPGMod.LOGGER.info("newSlots[" + i + "] == null");
//                }
//            }

            trinketSlotEnd = slots.size();
        });
    }
/**/

/**/
    // Real one
    @Override
    public void trinkets$updateTrinketSlots(boolean slotsChanged) {
        TrinketsApi.getTrinketComponent(owner).ifPresent(trinkets -> {
//            RPGMod.LOGGER.info("slotsChanged: " + slotsChanged);
            if (slotsChanged) trinkets.update();
            Map<String, SlotGroup> groups = trinkets.getGroups();
//            RPGMod.LOGGER.info("trinkets.getGroups: " + trinkets.getGroups());
            groupPos.clear();
            while (trinketSlotStart < trinketSlotEnd) {
                slots.remove(trinketSlotStart);
                ((ScreenHandlerAccessor) (this)).getTrackedStacks().remove(trinketSlotStart);
                ((ScreenHandlerAccessor) (this)).getPreviousTrackedStacks().remove(trinketSlotStart);
                trinketSlotEnd--;
            }

//            RPGMod.LOGGER.info("first time: trinkets.getInventory().entrySet(): " + trinkets.getInventory().entrySet());
            int groupNum = 1; // Start at 1 because offhand exists

//            RPGMod.LOGGER.info("groups.values(): " + groups.values());


            //------------------------------------
            for (SlotGroup group : groups.values().stream().sorted(Comparator.comparing(SlotGroup::getOrder)).toList()) {
                if (!hasSlots(trinkets, group)) {
                    continue;
                }
                int order = group.getOrder();
                int id = group.getSlotId();
                if (id != -1) {
//                    if (this.slots.size() > id) {
//                        Slot slot = this.slots.get(id);
//                        if (!(slot instanceof SurvivalTrinketSlot)) {
//                            groupPos.put(group, new Point(slot.x, slot.y));
//                            groupNums.put(group, -id);
//                        }
//                    }
                    continue;
                } else {
//                    int x = 77;
//                    int y;
//                    if (groupNum >= 4) {
//                        x = 4 - (groupNum / 4) * 18;
//                        y = 8 + (groupNum % 4) * 18;
//                    } else {
//                        y = 62 - groupNum * 18;
//                    }
//                    groupPos.put(group, new Point(x, y));
//                    groupNums.put(group, groupNum);
//                    groupNum++;
                    int x;
                    int y;
                    if (order == 0) {
//                        RPGMod.LOGGER.info("necklaces slot");
                        // necklaces
                        x = 52;
                        y = -10;//8; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 1) {
//                        RPGMod.LOGGER.info("belts slot");
                        // belts
                        x = 8;
                        y = 44;//62; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 2) {
//                        RPGMod.LOGGER.info("rings 1 slot");
                        // rings 1
                        x = 77;
                        y = 8;//26; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 3) {
//                        RPGMod.LOGGER.info("rings 2 slot");
                        // rings 2
                        x = 77;
                        y = 26;//44; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 4) {
//                        RPGMod.LOGGER.info("spell slot 1 slot");
                        // spell slot 1
                        x = this.spellSlotsX;
                        y = this.spellSlotsY;

                    } else if (order == 5) {
//                        RPGMod.LOGGER.info("spell slot 2 slot");
                        // spell slot 2
                        x = this.spellSlotsX + 18;
                        y = this.spellSlotsY;

                    } else if (order == 6) {
//                        RPGMod.LOGGER.info("spell slot 3 slot");
                        // spell slot 3
                        x = this.spellSlotsX + 36;
                        y = this.spellSlotsY;

                    } else if (order == 7) {
//                        RPGMod.LOGGER.info("spell slot 4 slot");
                        // spell slot 4
                        x = this.spellSlotsX + 54;
                        y = this.spellSlotsY;

                    } else if (order == 8) {
//                        RPGMod.LOGGER.info("spell slot 5 slot");
                        // spell slot 5
                        x = this.spellSlotsX;
                        y = this.spellSlotsY + 18;

                    } else if (order == 9) {
//                        RPGMod.LOGGER.info("spell slot 6 slot");
                        // spell slot 6
                        x = this.spellSlotsX + 18;
                        y = this.spellSlotsY + 18;

                    } else if (order == 10) {
//                        RPGMod.LOGGER.info("spell slot 7 slot");
                        // spell slot 7
                        x = this.spellSlotsX + 36;
                        y = this.spellSlotsY + 18;

                    } else if (order == 11) {
//                        RPGMod.LOGGER.info("spell slot 8 slot");
                        // spell slot 8
                        x = this.spellSlotsX + 54;
                        y = this.spellSlotsY + 18;

                    } else if (order == 12) {
//                        RPGMod.LOGGER.info("gloves slot");
                        // gloves
                        x = 77;
                        y = 44;//62; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 13) {
//                        RPGMod.LOGGER.info("shoulders slot");
                        // shoulders
                        x = 8;
                        y = 8;//26; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else {
                        continue;
                    }
                    groupPos.put(group, new Point(x, y));
                    groupNums.put(group, groupNum);
                    groupNum++;
                }
            }
            //------------------------------------


//            for (SlotGroup group : groups.values().stream().sorted(Comparator.comparing(SlotGroup::getOrder)).toList()) {
//                if (!hasSlots(trinkets, group)) {
//                    RPGMod.LOGGER.info("!hasSlots(trinkets, group) -> continue");
//                    continue;
//                }
//                int order = group.getOrder();
//                int id = group.getSlotId();
//                if (id != -1) {
//                    return;
//                } else {
//                    int x;
//                    int y;
//                    if (order == 0) {
////                        RPGMod.LOGGER.info("necklaces slot");
//                        // necklaces
//                        x = 52;
//                        y = -10;//8; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                    } else if (order == 1) {
////                        RPGMod.LOGGER.info("belts slot");
//                        // belts
//                        x = 8;
//                        y = 44;//62; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                    } else if (order == 2) {
////                        RPGMod.LOGGER.info("rings 1 slot");
//                        // rings 1
//                        x = 77;
//                        y = 8;//26; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                    } else if (order == 3) {
////                        RPGMod.LOGGER.info("rings 2 slot");
//                        // rings 2
//                        x = 77;
//                        y = 26;//44; // -18 to negate an offset on the OwoScreen I can't find the cause of
//
//                    } else if (order == 4) {
////                        RPGMod.LOGGER.info("spell slot 1 slot");
//                        // spell slot 1
//                        x = this.spellSlotsX;
//                        y = this.spellSlotsY;
//
//                    } else if (order == 5) {
////                        RPGMod.LOGGER.info("spell slot 2 slot");
//                        // spell slot 2
//                        x = this.spellSlotsX + 18;
//                        y = this.spellSlotsY;
//
//                    } else if (order == 6) {
////                        RPGMod.LOGGER.info("spell slot 3 slot");
//                        // spell slot 3
//                        x = this.spellSlotsX + 36;
//                        y = this.spellSlotsY;
//
//                    } else if (order == 7) {
////                        RPGMod.LOGGER.info("spell slot 4 slot");
//                        // spell slot 4
//                        x = this.spellSlotsX + 54;
//                        y = this.spellSlotsY;
//
//                    } else if (order == 8) {
////                        RPGMod.LOGGER.info("spell slot 5 slot");
//                        // spell slot 5
//                        x = this.spellSlotsX;
//                        y = this.spellSlotsY + 18;
//
//                    } else if (order == 9) {
////                        RPGMod.LOGGER.info("spell slot 6 slot");
//                        // spell slot 6
//                        x = this.spellSlotsX + 18;
//                        y = this.spellSlotsY + 18;
//
//                    } else if (order == 10) {
////                        RPGMod.LOGGER.info("spell slot 7 slot");
//                        // spell slot 7
//                        x = this.spellSlotsX + 36;
//                        y = this.spellSlotsY + 18;
//
//                    } else if (order == 11) {
////                        RPGMod.LOGGER.info("spell slot 8 slot");
//                        // spell slot 8
//                        x = this.spellSlotsX + 54;
//                        y = this.spellSlotsY + 18;
//
//                    } else if (order == 12) {
////                        RPGMod.LOGGER.info("gloves slot");
//                        // gloves
//                        x = 77;
//                        y = 62;
//
//                    } else if (order == 13) {
//                        RPGMod.LOGGER.info("shoulders slot");
//                        // shoulders
//                        x = 8;
//                        y = 26;
//
//                    } else {
//                        return;
//                    }
//                    groupPos.put(group, new Point(x, y));
//                    groupNums.put(group, groupNum);
//                    groupNum++;
//                }
//            }


//            RPGMod.LOGGER.info("second time: trinkets.getInventory().entrySet(): " + trinkets.getInventory().entrySet());
            groupCount = Math.max(0, groupNum - 4); // TODO maybe -6
            trinketSlotStart = slots.size();
            slotWidths.clear();
            slotHeights.clear();
            slotTypes.clear();

            int activeSpellSlotAmount = (int) owner.getAttributeInstance(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT).getValue();
            AdventureTrinketSlot[] newSlots = new AdventureTrinketSlot[this.trinketSlotAmount];
//            RPGMod.LOGGER.info("newSlots's length: " + newSlots.length);
//            RPGMod.LOGGER.info("third time: trinkets.getInventory().entrySet(): " + trinkets.getInventory().entrySet());
//            RPGMod.LOGGER.info("add slots to screenHandler");

            //------------------------------------
//            for (Map.Entry<String, Map<String, TrinketInventory>> entry : trinkets.getInventory().entrySet()) {
//                String groupId = entry.getKey();
//                SlotGroup group = groups.get(groupId);
//                int groupOffset = 1;
//
//                if (group.getSlotId() != -1) {
//                    groupOffset++;
//                }
//                int width = 0;
//                Point pos = trinkets$getGroupPos(group);
//                if (pos == null) {
//                    continue;
//                }
//                for (Map.Entry<String, TrinketInventory> slot : entry.getValue().entrySet().stream().sorted((a, b) ->
//                        Integer.compare(a.getValue().getSlotType().getOrder(), b.getValue().getSlotType().getOrder())).toList()) {
//                    TrinketInventory stacks = slot.getValue();
//                    if (stacks.size() == 0) {
//                        continue;
//                    }
//                    int slotOffset = 1;
//                    int x = (int) ((groupOffset / 2) * 18 * Math.pow(-1, groupOffset));
//                    slotHeights.computeIfAbsent(group, (k) -> new ArrayList<>()).add(new Point(x, stacks.size()));
//                    slotTypes.computeIfAbsent(group, (k) -> new ArrayList<>()).add(stacks.getSlotType());
//                    for (int i = 0; i < stacks.size(); i++) {
//                        int y = (int) (pos.y() + (slotOffset / 2) * 18 * Math.pow(-1, slotOffset));
//                        this.addSlot(new SurvivalTrinketSlot(stacks, i, x + pos.x(), y, group, stacks.getSlotType(), i, groupOffset == 1 && i == 0));
//                        slotOffset++;
//                    }
//                    groupOffset++;
//                    width++;
//                }
//                slotWidths.put(group, width);
//            }
            //------------------------------------

            for (Map.Entry<String, Map<String, TrinketInventory>> entry : trinkets.getInventory().entrySet()) {
                String groupId = entry.getKey();
                SlotGroup group = groups.get(groupId);
                int groupOffset = 1;

                if (group.getSlotId() != -1) {
                    groupOffset++;
                }
                int width = 0;
                Point pos = trinkets$getGroupPos(group);
                if (pos == null) {
//                    RPGMod.LOGGER.info("pos == null -> continue");
                    continue;
                }
                for (Map.Entry<String, TrinketInventory> slot : entry.getValue().entrySet().stream().sorted((a, b) ->
                        Integer.compare(a.getValue().getSlotType().getOrder(), b.getValue().getSlotType().getOrder())).toList()) {
                    TrinketInventory stacks = slot.getValue();
                    if (stacks.size() == 0) {
//                        RPGMod.LOGGER.info("stacks.size() == 0 -> continue");
                        continue;
                    }
                    int x = groupPos.get(group).x();
                    slotHeights.computeIfAbsent(group, (k) -> new ArrayList<>()).add(new Point(x, stacks.size()));
                    slotTypes.computeIfAbsent(group, (k) -> new ArrayList<>()).add(stacks.getSlotType());

                    for (int i = 0; i < stacks.size(); i++) {
                        int order = group.getOrder();
//                        RPGMod.LOGGER.info(group.getName() + "'s order is: " + order);
//                        if (order >= this.trinketSlotAmount) {
//                            RPGMod.LOGGER.info("order >= this.trinketSlotAmount -> continue");
//                            continue;
//                        }
                        newSlots[order] = new AdventureTrinketSlot(stacks, i, groupPos.get(group).x(), groupPos.get(group).y(), group, stacks.getSlotType(), 0, owner) {
                            @Override
                            public boolean isEnabled() {
                                return !((Objects.equals(groupId, "spell_slot_1") && activeSpellSlotAmount < 1)
                                        || (Objects.equals(groupId, "spell_slot_2") && activeSpellSlotAmount < 2)
                                        || (Objects.equals(groupId, "spell_slot_3") && activeSpellSlotAmount < 3)
                                        || (Objects.equals(groupId, "spell_slot_4") && activeSpellSlotAmount < 4)
                                        || (Objects.equals(groupId, "spell_slot_5") && activeSpellSlotAmount < 5)
                                        || (Objects.equals(groupId, "spell_slot_6") && activeSpellSlotAmount < 6)
                                        || (Objects.equals(groupId, "spell_slot_7") && activeSpellSlotAmount < 7)
                                        || (Objects.equals(groupId, "spell_slot_8") && activeSpellSlotAmount < 8));
                            }
                        };
                    }

                    groupOffset++;
                    width++;
                }
                slotWidths.put(group, width);
            }

            // add slots to screenHandler

            for (int i = 0; i < this.trinketSlotAmount; i++) {
                if (newSlots[i] != null) {
//                    RPGMod.LOGGER.info("newSlots[" + i + "] != null");
                    this.addSlot(newSlots[i]);
                } else {
//                    RPGMod.LOGGER.info("newSlots[" + i + "] == null");
                }
            }


//            RPGMod.LOGGER.info("added slots to screenHandler");
//            RPGMod.LOGGER.info("slots.size(): " + slots.size());
            trinketSlotEnd = slots.size();
        });
    }
/**/
    private boolean hasSlots(TrinketComponent comp, SlotGroup group) {
        for (TrinketInventory inv : comp.getInventory().get(group.getName()).values()) {
            if (inv.size() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int trinkets$getGroupNum(SlotGroup group) {
        return groupNums.getOrDefault(group, 0);
    }

    @Nullable
    @Override
    public Point trinkets$getGroupPos(SlotGroup group) {
        return groupPos.get(group);
    }

    @NotNull
    @Override
    public List<Point> trinkets$getSlotHeights(SlotGroup group) {
        return slotHeights.getOrDefault(group, ImmutableList.of());
    }

    @Nullable
    @Override
    public Point trinkets$getSlotHeight(SlotGroup group, int i) {
        List<Point> points = this.trinkets$getSlotHeights(group);
        return i < points.size() ? points.get(i) : null;
    }

    @NotNull
    @Override
    public List<SlotType> trinkets$getSlotTypes(SlotGroup group) {
        return slotTypes.getOrDefault(group, ImmutableList.of());
    }

    @Override
    public int trinkets$getSlotWidth(SlotGroup group) {
        return slotWidths.getOrDefault(group, 0);
    }

    @Override
    public int trinkets$getGroupCount() {
        return groupCount;
    }

    @Override
    public int trinkets$getTrinketSlotStart() {
        return trinketSlotStart;
    }

    @Override
    public int trinkets$getTrinketSlotEnd() {
        return trinketSlotEnd;
    }

    public int[] getSpellSlotIds() {
        return spellSlotIds;
    }
}
