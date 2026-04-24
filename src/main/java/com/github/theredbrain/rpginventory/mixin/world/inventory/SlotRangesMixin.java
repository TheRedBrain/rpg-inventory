package com.github.theredbrain.rpginventory.mixin.world.inventory;

import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import net.minecraft.world.inventory.SlotRange;
import net.minecraft.world.inventory.SlotRanges;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(SlotRanges.class)
public class SlotRangesMixin {

	@Shadow
	@Final
	private static List<SlotRange> SLOTS;

	@Shadow
	private static void addSingleSlot(List<SlotRange> output, String name, int id) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	static {
		int id = ExtendedEquipmentSlot.BELT.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.belt", id);

		id = ExtendedEquipmentSlot.GLOVES.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.gloves", id);

		id = ExtendedEquipmentSlot.NECKLACE.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.necklace", id);

		id = ExtendedEquipmentSlot.RING_1.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.ring_1", id);

		id = ExtendedEquipmentSlot.RING_2.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.ring_2", id);

		id = ExtendedEquipmentSlot.SHOULDERS.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.shoulders", id);

		id = ExtendedEquipmentSlot.SPELL_1.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.spell_1", id);

		id = ExtendedEquipmentSlot.SPELL_2.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.spell_2", id);

		id = ExtendedEquipmentSlot.SPELL_3.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.spell_3", id);

		id = ExtendedEquipmentSlot.SPELL_4.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.spell_4", id);

		id = ExtendedEquipmentSlot.SPELL_5.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.spell_5", id);

		id = ExtendedEquipmentSlot.SPELL_6.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.spell_6", id);

		id = ExtendedEquipmentSlot.SPELL_7.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.spell_7", id);

		id = ExtendedEquipmentSlot.SPELL_8.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.spell_8", id);

		id = ExtendedEquipmentSlot.RELIC.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.relic", id);

		id = ExtendedEquipmentSlot.CLASS_ITEM.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.class_item", id);

		id = ExtendedEquipmentSlot.SHEATHED_HAND.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.sheathed_hand", id);

		id = ExtendedEquipmentSlot.SHEATHED_OFF_HAND.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.sheathed_off_hand", id);

		id = ExtendedEquipmentSlot.ALTERNATIVE_HAND.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.alternative_hand", id);

		id = ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND.getIndex(1000);
		addSingleSlot(SLOTS, "rpginventory.alternative_off_hand", id);
	}
}
