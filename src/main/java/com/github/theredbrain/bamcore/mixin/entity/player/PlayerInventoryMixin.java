package com.github.theredbrain.bamcore.mixin.entity.player;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.bamcore.api.item.CustomArmorItem;
import com.github.theredbrain.bamcore.api.item.CustomDyeableArmorItem;
import com.github.theredbrain.bamcore.util.ItemUtils;
import com.github.theredbrain.bamcore.registry.ItemRegistry;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements DuckPlayerInventoryMixin {

//    @Shadow
//    @Final
//    @Mutable
//    public static int[] ARMOR_SLOTS;

    @Shadow
    @Final
    public DefaultedList<ItemStack> main;

    @Shadow
    @Final
    @Mutable
    public DefaultedList<ItemStack> armor;

    @Shadow
    @Final
    public DefaultedList<ItemStack> offHand;

    @Shadow
    @Final
    @Mutable
    private List<DefaultedList<ItemStack>> combinedInventory;

    @Shadow
    @Final
    public PlayerEntity player;

    @Shadow
    private boolean canStackAddMore(ItemStack existingStack, ItemStack stack) {
        throw new AssertionError();
    }

//    @Shadow public abstract boolean insertStack(ItemStack stack);

    @Shadow public int selectedSlot;

    @Shadow public abstract ItemStack getStack(int slot);

    private DefaultedList<ItemStack> mainHand;// = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> alternativeMainHand;// = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> alternativeOffHand;// = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> emptyMainHand;// = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> emptyOffHand;// = DefaultedList.ofSize(1, ItemStack.EMPTY);

    /**
     * @author TheRedBrain
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void PlayerInventory(PlayerEntity player, CallbackInfo ci) {
        this.mainHand = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.alternativeMainHand = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.alternativeOffHand = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.emptyMainHand = DefaultedList.ofSize(1, ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
        this.emptyOffHand = DefaultedList.ofSize(1, ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
        this.combinedInventory = ImmutableList.of(this.main, this.armor, this.mainHand, this.offHand, this.alternativeMainHand, this.alternativeOffHand);
    }

    @Inject(method = "getMainHandStack", at = @At("HEAD"), cancellable = true)
    public void bamcore$getMainHandStack(CallbackInfoReturnable<ItemStack> cir) {
        if (!player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) && !player.isCreative()) {
            ItemStack stack = ItemStack.EMPTY;
            if (!player.hasStatusEffect(StatusEffectsRegistry.WEAPONS_SHEATHED_EFFECT)) {
                ItemStack stack2 = this.mainHand.get(0);
                if (ItemUtils.isUsable(stack2)) stack = stack2;
            } else if (PlayerInventory.isValidHotbarIndex(this.selectedSlot)) {
                stack = this.main.get(this.selectedSlot);
            }
            boolean bl = stack.isEmpty()/* || !ItemUtils.isUsable(stack)*/;
            cir.setReturnValue(bl ? this.emptyMainHand.get(0) : stack);
            cir.cancel();
        }
    }

    @Override
    public ItemStack bamcore$getOffHandStack() {
        ItemStack stack = this.offHand.get(0);
        boolean bl = !player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)
                && !player.isCreative()
                && (player.hasStatusEffect(StatusEffectsRegistry.WEAPONS_SHEATHED_EFFECT) || !ItemUtils.isUsable(stack));
        return bl ? this.emptyMainHand.get(0) : stack;
    }

    /**
     * call this to set this player's internal unarmed weapon
     * @param itemStack is the default itemStack used when the main hand is empty
     */
    @Override
    public void bamcore$setEmptyMainHandStack(ItemStack itemStack) {
        this.emptyMainHand.set(0, itemStack);
    }

    /**
     * call this to set this player's internal unarmed weapon
     * @param itemStack is the default itemStack used when the offhand is empty
     */
    @Override
    public void bamcore$setEmptyOffHandStack(ItemStack itemStack) {
        this.emptyOffHand.set(0, itemStack);
    }

//    /**
//     * @author TheRedBrain
//     * @reason TODO
//     */
//    @Inject(method = "getOccupiedSlotWithRoomForStack", at = @At("HEAD"), cancellable = true)
//    public void bam$getOccupiedSlotWithRoomForStack(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
//        if (((DuckPlayerEntityMixin)this.player).isAdventure() && !this.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
//
//            for (int i = 0; i < this.main.size(); ++i) {
//                if (!this.canStackAddMore(this.main.get(i), stack) || (i < 9 && !stack.isIn(Tags.ADVENTURE_HOTBAR_ITEMS))) continue;
//                cir.setReturnValue(i);
//                cir.cancel();
//            }
//            cir.setReturnValue(-1);
//            cir.cancel();
//        }
//    }

    /**
     *
     * @author TheRedBrain
     * @reason TODO
     */
    @Overwrite
    public int getEmptySlot() {
        for (int i = ((DuckPlayerEntityMixin)this.player).bamcore$isAdventure() && !this.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) ? 9 : 0; i < this.main.size(); ++i) {
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

        for (int i = ((DuckPlayerEntityMixin)this.player).bamcore$isAdventure() && !this.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) ? 9 : 0; i < this.main.size(); ++i) {
            if (!this.canStackAddMore(this.main.get(i), stack)) continue;
            return i;
        }
        return -1;
    }

    /**
     * @author TheRedBrain
     * @reason convenience, might redo later
     */
    @Overwrite
    public NbtList writeNbt(NbtList nbtList) {
        NbtCompound nbtCompound;
        int i;
        for (i = 0; i < this.main.size(); ++i) {
            if (this.main.get(i).isEmpty()) continue;
            nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)i);
            this.main.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        for (i = 0; i < this.armor.size(); ++i) {
            if (this.armor.get(i).isEmpty()) continue;
            nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(i + 100));
            this.armor.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        for (i = 0; i < this.mainHand.size(); ++i) {
            if (this.mainHand.get(i).isEmpty()) continue;
            nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(i + 130));
            this.mainHand.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        for (i = 0; i < this.offHand.size(); ++i) {
            if (this.offHand.get(i).isEmpty()) continue;
            nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(i + 140));
            this.offHand.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        for (i = 0; i < this.alternativeMainHand.size(); ++i) {
            if (this.alternativeMainHand.get(i).isEmpty()) continue;
            nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(i + 145));
            this.alternativeMainHand.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        for (i = 0; i < this.alternativeOffHand.size(); ++i) {
            if (this.alternativeOffHand.get(i).isEmpty()) continue;
            nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(i + 150));
            this.alternativeOffHand.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        return nbtList;
    }

    /**
     * @author TheRedBrain
     * @reason convenience, might redo later
     */
    @Overwrite
    public void readNbt(NbtList nbtList) {
        this.main.clear();
        this.armor.clear();
        this.offHand.clear();
        this.mainHand.clear();
        this.alternativeOffHand.clear();
        this.alternativeMainHand.clear();
        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 0xFF;
            ItemStack itemStack = ItemStack.fromNbt(nbtCompound);
            if (itemStack.isEmpty()) continue;
            if (j >= 0 && j < this.main.size()) {
                this.main.set(j, itemStack);
                continue;
            }
            if (j >= 100 && j < this.armor.size() + 100) {
                this.armor.set(j - 100, itemStack);
                continue;
            }
            if (j >= 130 && j < this.mainHand.size() + 130) {
                this.mainHand.set(j - 130, itemStack);
                continue;
            }
            if (j >= 140 && j < this.offHand.size() + 140) {
                this.offHand.set(j - 140, itemStack);
                continue;
            }
            if (j >= 145 && j < this.alternativeMainHand.size() + 145) {
                this.alternativeMainHand.set(j - 145, itemStack);
                continue;
            }
            if (j >= 150 && j < this.alternativeOffHand.size() + 150) {
                this.alternativeOffHand.set(j - 150, itemStack);
            }
        }
    }

    @Inject(method = "size", at = @At("RETURN"), cancellable = true)
    public void bam$size(CallbackInfoReturnable<Integer> cir) {
        int oldSize = cir.getReturnValue();
        cir.setReturnValue(oldSize + this.mainHand.size() + this.alternativeOffHand.size() + this.alternativeMainHand.size() + this.emptyMainHand.size() + this.emptyOffHand.size());
    }

    @Inject(method = "isEmpty", at = @At("TAIL"), cancellable = true)
    public void bam$isEmpty(CallbackInfoReturnable<Boolean> cir) {
        for (ItemStack itemStack : this.mainHand) {
            if (itemStack.isEmpty()) continue;
            cir.setReturnValue(false);
            cir.cancel();
        }
        for (ItemStack itemStack : this.alternativeMainHand) {
            if (itemStack.isEmpty()) continue;
            cir.setReturnValue(false);
            cir.cancel();
        }
        for (ItemStack itemStack : this.alternativeOffHand) {
            if (itemStack.isEmpty()) continue;
            cir.setReturnValue(false);
            cir.cancel();
        }
        for (ItemStack itemStack : this.emptyMainHand) {
            if (itemStack.isEmpty()) continue;
            cir.setReturnValue(false);
            cir.cancel();
        }
        for (ItemStack itemStack : this.emptyOffHand) {
            if (itemStack.isEmpty()) continue;
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    /**
     * @author TheRedBrain
     * @reason convenience, might redo later
     */
    @Overwrite
    public void damageArmor(DamageSource damageSource, float amount, int[] slots) {
        if (amount <= 0.0f) {
            return;
        }
        if ((amount /= 6.0f) < 1.0f) {
            amount = 1.0f;
        }
        for (int i : slots) {
            ItemStack itemStack = this.armor.get(i);
            if (damageSource.isIn(DamageTypeTags.IS_FIRE) && itemStack.getItem().isFireproof() || !(itemStack.getItem() instanceof ArmorItem)) continue;
            if ((itemStack.getItem() instanceof CustomArmorItem && !(((CustomArmorItem)itemStack.getItem()).isProtecting(itemStack))) || (itemStack.getItem() instanceof CustomDyeableArmorItem && !(((CustomDyeableArmorItem)itemStack.getItem()).isProtecting(itemStack))))  continue;
            itemStack.damage((int)amount, this.player, player -> player.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i)));
        }
    }

    public ItemStack bamcore$getMainHand() {
        return this.mainHand.get(0);
    }

    public ItemStack bamcore$setMainHand(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getMainHand();
        this.mainHand.set(0, itemStack);
        return oldStack;
    }

    public ItemStack bamcore$getAlternativeMainHand() {
        return this.alternativeMainHand.get(0);
    }

    public ItemStack bamcore$setAlternativeMainHand(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getAlternativeMainHand();
        this.alternativeMainHand.set(0, itemStack);
        return oldStack;
    }

    public ItemStack bamcore$getAlternativeOffHand() {
        return this.alternativeOffHand.get(0);
    }

    public ItemStack bamcore$setAlternativeOffHand(ItemStack itemStack) {
        ItemStack oldStack = bamcore$getAlternativeOffHand();
        this.alternativeOffHand.set(0, itemStack);
        return oldStack;
    }
}
