package com.github.theredbrain.bamcore.mixin.item;

import com.github.theredbrain.bamcore.item.CustomArmorItem;
import com.github.theredbrain.bamcore.item.CustomDyeableArmorItem;
import com.github.theredbrain.bamcore.item.DuckItemMixin;
import com.github.theredbrain.bamcore.item.DuckItemStackMixin;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin implements DuckItemStackMixin {

    @Shadow
    public Item getItem() {
        throw new AssertionError();
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "getAttributeModifiers", at = @At("HEAD"), cancellable = true)
    public void bam$getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        if (this.getItem() instanceof CustomArmorItem && !(((CustomArmorItem)this.getItem()).isProtecting((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
        if (this.getItem() instanceof CustomDyeableArmorItem && !(((CustomDyeableArmorItem)this.getItem()).isProtecting((ItemStack) (Object) this))) {
            cir.setReturnValue(HashMultimap.create());
            cir.cancel();
        }
    }

    public TypedActionResult<ItemStack> adventureUse(World world, PlayerEntity user) {
        return ((DuckItemMixin)this.getItem()).adventureUse(world, user);
    }

//    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasNbt()Z", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void addEquipmentPowerTooltips(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
//        this.
//        for(EquipmentSlot slot : EquipmentSlot.values()) {
//            List<StackPowerUtil.StackPower> powers = StackPowerUtil.getPowers((ItemStack)(Object)this, slot)
//                    .stream()
//                    .filter(sp -> !sp.isHidden)
//                    .toList();
//            if(powers.size() > 0) {
//                list.add(Text.empty());
//                list.add((Text.translatable("item.modifiers." + slot.getName())).formatted(Formatting.GRAY));
//                powers.forEach(sp -> {
//
//                    if(PowerTypeRegistry.contains(sp.powerId)) {
//                        PowerType<?> powerType = PowerTypeRegistry.get(sp.powerId);
//                        list.add(
//                                Text.literal(" ")
//                                        .append(powerType.getName())
//                                        .formatted(sp.isNegative ? Formatting.RED : Formatting.BLUE));
//                        if(context.isAdvanced()) {
//                            list.add(
//                                    Text.literal("  ")
//                                            .append(powerType.getDescription())
//                                            .formatted(Formatting.GRAY));
//                        }
//                    }
//                });
//            }
//        }
//        PowerHolderComponent.getPowers(player, TooltipPower.class)
//                .stream().filter(t -> t.doesApply((ItemStack) (Object)this))
//                .sorted(Comparator.comparing(TooltipPower::getOrder))
//                .forEachOrdered(t -> t.addToTooltip(list));
//    }
}
