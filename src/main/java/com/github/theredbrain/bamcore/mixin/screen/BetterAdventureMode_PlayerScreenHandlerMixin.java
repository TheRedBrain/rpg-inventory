package com.github.theredbrain.bamcore.mixin.screen;

import com.google.common.collect.ImmutableList;
import dev.emi.trinkets.Point;
import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.TrinketsClient;
import dev.emi.trinkets.api.*;
import dev.emi.trinkets.mixin.accessor.ScreenHandlerAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = PlayerScreenHandler.class, priority = 1050) // overrides Trinkets
public abstract class BetterAdventureMode_PlayerScreenHandlerMixin extends ScreenHandler implements TrinketPlayerScreenHandler {
    @Shadow @Final
    private PlayerEntity owner;

    @Unique
    private final Map<SlotGroup, Integer> groupNums = new HashMap<>();
    @Unique
    private final Map<SlotGroup, Point> groupPos = new HashMap<>();
    @Unique
    private final Map<SlotGroup, List<Point>> slotHeights = new HashMap<>();
    @Unique
    private final Map<SlotGroup, List<SlotType>> slotTypes = new HashMap<>();
    @Unique
    private final Map<SlotGroup, Integer> slotWidths = new HashMap<>();
    @Unique
    private int trinketSlotStart = 0;
    @Unique
    private int trinketSlotEnd = 0;
    @Unique
    private int groupCount = 0;
    @Unique
    private PlayerInventory inventory;
//    @Shadow @Final
//    private PlayerEntity owner;
//
//    @Unique
//    private final Map<SlotGroup, Integer> groupNums = new HashMap<>();
//    @Unique
//    private final Map<SlotGroup, Point> groupPos = new HashMap<>();
//    @Unique
//    private final Map<SlotGroup, List<Point>> slotHeights = new HashMap<>();
//    @Unique
//    private final Map<SlotGroup, List<SlotType>> slotTypes = new HashMap<>();
//    @Unique
//    private final Map<SlotGroup, Integer> slotWidths = new HashMap<>();
//    @Unique
//    private int trinketSlotStart = 0;
//    @Unique
//    private int trinketSlotEnd = 0;
//    @Unique
//    private int groupCount = 0;
//    @Unique
//    private PlayerInventory inventory;
    private int spellSlotsX = 98;
    private int spellSlotsY = 62;
////    private int activeSpellSlotAmount = 0;
////    private final int[] spellSlotIds = {48, 49, 50, 51, 52, 53, 54, 55};
//    private final int trinketSlotAmount = 14;
//    @Shadow
//    @Final
//    private static EquipmentSlot[] EQUIPMENT_SLOT_ORDER;
//    @Shadow
//    @Final
//    static Identifier[] EMPTY_ARMOR_SLOT_TEXTURES;
//    @Shadow
//    @Final
//    public static Identifier BLOCK_ATLAS_TEXTURE;
//    @Shadow
//    @Final
//    public static Identifier EMPTY_OFFHAND_ARMOR_SLOT;
//    @Shadow
//    static void onEquipStack(PlayerEntity player, EquipmentSlot slot, ItemStack newStack, ItemStack currentStack) {
//        throw new AssertionError();
//    }

    public BetterAdventureMode_PlayerScreenHandlerMixin() {
        super(null, 0);
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void PlayerScreenHandler(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {

//        // TODO config to allow vanilla screen
//        // this replaces the 4 equipment slots and the offhand slot to allow their behaviour to be controlled by status effects and item tags
//        int i;
//        for (i = 0; i < 4; ++i) {
//            final EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[i];
//            this.slots.set(5 + i, new Slot(inventory, 39 - i, 8, 8 + i * 18) {
//
//                @Override
//                public void setStack(ItemStack stack) {
//                    onEquipStack(owner, equipmentSlot, stack, this.getStack());
//                    super.setStack(stack);
//                }
//
//                @Override
//                public int getMaxItemCount() {
//                    return 1;
//                }
//
//                @Override
//                public boolean canInsert(ItemStack stack) {
//                    return equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack);
////                    return equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack) && (owner.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
//                }
//
//                @Override
//                public boolean canTakeItems(PlayerEntity playerEntity) {
//                    ItemStack itemStack = this.getStack();
//                    if (!itemStack.isEmpty() && !playerEntity.isCreative() && EnchantmentHelper.hasBindingCurse(itemStack)) {
//                        return false;
//                    }
//                    return super.canTakeItems(playerEntity);
////                    return super.canTakeItems(playerEntity) && (owner.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
//                }
//
//                @Override
//                public Pair<Identifier, Identifier> getBackgroundSprite() {
//                    return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, EMPTY_ARMOR_SLOT_TEXTURES[equipmentSlot.getEntitySlotId()]);
////                    return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_OFFHAND_ARMOR_SLOT);
//                }
//            });
//        }
//        this.slots.set(45, new Slot(inventory, 40, 77, 62){
//
//            @Override
//            public void setStack(ItemStack stack) {
//                onEquipStack(owner, EquipmentSlot.OFFHAND, stack, this.getStack());
//                super.setStack(stack);
//            }
//
////            @Override
////            public int getMaxItemCount() {
////                return 1;
////            }
////
////            @Override
////            public boolean canInsert(ItemStack stack) {
////                return stack.isIn(Tags.OFF_HAND_ITEMS) && (owner.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
////            }
////
////            @Override
////            public boolean canTakeItems(PlayerEntity playerEntity) {
////                ItemStack itemStack = this.getStack();
////                if (!itemStack.isEmpty() && !playerEntity.isCreative() && EnchantmentHelper.hasBindingCurse(itemStack)) {
////                    return false;
////                }
////                return super.canTakeItems(playerEntity) && (owner.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
////            }
//
//            @Override
//            public Pair<Identifier, Identifier> getBackgroundSprite() {
//                return Pair.of(BLOCK_ATLAS_TEXTURE, EMPTY_OFFHAND_ARMOR_SLOT);
//            }
//        });
        this.inventory = inventory;
        trinkets$updateTrinketSlots(true);
    }

    @Override
    public void trinkets$updateTrinketSlots(boolean slotsChanged) {
//        boolean bl = owner.hasStatusEffect(BetterAdventureModeCoreStatusEffects.IS_CREATIVE);
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

            // TODO config to allow vanilla screen
//            if (bl) {
//                for (SlotGroup group : groups.values().stream().sorted(Comparator.comparing(SlotGroup::getOrder)).toList()) {
//                    if (!hasSlots(trinkets, group)) {
//                        continue;
//                    }
//                    int id = group.getSlotId();
//                    if (id != -1) {
//                        if (this.slots.size() > id) {
//                            Slot slot = this.slots.get(id);
//                            if (!(slot instanceof SurvivalTrinketSlot)) {
//                                groupPos.put(group, new Point(slot.x, slot.y));
//                                groupNums.put(group, -id);
//                            }
//                        }
//                    } else {
//                        int x = 77;
//                        int y;
//                        if (groupNum >= 4) {
//                            x = 4 - (groupNum / 4) * 18;
//                            y = 8 + (groupNum % 4) * 18;
//                        } else {
//                            y = 62 - groupNum * 18;
//                        }
//                        groupPos.put(group, new Point(x, y));
//                        groupNums.put(group, groupNum);
//                        groupNum++;
//                    }
//                }
//            } else {
            for (SlotGroup group : groups.values().stream().sorted(Comparator.comparing(SlotGroup::getOrder)).toList()) {
                if (!hasSlots(trinkets, group)) {
                    continue;
                }
                int order = group.getOrder();
                int id = group.getSlotId();
                if (id != -1) {
//                        if (this.slots.size() > id) {
//                            Slot slot = this.slots.get(id);
//                            if (!(slot instanceof SurvivalTrinketSlot)) {
//                                groupPos.put(group, new Point(slot.x, slot.y));
//                                groupNums.put(group, -id);
//                            }
//                        }
                    continue;
                } else {
                    int x;
                    int y;
                    // begins at 1 because order=0 is the default
                    // this way most unwanted cases are avoided
                    if (order == 1) {
                        // alternative main hand
                        x = 59;
                        y = 80;//98; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 2) {
                        // alternative off hand
                        x = 77;
                        y = 80;//98; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 3) {
                        // belts
                        x = 8;
                        y = 44;//62; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 4) {
                        // gloves
                        x = 77;
                        y = 44;//62; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 5) {
                        // main hand
                        x = 8;
                        y = 80;//98; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 6) {
                        // necklaces
                        x = 52;
                        y = -10;//8; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 7) {
                        // rings 1
                        x = 77;
                        y = 8;//26; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 8) {
                        // rings 2
                        x = 77;
                        y = 26;//44; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 9) {
                        // shoulders
                        x = 8;
                        y = 8;//26; // -18 to negate an offset on the OwoScreen I can't find the cause of

                    } else if (order == 10) {
                        // spell slot 1
                        x = this.spellSlotsX;
                        y = this.spellSlotsY;

                    } else if (order == 11) {
                        // spell slot 2
                        x = this.spellSlotsX + 18;
                        y = this.spellSlotsY;

                    } else if (order == 12) {
                        // spell slot 3
                        x = this.spellSlotsX + 36;
                        y = this.spellSlotsY;

                    } else if (order == 13) {
                        // spell slot 4
                        x = this.spellSlotsX + 54;
                        y = this.spellSlotsY;

                    } else if (order == 14) {
                        // spell slot 5
                        x = this.spellSlotsX;
                        y = this.spellSlotsY + 18;

                    } else if (order == 15) {
                        // spell slot 6
                        x = this.spellSlotsX + 18;
                        y = this.spellSlotsY + 18;

                    } else if (order == 16) {
                        // spell slot 7
                        x = this.spellSlotsX + 36;
                        y = this.spellSlotsY + 18;

                    } else if (order == 17) {
                        // spell slot 8
                        x = this.spellSlotsX + 54;
                        y = this.spellSlotsY + 18;

                    } else {
//                            if (groupNum >= 4) {
//                                x = 4 - (groupNum / 4) * 18;
//                                y = 8 + (groupNum % 4) * 18;
//                            } else {
//                                x = 77;
//                                y = 62 - groupNum * 18;
//                            }
                        continue;
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
                    int x = (int) ((groupOffset / 2) * 18 * Math.pow(-1, groupOffset));
                    slotHeights.computeIfAbsent(group, (k) -> new ArrayList<>()).add(new Point(x, stacks.size()));
                    slotTypes.computeIfAbsent(group, (k) -> new ArrayList<>()).add(stacks.getSlotType());
                    for (int i = 0; i < stacks.size(); i++) {
                        int y = (int) (pos.y() + (slotOffset / 2) * 18 * Math.pow(-1, slotOffset));
                        this.addSlot(new SurvivalTrinketSlot(stacks, i, x + pos.x(), y, group, stacks.getSlotType(), i, groupOffset == 1 && i == 0));
                        slotOffset++;
                    }
                    groupOffset++;
                    width++;
                }
                slotWidths.put(group, width);
            }

            trinketSlotEnd = slots.size();
        });
    }

    @Unique
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

    @Inject(at = @At("HEAD"), method = "onClosed")
    private void onClosed(PlayerEntity player, CallbackInfo info) {
        if (player.getWorld().isClient) {
            TrinketsClient.activeGroup = null;
            TrinketsClient.activeType = null;
            TrinketsClient.quickMoveGroup = null;
        }
    }

    @Inject(at = @At("HEAD"), method = "quickMove", cancellable = true)
    private void quickMove(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> info) {
        Slot slot = slots.get(index);

        if (slot.hasStack()) {
            ItemStack stack = slot.getStack();
            if (index >= trinketSlotStart && index < trinketSlotEnd) {
                if (!this.insertItem(stack, 9, 45, false)) {
                    info.setReturnValue(ItemStack.EMPTY);
                } else {
                    info.setReturnValue(stack);
                }
            } else if (index >= 9 && index < 45) {
                TrinketsApi.getTrinketComponent(player).ifPresent(trinkets -> {
                            for (int i = trinketSlotStart; i < trinketSlotEnd; i++) {
                                Slot s = slots.get(i);
                                if (!(s instanceof SurvivalTrinketSlot) || !s.canInsert(stack)) {
                                    continue;
                                }

                                SurvivalTrinketSlot ts = (SurvivalTrinketSlot) s;
                                SlotType type = ts.getType();
                                SlotReference ref = new SlotReference((TrinketInventory) ts.inventory, ts.getIndex());

                                boolean res = TrinketsApi.evaluatePredicateSet(type.getQuickMovePredicates(), stack, ref, player);

                                if (res) {
                                    if (this.insertItem(stack, i, i + 1, false)) {
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
                        }
                );
            }
        }
    }
//
//    @Override
//    public void trinkets$updateTrinketSlots(boolean slotsChanged) {
////        if (this.owner.getServer() == null) {
////            return;
////        }
////        BetterAdventureModeCore.LOGGER.info("screenHandler activeSpellSlotAmount: " + activeSpellSlotAmount);
////        boolean useCustomInventoryScreen;
////        if (this.owner.getServer() != null && ((ServerPlayerEntity)this.owner).interactionManager != null) {
////            useCustomInventoryScreen = !owner.isCreative() && this.owner.getServer().getGameRules().getBoolean(GameRulesRegistry.USE_CUSTOM_INVENTORY_SCREEN); // TODO replace with config value
////        } else {
////            useCustomInventoryScreen = false;
////        }
//        TrinketsApi.getTrinketComponent(owner).ifPresent(trinkets -> {
//            if (slotsChanged) trinkets.update();
//            Map<String, SlotGroup> groups = trinkets.getGroups();
//            groupPos.clear();
//            while (trinketSlotStart < trinketSlotEnd) {
//                slots.remove(trinketSlotStart);
//                ((ScreenHandlerAccessor) (this)).getTrackedStacks().remove(trinketSlotStart);
//                ((ScreenHandlerAccessor) (this)).getPreviousTrackedStacks().remove(trinketSlotStart);
//                trinketSlotEnd--;
//            }
//
//            int groupNum = 1; // Start at 1 because offhand exists
//
//            // ----------------------------------------
////            if (true) {
////                for (SlotGroup group : groups.values().stream().sorted(Comparator.comparing(SlotGroup::getOrder)).toList()) {
////                    if (!hasSlots(trinkets, group)) {
////                        continue;
////                    }
////                    int order = group.getOrder();
////                    int id = group.getSlotId();
////                    if (id != -1) {
//////                        if (this.slots.size() > id) {
//////                            Slot slot = this.slots.get(id);
//////                            if (!(slot instanceof SurvivalTrinketSlot)) {
//////                                groupPos.put(group, new Point(slot.x, slot.y));
//////                                groupNums.put(group, -id);
//////                            }
//////                        }
////                        continue;
////                    } else {
////                        int x;
////                        int y;
////                        // begins at 1 because order=0 is the default
////                        // this way most unwanted cases are avoided
////                        if (order == 1) {
////                            // alternative main hand
////                            x = 59;
////                            y = 80;//98; // -18 to negate an offset on the OwoScreen I can't find the cause of
////
////                        } else if (order == 2) {
////                            // alternative off hand
////                            x = 77;
////                            y = 80;//98; // -18 to negate an offset on the OwoScreen I can't find the cause of
////
////                        } else if (order == 3) {
////                            // belts
////                            x = 8;
////                            y = 44;//62; // -18 to negate an offset on the OwoScreen I can't find the cause of
////
////                        } else if (order == 4) {
////                            // gloves
////                            x = 77;
////                            y = 44;//62; // -18 to negate an offset on the OwoScreen I can't find the cause of
////
////                        } else if (order == 5) {
////                            // main hand
////                            x = 8;
////                            y = 80;//98; // -18 to negate an offset on the OwoScreen I can't find the cause of
////
////                        } else if (order == 6) {
////                            // necklaces
////                            x = 52;
////                            y = -10;//8; // -18 to negate an offset on the OwoScreen I can't find the cause of
////
////                        } else if (order == 7) {
////                            // rings 1
////                            x = 77;
////                            y = 8;//26; // -18 to negate an offset on the OwoScreen I can't find the cause of
////
////                        } else if (order == 8) {
////                            // rings 2
////                            x = 77;
////                            y = 26;//44; // -18 to negate an offset on the OwoScreen I can't find the cause of
////
////                        } else if (order == 9) {
////                            // shoulders
////                            x = 8;
////                            y = 8;//26; // -18 to negate an offset on the OwoScreen I can't find the cause of
////
////                        } else if (order == 10) {
////                            // spell slot 1
////                            x = this.spellSlotsX;
////                            y = this.spellSlotsY;
////
////                        } else if (order == 11) {
////                            // spell slot 2
////                            x = this.spellSlotsX + 18;
////                            y = this.spellSlotsY;
////
////                        } else if (order == 12) {
////                            // spell slot 3
////                            x = this.spellSlotsX + 36;
////                            y = this.spellSlotsY;
////
////                        } else if (order == 13) {
////                            // spell slot 4
////                            x = this.spellSlotsX + 54;
////                            y = this.spellSlotsY;
////
////                        } else if (order == 14) {
////                            // spell slot 5
////                            x = this.spellSlotsX;
////                            y = this.spellSlotsY + 18;
////
////                        } else if (order == 15) {
////                            // spell slot 6
////                            x = this.spellSlotsX + 18;
////                            y = this.spellSlotsY + 18;
////
////                        } else if (order == 16) {
////                            // spell slot 7
////                            x = this.spellSlotsX + 36;
////                            y = this.spellSlotsY + 18;
////
////                        } else if (order == 17) {
////                            // spell slot 8
////                            x = this.spellSlotsX + 54;
////                            y = this.spellSlotsY + 18;
////
////                        } else {
//////                            if (groupNum >= 4) {
//////                                x = 4 - (groupNum / 4) * 18;
//////                                y = 8 + (groupNum % 4) * 18;
//////                            } else {
//////                                x = 77;
//////                                y = 62 - groupNum * 18;
//////                            }
////                            continue;
////                        }
////                        groupPos.put(group, new Point(x, y));
////                        groupNums.put(group, groupNum);
////                        groupNum++;
////                    }
////                }
////            } else {
//                for (SlotGroup group : groups.values().stream().sorted(Comparator.comparing(SlotGroup::getOrder)).toList()) {
//                    if (!hasSlots(trinkets, group)) {
//                        continue;
//                    }
//                    int id = group.getSlotId();
//                    if (id != -1) {
//                        if (this.slots.size() > id) {
//                            Slot slot = this.slots.get(id);
//                            if (!(slot instanceof SurvivalTrinketSlot)) {
//                                groupPos.put(group, new Point(slot.x, slot.y));
//                                groupNums.put(group, -id);
//                            }
//                        }
//                    } else {
//                        int x = 77;
//                        int y;
//                        if (groupNum >= 4) {
//                            x = 4 - (groupNum / 4) * 18;
//                            y = 8 + (groupNum % 4) * 18;
//                        } else {
//                            y = 62 - groupNum * 18;
//                        }
//                        groupPos.put(group, new Point(x, y));
//                        groupNums.put(group, groupNum);
//                        groupNum++;
//                    }
//                }
////            }
//
//            // ----------------------------------------
//
//            groupCount = Math.max(0, groupNum - 4);
//            trinketSlotStart = slots.size();
//            slotWidths.clear();
//            slotHeights.clear();
//            slotTypes.clear();
//
////            this.activeSpellSlotAmount = (int) owner.getAttributeInstance(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT).getValue();
//
//            // ----------------------------------------
//
////            if (false/*this.owner.getServer().getGameRules().getBoolean(GameRulesRegistry.USE_CUSTOM_INVENTORY_SCREEN)*/) {
////                int activeSpellSlotAmount = (int) owner.getAttributeInstance(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT).getValue();
////                SurvivalTrinketSlot[] newSlots = new SurvivalTrinketSlot[this.trinketSlotAmount];
////
////                for (Map.Entry<String, Map<String, TrinketInventory>> entry : trinkets.getInventory().entrySet()) {
////                    String groupId = entry.getKey();
////                    SlotGroup group = groups.get(groupId);
//////                int groupOffset = 1;
//////
//////                if (group.getSlotId() != -1) {
//////                    groupOffset++;
//////                }
////                    int width = 0;
////                    Point pos = trinkets$getGroupPos(group);
////                    if (pos == null) {
////                        continue;
////                    }
////                    for (Map.Entry<String, TrinketInventory> slot : entry.getValue().entrySet().stream().sorted((a, b) ->
////                            Integer.compare(a.getValue().getSlotType().getOrder(), b.getValue().getSlotType().getOrder())).toList()) {
////                        TrinketInventory stacks = slot.getValue();
////                        if (stacks.size() == 0) {
////                            continue;
////                        }
////                        int x = groupPos.get(group).x();
////                        slotHeights.computeIfAbsent(group, (k) -> new ArrayList<>()).add(new Point(x, stacks.size()));
////                        slotTypes.computeIfAbsent(group, (k) -> new ArrayList<>()).add(stacks.getSlotType());
////
////                        for (int i = 0; i < stacks.size(); i++) {
////                            int index = group.getOrder() - 1; // -1 to account for all groups with order=0 being ignored
////                            newSlots[index] = new SurvivalTrinketSlot(stacks, i, groupPos.get(group).x(), groupPos.get(group).y(), group, stacks.getSlotType(), 0, false) {
////                                @Override
////                                public boolean isEnabled() {
////                                    return !((Objects.equals(groupId, "spell_slot_1") && activeSpellSlotAmount < 1)
////                                            || (Objects.equals(groupId, "spell_slot_2") && activeSpellSlotAmount < 2)
////                                            || (Objects.equals(groupId, "spell_slot_3") && activeSpellSlotAmount < 3)
////                                            || (Objects.equals(groupId, "spell_slot_4") && activeSpellSlotAmount < 4)
////                                            || (Objects.equals(groupId, "spell_slot_5") && activeSpellSlotAmount < 5)
////                                            || (Objects.equals(groupId, "spell_slot_6") && activeSpellSlotAmount < 6)
////                                            || (Objects.equals(groupId, "spell_slot_7") && activeSpellSlotAmount < 7)
////                                            || (Objects.equals(groupId, "spell_slot_8") && activeSpellSlotAmount < 8));
////                                }
////                            };
////                        }
////
//////                    groupOffset++;
////                        width++;
////                    }
////                    slotWidths.put(group, width);
////                }
////
////                // add slots to screenHandler
////                for (int i = 0; i < this.trinketSlotAmount; i++) {
////                    if (newSlots[i] != null) {
////                        this.addSlot(newSlots[i]);
////                    }
////                }
////            } else {
//                for (Map.Entry<String, Map<String, TrinketInventory>> entry : trinkets.getInventory().entrySet()) {
//                    String groupId = entry.getKey();
//                    SlotGroup group = groups.get(groupId);
//                    int order = group.getOrder();
//                    int groupOffset = 1;
//
//                    if (group.getSlotId() != -1) {
//                        groupOffset++;
//                    }
//                    int width = 0;
//                    Point pos = trinkets$getGroupPos(group);
//                    if (pos == null) {
//                        continue;
//                    }
//                    for (Map.Entry<String, TrinketInventory> slot : entry.getValue().entrySet().stream().sorted((a, b) ->
//                            Integer.compare(a.getValue().getSlotType().getOrder(), b.getValue().getSlotType().getOrder())).toList()) {
//                        TrinketInventory stacks = slot.getValue();
//                        if (stacks.size() == 0) {
//                            continue;
//                        }
//                        int slotOffset = 1;
//                        int x = (int) ((groupOffset / 2) * 18 * Math.pow(-1, groupOffset));
//                        slotHeights.computeIfAbsent(group, (k) -> new ArrayList<>()).add(new Point(x, stacks.size()));
//                        slotTypes.computeIfAbsent(group, (k) -> new ArrayList<>()).add(stacks.getSlotType());
//                        for (int i = 0; i < stacks.size(); i++) {
//                            int y = (int) (pos.y() + (slotOffset / 2) * 18 * Math.pow(-1, slotOffset));
//                            this.addSlot(new SurvivalTrinketSlot(stacks, i, x + pos.x(), y, group, stacks.getSlotType(), i, groupOffset == 1 && i == 0)/* {
//                                @Override
//                                public boolean isEnabled() {
//                                    return super.isEnabled() && !(*//*(Objects.equals(groupId, "spell_slot_1") && activeSpellSlotAmount < 1)
//                                            || (Objects.equals(groupId, "spell_slot_2") && activeSpellSlotAmount < 2)
//                                            || (Objects.equals(groupId, "spell_slot_3") && activeSpellSlotAmount < 3)
//                                            || (Objects.equals(groupId, "spell_slot_4") && activeSpellSlotAmount < 4)
//                                            || (Objects.equals(groupId, "spell_slot_5") && activeSpellSlotAmount < 5)
//                                            || (Objects.equals(groupId, "spell_slot_6") && activeSpellSlotAmount < 6)
//                                            || (Objects.equals(groupId, "spell_slot_7") && activeSpellSlotAmount < 7)
//                                            || (Objects.equals(groupId, "spell_slot_8") && activeSpellSlotAmount < 8)
//                                            || *//*(order == 0));
//                                }
//                            }*/);
//                            slotOffset++;
//                        }
//                        groupOffset++;
//                        width++;
//                    }
//                    slotWidths.put(group, width);
//                }
////            }
//
//            trinketSlotEnd = slots.size();
//        });
//    }
//
//    @Unique
//    private boolean hasSlots(TrinketComponent comp, SlotGroup group) {
//        for (TrinketInventory inv : comp.getInventory().get(group.getName()).values()) {
//            if (inv.size() > 0) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public int trinkets$getGroupNum(SlotGroup group) {
//        return groupNums.getOrDefault(group, 0);
//    }
//
//    @Nullable
//    @Override
//    public Point trinkets$getGroupPos(SlotGroup group) {
//        return groupPos.get(group);
//    }
//
//    @NotNull
//    @Override
//    public List<Point> trinkets$getSlotHeights(SlotGroup group) {
//        return slotHeights.getOrDefault(group, ImmutableList.of());
//    }
//
//    @Nullable
//    @Override
//    public Point trinkets$getSlotHeight(SlotGroup group, int i) {
//        List<Point> points = this.trinkets$getSlotHeights(group);
//        return i < points.size() ? points.get(i) : null;
//    }
//
//    @NotNull
//    @Override
//    public List<SlotType> trinkets$getSlotTypes(SlotGroup group) {
//        return slotTypes.getOrDefault(group, ImmutableList.of());
//    }
//
//    @Override
//    public int trinkets$getSlotWidth(SlotGroup group) {
//        return slotWidths.getOrDefault(group, 0);
//    }
//
//    @Override
//    public int trinkets$getGroupCount() {
//        return 1;
//    }
//
//    @Override
//    public int trinkets$getTrinketSlotStart() {
//        return trinketSlotStart;
//    }
//
//    @Override
//    public int trinkets$getTrinketSlotEnd() {
//        return trinketSlotEnd;
//    }
//
//    @Inject(at = @At("HEAD"), method = "onClosed")
//    private void onClosed(PlayerEntity player, CallbackInfo info) {
//        if (player.getWorld().isClient) {
//            TrinketsClient.activeGroup = null;
//            TrinketsClient.activeType = null;
//            TrinketsClient.quickMoveGroup = null;
//        }
//    }
//
//    @Inject(at = @At("HEAD"), method = "quickMove", cancellable = true)
//    private void quickMove(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> info) {
//        Slot slot = slots.get(index);
//
//        if (slot.hasStack()) {
//            ItemStack stack = slot.getStack();
//            if (index >= trinketSlotStart && index < trinketSlotEnd) {
//                if (!this.insertItem(stack, 9, 45, false)) {
//                    info.setReturnValue(ItemStack.EMPTY);
//                } else {
//                    info.setReturnValue(stack);
//                }
//            } else if (index >= 9 && index < 45) {
//                TrinketsApi.getTrinketComponent(player).ifPresent(trinkets -> {
//                    for (int i = trinketSlotStart; i < trinketSlotEnd; i++) {
//                        Slot s = slots.get(i);
//                        if (!(s instanceof SurvivalTrinketSlot) || !s.canInsert(stack)) {
//                            continue;
//                        }
//
//                        SurvivalTrinketSlot ts = (SurvivalTrinketSlot) s;
//                        SlotType type = ts.getType();
//                        SlotReference ref = new SlotReference((TrinketInventory) ts.inventory, ts.getIndex());
//
//                        boolean res = TrinketsApi.evaluatePredicateSet(type.getQuickMovePredicates(), stack, ref, player);
//
//                        if (res) {
//                            if (this.insertItem(stack, i, i + 1, false)) {
//                                if (player.getWorld().isClient) {
//                                    TrinketsClient.quickMoveTimer = 20;
//                                    TrinketsClient.quickMoveGroup = TrinketsApi.getPlayerSlots(this.owner).get(type.getGroup());
//                                    if (ref.index() > 0) {
//                                        TrinketsClient.quickMoveType = type;
//                                    } else {
//                                        TrinketsClient.quickMoveType = null;
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                });
//            }
//        }
//    }
}
