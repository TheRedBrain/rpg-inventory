package com.github.theredbrain.rpginventory.mixin.component.type;

import com.github.theredbrain.rpginventory.component.type.ExtendedAttributeModifierSlot;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.serialization.Codec;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.StringIdentifiable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

@Mixin(AttributeModifierSlot.class)
public class AttributeModifierSlotMixin {
	@Invoker("<init>")
	private static AttributeModifierSlot init(String enumName, int index, int id, String name, EquipmentSlot slot) {
		throw new AssertionError(); // unreachable statement
	}

	@Invoker("<init>")
	private static AttributeModifierSlot init(String enumName, int index, int id, String name, Predicate<EquipmentSlot> slotPredicate) {
		throw new AssertionError(); // unreachable statement
	}

//	@Shadow
//	@Final
//	@Mutable
//	public static final IntFunction<AttributeModifierSlot> ID_TO_VALUE;

	@Shadow
	@Final
	@Mutable
	public static final Codec<AttributeModifierSlot> CODEC;

//	public static final PacketCodec<ByteBuf, AttributeModifierSlot> PACKET_CODEC = PacketCodecs.indexed(ID_TO_VALUE, id -> id.id);

//	@Shadow
//	@Final
//	private int id;

	// synthetic field, find the name in bytecode
	// if you are using McDev plugin add @SuppressWarnings("ShadowTarget")
	@Shadow
	@Final
	@Mutable
	private static AttributeModifierSlot[] field_49231;

	@WrapMethod(method = "forEquipmentSlot")
	private static AttributeModifierSlot forEquipmentSlot(EquipmentSlot slot, Operation<AttributeModifierSlot> original) {
		if (slot == EquipmentSlot.MAINHAND) {
			return AttributeModifierSlot.MAINHAND;
		} else if (slot == EquipmentSlot.OFFHAND) {
			return AttributeModifierSlot.OFFHAND;
		} else if (slot == EquipmentSlot.FEET) {
			return AttributeModifierSlot.FEET;
		} else if (slot == EquipmentSlot.LEGS) {
			return AttributeModifierSlot.LEGS;
		} else if (slot == EquipmentSlot.CHEST) {
			return AttributeModifierSlot.CHEST;
		} else if (slot == EquipmentSlot.HEAD) {
			return AttributeModifierSlot.HEAD;
		} else if (slot == EquipmentSlot.BODY) {
			return AttributeModifierSlot.BODY;
		} else if (slot == ExtendedEquipmentSlot.BELT) {
			return ExtendedAttributeModifierSlot.BELT;
		} else if (slot == ExtendedEquipmentSlot.GLOVES) {
			return ExtendedAttributeModifierSlot.GLOVES;
		} else if (slot == ExtendedEquipmentSlot.NECKLACE) {
			return ExtendedAttributeModifierSlot.NECKLACE;
		} else if (slot == ExtendedEquipmentSlot.RING_1) {
			return ExtendedAttributeModifierSlot.RING_1;
		} else if (slot == ExtendedEquipmentSlot.RING_2) {
			return ExtendedAttributeModifierSlot.RING_2;
		} else if (slot == ExtendedEquipmentSlot.SHOULDERS) {
			return ExtendedAttributeModifierSlot.SHOULDERS;
		} else if (slot == ExtendedEquipmentSlot.SPELL_1) {
			return ExtendedAttributeModifierSlot.SPELL_1;
		} else if (slot == ExtendedEquipmentSlot.SPELL_2) {
			return ExtendedAttributeModifierSlot.SPELL_2;
		} else if (slot == ExtendedEquipmentSlot.SPELL_3) {
			return ExtendedAttributeModifierSlot.SPELL_3;
		} else if (slot == ExtendedEquipmentSlot.SPELL_4) {
			return ExtendedAttributeModifierSlot.SPELL_4;
		} else if (slot == ExtendedEquipmentSlot.SPELL_5) {
			return ExtendedAttributeModifierSlot.SPELL_5;
		} else if (slot == ExtendedEquipmentSlot.SPELL_6) {
			return ExtendedAttributeModifierSlot.SPELL_6;
		} else if (slot == ExtendedEquipmentSlot.SPELL_7) {
			return ExtendedAttributeModifierSlot.SPELL_7;
		} else if (slot == ExtendedEquipmentSlot.SPELL_8) {
			return ExtendedAttributeModifierSlot.SPELL_8;
		} else if (slot == ExtendedEquipmentSlot.RELIC) {
			return ExtendedAttributeModifierSlot.RELIC;
		} else if (slot == ExtendedEquipmentSlot.CLASS_ITEM) {
			return ExtendedAttributeModifierSlot.CLASS_ITEM;
		} else {
			return original.call(slot);
		}
	}

	// add new property from the static constructor
	// static blocks are merged into the target class (at the end)
	static {
		ArrayList<AttributeModifierSlot> values = new ArrayList<>(Arrays.asList(field_49231));
		AttributeModifierSlot last = values.get(values.size() - 1);

		// add new value
		values.add(init("BELT", last.ordinal() + 1, last.ordinal() + 1, "belt", ExtendedEquipmentSlot.BELT));
		values.add(init("GLOVES", last.ordinal() + 2, last.ordinal() + 2, "gloves", ExtendedEquipmentSlot.GLOVES));
		values.add(init("NECKLACE", last.ordinal() + 3, last.ordinal() + 3, "necklace", ExtendedEquipmentSlot.NECKLACE));
		values.add(init("RING_1", last.ordinal() + 4, last.ordinal() + 4, "ring_1", ExtendedEquipmentSlot.RING_1));
		values.add(init("RING_2", last.ordinal() + 5, last.ordinal() + 5, "ring_2", ExtendedEquipmentSlot.RING_2));
		values.add(init("SHOULDERS", last.ordinal() + 6, last.ordinal() + 6, "shoulders", ExtendedEquipmentSlot.SHOULDERS));
		values.add(init("SPELL_1", last.ordinal() + 7, last.ordinal() + 7, "spell_1", ExtendedEquipmentSlot.SPELL_1));
		values.add(init("SPELL_2", last.ordinal() + 8, last.ordinal() + 8, "spell_2", ExtendedEquipmentSlot.SPELL_2));
		values.add(init("SPELL_3", last.ordinal() + 9, last.ordinal() + 9, "spell_3", ExtendedEquipmentSlot.SPELL_3));
		values.add(init("SPELL_4", last.ordinal() + 10, last.ordinal() + 10, "spell_4", ExtendedEquipmentSlot.SPELL_4));
		values.add(init("SPELL_5", last.ordinal() + 11, last.ordinal() + 11, "spell_5", ExtendedEquipmentSlot.SPELL_5));
		values.add(init("SPELL_6", last.ordinal() + 12, last.ordinal() + 12, "spell_6", ExtendedEquipmentSlot.SPELL_6));
		values.add(init("SPELL_7", last.ordinal() + 13, last.ordinal() + 13, "spell_7", ExtendedEquipmentSlot.SPELL_7));
		values.add(init("SPELL_8", last.ordinal() + 14, last.ordinal() + 14, "spell_8", ExtendedEquipmentSlot.SPELL_8));
		values.add(init("RINGS", last.ordinal() + 15, last.ordinal() + 15, "rings", ExtendedAttributeModifierSlot::isRingsSlot));
		values.add(init("SPELLS", last.ordinal() + 16, last.ordinal() + 16, "spells", ExtendedAttributeModifierSlot::isSpellsSlot));
		values.add(init("RELIC", last.ordinal() + 17, last.ordinal() + 17, "relic", ExtendedEquipmentSlot.RELIC));
		values.add(init("CLASS_ITEM", last.ordinal() + 18, last.ordinal() + 18, "class_item", ExtendedEquipmentSlot.CLASS_ITEM));

		field_49231 = values.toArray(new AttributeModifierSlot[0]);

//		ID_TO_VALUE = ValueLists.createIdToValueFunction(
//				id -> id.id, values(), ValueLists.OutOfBoundsHandling.ZERO
//		);

		CODEC = StringIdentifiable.createCodec(AttributeModifierSlot::values);
	}
}
