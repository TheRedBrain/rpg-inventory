package com.github.theredbrain.rpginventory.mixin.trinkets;
//
//import com.github.theredbrain.rpginventory.RPGInventory;
//import com.github.theredbrain.rpginventory.screen.DuckPlayerScreenHandlerMixin;
//import com.github.theredbrain.rpginventory.util.ItemUtils;
//import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
//import dev.emi.trinkets.SurvivalTrinketSlot;
//import dev.emi.trinkets.api.SlotGroup;
//import dev.emi.trinkets.api.SlotType;
//import dev.emi.trinkets.api.TrinketInventory;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.ArrayList;
//import java.util.List;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.Container;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.ItemStack;
//
//@Mixin(value = SurvivalTrinketSlot.class)
//public abstract class SurvivalTrinketSlotMixin extends Slot {
//
//	@Shadow(remap = false)
//	@Final
//	private TrinketInventory trinketInventory;
//
//	@Shadow(remap = false)
//	@Final
//	private boolean alwaysVisible;
//
//	public SurvivalTrinketSlotMixin(Container inventory, int index, int x, int y) {
//		super(inventory, index, x, y);
//	}
//
//	@Inject(method = "<init>", at = @At("TAIL"), remap = false)
//	public void SurvivalTrinketSlot(TrinketInventory inventory, int index, int x, int y, SlotGroup group, SlotType type, int slotOffset, boolean alwaysVisible, CallbackInfo ci) {
//		addSlotTooltip(this, group.getName(), type.getName());
//	}
//
//	@Unique
//	private static void addSlotTooltip(Slot slot, String groupName, String slotName) {
//		List<Component> list = new ArrayList<>();
//		Component text = Component.translatable("slot.tooltip." + groupName + "." + slotName);
//		if (!text.getString().isEmpty()) {
//			list.add(text);
//			((SlotCustomization) slot).slotcustomizationapi$setSlotTooltipText(list);
//		}
//	}
//
//	@Inject(method = "canInsert", at = @At("RETURN"), cancellable = true)
//	public void rpginventory$canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
//
//		LivingEntity livingEntity = trinketInventory.getComponent().getEntity();
//		boolean bl = false;
//		boolean isOwned = true;
//		if (livingEntity instanceof Player playerEntity) {
//			bl = playerEntity.isCreative();
//			isOwned = ItemUtils.isUsableByPlayer(stack, playerEntity);
//		}
//
//		cir.setReturnValue(cir.getReturnValue() && isOwned && (livingEntity.hasEffect(RPGInventory.CIVILISATION) || bl || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !livingEntity.hasEffect(RPGInventory.WILDERNESS))));
//	}
//
//	/**
//	 * @author TheRedBrain
//	 */
//	@Inject(method = "canTakeItems", at = @At("RETURN"), cancellable = true)
//	public void rpginventory$canTakeItems(Player player, CallbackInfoReturnable<Boolean> cir) {
//		cir.setReturnValue(cir.getReturnValue() && (player.hasEffect(RPGInventory.CIVILISATION) || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !player.hasEffect(RPGInventory.WILDERNESS)) || player.isCreative()));
//	}
//
//	@Inject(method = "isEnabled", at = @At(value = "HEAD"), cancellable = true)
//	public void rpginventory$isEnabled_checkForRecipeBook(CallbackInfoReturnable<Boolean> cir) {
//		if (alwaysVisible && x < 0 && trinketInventory.getComponent().getEntity() instanceof Player player) {
//			if (player.containerMenu instanceof DuckPlayerScreenHandlerMixin playerScreenHandler && playerScreenHandler.rpginventory$isAttributeScreenVisible()) {
//				cir.setReturnValue(false);
//				cir.cancel();
//			}
//		}
//	}
//}
