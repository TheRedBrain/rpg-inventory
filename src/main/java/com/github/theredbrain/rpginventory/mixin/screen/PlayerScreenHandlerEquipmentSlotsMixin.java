package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.registry.GameRulesRegistry;
import com.github.theredbrain.rpginventory.registry.StatusEffectsRegistry;
import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
        cir.setReturnValue((cir.getReturnValue() || rpginventory$isOfEquipmentTag(stack, this.field_7834)) && (this.field_39410.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT) || this.field_39410.isCreative() || (bl && !this.field_39410.hasStatusEffect(StatusEffectsRegistry.WILDERNESS_EFFECT))));
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
