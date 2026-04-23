package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = {"net/minecraft/screen/PlayerScreenHandler$1"})
public abstract class PlayerScreenHandlerOffHandSlotMixin extends Slot {

	@Shadow
	@Final
	Player field_39410;

	public PlayerScreenHandlerOffHandSlotMixin(Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean isActive() {
		return !RPGInventory.isHandSlotOverhaulActive() || !((DuckPlayerEntityMixin) this.field_39410).rpginventory$isOffhandStackSheathed();
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

		boolean handSlotOverhaulIsInactive = !RPGInventory.isHandSlotOverhaulActive();

		return (EquipmentSlot.OFFHAND == this.field_39410.getEquipmentSlotForItem(stack) || stack.is(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || handSlotOverhaulIsInactive) && (handSlotOverhaulIsInactive || !((DuckPlayerEntityMixin) this.field_39410).rpginventory$isOffhandStackSheathed()) && ItemUtils.isUsableByPlayer(stack, this.field_39410) && (stack.has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || this.field_39410.hasEffect(RPGInventory.CIVILISATION) || this.field_39410.isCreative() || (serverConfig.allow_equipment_changes.get() && !this.field_39410.hasEffect(RPGInventory.WILDERNESS)));
	}

	@Override
	public boolean mayPickup(Player playerEntity) {
		return !this.getItem().has(RPGInventory.LOAD_OUT_ITEM) && (this.getItem().has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || playerEntity.hasEffect(RPGInventory.CIVILISATION) || playerEntity.isCreative() || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !playerEntity.hasEffect(RPGInventory.WILDERNESS)));
	}
}
