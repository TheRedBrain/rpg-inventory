package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"net/minecraft/screen/PlayerScreenHandler$1"})
public class PlayerScreenHandlerEquipmentSlotsMixin {

    @Shadow @Final EquipmentSlot field_7834;

    @Shadow @Final PlayerEntity field_39410;

    @Inject(method = "canInsert", at = @At("RETURN"), cancellable = true)
    public void rpginventory$canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = true;
        if (this.field_39410.getServer() != null) {
            bl = this.field_39410.getServer().getGameRules().getBoolean(GameRulesRegistry.CAN_CHANGE_EQUIPMENT);
        }

        StatusEffect civilisation_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.civilisation_status_effect_identifier));
        boolean hasCivilisationEffect = civilisation_status_effect != null && this.field_39410.hasStatusEffect(civilisation_status_effect);

        StatusEffect wilderness_status_effect = Registries.STATUS_EFFECT.get(Identifier.tryParse(RPGInventory.serverConfig.wilderness_status_effect_identifier));
        boolean hasWildernessEffect = wilderness_status_effect != null && this.field_39410.hasStatusEffect(wilderness_status_effect);

        cir.setReturnValue((cir.getReturnValue() || rpginventory$isOfEquipmentTag(stack, this.field_7834)) && (hasCivilisationEffect || this.field_39410.isCreative() || (bl && !hasWildernessEffect)));
    }

    @Unique
    private boolean rpginventory$isOfEquipmentTag(ItemStack itemStack, EquipmentSlot slot) {
        return switch (slot) {
            case FEET -> itemStack.isIn(Tags.BOOTS);
            case LEGS -> itemStack.isIn(Tags.LEGGINGS);
            case CHEST -> itemStack.isIn(Tags.CHEST_PLATES);
            case HEAD -> itemStack.isIn(Tags.HELMETS);
            default -> false;
        };
    }
}
