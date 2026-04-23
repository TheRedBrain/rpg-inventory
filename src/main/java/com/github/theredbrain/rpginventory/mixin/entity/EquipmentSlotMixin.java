package com.github.theredbrain.rpginventory.mixin.entity;

import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlotType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.world.entity.EquipmentSlot;

@Mixin(EquipmentSlot.class)
public class EquipmentSlotMixin {
	@Invoker("<init>")
	private static EquipmentSlot init(String enumName, int id, EquipmentSlot.Type type, int entityId, int maxCount, int armorStandId, String name) {
		throw new AssertionError(); // unreachable statement
	}

	// synthetic field, find the name in bytecode
	// if you are using McDev plugin add @SuppressWarnings("ShadowTarget")
	@Shadow
	@Final
	@Mutable
	private static EquipmentSlot[] field_6176;

//	@Shadow
//	@Final
//	@Mutable
//	public static Codec<EquipmentSlot> field_45739;

	// add new property from the static constructor
	// static blocks are merged into the target class (at the end)
	static {
		ArrayList<EquipmentSlot> values = new ArrayList<>(Arrays.asList(field_6176));
		EquipmentSlot last = values.get(values.size() - 1);

		// add new value
		values.add(init("BELT", last.ordinal() + 1, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 0, 1, -1, "belt"));
		values.add(init("GLOVES", last.ordinal() + 2, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 1, 1, -1, "gloves"));
		values.add(init("NECKLACE", last.ordinal() + 3, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 2, 1, -1, "necklace"));
		values.add(init("RING_1", last.ordinal() + 4, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 3, 1, -1, "ring_1"));
		values.add(init("RING_2", last.ordinal() + 5, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 4, 1, -1, "ring_2"));
		values.add(init("SHOULDERS", last.ordinal() + 6, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 5, 1, -1, "shoulders"));
		values.add(init("SPELL_1", last.ordinal() + 7, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 6, 1, -1, "spell_1"));
		values.add(init("SPELL_2", last.ordinal() + 8, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 7, 1, -1, "spell_2"));
		values.add(init("SPELL_3", last.ordinal() + 9, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 8, 1, -1, "spell_3"));
		values.add(init("SPELL_4", last.ordinal() + 10, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 9, 1, -1, "spell_4"));
		values.add(init("SPELL_5", last.ordinal() + 11, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 10, 1, -1, "spell_5"));
		values.add(init("SPELL_6", last.ordinal() + 12, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 11, 1, -1, "spell_6"));
		values.add(init("SPELL_7", last.ordinal() + 13, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 12, 1, -1, "spell_7"));
		values.add(init("SPELL_8", last.ordinal() + 14, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 13, 1, -1, "spell_8"));
		values.add(init("RELIC", last.ordinal() + 15, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 14, 1, -1, "relic"));
		values.add(init("CLASS_ITEM", last.ordinal() + 16, ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 15, 1, -1, "class_item"));

		field_6176 = values.toArray(new EquipmentSlot[0]);

//		field_45739 = StringIdentifiable.createCodec(EquipmentSlot::values);
	}
}
