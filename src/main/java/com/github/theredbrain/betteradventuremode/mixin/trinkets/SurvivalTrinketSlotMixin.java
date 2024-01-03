package com.github.theredbrain.betteradventuremode.mixin.trinkets;

import com.github.theredbrain.betteradventuremode.registry.EntityAttributesRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
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
    public void bamcore$canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
        boolean bl = false;
        if (livingEntity instanceof PlayerEntity playerEntity && !((DuckPlayerEntityMixin) playerEntity).betteradventuremode$isAdventure()) {
            bl = true;
        }
        boolean bl2 = true;
        if (livingEntity.getServer() != null) {
            bl2 = livingEntity.getServer().getGameRules().getBoolean(GameRulesRegistry.REQUIRE_CIVILISATION_EFFECT_TO_CHANGE_GEAR_IN_ADVENTURE_MODE);
        }
        cir.setReturnValue(cir.getReturnValue() && (livingEntity.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT)|| bl || !bl2));
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "canTakeItems", at = @At("RETURN"), cancellable = true)
    public void bamcore$canTakeItems(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = true;
        if (player.getServer() != null) {
            bl = player.getServer().getGameRules().getBoolean(GameRulesRegistry.REQUIRE_CIVILISATION_EFFECT_TO_CHANGE_GEAR_IN_ADVENTURE_MODE);
        }
        cir.setReturnValue(cir.getReturnValue() && (player.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || !bl || !((DuckPlayerEntityMixin) player).betteradventuremode$isAdventure()));
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "isEnabled", at = @At("RETURN"), cancellable = true)
    public void isEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue()
                && !((Objects.equals(this.group.getName(), "spell_slot_1") && trinketInventory.getComponent().getEntity().getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 1)
                || (Objects.equals(this.group.getName(), "spell_slot_2") && trinketInventory.getComponent().getEntity().getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 2)
                || (Objects.equals(this.group.getName(), "spell_slot_3") && trinketInventory.getComponent().getEntity().getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 3)
                || (Objects.equals(this.group.getName(), "spell_slot_4") && trinketInventory.getComponent().getEntity().getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 4)
                || (Objects.equals(this.group.getName(), "spell_slot_5") && trinketInventory.getComponent().getEntity().getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 5)
                || (Objects.equals(this.group.getName(), "spell_slot_6") && trinketInventory.getComponent().getEntity().getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 6)
                || (Objects.equals(this.group.getName(), "spell_slot_7") && trinketInventory.getComponent().getEntity().getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 7)
                || (Objects.equals(this.group.getName(), "spell_slot_8") && trinketInventory.getComponent().getEntity().getAttributeValue(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT) < 8)
                || (this.group.getOrder() == 0)));
    }
}
