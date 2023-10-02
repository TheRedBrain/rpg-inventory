package com.github.theredbrain.bamcore.screen.slot;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreStatusEffects;
import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.TrinketsClient;
import dev.emi.trinkets.api.*;
import io.wispforest.owo.ui.core.PositionedRectangle;
import io.wispforest.owo.util.pond.OwoSlotExtension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class AdventureTrinketSlot extends Slot implements TrinketSlot, OwoSlotExtension {
    private final SlotGroup group;
    private final SlotType type;
    private final int slotOffset;
    private final TrinketInventory trinketInventory;
    private final PlayerEntity owner;

    private boolean owo$disabledOverride = false;

    private @Nullable PositionedRectangle owo$scissorArea = null;

    public AdventureTrinketSlot(TrinketInventory inventory, int index, int x, int y, SlotGroup group, SlotType type, int slotOffset, PlayerEntity owner) {
//        super(inventory, index, x, y, group, type, slotOffset, alwaysVisible);
        super(inventory, index, x, y);
        this.group = group;
        this.type = type;
        this.slotOffset = slotOffset;
        this.trinketInventory = inventory;
        this.owner = owner;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return TrinketSlot.canInsert(stack, new SlotReference(trinketInventory, slotOffset), trinketInventory.getComponent().getEntity())
                && (owner.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
    }

    @Override
    public boolean canTakeItems(PlayerEntity player) {
        ItemStack stack = this.getStack();
        return TrinketsApi.getTrinket(stack.getItem()).canUnequip(stack, new SlotReference(trinketInventory, slotOffset), player)
                && (owner.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
    }

    @Override
    public boolean isTrinketFocused() {
        if (TrinketsClient.activeGroup == group) {
            return slotOffset == 0 || TrinketsClient.activeType == type;
        } else if (TrinketsClient.quickMoveGroup == group) {
            return slotOffset == 0 || TrinketsClient.quickMoveType == type && TrinketsClient.quickMoveTimer > 0;
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        return !this.owo$disabledOverride;
    }

    @Override
    public Identifier getBackgroundIdentifier() {
        return type.getIcon();
    }

    @Override
    public SlotType getType() {
        return type;
    }

    @Override
    public void owo$setDisabledOverride(boolean disabled) {
        this.owo$disabledOverride = disabled;
    }

    @Override
    public boolean owo$getDisabledOverride() {
        return this.owo$disabledOverride;
    }

    @Override
    public void owo$setScissorArea(@Nullable PositionedRectangle scissor) {
        this.owo$scissorArea = scissor;
    }

    @Override
    public @Nullable PositionedRectangle owo$getScissorArea() {
        return this.owo$scissorArea;
    }
}
