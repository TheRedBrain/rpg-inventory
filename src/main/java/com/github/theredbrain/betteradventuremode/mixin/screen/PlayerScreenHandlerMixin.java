package com.github.theredbrain.betteradventuremode.mixin.screen;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.registry.EntityAttributesRegistry;
import com.github.theredbrain.betteradventuremode.screen.DuckSlotMixin;
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
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(value = PlayerScreenHandler.class, priority = 1050) // overrides Trinkets
public abstract class PlayerScreenHandlerMixin extends ScreenHandler implements TrinketPlayerScreenHandler {
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
    @Unique
    private final int spellSlotsX = 98;
    @Unique
    private final int spellSlotsY = 93;

    public PlayerScreenHandlerMixin() {
        super(null, 0);
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void PlayerScreenHandler(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        this.inventory = inventory;
        trinkets$updateTrinketSlots(true);

        if (BetterAdventureMode.serverConfig.use_adventure_inventory_screen) {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.slots.get(j + (i + 1) * 9).y = 146 + i * 18;
                }
            }
            for (int i = 0; i < 9; ++i) {
                this.slots.get(i + 36).y = 204;
            }
        }

        // disable vanilla crafting slots
        ((DuckSlotMixin)this.slots.get(0)).betteradventuremode$setDisabledOverride(true);
        ((DuckSlotMixin)this.slots.get(1)).betteradventuremode$setDisabledOverride(true);
        ((DuckSlotMixin)this.slots.get(2)).betteradventuremode$setDisabledOverride(true);
        ((DuckSlotMixin)this.slots.get(3)).betteradventuremode$setDisabledOverride(true);
        ((DuckSlotMixin)this.slots.get(4)).betteradventuremode$setDisabledOverride(true);

        // disable vanilla armor slots
        ((DuckSlotMixin)this.slots.get(5)).betteradventuremode$setDisabledOverride(true);
        ((DuckSlotMixin)this.slots.get(6)).betteradventuremode$setDisabledOverride(true);
        ((DuckSlotMixin)this.slots.get(7)).betteradventuremode$setDisabledOverride(true);
        ((DuckSlotMixin)this.slots.get(8)).betteradventuremode$setDisabledOverride(true);

        // disable vanilla offhand slot
        ((DuckSlotMixin)this.slots.get(45)).betteradventuremode$setDisabledOverride(true);
    }

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

            if (BetterAdventureMode.serverConfig.use_adventure_inventory_screen) {
                HashMap<Integer, Boolean> presentGroups = new HashMap<>(Map.of());
                for (SlotGroup group : groups.values().stream().sorted(Comparator.comparing(SlotGroup::getOrder)).toList()) {
                    if (!betteradventuremode$hasSlots(trinkets, group)) {
                        continue;
                    }
                    int order = group.getOrder();
                    int id = group.getSlotId();
                    if (id != -1) {
                        BetterAdventureMode.warn("Trinket slot groups with id != -1 are ignored. This applies to group " + group.getName());
                    } else {
                        int x;
                        int y;
                        // begins at 1 because order=0 is the default
                        // this way most unwanted cases are avoided
                        if (order == 1) {
                            // leggings
                            x = 8;
                            y = 93;

                        } else if (order == 2) {
                            // belts
                            x = 8;
                            y = 75;

                        } else if (order == 3) {
                            // chest_plates
                            x = 8;
                            y = 57;

                        } else if (order == 4) {
                            // shoulders
                            x = 8;
                            y = 39;

                        } else if (order == 5) {
                            // helmets
                            x = 33;
                            y = 21;

                        } else if (order == 6) {
                            // necklaces
                            x = 52;
                            y = 21;

                        } else if (order == 7) {
                            // rings 1
                            x = 77;
                            y = 39;

                        } else if (order == 8) {
                            // rings 2
                            x = 77;
                            y = 57;

                        } else if (order == 9) {
                            // gloves
                            x = 77;
                            y = 75;

                        } else if (order == 10) {
                            // boots
                            x = 77;
                            y = 93;

                        } else if (order == 11) {
                            // main_hand
                            x = 8;
                            y = 111;

                        } else if (order == 12) {
                            // off_hand
                            x = 26;
                            y = 111;

                        } else if (order == 13) {
                            // alternative main hand
                            x = 59;
                            y = 111;

                        } else if (order == 14) {
                            // alternative off hand
                            x = 77;
                            y = 111;

                        } else if (order == 15) {
                            // spell slot 1
                            x = this.spellSlotsX;
                            y = this.spellSlotsY;

                        } else if (order == 16) {
                            // spell slot 2
                            x = this.spellSlotsX + 18;
                            y = this.spellSlotsY;

                        } else if (order == 17) {
                            // spell slot 3
                            x = this.spellSlotsX + 36;
                            y = this.spellSlotsY;

                        } else if (order == 18) {
                            // spell slot 4
                            x = this.spellSlotsX + 54;
                            y = this.spellSlotsY;

                        } else if (order == 19) {
                            // spell slot 5
                            x = this.spellSlotsX;
                            y = this.spellSlotsY + 18;

                        } else if (order == 20) {
                            // spell slot 6
                            x = this.spellSlotsX + 18;
                            y = this.spellSlotsY + 18;

                        } else if (order == 21) {
                            // spell slot 7
                            x = this.spellSlotsX + 36;
                            y = this.spellSlotsY + 18;

                        } else if (order == 22) {
                            // spell slot 8
                            x = this.spellSlotsX + 54;
                            y = this.spellSlotsY + 18;

                        } else if (order == 23) {
                            // these include empty hand slots which are necessary but should not be interacted with by the player
                            if (!Objects.equals(group.getName(), "empty_main_hand") && !Objects.equals(group.getName(), "empty_off_hand") && BetterAdventureModeClient.clientConfig.show_debug_log) {
                                BetterAdventureMode.warn("Trinket Slots with order == 23 can not be interacted with by the player. This applies to group " + group.getName());
                            }
                            continue;
                        } else {
                            if (BetterAdventureModeClient.clientConfig.show_debug_log) {
                                BetterAdventureMode.warn("Trinket slot groups with order <= 0 or order > 23 are ignored. This applies to group " + group.getName());
                            }
                            continue;
                        }
                        groupPos.put(group, new Point(x, y));
                        groupNums.put(group, groupNum);
                        groupNum++;

                        if (presentGroups.getOrDefault(order, false) && BetterAdventureModeClient.clientConfig.show_debug_log) {
                            BetterAdventureMode.warn("Multiple slot groups with order " + order + " are defined. This may lead to unexpected behaviour. This applies to group " + group.getName());
                        } else {
                            presentGroups.put(order, true);
                        }
                    }
                }
            } else {
                for (SlotGroup group : groups.values().stream().sorted(Comparator.comparing(SlotGroup::getOrder)).toList()) {
                    if (!betteradventuremode$hasSlots(trinkets, group)) {
                        continue;
                    }
                    int id = group.getSlotId();
                    if (id != -1) {
                        if (this.slots.size() > id) {
                            Slot slot = this.slots.get(id);
                            if (!(slot instanceof SurvivalTrinketSlot)) {
                                groupPos.put(group, new Point(slot.x, slot.y));
                                groupNums.put(group, -id);
                            }
                        }
                    } else {
                        int x = 77;
                        int y;
                        if (groupNum >= 4) {
                            x = 4 - (groupNum / 4) * 18;
                            y = 8 + (groupNum % 4) * 18;
                        } else {
                            y = 62 - groupNum * 18;
                        }
                        groupPos.put(group, new Point(x, y));
                        groupNums.put(group, groupNum);
                        groupNum++;
                    }
                }
            }
            groupCount = Math.max(0, groupNum - 4);
            trinketSlotStart = slots.size();
            slotWidths.clear();
            slotHeights.clear();
            slotTypes.clear();

            if (BetterAdventureMode.serverConfig.use_adventure_inventory_screen) {
                int trinketSlotAmount = 22;

                SurvivalTrinketSlot[] newSlots = new SurvivalTrinketSlot[trinketSlotAmount];

                for (Map.Entry<String, Map<String, TrinketInventory>> entry : trinkets.getInventory().entrySet()) {
                    String groupId = entry.getKey();
                    SlotGroup group = groups.get(groupId);

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
                        if (stacks.size() > 1 && BetterAdventureModeClient.clientConfig.show_debug_log) {
                            BetterAdventureMode.warn("Multiple slots are defined for slot group " + slot.getKey() + ". This may lead to unexpected behaviour");
                        }
                        int x = groupPos.get(group).x();
                        slotHeights.computeIfAbsent(group, (k) -> new ArrayList<>()).add(new Point(x, stacks.size()));
                        slotTypes.computeIfAbsent(group, (k) -> new ArrayList<>()).add(stacks.getSlotType());

                        for (int i = 0; i < stacks.size(); i++) {
                            int index = group.getOrder() - 1; // -1 to account for all groups with order=0 being ignored
                            newSlots[index] = new SurvivalTrinketSlot(stacks, i, groupPos.get(group).x(), groupPos.get(group).y(), group, stacks.getSlotType(), 0, true);
                        }

//                    groupOffset++;
                        width++;
                    }
                    slotWidths.put(group, width);
                }

                // add slots to screenHandler
                for (int i = 0; i < trinketSlotAmount; i++) {
                    if (newSlots[i] != null) {
                        this.addSlot(newSlots[i]);
                    }
                }
            } else {
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
            }

            trinketSlotEnd = slots.size();
        });
    }

    @Unique
    private boolean betteradventuremode$hasSlots(TrinketComponent comp, SlotGroup group) {
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
    private void betteradventuremode$onClosed(PlayerEntity player, CallbackInfo info) {
        if (player.getWorld().isClient) {
            TrinketsClient.activeGroup = null;
            TrinketsClient.activeType = null;
            TrinketsClient.quickMoveGroup = null;
        }
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public ItemStack quickMove(PlayerEntity player, int index) {
        Slot slot = slots.get(index);
        boolean bl = false;
        if (slot.hasStack()) {
            ItemStack stack = slot.getStack();
            if (index >= trinketSlotStart && index < trinketSlotEnd) {
                if (!this.insertItem(stack, 9, bl ? 45 : 36, false)) {
                    return ItemStack.EMPTY;
                } else {
                    return stack;
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

                        if ((Objects.equals(type.getGroup(), "spell_slot_1") && player.getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 1)
                                || (Objects.equals(type.getGroup(), "spell_slot_2") && player.getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 2)
                                || (Objects.equals(type.getGroup(), "spell_slot_3") && player.getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 3)
                                || (Objects.equals(type.getGroup(), "spell_slot_4") && player.getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 4)
                                || (Objects.equals(type.getGroup(), "spell_slot_5") && player.getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 5)
                                || (Objects.equals(type.getGroup(), "spell_slot_6") && player.getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 6)
                                || (Objects.equals(type.getGroup(), "spell_slot_7") && player.getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 7)
                                || (Objects.equals(type.getGroup(), "spell_slot_8") && player.getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 8)
                        ) {
                            continue;
                        }

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
                });
            }
        }
        ItemStack itemStack = ItemStack.EMPTY;
        if (slot.hasStack()) {
            int i;
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 0) {
                if (!this.insertItem(itemStack2, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if ((index >= 9 && index < 36 ? !this.insertItem(itemStack2, 36, 45, false) : (index >= 36 && index < 45 ? !this.insertItem(itemStack2, 9, 36, false) : !this.insertItem(itemStack2, 9, 45, false))))/*)))*/ {
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
            if (index == 0) {
                player.dropItem(itemStack2, false);
            }
        }
        return itemStack;
    }
}
