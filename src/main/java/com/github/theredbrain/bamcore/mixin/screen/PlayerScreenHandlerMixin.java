package com.github.theredbrain.bamcore.mixin.screen;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreStatusEffects;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.mojang.datafixers.util.Pair;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerScreenHandler.class, priority = 950) // is applied before Trinkets
public abstract class PlayerScreenHandlerMixin extends ScreenHandler implements TrinketPlayerScreenHandler {
    @Shadow
    @Final
    private static EquipmentSlot[] EQUIPMENT_SLOT_ORDER;
    @Shadow
    @Final
    static Identifier[] EMPTY_ARMOR_SLOT_TEXTURES;
//    @Shadow
//    @Final
//    @Mutable
//    public static Identifier EMPTY_HELMET_SLOT_TEXTURE = BetterAdventureModeCore.identifier("item/empty_armor_slot_helmet");
//    @Shadow
//    @Final
//    @Mutable
//    public static Identifier EMPTY_CHESTPLATE_SLOT_TEXTURE = BetterAdventureModeCore.identifier("item/empty_armor_slot_chestplate");
//    @Shadow
//    @Final
//    @Mutable
//    public static Identifier EMPTY_LEGGINGS_SLOT_TEXTURE = BetterAdventureModeCore.identifier("item/empty_armor_slot_leggings");
//    @Shadow
//    @Final
//    @Mutable
//    public static Identifier EMPTY_BOOTS_SLOT_TEXTURE = new Identifier("item/empty_armor_slot_boots");
//    @Shadow
//    @Final
//    @Mutable
//    public static Identifier EMPTY_OFFHAND_ARMOR_SLOT = new Identifier("item/empty_armor_slot_shield");
    @Shadow
    static void onEquipStack(PlayerEntity player, EquipmentSlot slot, ItemStack newStack, ItemStack currentStack) {
        throw new AssertionError();
    }

    public PlayerScreenHandlerMixin() {
        super(null, 0);
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void PlayerScreenHandler(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {

        // this adds a copy of the 4 equipment slots whose behaviour is controlled by status effects and item tags
        int i;
        for (i = 0; i < 4; ++i) {
            final EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[i];
            this.addSlot(new Slot(inventory, 39 - i, 8, 8 + i * 18){

                @Override
                public void setStack(ItemStack stack) {
                    onEquipStack(owner, equipmentSlot, stack, this.getStack());
                    super.setStack(stack);
                }

                @Override
                public int getMaxItemCount() {
                    return 1;
                }

                @Override
                public boolean canInsert(ItemStack stack) {
                    return equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack) && (owner.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
                }

                @Override
                public boolean canTakeItems(PlayerEntity playerEntity) {
                    ItemStack itemStack = this.getStack();
                    if (!itemStack.isEmpty() && !playerEntity.isCreative() && EnchantmentHelper.hasBindingCurse(itemStack)) {
                        return false;
                    }
                    return super.canTakeItems(playerEntity) && (owner.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT) || !(((DuckPlayerEntityMixin)owner).bamcore$isAdventure()));
                }

                @Override
                public Pair<Identifier, Identifier> getBackgroundSprite() {
                    return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, EMPTY_ARMOR_SLOT_TEXTURES[equipmentSlot.getEntitySlotId()]);
                }
            });
        }
    }
}
