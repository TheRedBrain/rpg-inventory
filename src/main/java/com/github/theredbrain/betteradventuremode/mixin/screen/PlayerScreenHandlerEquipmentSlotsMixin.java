package com.github.theredbrain.betteradventuremode.mixin.screen;

import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.GameRulesRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.registry.Tags;
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
    public void betteradventuremode$canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = true;
        if (this.field_39410.getServer() != null) {
            bl = this.field_39410.getServer().getGameRules().getBoolean(GameRulesRegistry.REQUIRE_CIVILISATION_EFFECT_TO_CHANGE_GEAR_IN_ADVENTURE_MODE);
        }
        cir.setReturnValue((cir.getReturnValue() || betteradventuremode$isOfEquipmentTag(stack, this.field_7834)) && (this.field_39410.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT)|| !((DuckPlayerEntityMixin) this.field_39410).betteradventuremode$isAdventure() || !bl));
    }

    @Unique
    private boolean betteradventuremode$isOfEquipmentTag(ItemStack itemStack, EquipmentSlot slot) {
        return switch (slot) {
            case FEET -> itemStack.isIn(Tags.BOOTS);
            case LEGS -> itemStack.isIn(Tags.LEGGINGS);
            case CHEST -> itemStack.isIn(Tags.CHEST_PLATES);
            case HEAD -> itemStack.isIn(Tags.HELMETS);
            default -> false;
        };
    }
}
