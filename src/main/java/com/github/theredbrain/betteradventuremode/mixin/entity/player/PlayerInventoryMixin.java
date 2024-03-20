package com.github.theredbrain.betteradventuremode.mixin.entity.player;

import com.github.theredbrain.betteradventuremode.item.ArmorTrinketItem;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
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
import java.util.function.Consumer;

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
    public void betteradventuremode$getMainHandStack(CallbackInfoReturnable<ItemStack> cir) {
        if (!player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) && !player.isCreative()) {
            ItemStack emptyMainHandStack = betteradventuremode$getEmptyMainHand();
            ItemStack mainHandStack = betteradventuremode$getMainHand();
            if (!((DuckPlayerEntityMixin)player).betteradventuremode$isMainHandStackSheathed()) {
                cir.setReturnValue(ItemUtils.isUsable(mainHandStack) ? mainHandStack : emptyMainHandStack);
                cir.cancel();
            }
        }
    }

    @Override
    public ItemStack betteradventuremode$getOffHandStack() {
        if (!player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) && !player.isCreative()) {
            ItemStack emptyOffHandStack = betteradventuremode$getEmptyOffHand();
            ItemStack offHandStack = this.offHand.get(0);
            if (!((DuckPlayerEntityMixin)player).betteradventuremode$isOffHandStackSheathed()) {
                return ItemUtils.isUsable(offHandStack) ? offHandStack : emptyOffHandStack;
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     *
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public int getEmptySlot() {
        for (int i = !this.player.isCreative() && !this.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) ? 9 : 0; i < this.main.size(); ++i) {
            if (!this.main.get(i).isEmpty()) continue;
            return i;
        }
        return -1;
    }

    /**
     *
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public int getOccupiedSlotWithRoomForStack(ItemStack stack) {
        if (this.canStackAddMore(this.getStack(this.selectedSlot), stack)) {
            return this.selectedSlot;
        }

        for (int i = !this.player.isCreative() && !this.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) ? 9 : 0; i < this.main.size(); ++i) {
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
    public void damageArmor(DamageSource damageSource, float amount, int[] slots) {
        if (amount <= 0.0f) {
            return;
        }
        // divide by 6 because gloves and shoulders exist
        if ((amount /= 6.0f) < 1.0f) {
            amount = 1.0f;
        }
        float finalAmount = amount;

        int[] var4 = slots;
        int var5 = slots.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            int i = var4[var6];
            ItemStack itemStack = (ItemStack)this.armor.get(i);
            if ((!damageSource.isIn(DamageTypeTags.IS_FIRE) || !itemStack.getItem().isFireproof()) && itemStack.getItem() instanceof ArmorItem) {
                itemStack.damage((int)finalAmount, (LivingEntity)this.player, (Consumer)((player) -> {
                    ((LivingEntity)player).sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i));
                }));
            }
        }

        if (var5 > 1) {
            // armor trinkets
            TrinketsApi.getTrinketComponent(player).ifPresent(trinkets ->
                    trinkets.forEach((slotReference, itemStack) -> {
                        if ((!(damageSource.isIn(DamageTypeTags.IS_FIRE)) || itemStack.getItem().isFireproof()) && itemStack.getItem() instanceof ArmorTrinketItem && ItemUtils.isUsable(itemStack)) {
                            itemStack.damage((int) finalAmount, this.player, player -> TrinketsApi.onTrinketBroken(itemStack, slotReference, player));
                        }
                    }));
        }
    }

    public ItemStack betteradventuremode$getMainHand() {
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

    public ItemStack betteradventuremode$setMainHand(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getMainHand();
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

    public ItemStack betteradventuremode$getAlternativeMainHand() {
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

    public ItemStack betteradventuremode$setAlternativeMainHand(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getAlternativeMainHand();
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

    public ItemStack betteradventuremode$getAlternativeOffHand() {
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

    public ItemStack betteradventuremode$setAlternativeOffHand(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getAlternativeOffHand();
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

    public ItemStack betteradventuremode$getEmptyMainHand() {
        ItemStack emptyMainHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("empty_main_hand") != null) {
                if (trinkets.get().getInventory().get("empty_main_hand").get("empty_main_hand") != null) {
                    emptyMainHandStack = trinkets.get().getInventory().get("empty_main_hand").get("empty_main_hand").getStack(0);
                }
            }
        }
        return emptyMainHandStack;
    }

    public ItemStack betteradventuremode$setEmptyMainHand(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getEmptyMainHand();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("empty_main_hand") != null) {
                if (trinkets.get().getInventory().get("empty_main_hand").get("empty_main_hand") != null) {
                    trinkets.get().getInventory().get("empty_main_hand").get("empty_main_hand").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack betteradventuremode$getEmptyOffHand() {
        ItemStack emptyOffHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("empty_off_hand") != null) {
                if (trinkets.get().getInventory().get("empty_off_hand").get("empty_off_hand") != null) {
                    emptyOffHandStack = trinkets.get().getInventory().get("empty_off_hand").get("empty_off_hand").getStack(0);
                }
            }
        }
        return emptyOffHandStack;
    }

    public ItemStack betteradventuremode$setEmptyOffHand(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getEmptyOffHand();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("empty_off_hand") != null) {
                if (trinkets.get().getInventory().get("empty_off_hand").get("empty_off_hand") != null) {
                    trinkets.get().getInventory().get("empty_off_hand").get("empty_off_hand").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack betteradventuremode$getSheathedMainHand() {
        ItemStack sheathedMainHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("sheathed_main_hand") != null) {
                if (trinkets.get().getInventory().get("sheathed_main_hand").get("sheathed_main_hand") != null) {
                    sheathedMainHandStack = trinkets.get().getInventory().get("sheathed_main_hand").get("sheathed_main_hand").getStack(0);
                }
            }
        }
        return sheathedMainHandStack;
    }

    public ItemStack betteradventuremode$setSheathedMainHand(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getSheathedMainHand();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("sheathed_main_hand") != null) {
                if (trinkets.get().getInventory().get("sheathed_main_hand").get("sheathed_main_hand") != null) {
                    trinkets.get().getInventory().get("sheathed_main_hand").get("sheathed_main_hand").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack betteradventuremode$getSheathedOffHand() {
        ItemStack sheathedOffHandStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("sheathed_off_hand") != null) {
                if (trinkets.get().getInventory().get("sheathed_off_hand").get("sheathed_off_hand") != null) {
                    sheathedOffHandStack = trinkets.get().getInventory().get("sheathed_off_hand").get("sheathed_off_hand").getStack(0);
                }
            }
        }
        return sheathedOffHandStack;
    }

    public ItemStack betteradventuremode$setSheathedOffHand(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getSheathedOffHand();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("sheathed_off_hand") != null) {
                if (trinkets.get().getInventory().get("sheathed_off_hand").get("sheathed_off_hand") != null) {
                    trinkets.get().getInventory().get("sheathed_off_hand").get("sheathed_off_hand").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack betteradventuremode$getGlovesStack() {
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

    public ItemStack betteradventuremode$setGlovesStack(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getGlovesStack();
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

    public ItemStack betteradventuremode$getShouldersStack() {
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

    public ItemStack betteradventuremode$setShouldersStack(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getShouldersStack();
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

    public ItemStack betteradventuremode$getRing1Stack() {
        ItemStack rings1Stack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("rings_1") != null) {
                if (trinkets.get().getInventory().get("rings_1").get("ring") != null) {
                    rings1Stack = trinkets.get().getInventory().get("rings_1").get("ring").getStack(0);
                }
            }
        }
        return rings1Stack;
    }

    public ItemStack betteradventuremode$setRing1Stack(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getRing1Stack();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("rings_1") != null) {
                if (trinkets.get().getInventory().get("rings_1").get("ring") != null) {
                    trinkets.get().getInventory().get("rings_1").get("ring").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack betteradventuremode$getRing2Stack() {
        ItemStack rings2Stack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("rings_2") != null) {
                if (trinkets.get().getInventory().get("rings_2").get("ring") != null) {
                    rings2Stack = trinkets.get().getInventory().get("rings_2").get("ring").getStack(0);
                }
            }
        }
        return rings2Stack;
    }

    public ItemStack betteradventuremode$setRing2Stack(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getRing2Stack();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("rings_2") != null) {
                if (trinkets.get().getInventory().get("rings_2").get("ring") != null) {
                    trinkets.get().getInventory().get("rings_2").get("ring").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack betteradventuremode$getBeltStack() {
        ItemStack beltsStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("belts") != null) {
                if (trinkets.get().getInventory().get("belts").get("belt") != null) {
                    beltsStack = trinkets.get().getInventory().get("belts").get("belt").getStack(0);
                }
            }
        }
        return beltsStack;
    }

    public ItemStack betteradventuremode$setBeltStack(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getBeltStack();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("belts") != null) {
                if (trinkets.get().getInventory().get("belts").get("belt") != null) {
                    trinkets.get().getInventory().get("belts").get("belt").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public ItemStack betteradventuremode$getNecklaceStack() {
        ItemStack necklacesStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("necklaces") != null) {
                if (trinkets.get().getInventory().get("necklaces").get("necklace") != null) {
                    necklacesStack = trinkets.get().getInventory().get("necklaces").get("necklace").getStack(0);
                }
            }
        }
        return necklacesStack;
    }

    public ItemStack betteradventuremode$setNecklaceStack(ItemStack itemStack) {
        ItemStack oldStack = betteradventuremode$getNecklaceStack();
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("necklaces") != null) {
                if (trinkets.get().getInventory().get("necklaces").get("necklace") != null) {
                    trinkets.get().getInventory().get("necklaces").get("necklace").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }


    public ItemStack betteradventuremode$getSpellSlotStack(int spellSlotNumber) {
        ItemStack spellSlotStack = ItemStack.EMPTY;
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber) != null) {
                if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell") != null) {
                    spellSlotStack = trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell").getStack(0);
                }
            }
        }
        return spellSlotStack;
    }

    public ItemStack betteradventuremode$setSpellSlotStack(ItemStack itemStack, int spellSlotNumber) {
        ItemStack oldStack = betteradventuremode$getSpellSlotStack(spellSlotNumber);
        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber) != null) {
                if (trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell") != null) {
                    trinkets.get().getInventory().get("spell_slot_" + spellSlotNumber).get("spell").setStack(0, itemStack);
                }
            }
        }
        return oldStack;
    }

    public List<ItemStack> betteradventuremode$getArmor() {
        List<ItemStack> list = new java.util.ArrayList<>(List.of(this.betteradventuremode$getGlovesStack(), this.betteradventuremode$getShouldersStack()));
        list.addAll(this.armor);
        return list;
    }
}
