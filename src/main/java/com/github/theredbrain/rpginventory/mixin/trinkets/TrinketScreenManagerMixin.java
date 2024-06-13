package com.github.theredbrain.rpginventory.mixin.trinkets;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import dev.emi.trinkets.*;
import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(value = TrinketScreenManager.class, remap = false)
public class TrinketScreenManagerMixin {

    @Shadow public static SlotGroup group;

    @Shadow public static TrinketScreen currentScreen;

    @Shadow public static Rect2i typeBounds;

    @Shadow public static Rect2i currentBounds;

    @Shadow public static SlotGroup quickMoveGroup;

    @Shadow public static Rect2i quickMoveTypeBounds;

    @Shadow public static Rect2i quickMoveBounds;

    /**
     * @author TheRedBrain
     * @reason integrating disabled groups based on entity attribute value
     */
    @Overwrite
    public static void update(float mouseX, float mouseY) {
        TrinketPlayerScreenHandler handler = currentScreen.trinkets$getHandler();
        Slot focusedSlot = currentScreen.trinkets$getFocusedSlot();
        int x = currentScreen.trinkets$getX();
        int y = currentScreen.trinkets$getY();
        if (group != null) {
            if (TrinketsClient.activeType != null) {
                if (!typeBounds.contains(Math.round(mouseX) - x, Math.round(mouseY) - y)) {
                    TrinketsClient.activeType = null;
                } else if (focusedSlot != null) {
                    if (!(focusedSlot instanceof TrinketSlot ts && ts.getType() == TrinketsClient.activeType)) {
                        TrinketsClient.activeType = null;
                    }
                }
            }
            if (TrinketsClient.activeType == null) {
                if (!currentBounds.contains(Math.round(mouseX) - x, Math.round(mouseY) - y)) {
                    TrinketsClient.activeGroup = null;
                    group = null;
                } else {
                    if (focusedSlot instanceof TrinketSlot ts) {
                        int i = handler.trinkets$getSlotTypes(group).indexOf(ts.getType());
                        if (i >= 0) {
                            Point slotHeight = handler.trinkets$getSlotHeight(group, i);
                            if (slotHeight != null) {
                                Rect2i r = currentScreen.trinkets$getGroupRect(group);
                                int height = slotHeight.y();
                                if (height > 1) {
                                    TrinketsClient.activeType = ts.getType();
                                    typeBounds = new Rect2i(r.getX() + slotHeight.x() - 2, r.getY() - (height - 1) / 2 * 18 - 3, 23, height * 18 + 5);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (group == null && quickMoveGroup != null) {
            if (quickMoveTypeBounds.contains(Math.round(mouseX) - x, Math.round(mouseY) - y)) {
                TrinketsClient.activeGroup = quickMoveGroup;
                TrinketsClient.activeType = TrinketsClient.quickMoveType;
                int i = handler.trinkets$getSlotTypes(TrinketsClient.activeGroup).indexOf(TrinketsClient.activeType);
                if (i >= 0) {
                    Point slotHeight = handler.trinkets$getSlotHeight(TrinketsClient.activeGroup, i);
                    if (slotHeight != null) {
                        Rect2i r = currentScreen.trinkets$getGroupRect(TrinketsClient.activeGroup);
                        int height = slotHeight.y();
                        if (height > 1) {
                            typeBounds = new Rect2i(r.getX() + slotHeight.x() - 2, r.getY() - (height - 1) / 2 * 18 - 3, 23, height * 18 + 5);
                        }
                    }
                }
                TrinketsClient.quickMoveGroup = null;
            } else if (quickMoveBounds.contains(Math.round(mouseX) - x, Math.round(mouseY) - y)) {
                TrinketsClient.activeGroup = quickMoveGroup;
                TrinketsClient.quickMoveGroup = null;
            }
        }

        if (group == null) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            for (SlotGroup g : TrinketsApi.getPlayerSlots(player).values()) {
                Rect2i r = currentScreen.trinkets$getGroupRect(g);
                if (r.getX() < 0 && currentScreen.trinkets$isRecipeBookOpen()) {
                    continue;
                }
                if (player != null && ((Objects.equals(g.getName(), "spell_slot_1") && player.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 1)
                        || (Objects.equals(g.getName(), "spell_slot_2") && player.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 2)
                        || (Objects.equals(g.getName(), "spell_slot_3") && player.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 3)
                        || (Objects.equals(g.getName(), "spell_slot_4") && player.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 4)
                        || (Objects.equals(g.getName(), "spell_slot_5") && player.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 5)
                        || (Objects.equals(g.getName(), "spell_slot_6") && player.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 6)
                        || (Objects.equals(g.getName(), "spell_slot_7") && player.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 7)
                        || (Objects.equals(g.getName(), "spell_slot_8") && player.getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 8)
                        || (Objects.equals(g.getName(), "main_hand") && ((DuckPlayerEntityMixin)player).rpginventory$isMainHandStackSheathed())
                        || (Objects.equals(g.getName(), "sheathed_main_hand")  && !((DuckPlayerEntityMixin)player).rpginventory$isMainHandStackSheathed())
                        || (Objects.equals(g.getName(), "sheathed_offhand")  && !((DuckPlayerEntityMixin)player).rpginventory$isOffHandStackSheathed())
                )) {
                    continue;
                }
                if (r.contains(Math.round(mouseX) - x, Math.round(mouseY) - y)) {
                    TrinketsClient.activeGroup = g;
                    TrinketsClient.quickMoveGroup = null;
                    break;
                }
            }
        }

        if (group != TrinketsClient.activeGroup) {
            group = TrinketsClient.activeGroup;

            if (group != null) {
                int slotsWidth = handler.trinkets$getSlotWidth(group) + 1;
                if (group.getSlotId() == -1) slotsWidth -= 1;
                Rect2i r = currentScreen.trinkets$getGroupRect(group);
                currentBounds = new Rect2i(0, 0, 0, 0);

                if (r != null) {
                    int l = (slotsWidth - 1) / 2 * 18;

                    if (slotsWidth > 1) {
                        currentBounds = new Rect2i(r.getX() - l - 3, r.getY() - 3, slotsWidth * 18 + 5, 23);
                    } else {
                        currentBounds = r;
                    }

                    if (focusedSlot instanceof TrinketSlot ts) {
                        int i = handler.trinkets$getSlotTypes(group).indexOf(ts.getType());
                        if (i >= 0) {
                            Point slotHeight = handler.trinkets$getSlotHeight(group, i);
                            if (slotHeight != null) {
                                int height = slotHeight.y();
                                if (height > 1) {
                                    TrinketsClient.activeType = ts.getType();
                                    typeBounds = new Rect2i(r.getX() + slotHeight.x() - 2, r.getY() - (height - 1) / 2 * 18 - 3, 23, height * 18 + 5);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (quickMoveGroup != TrinketsClient.quickMoveGroup) {
            quickMoveGroup = TrinketsClient.quickMoveGroup;

            if (quickMoveGroup != null) {
                int slotsWidth = handler.trinkets$getSlotWidth(quickMoveGroup) + 1;

                if (quickMoveGroup.getSlotId() == -1) slotsWidth -= 1;
                Rect2i r = currentScreen.trinkets$getGroupRect(quickMoveGroup);
                quickMoveBounds = new Rect2i(0, 0, 0, 0);

                if (r != null) {
                    int l = (slotsWidth - 1) / 2 * 18;
                    quickMoveBounds = new Rect2i(r.getX() - l - 5, r.getY() - 5, slotsWidth * 18 + 8, 26);
                    if (TrinketsClient.quickMoveType != null) {
                        int i = handler.trinkets$getSlotTypes(quickMoveGroup).indexOf(TrinketsClient.quickMoveType);
                        if (i >= 0) {
                            Point slotHeight = handler.trinkets$getSlotHeight(quickMoveGroup, i);
                            if (slotHeight != null) {
                                int height = slotHeight.y();
                                quickMoveTypeBounds = new Rect2i(r.getX() + slotHeight.x() - 2, r.getY() - (height - 1) / 2 * 18 - 3, 23, height * 18 + 5);
                            }
                        }
                    }
                }
            }
        }
    }
}
