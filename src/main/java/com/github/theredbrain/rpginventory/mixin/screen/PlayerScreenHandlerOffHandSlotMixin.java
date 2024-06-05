package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"net/minecraft/screen/PlayerScreenHandler$2"})
public abstract class PlayerScreenHandlerOffHandSlotMixin extends Slot {

    @Shadow @Final PlayerEntity field_42464;

    public PlayerScreenHandlerOffHandSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        boolean bl = true;
        if (this.field_42464.getServer() != null) {
            bl = this.field_42464.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
        }

        StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
        boolean hasCivilisationEffect = civilisation_status_effect != null && this.field_42464.hasStatusEffect(civilisation_status_effect);

        StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
        boolean hasWildernessEffect = wilderness_status_effect != null && this.field_42464.hasStatusEffect(wilderness_status_effect);

        return stack.isIn(Tags.OFF_HAND_ITEMS) && (hasCivilisationEffect || this.field_42464.isCreative() || (bl && !hasWildernessEffect));
    }
}
