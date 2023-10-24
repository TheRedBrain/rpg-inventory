package com.github.theredbrain.bamcore.entity.player;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Optional;

public interface DuckPlayerInventoryMixin {

    List<ItemStack> getArmorTrinkets();

    /**
     *
     * @return
     */
    ItemStack bamcore$getOffHandStack();

    // these methods get/set the ItemStacks in the TrinketSlot inventories
    ItemStack bamcore$getEmptyMainHand();
    ItemStack bamcore$setEmptyMainHand(ItemStack itemStack);
    ItemStack bamcore$getEmptyOffHand();
    ItemStack bamcore$setEmptyOffHand(ItemStack itemStack);
    ItemStack bamcore$getMainHand();
    ItemStack bamcore$setMainHand(ItemStack itemStack);
    ItemStack bamcore$getOffHand();
    ItemStack bamcore$setOffHand(ItemStack itemStack);
    ItemStack bamcore$getAlternativeMainHand();
    ItemStack bamcore$setAlternativeMainHand(ItemStack itemStack);
    ItemStack bamcore$getAlternativeOffHand();
    ItemStack bamcore$setAlternativeOffHand(ItemStack itemStack);
    ItemStack bamcore$getHeadStack();
    ItemStack bamcore$setHeadStack(ItemStack itemStack);
    ItemStack bamcore$getChestStack();
    ItemStack bamcore$setChestStack(ItemStack itemStack);
    ItemStack bamcore$getLegStack();
    ItemStack bamcore$setLegStack(ItemStack itemStack);
    ItemStack bamcore$getFeetStack();
    ItemStack bamcore$setFeetStack(ItemStack itemStack);
    ItemStack bamcore$getSpellSlotStack(int spellSlotNumber);
    ItemStack bamcore$setSpellSlotStack(ItemStack itemStack, int spellSlotNumber);
}
