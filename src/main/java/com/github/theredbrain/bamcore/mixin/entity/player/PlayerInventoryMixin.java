package com.github.theredbrain.bamcore.mixin.entity.player;

import com.github.theredbrain.bamcore.api.item.GlovesArmorTrinketItem;
import com.github.theredbrain.bamcore.api.item.ArmorTrinketItem;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.bamcore.api.item.CustomArmorItem;
import com.github.theredbrain.bamcore.api.item.CustomDyeableArmorItem;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreItemUtils;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreStatusEffects;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements DuckPlayerInventoryMixin {

    @Shadow
    @Final
    public DefaultedList<ItemStack> main;

    @Shadow
    @Final
    @Mutable
    public DefaultedList<ItemStack> armor;
    @Shadow
    @Final
    public PlayerEntity player;

    @Shadow
    private boolean canStackAddMore(ItemStack existingStack, ItemStack stack) {
        throw new AssertionError();
    }

    @Shadow public int selectedSlot;

    @Shadow public abstract ItemStack getStack(int slot);

    @Shadow @Final public DefaultedList<ItemStack> offHand;

    @Inject(method = "getMainHandStack", at = @At("HEAD"), cancellable = true)
    public void bamcore$getMainHandStack(CallbackInfoReturnable<ItemStack> cir) {
        if (!player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.ADVENTURE_BUILDING_EFFECT) && !player.isCreative()) {
            ItemStack emptyMainHandStack = bamcore$getEmptyMainHand();
            ItemStack mainHandStack = bamcore$getMainHand();
            if (!player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.WEAPONS_SHEATHED_EFFECT)) {
                cir.setReturnValue(BetterAdventureModCoreItemUtils.isUsable(mainHandStack) ? mainHandStack : emptyMainHandStack);
                cir.cancel();
            }
        }
    }

    @Override
    public ItemStack bamcore$getOffHandStack() {
        if (!player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.ADVENTURE_BUILDING_EFFECT) && !player.isCreative()) {
            ItemStack emptyOffHandStack = bamcore$getEmptyOffHand();
            ItemStack offHandStack = bamcore$getOffHand();
            if (!player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.WEAPONS_SHEATHED_EFFECT) && !player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.TWO_HANDED_EFFECT)) {
                return BetterAdventureModCoreItemUtils.isUsable(offHandStack) ? offHandStack : emptyOffHandStack;
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     *
     * @author TheRedBrain
     * @reason TODO
     */
    @Overwrite
    public int getEmptySlot() {
        for (int i = ((DuckPlayerEntityMixin)this.player).bamcore$isAdventure() && !this.player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.ADVENTURE_BUILDING_EFFECT) ? 9 : 0; i < this.main.size(); ++i) {
            if (!this.main.get(i).isEmpty()) continue;
            return i;
        }
        return -1;
    }

    /**
     *
     * @author TheRedBrain
     * @reason TODO
     */
    @Overwrite
    public int getOccupiedSlotWithRoomForStack(ItemStack stack) {
        if (this.canStackAddMore(this.getStack(this.selectedSlot), stack)) {
            return this.selectedSlot;
        }

        for (int i = ((DuckPlayerEntityMixin)this.player).bamcore$isAdventure() && !this.player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.ADVENTURE_BUILDING_EFFECT) ? 9 : 0; i < this.main.size(); ++i) {
            if (!this.canStackAddMore(this.main.get(i), stack)) continue;
            return i;
        }
        return -1;
    }

    /**
     * @author TheRedBrain
     * @reason overhaul armor
     */
    @Overwrite
    public ItemStack getArmorStack(int slot) {
        if (slot == 0) {
            return this.bamcore$getFeetStack();
        } else if (slot == 1) {
            return this.bamcore$getLegStack();
        } else if (slot == 2) {
            return this.bamcore$getChestStack();
        } else if (slot == 3) {
            return this.bamcore$getHeadStack();
        } else {
            return ItemStack.EMPTY;
        }
    }

    /**
     * @author TheRedBrain
     * @reason overhaul armor
     */
    @Overwrite
    public void damageArmor(DamageSource damageSource, float amount, int[] slots) {
        if (amount <= 0.0f) {
            return;
        }
        // divide by 6 because gloves and shoulders exist
        if ((amount /= 6.0f) < 1.0f) {
            amount = 1.0f;
        }
        for (int i : slots) {
            ItemStack itemStack = this.armor.get(i);
            if (damageSource.isIn(DamageTypeTags.IS_FIRE) && itemStack.getItem().isFireproof() || !(itemStack.getItem() instanceof ArmorItem)) continue;
            if ((itemStack.getItem() instanceof CustomArmorItem && !(((CustomArmorItem)itemStack.getItem()).isProtecting(itemStack))) || (itemStack.getItem() instanceof CustomDyeableArmorItem && !(((CustomDyeableArmorItem)itemStack.getItem()).isProtecting(itemStack))))  continue;
            itemStack.damage((int)amount, this.player, player -> player.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i)));
        }
        float finalAmount = amount;
        TrinketsApi.getTrinketComponent(player).ifPresent(trinkets ->
                trinkets.forEach((slotReference, itemStack) -> {
                    if ((!(damageSource.isIn(DamageTypeTags.IS_FIRE)) || itemStack.getItem().isFireproof()) && itemStack.getItem() instanceof ArmorTrinketItem && (((ArmorTrinketItem) itemStack.getItem()).isProtecting(itemStack))) {
                        itemStack.damage((int) finalAmount, this.player, player -> TrinketsApi.onTrinketBroken(itemStack, slotReference, player));
                    }
                }));
    }

    public ItemStack bamcore$getMainHand() {
        ItemStack mainHandStack = this.main.get(this.selectedSlot);
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("main_hand") != null) {
                if (trinkets.get().getInventory().get("main_hand").get("main_hand") != null) {
                    mainHandStack = trinkets.get().getInventory().get("main_hand").get("main_hand").getStack(0);
                }
            }
        }
        return mainHandStack;
    }

    public ItemStack bamcore$setMainHand(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getMainHand();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("main_hand") != null) {
                if (trinkets.get().getInventory().get("main_hand").get("main_hand") != null) {
                    trinkets.get().getInventory().get("main_hand").get("main_hand").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack bamcore$getOffHand() {
        ItemStack offHandStack = this.offHand.get(0);
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("off_hand") != null) {
                if (trinkets.get().getInventory().get("off_hand").get("off_hand") != null) {
                    offHandStack = trinkets.get().getInventory().get("off_hand").get("off_hand").getStack(0);
                }
            }
        }
        return offHandStack;
    }

    public ItemStack bamcore$setOffHand(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getOffHand();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("off_hand") != null) {
                if (trinkets.get().getInventory().get("off_hand").get("off_hand") != null) {
                    trinkets.get().getInventory().get("off_hand").get("off_hand").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack bamcore$getAlternativeMainHand() {
        ItemStack alternativeMainHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("alternative_main_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand") != null) {
                    alternativeMainHandStack = trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand").getStack(0);
                }
            }
        }
        return alternativeMainHandStack;
    }

    public ItemStack bamcore$setAlternativeMainHand(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getAlternativeMainHand();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("alternative_main_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand") != null) {
                    trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack bamcore$getAlternativeOffHand() {
        ItemStack alternativeOffHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("alternative_off_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand") != null) {
                    alternativeOffHandStack = trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand").getStack(0);
                }
            }
        }
        return alternativeOffHandStack;
    }

    public ItemStack bamcore$setAlternativeOffHand(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getAlternativeOffHand();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("alternative_off_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand") != null) {
                    trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack bamcore$getEmptyMainHand() {
        ItemStack alternativeMainHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("alternative_main_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand") != null) {
                    alternativeMainHandStack = trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand").getStack(0);
                }
            }
        }
        return alternativeMainHandStack;
    }

    public ItemStack bamcore$setEmptyMainHand(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getAlternativeMainHand();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("alternative_main_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand") != null) {
                    trinkets.get().getInventory().get("alternative_main_hand").get("alternative_main_hand").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack bamcore$getEmptyOffHand() {
        ItemStack alternativeOffHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("alternative_off_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand") != null) {
                    alternativeOffHandStack = trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand").getStack(0);
                }
            }
        }
        return alternativeOffHandStack;
    }

    public ItemStack bamcore$setEmptyOffHand(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getAlternativeOffHand();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("alternative_off_hand") != null) {
                if (trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand") != null) {
                    trinkets.get().getInventory().get("alternative_off_hand").get("alternative_off_hand").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack bamcore$getHeadStack() {
        ItemStack alternativeOffHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("helmets") != null) {
                if (trinkets.get().getInventory().get("helmets").get("helmet") != null) {
                    alternativeOffHandStack = trinkets.get().getInventory().get("helmets").get("helmet").getStack(0);
                }
            }
        }
        return alternativeOffHandStack;
    }

    public ItemStack bamcore$setHeadStack(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getHeadStack();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("helmets") != null) {
                if (trinkets.get().getInventory().get("helmets").get("helmet") != null) {
                    trinkets.get().getInventory().get("helmets").get("helmet").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack bamcore$getChestStack() {
        ItemStack alternativeOffHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("chest_plates") != null) {
                if (trinkets.get().getInventory().get("chest_plates").get("chest_plate") != null) {
                    alternativeOffHandStack = trinkets.get().getInventory().get("chest_plates").get("chest_plate").getStack(0);
                }
            }
        }
        return alternativeOffHandStack;
    }

    public ItemStack bamcore$setChestStack(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getChestStack();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("chest_plates") != null) {
                if (trinkets.get().getInventory().get("chest_plates").get("chest_plate") != null) {
                    trinkets.get().getInventory().get("chest_plates").get("chest_plate").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack bamcore$getLegStack() {
        ItemStack alternativeOffHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("leggings") != null) {
                if (trinkets.get().getInventory().get("leggings").get("leggings") != null) {
                    alternativeOffHandStack = trinkets.get().getInventory().get("leggings").get("leggings").getStack(0);
                }
            }
        }
        return alternativeOffHandStack;
    }

    public ItemStack bamcore$setLegStack(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getLegStack();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("leggings") != null) {
                if (trinkets.get().getInventory().get("leggings").get("leggings") != null) {
                    trinkets.get().getInventory().get("leggings").get("leggings").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack bamcore$getFeetStack() {
        ItemStack alternativeOffHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("boots") != null) {
                if (trinkets.get().getInventory().get("boots").get("boots") != null) {
                    alternativeOffHandStack = trinkets.get().getInventory().get("boots").get("boots").getStack(0);
                }
            }
        }
        return alternativeOffHandStack;
    }

    public ItemStack bamcore$setFeetStack(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getFeetStack();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("boots") != null) {
                if (trinkets.get().getInventory().get("boots").get("boots") != null) {
                    trinkets.get().getInventory().get("boots").get("boots").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public List<ItemStack> getArmorTrinkets() {
        return List.of(this.bamcore$getFeetStack(), this.bamcore$getLegStack(), this.bamcore$getGlovesStack(), this.bamcore$getChestStack(), this.bamcore$getShouldersStack(), this.bamcore$getHeadStack());
    }

    private ItemStack bamcore$getGlovesStack() {
        ItemStack glovesStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("gloves") != null) {
                if (trinkets.get().getInventory().get("gloves").get("gloves") != null) {
                    glovesStack = trinkets.get().getInventory().get("gloves").get("gloves").getStack(0);
                }
            }
        }
        return glovesStack;
    }

    private ItemStack bamcore$getShouldersStack() {
        ItemStack shouldersStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("shoulders") != null) {
                if (trinkets.get().getInventory().get("shoulders").get("shoulders") != null) {
                    shouldersStack = trinkets.get().getInventory().get("shoulders").get("shoulders").getStack(0);
                }
            }
        }
        return shouldersStack;
    }
}
