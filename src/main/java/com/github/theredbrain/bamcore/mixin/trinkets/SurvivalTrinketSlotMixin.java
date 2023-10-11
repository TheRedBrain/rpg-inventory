package com.github.theredbrain.bamcore.mixin.trinkets;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreEntityAttributes;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreStatusEffects;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.api.*;
import io.wispforest.owo.mixin.ui.SlotAccessor;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.core.PositionedRectangle;
import io.wispforest.owo.util.pond.OwoSlotExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(SurvivalTrinketSlot.class)
public abstract class SurvivalTrinketSlotMixin extends Slot/* implements OwoSlotExtension, SlotAccessor*/ {

    @Shadow @Final private TrinketInventory trinketInventory;
    @Shadow @Final private SlotGroup group;
//    @Unique
//    private boolean owo$disabledOverride = false;
//
//    @Unique
//    private @Nullable PositionedRectangle owo$scissorArea = null;

    public SurvivalTrinketSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

//    /**
//     * @author TheRedBrain
//     */
//    @Override
//    public void onQuickTransfer(ItemStack newItem, ItemStack original) {
//        LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
//        if (livingEntity instanceof PlayerEntity playerEntity) {
//            ((DuckPlayerEntityMixin)playerEntity).setShouldUpdateTrinketSlots(true);
//        }
//        super.onQuickTransfer(newItem, original);
//    }
//
//    /**
//     * @author TheRedBrain
//     */
//    @Override
//    public void setStack(ItemStack stack) {
//        LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
//        if (livingEntity instanceof PlayerEntity playerEntity) {
//            ((DuckPlayerEntityMixin)playerEntity).setShouldUpdateTrinketSlots(true);
//        }
//        super.setStack(stack);
//    }
//
//    /**
//     * @author TheRedBrain
//     */
//    @Override
//    public ItemStack takeStack(int amount) {
//        LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
//        if (livingEntity instanceof PlayerEntity playerEntity) {
//            ((DuckPlayerEntityMixin)playerEntity).setShouldUpdateTrinketSlots(true);
//        }
//        return super.takeStack(amount);
//    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "canInsert", at = @At("RETURN"), cancellable = true)
    public void bamcore$canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
        boolean bl = false;
        if (livingEntity instanceof PlayerEntity playerEntity && !((DuckPlayerEntityMixin) playerEntity).bamcore$isAdventure()) {
            bl = true;
        }
        cir.setReturnValue(cir.getReturnValue() && (livingEntity.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT) || bl));
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "canTakeItems", at = @At("RETURN"), cancellable = true)
    public void bamcore$canTakeItems(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
        boolean bl = false;
        if (livingEntity instanceof PlayerEntity playerEntity && !((DuckPlayerEntityMixin) playerEntity).bamcore$isAdventure()) {
            bl = true;
        }
        cir.setReturnValue(cir.getReturnValue() && (livingEntity.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT) || bl));
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "isEnabled", at = @At("RETURN"), cancellable = true)
    public void isEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue()/* && !this.owo$disabledOverride*/
                && !((Objects.equals(this.group.getName(), "spell_slot_1") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT) < 1)
                || (Objects.equals(this.group.getName(), "spell_slot_2") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT) < 2)
                || (Objects.equals(this.group.getName(), "spell_slot_3") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT) < 3)
                || (Objects.equals(this.group.getName(), "spell_slot_4") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT) < 4)
                || (Objects.equals(this.group.getName(), "spell_slot_5") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT) < 5)
                || (Objects.equals(this.group.getName(), "spell_slot_6") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT) < 6)
                || (Objects.equals(this.group.getName(), "spell_slot_7") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT) < 7)
                || (Objects.equals(this.group.getName(), "spell_slot_8") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureModeCoreEntityAttributes.ACTIVE_SPELL_SLOT_AMOUNT) < 8)
                || (this.group.getOrder() == 0)));
    }

//    @Override
//    public void owo$setDisabledOverride(boolean disabled) {
//        this.owo$disabledOverride = disabled;
//    }
//
//    @Override
//    public boolean owo$getDisabledOverride() {
//        return this.owo$disabledOverride;
//    }
//
//    @Override
//    public void owo$setScissorArea(@Nullable PositionedRectangle scissor) {
//        this.owo$scissorArea = scissor;
//    }
//
//    @Override
//    public @Nullable PositionedRectangle owo$getScissorArea() {
//        return this.owo$scissorArea;
//    }
}
