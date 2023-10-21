package com.github.theredbrain.bamcore.mixin.screen.slot;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreStatusEffects;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.screen.slot.BetterAdventureModeSlotExtension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin implements BetterAdventureModeSlotExtension {

    @Shadow @Final public Inventory inventory;
    @Unique
    private boolean bamcore$canAlwaysTakeItem = true;

    @Unique
    private boolean bamcore$canAlwaysInsertItem = true;

    @Unique
    private TagKey<Item> bamcore$insertOnlyItemsOfTag = null;

    @Override
    public void bamcore$setTakeItemOverride(boolean canAlwaysTakeItem) {
        this.bamcore$canAlwaysTakeItem = canAlwaysTakeItem;
    }

    @Override
    public boolean bamcore$getTakeItemOverride() {
        return bamcore$canAlwaysTakeItem;
    }

    @Override
    public void bamcore$setInsertItemOverride(boolean canAlwaysInsertItem) {
        this.bamcore$canAlwaysInsertItem = canAlwaysInsertItem;
    }

    @Override
    public boolean bamcore$getInsertItemOverride() {
        return bamcore$canAlwaysInsertItem;
    }

    @Override
    public TagKey<Item> bamcore$getInsertOnlyItemsOfTag() {
        return bamcore$insertOnlyItemsOfTag;
    }

    @Override
    public void bamcore$setInsertOnlyItemsOfTag(TagKey<Item> bamcore$insertOnlyItemsOfTag) {
        this.bamcore$insertOnlyItemsOfTag = bamcore$insertOnlyItemsOfTag;
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "canInsert", at = @At("RETURN"), cancellable = true)
    public void bamcore$canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = true;
        boolean itemStackIsOfTag = this.bamcore$insertOnlyItemsOfTag == null || stack.isIn(this.bamcore$insertOnlyItemsOfTag);
        if (inventory instanceof PlayerInventory playerInventory) {
            PlayerEntity player = playerInventory.player;
            bl = !(((DuckPlayerEntityMixin) player).bamcore$isAdventure()) || player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT);
        }
        BetterAdventureModeCore.LOGGER.info("canInsertItem: " + bl);
        if (this.bamcore$insertOnlyItemsOfTag != null) {
            BetterAdventureModeCore.LOGGER.info("itemTag: " + this.bamcore$insertOnlyItemsOfTag);
        }
        cir.setReturnValue(cir.getReturnValue() && (this.bamcore$canAlwaysInsertItem || (/*TODO config || */bl || itemStackIsOfTag)));
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "canTakeItems", at = @At("RETURN"), cancellable = true)
    public void bamcore$canTakeItems(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = !(((DuckPlayerEntityMixin) player).bamcore$isAdventure()) || player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.CIVILISATION_EFFECT);
        BetterAdventureModeCore.LOGGER.info("canTakeItem: " + bl);
        cir.setReturnValue(cir.getReturnValue() && (this.bamcore$canAlwaysTakeItem || (/*TODO config || */bl)));
    }
}
