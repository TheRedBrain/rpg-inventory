package com.github.theredbrain.rpginventory.mixin.trinkets;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import dev.emi.trinkets.SurvivalTrinketSlot;
import dev.emi.trinkets.api.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
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
    public void rpginventory$canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
        boolean bl = false;
        if (livingEntity instanceof PlayerEntity playerEntity && playerEntity.isCreative()) {
            bl = true;
        }
        boolean bl2 = true;
        if (livingEntity.getServer() != null) {
            bl2 = livingEntity.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
        }
        StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
        boolean hasCivilisationEffect = civilisation_status_effect != null && livingEntity.hasStatusEffect(civilisation_status_effect);

        StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
        boolean hasWildernessEffect = wilderness_status_effect != null && livingEntity.hasStatusEffect(wilderness_status_effect);

        cir.setReturnValue(cir.getReturnValue() && (hasCivilisationEffect || bl || (bl2 && !hasWildernessEffect)));
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "canTakeItems", at = @At("RETURN"), cancellable = true)
    public void rpginventory$canTakeItems(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = true;
        if (player.getServer() != null) {
            bl = player.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
        }
        StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
        boolean hasCivilisationEffect = civilisation_status_effect != null && player.hasStatusEffect(civilisation_status_effect);

        StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
        boolean hasWildernessEffect = wilderness_status_effect != null && player.hasStatusEffect(wilderness_status_effect);

        cir.setReturnValue(cir.getReturnValue() && (hasCivilisationEffect || (bl && !hasWildernessEffect) || player.isCreative()));
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "isEnabled", at = @At("RETURN"), cancellable = true)
    public void rpginventory$isEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue()
                && !((Objects.equals(this.group.getName(), "spell_slot_1") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 1)
                || (Objects.equals(this.group.getName(), "spell_slot_2") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 2)
                || (Objects.equals(this.group.getName(), "spell_slot_3") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 3)
                || (Objects.equals(this.group.getName(), "spell_slot_4") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 4)
                || (Objects.equals(this.group.getName(), "spell_slot_5") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 5)
                || (Objects.equals(this.group.getName(), "spell_slot_6") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 6)
                || (Objects.equals(this.group.getName(), "spell_slot_7") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 7)
                || (Objects.equals(this.group.getName(), "spell_slot_8") && trinketInventory.getComponent().getEntity().getAttributeValue(RPGInventory.ACTIVE_SPELL_SLOT_AMOUNT) < 8)
                || (this.group.getOrder() == 0)));
    }
}
