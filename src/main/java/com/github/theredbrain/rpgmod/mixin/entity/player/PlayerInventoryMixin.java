package com.github.theredbrain.rpgmod.mixin.entity.player;

import com.github.theredbrain.rpgmod.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgmod.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpgmod.item.CustomArmorItem;
import com.github.theredbrain.rpgmod.item.CustomDyeableArmorItem;
import com.github.theredbrain.rpgmod.registry.ItemRegistry;
import com.github.theredbrain.rpgmod.registry.StatusEffectsRegistry;
import com.github.theredbrain.rpgmod.registry.Tags;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements DuckPlayerInventoryMixin {

    @Shadow
    @Final
    @Mutable
    public static int[] ARMOR_SLOTS;//ARMOR_SLOTS;// = new int[]{0, 1, 2, 3};

    @Shadow
    @Final
    public DefaultedList<ItemStack> main;

    @Shadow
    @Final
    @Mutable
    public DefaultedList<ItemStack> armor;// = DefaultedList.ofSize(10, ItemStack.EMPTY);//this.newArmourInventory();

    @Shadow
    @Final
    public DefaultedList<ItemStack> offHand;

    @Shadow
    @Final
    @Mutable
    private List<DefaultedList<ItemStack>> combinedInventory;// = ImmutableList.of(this.main, this.armor, this.offHand, this.mainHand, this.alternativeMainHand, this.alternativeOffHand);//this.newCombinedInventory();

    @Shadow
    @Final
    public PlayerEntity player;

    @Shadow
    private boolean canStackAddMore(ItemStack existingStack, ItemStack stack) {
        throw new AssertionError();
    }

    @Shadow public abstract boolean insertStack(ItemStack stack);

    private DefaultedList<ItemStack> accessories;// = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> mainHand;// = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> alternativeMainHand;// = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> alternativeOffHand;// = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> emptyMainHand;// = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> emptyOffHand;// = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> playerSkinItem;// = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> mount;// = DefaultedList.ofSize(1, ItemStack.EMPTY);

    /**
     * @author TheRedBrain
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void PlayerInventory(PlayerEntity player, CallbackInfo ci) {
        ARMOR_SLOTS = new int[]{0, 1, 2, 3, 4, 5};
        this.armor = DefaultedList.ofSize(6, ItemStack.EMPTY);
        this.accessories = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.mainHand = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.alternativeMainHand = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.alternativeOffHand = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.emptyMainHand = DefaultedList.ofSize(1, ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
        this.emptyOffHand = DefaultedList.ofSize(1, ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
        this.playerSkinItem = DefaultedList.ofSize(1, ItemRegistry.NO_LEGS_PLAYER_SKIN_ARMOR_ITEM.getDefaultStack());
        this.mount = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.combinedInventory = ImmutableList.of(this.main, this.armor, this.accessories, this.mainHand, this.offHand, this.alternativeMainHand, this.alternativeOffHand);
    }

    @Inject(method = "getMainHandStack", at = @At("HEAD"), cancellable = true)
    public void bam$getMainHandStack(CallbackInfoReturnable<ItemStack> cir) {
        if (!player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) && !player.isCreative()) {
            ItemStack stack = this.mainHand.get(0);
            cir.setReturnValue(stack.isEmpty() ? this.emptyMainHand.get(0) : stack);
            cir.cancel();
        }
    }

    /**
     * call this to set this player's internal unarmed weapon
     * @param itemStack is the default itemStack used when the main hand is empty
     */
    public void setEmptyMainHandStack(ItemStack itemStack) {
        this.emptyMainHand.set(0, itemStack);
    }

    /**
     * call this to set this player's internal unarmed weapon
     * @param itemStack is the default itemStack used when the off hand is empty
     */
    public void setEmptyOffHandStack(ItemStack itemStack) {
        this.emptyOffHand.set(0, itemStack);
    }

//    /**
//     * @author TheRedBrain
//     */
//    @Overwrite
//    public ItemStack getMainHandStack() {
//        ItemStack stack;
//        if (!player.hasStatusEffect(StatusEffectsRegistry.CIVILISATION_EFFECT)
//                && ((DuckPlayerEntityMixin)player).isAdventure()
//                && ((DuckPlayerEntityMixin)player).isAlternativeMainHandUsed()) {
//            stack = this.bam$getAlternativeMainHand();
//        } else {
//            stack = this.mainHand.get(0);
//        }
//        return stack.isEmpty() ? this.emptyOffHand.get(0) : stack;
//    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "getOccupiedSlotWithRoomForStack", at = @At("HEAD"), cancellable = true)
    public void bam$getOccupiedSlotWithRoomForStack(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (((DuckPlayerEntityMixin)this.player).isAdventure() && !this.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {

            for (int i = 0; i < this.main.size(); ++i) {
                if (!this.canStackAddMore(this.main.get(i), stack) || (i < 9 && !stack.isIn(Tags.ADVENTURE_HOTBAR_ITEMS))) continue;
                cir.setReturnValue(i);
                cir.cancel();
            }
            cir.setReturnValue(-1);
            cir.cancel();
        }
    }

    /**
     * @author TheRedBrain
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
        for (i = 0; i < this.accessories.size(); ++i) {
            if (this.accessories.get(i).isEmpty()) continue;
            nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(i + 130));
            this.accessories.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        for (i = 0; i < this.mainHand.size(); ++i) {
            if (this.mainHand.get(i).isEmpty()) continue;
            nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(i + 150));
            this.mainHand.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        for (i = 0; i < this.offHand.size(); ++i) {
            if (this.offHand.get(i).isEmpty()) continue;
            nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(i + 160));
            this.offHand.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        for (i = 0; i < this.alternativeMainHand.size(); ++i) {
            if (this.alternativeMainHand.get(i).isEmpty()) continue;
            nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(i + 165));
            this.alternativeMainHand.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        for (i = 0; i < this.alternativeOffHand.size(); ++i) {
            if (this.alternativeOffHand.get(i).isEmpty()) continue;
            nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(i + 170));
            this.alternativeOffHand.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        return nbtList;
    }

    /**
     * @author TheRedBrain
     */
    @Overwrite
    public void readNbt(NbtList nbtList) {
        this.main.clear();
        this.armor.clear();
        this.accessories.clear();
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
            if (j >= 130 && j < this.accessories.size() + 130) {
                this.accessories.set(j - 130, itemStack);
                continue;
            }
            if (j >= 150 && j < this.mainHand.size() + 150) {
                this.mainHand.set(j - 150, itemStack);
                continue;
            }
            if (j >= 160 && j < this.offHand.size() + 160) {
                this.offHand.set(j - 160, itemStack);
                continue;
            }
            if (j >= 165 && j < this.alternativeMainHand.size() + 165) {
                this.alternativeMainHand.set(j - 165, itemStack);
                continue;
            }
            if (j >= 170 && j < this.alternativeOffHand.size() + 170) {
                this.alternativeOffHand.set(j - 170, itemStack);
            }
        }
    }

    @Inject(method = "size", at = @At("RETURN"), cancellable = true)
    public void bam$size(CallbackInfoReturnable<Integer> cir) {
        int oldSize = cir.getReturnValue();
        cir.setReturnValue(oldSize + this.accessories.size() + this.mainHand.size() + this.alternativeOffHand.size() + this.alternativeMainHand.size() + this.emptyMainHand.size() + this.emptyOffHand.size() + this.playerSkinItem.size() + this.mount.size());
    }

    @Inject(method = "isEmpty", at = @At("TAIL"), cancellable = true)
    public void bam$isEmpty(CallbackInfoReturnable<Boolean> cir) {
        for (ItemStack itemStack : this.accessories) {
            if (itemStack.isEmpty()) continue;
            cir.setReturnValue(false);
            cir.cancel();
        }
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
        for (ItemStack itemStack : this.playerSkinItem) {
            if (itemStack.isEmpty()) continue;
            cir.setReturnValue(false);
            cir.cancel();
        }
        for (ItemStack itemStack : this.mount) {
            if (itemStack.isEmpty()) continue;
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    /**
     * @author TheRedBrain
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
            if (damageSource.isFire() && itemStack.getItem().isFireproof() || !(itemStack.getItem() instanceof ArmorItem)) continue;
            if ((itemStack.getItem() instanceof CustomArmorItem && !(((CustomArmorItem)itemStack.getItem()).isProtecting(itemStack))) || (itemStack.getItem() instanceof CustomDyeableArmorItem && !(((CustomDyeableArmorItem)itemStack.getItem()).isProtecting(itemStack))))  continue;
            itemStack.damage((int)amount, this.player, player -> player.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i)));
        }
    }

    public ItemStack bam$getOffHandStack() {
        ItemStack stack = this.offHand.get(0);
        return stack.isEmpty() && !player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) && !player.isCreative() ? this.emptyOffHand.get(0) : stack;
    }

    public DefaultedList<ItemStack> bam$getAccessories(int slot) {
        return this.accessories;
    }

    public ItemStack bam$getAccessorySlot(int slot) {
        return this.accessories.get(slot);
    }

    public ItemStack bam$setAccessorySlot(int slot, ItemStack itemStack) {
        ItemStack oldStack = bam$getMainHand();
        this.mainHand.set(slot, itemStack);
        return oldStack;
    }

    public ItemStack bam$getMainHand() {
        return this.mainHand.get(0);
    }

    public ItemStack bam$setMainHand(ItemStack itemStack) {
        ItemStack oldStack = bam$getMainHand();
        this.mainHand.set(0, itemStack);
        return oldStack;
    }

    public ItemStack bam$getAlternativeMainHand() {
        return this.alternativeMainHand.get(0);
    }

    public ItemStack bam$setAlternativeMainHand(ItemStack itemStack) {
        ItemStack oldStack = bam$getAlternativeMainHand();
        this.alternativeMainHand.set(0, itemStack);
        return oldStack;
    }

    public ItemStack bam$getAlternativeOffHand() {
        return this.alternativeOffHand.get(0);
    }

    public ItemStack bam$setAlternativeOffHand(ItemStack itemStack) {
        ItemStack oldStack = bam$getAlternativeOffHand();
        this.alternativeOffHand.set(0, itemStack);
        return oldStack;
    }

    public ItemStack bam$getPlayerSkinItem() {
        return this.playerSkinItem.get(0);
    }

    public ItemStack bam$setPlayerSkinItem(ItemStack itemStack) {
        ItemStack oldStack = bam$getPlayerSkinItem();
        this.playerSkinItem.set(0, itemStack);
        return oldStack;
    }
}
