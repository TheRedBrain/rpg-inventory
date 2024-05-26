package com.github.theredbrain.betteradventuremode.mixin.trinkets;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.registry.GameRulesRegistry;
import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.api.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(SurvivalTrinketSlot.class)
public abstract class SurvivalTrinketSlotMixin extends Slot {

    @Shadow @Final private TrinketInventory trinketInventory;
    @Shadow @Final private SlotGroup group;

    public SurvivalTrinketSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "canInsert", at = @At("RETURN"), cancellable = true)
    public void betteradventuremode$canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
        boolean bl = false;
        if (livingEntity instanceof PlayerEntity playerEntity && playerEntity.isCreative()) {
            bl = true;
        }
        boolean bl2 = true;
        if (livingEntity.getServer() != null) {
            bl2 = livingEntity.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
        }
        cir.setReturnValue(cir.getReturnValue() && (livingEntity.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || bl || (bl2 && !livingEntity.hasStatusEffect(StatusEffectsRegistry.WILDERNESS_EFFECT))));
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "canTakeItems", at = @At("RETURN"), cancellable = true)
    public void betteradventuremode$canTakeItems(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = true;
        if (player.getServer() != null) {
            bl = player.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
        }
        cir.setReturnValue(cir.getReturnValue() && (player.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || (bl && !player.hasStatusEffect(StatusEffectsRegistry.WILDERNESS_EFFECT)) || player.isCreative()));
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "isEnabled", at = @At("RETURN"), cancellable = true)
    public void betteradventuremode$isEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue()
                && !((Objects.equals(this.group.getName(), "spell_slot_1") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureMode.ACTIVE_SPELL_SLOT_AMOUNT) < 1)
                || (Objects.equals(this.group.getName(), "spell_slot_2") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureMode.ACTIVE_SPELL_SLOT_AMOUNT) < 2)
                || (Objects.equals(this.group.getName(), "spell_slot_3") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureMode.ACTIVE_SPELL_SLOT_AMOUNT) < 3)
                || (Objects.equals(this.group.getName(), "spell_slot_4") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureMode.ACTIVE_SPELL_SLOT_AMOUNT) < 4)
                || (Objects.equals(this.group.getName(), "spell_slot_5") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureMode.ACTIVE_SPELL_SLOT_AMOUNT) < 5)
                || (Objects.equals(this.group.getName(), "spell_slot_6") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureMode.ACTIVE_SPELL_SLOT_AMOUNT) < 6)
                || (Objects.equals(this.group.getName(), "spell_slot_7") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureMode.ACTIVE_SPELL_SLOT_AMOUNT) < 7)
                || (Objects.equals(this.group.getName(), "spell_slot_8") && trinketInventory.getComponent().getEntity().getAttributeValue(BetterAdventureMode.ACTIVE_SPELL_SLOT_AMOUNT) < 8)
                || (this.group.getOrder() == 0)));
    }
}
